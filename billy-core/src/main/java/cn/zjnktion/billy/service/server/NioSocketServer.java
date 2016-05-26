package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.common.RuntimeIOException;
import cn.zjnktion.billy.service.DefaultTransportMetadata;
import cn.zjnktion.billy.service.TransportMetadata;
import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.UnbindFuture;
import cn.zjnktion.billy.processor.NioSocketProcessor;
import cn.zjnktion.billy.processor.Processor;
import cn.zjnktion.billy.session.NioSocketSession;
import cn.zjnktion.billy.session.NioSocketSessionConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by zhengjn on 2016/4/5.
 */
public final class NioSocketServer extends AbstractNioServer<NioSocketSession> implements SocketServer {

    public static final TransportMetadata METADATA = new DefaultTransportMetadata("nio", "socket", false, true);

    private boolean keepAlive = false;

    private boolean reuseAddress = false;

    private int backlog = 50;

    private boolean tcpNoDelay = false;

    private int sendBufferSize = 1024;

    private int receiveBufferSize = 1024;

    private final AtomicReference<Polling> pollingRef = new AtomicReference<Polling>();
    private final Semaphore pollLock = new Semaphore(1);

    private final Map<SocketAddress, ServerSocketChannel> boundChannels = Collections.synchronizedMap(new HashMap<SocketAddress, ServerSocketChannel>());

    public NioSocketServer() {
        this(null, new NioSocketProcessor());
    }

    public NioSocketServer(Executor executor) {
        this(executor, new NioSocketProcessor());
    }

    public NioSocketServer(Processor<NioSocketSession> processor) {
        this(null, processor);
    }

    public NioSocketServer(Executor executor, Processor<NioSocketSession> processor) {
        super(null, executor, processor);

        // start polling
        try {
            poll();
        }
        catch (Exception e) {
            throw new RuntimeIOException("Failed to start polling.", e);
        }
    }

    @Override
    protected void poll() throws InterruptedException {
        Polling polling = pollingRef.get();

        if (polling == null) {
            pollLock.acquire();
            polling = new Polling();

            if (pollingRef.compareAndSet(null, polling)) {
                executeWorker(polling);
            }
            else {
                pollLock.release();
            }
        }
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public TransportMetadata getTransportMetadata() {
        return METADATA;
    }

    private class Polling implements Runnable {

        public void run() {
            assert (pollingRef.get() == this);

            pollLock.release();

            while (selectable) {
                try {

                    int selected = selector.select();
                    if (selected > 0) {
                        accept(selector.selectedKeys().iterator());
                    }

                    bindChannels();

                    unbindChannels();
                }
                catch (ClosedSelectorException e) {
                    ExceptionSupervisor.getInstance().exceptionCaught(e);
                    // if the selector had been closed ,it means that no need to poll any more.
                    break;
                }
                catch (Exception e) {
                    ExceptionSupervisor.getInstance().exceptionCaught(e);

                    // if some unexpected exceptions occur, we should poll after 500ms.
                    try {
                        TimeUnit.MILLISECONDS.sleep(500L);
                    } catch (InterruptedException e1) {
                        ExceptionSupervisor.getInstance().exceptionCaught(e1);
                    }
                }
            }
        }
    }

    private int select() throws Exception {
        return selector.select();
    }

    private void bindChannels() {
        for(;;) {
            BindFuture future = bindQueue.poll();

            if (future == null) {
                return;
            }

            Set<SocketAddress> newAddresses = new HashSet<SocketAddress>();
            Map<SocketAddress, ServerSocketChannel> newChannels = new ConcurrentHashMap<SocketAddress, ServerSocketChannel>();
            List<SocketAddress> bindAddresses = future.getBindAddresses();

            boolean activated = false;
            try {
                for (SocketAddress socketAddress : bindAddresses) {
                    ServerSocketChannel channel = open(socketAddress);
                    newAddresses.add(channel.socket().getLocalSocketAddress());
                    newChannels.put(channel.socket().getLocalSocketAddress(), channel);
                }

                if (boundAddresses.isEmpty()) {
                    activated = true;
                }

                boundAddresses.addAll(newAddresses);
                boundChannels.putAll(newChannels);

                future.setBound(newAddresses);
            }
            catch (Exception e) {
                future.setCause(e);
            }
            finally {
                if (future.getCause() != null) {
                    for (ServerSocketChannel channel : newChannels.values()) {
                        try {
                            SelectionKey key = channel.keyFor(selector);
                            if (key != null) {
                                key.cancel();
                            }

                            channel.close();
                        }
                        catch (IOException e) {
                            ExceptionSupervisor.getInstance().exceptionCaught(e);
                        }
                    }
                }
                else {
                    if (activated) {
                        fireServiceActivated();
                    }
                }
            }
        }
    }

    private void unbindChannels() {
        for (;;) {
            UnbindFuture future = unbindQueue.poll();
            if (future == null) {
                return;
            }

            Set<SocketAddress> removeAddresses = new HashSet<SocketAddress>();
            Map<SocketAddress, ServerSocketChannel> removeChannels = new ConcurrentHashMap<SocketAddress, ServerSocketChannel>();
            List<SocketAddress> unbindAddresses = future.getUnbindAddresses();

            boolean deactivated = false;
            try {
                for (SocketAddress socketAddress : unbindAddresses) {
                    ServerSocketChannel channel = boundChannels.remove(socketAddress);

                    if (channel == null) {
                        continue;
                    }

                    removeAddresses.add(socketAddress);
                    removeChannels.put(channel.socket().getLocalSocketAddress(), channel);

                    try {
                        SelectionKey key = channel.keyFor(selector);
                        if (key != null) {
                            key.cancel();
                        }

                        channel.close();
                    } catch (IOException e) {
                        ExceptionSupervisor.getInstance().exceptionCaught(e);
                    }
                }

                boundAddresses.removeAll(removeAddresses);

                if (boundAddresses.isEmpty()) {
                    deactivated = true;
                }

                future.setUnbound(removeAddresses);
            }
            finally {
                if (deactivated) {
                    fireServiceDeactivated();
                }
            }
        }
    }

    private ServerSocketChannel open(SocketAddress socketAddress) throws Exception {
        ServerSocketChannel channel = null;

        if (selectorProvider != null) {
            channel = selectorProvider.openServerSocketChannel();
        }
        else {
            channel = ServerSocketChannel.open();
        }

        boolean success = false;

        try {
            channel.configureBlocking(false);

            ServerSocket socket = channel.socket();
            socket.setReuseAddress(isReuseAddress());
            try {
                socket.bind(socketAddress, getBacklog());
            }
            catch (IOException e) {
                channel.close();

                throw e;
            }

            channel.register(selector, SelectionKey.OP_ACCEPT);
            success = true;
        }
        finally {
            if (!success) {
                SelectionKey key = channel.keyFor(selector);
                if (key != null) {
                    key.cancel();
                }

                channel.close();
            }
        }

        return channel;
    }

    private void accept(Iterator<SelectionKey> iterator) throws Exception {
        while (iterator.hasNext()) {
            SelectionKey key = iterator.next();

            if (key.isValid() && key.isAcceptable()) {
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                SocketChannel acceptChannel = channel.accept();

                NioSocketSession session = createSession(processor, acceptChannel);
                if (session == null) {
                    continue;
                }
                System.out.println("Accepted one remote connection.");
            }

            iterator.remove();
        }
    }

    private NioSocketSession createSession(Processor<NioSocketSession> processor, SocketChannel acceptChannel) throws Exception {
        if (acceptChannel == null) {
            return null;
        }

        return new NioSocketSession(this);
    }
}
