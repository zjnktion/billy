package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.common.RuntimeIOException;
import cn.zjnktion.billy.processor.Processor;
import cn.zjnktion.billy.session.AbstractNioSession;
import cn.zjnktion.billy.session.SessionConfig;

import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractNioServer<S extends AbstractNioSession, C extends Channel> extends AbstractServer {

    protected final Processor<S> processor;

    protected final SelectorProvider selectorProvider;
    protected volatile Selector selector;

    protected volatile boolean selectable;

    protected final Queue<ServerOperationFuture> registerQueue = new ConcurrentLinkedQueue<ServerOperationFuture>();
    protected final Queue<ServerOperationFuture> unregisterQueue = new ConcurrentLinkedQueue<ServerOperationFuture>();
    protected final Semaphore semaphore = new Semaphore(1);

    protected final Map<SocketAddress, C> boundChannels = Collections.synchronizedMap(new HashMap<SocketAddress, C>());

    private AtomicReference<Register> registerRef = new AtomicReference<Register>();

    protected AbstractNioServer(SessionConfig sessionConfig, Executor executor, Processor<S> processor) {
        this(sessionConfig, executor, processor, null);
    }

    protected AbstractNioServer(SessionConfig sessionConfig, Executor executor, Processor<S> processor, SelectorProvider selectorProvider) {
        super(sessionConfig, executor);

        if (processor == null) {
            throw new IllegalArgumentException("Can not set a null processor.");
        }

        this.processor = processor;

        this.selectorProvider = selectorProvider;

        try {
            if (this.selectorProvider == null) {
                selector = Selector.open();
            } else {
                selector = this.selectorProvider.openSelector();
            }
            selectable = true;
        }
        catch (Exception e) {
            throw new RuntimeIOException("Failed to open selector.", e);
        }
        finally {
            if (!selectable) {
                try {
                    if (this.selector != null) {
                        this.selector.close();
                    }
                }
                catch (Exception e) {
                    ExceptionSupervisor.getInstance().exceptionCaught(e);
                }
            }
        }
    }

    protected final Set<SocketAddress> bind0(List<? extends SocketAddress> bindAddresses) throws Exception {
        ServerOperationFuture future = new ServerOperationFuture(bindAddresses);
        registerQueue.add(future);

        work();

        try {
            semaphore.acquire();

            TimeUnit.MILLISECONDS.sleep(10);

            wakeupSelector();
        }
        finally {
            semaphore.release();
        }

        future.awaitUninterruptibly();
        if (future.getCause() != null) {
            throw future.getCause();
        }

        Set<SocketAddress> boundAddresses = new HashSet<SocketAddress>();
        for (C channel : boundChannels.values()) {
            boundAddresses.add(localAddress(channel));
        }

        return boundAddresses;
    }

    protected final void unbind0(List<? extends SocketAddress> unbindAddresses) throws Exception {
        ServerOperationFuture future = new ServerOperationFuture(unbindAddresses);
        unregisterQueue.add(future);

        registerChannels();

        // wait a second so that the worker can select some.
        TimeUnit.MILLISECONDS.sleep(10);

        wakeupSelector();

        future.awaitUninterruptibly();
        if (future.getCause() != null) {
            throw future.getCause();
        }
    }

    protected final void dispose0() throws Exception {
        unbind(boundAddresses);
        unwork();
        wakeupSelector();
    }

    protected final void wakeupSelector() {
        selector.wakeup();
    }

    private void registerChannels() throws InterruptedException{
        // start the register class
        Register register = registerRef.get();

        if (register == null) {
            try {
                semaphore.acquire();

                register = new Register();
                if (registerRef.compareAndSet(null, register)) {
                    executeWorker(register);
                }
            }
            finally {
                semaphore.release();
            }
        }
    }

    /**
     * 面向连接和无连接有不同的实现
     * @throws InterruptedException
     */
    protected abstract void work() throws InterruptedException;

    /**
     * 面向连接和无连接有不同的实现
     * @throws InterruptedException
     */
    protected abstract void unwork() throws InterruptedException;

    /**
     * 面向连接和无连接有不同的实现
     * @param socketAddress
     * @return
     * @throws Exception
     */
    protected abstract C open(SocketAddress socketAddress) throws Exception;

    /**
     * 面向连接和无连接有不同的实现
     * @param channel
     * @throws Exception
     */
    protected abstract void close(C channel) throws Exception;

    protected abstract SocketAddress localAddress(C channel) throws Exception;

    /**
     * when call registerChannels method this class will be initialized.
     */
    private class Register implements Runnable {

        public void run() {
            for (;;) {
                ServerOperationFuture future = registerQueue.poll();

                if (future == null) {
                    return;
                }

                Map<SocketAddress, C> bindChannels = new ConcurrentHashMap<SocketAddress, C>();
                List<SocketAddress> bindAddresses = future.getBindAddresses();

                try {
                    for (SocketAddress bindAddress : bindAddresses) {
                        C channel = open(bindAddress);
                        bindChannels.put(localAddress(channel), channel);
                    }

                    boundChannels.putAll(bindChannels);

                    future.setCompleted();
                }
                catch (Exception e) {
                    future.setCause(e);
                }
                finally {
                    if (future.getCause() != null) {
                        for (C channel : bindChannels.values()) {
                            try {
                                close(channel);
                            }
                            catch (Exception e) {
                                ExceptionSupervisor.getInstance().exceptionCaught(e);
                            }
                        }
                    }

                    wakeupSelector();
                }
            }
        }
    }
}
