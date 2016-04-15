package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.common.RuntimeIOException;
import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.DefaultBindFuture;
import cn.zjnktion.billy.future.DefaultUnbindFuture;
import cn.zjnktion.billy.future.UnbindFuture;
import cn.zjnktion.billy.processor.Processor;
import cn.zjnktion.billy.session.AbstractNioSession;
import cn.zjnktion.billy.session.SessionConfig;

import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractNioServer<S extends AbstractNioSession> extends AbstractServer {

    protected final Processor<S> processor;

    protected final SelectorProvider selectorProvider;
    protected volatile Selector selector;

    protected volatile boolean selectable;

    protected final Queue<BindFuture> bindQueue = new ConcurrentLinkedQueue<BindFuture>();
    protected final Queue<UnbindFuture> unbindQueue = new ConcurrentLinkedQueue<UnbindFuture>();

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
        bindQueue.add(future);

        poll();

        // wait a second so that the polling can start to select.
        TimeUnit.MILLISECONDS.sleep(10);

        wakeupSelector();

        return future;
    }

    protected final UnbindFuture unbind0(List<? extends SocketAddress> unbindAddresses) throws Exception {
        DefaultUnbindFuture future = new DefaultUnbindFuture(unbindAddresses);
        unbindQueue.add(future);

        poll();

        // wait a second so that the polling can start to select.
        TimeUnit.MILLISECONDS.sleep(10);

        wakeupSelector();

        return future;
    }

    protected final void dispose0() throws Exception {
        unbind(boundAddresses);
        poll();
        wakeupSelector();
    }

    protected final void wakeupSelector() {
        selector.wakeup();
    }

    /**
     * 面向连接和无连接有不同的实现
     * @throws InterruptedException
     */
    protected abstract void poll() throws InterruptedException;
}
