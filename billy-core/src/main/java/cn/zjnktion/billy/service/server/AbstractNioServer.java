package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.common.RuntimeIOException;
import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.DefaultBindFuture;
import cn.zjnktion.billy.processor.Processor;
import cn.zjnktion.billy.session.AbstractNioSession;
import cn.zjnktion.billy.session.SessionConfig;

import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractNioServer<S extends AbstractNioSession, C extends Channel> extends AbstractServer {

    protected final Processor<S> processor;

    protected final SelectorProvider selectorProvider;
    protected volatile Selector selector;

    protected volatile boolean selectable;

    protected final Map<SocketAddress, C> boundChannels = Collections.synchronizedMap(new HashMap<SocketAddress, C>());

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

    protected final BindFuture bind0(List<? extends SocketAddress> bindAddresses) throws Exception {
        DefaultBindFuture future = new DefaultBindFuture(bindAddresses);

        bindChannels(future);

        TimeUnit.MILLISECONDS.sleep(10);

        return future;
    }

    protected final BindFuture unbind0(List<? extends SocketAddress> unbindAddresses) throws Exception {
        DefaultBindFuture future = new DefaultBindFuture(unbindAddresses);
        unbindQueue.add(future);

        unbindChannels();

        // wait a second so that the binder can start.
        TimeUnit.MILLISECONDS.sleep(10);

        return future;
    }

    protected final void dispose0() throws Exception {
        unbind(boundAddresses);
        unwork();
        wakeupSelector();
    }

    protected final void wakeupSelector() {
        selector.wakeup();
    }

    private void bindChannels(BindFuture future) throws InterruptedException{
        // start the binder class
        Binder binder = new Binder(future);

        executeWorker(binder);
    }

    private void unbindChannels() throws InterruptedException {

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
     * when call bindChannels method this class will be initialized.
     */
    private class Binder implements Runnable {

        private final BindFuture future;

        public Binder(BindFuture future) {
            this.future = future;
        }

        public void run() {
            Map<SocketAddress, C> bindChannels = new HashMap<SocketAddress, C>();
            List<SocketAddress> bindAddresses = future.getBindAddresses();

            try {
                for (SocketAddress bindAddress : bindAddresses) {
                    C channel = open(bindAddress);
                    bindChannels.put(localAddress(channel), channel);
                }

                boundChannels.putAll(bindChannels);

                Set<SocketAddress> boundAddresses = new HashSet<SocketAddress>();
                for (C channel : boundChannels.values()) {
                    AbstractNioServer.this.boundAddresses.add(localAddress(channel));
                }

                future.setBound();
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

    private class Unbinder implements Runnable {

        public void run() {
            BindFuture future = null;
            while ((future = unbindQueue.poll()) != null) {

            }


        }
    }
}
