package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.common.RuntimeIOException;
import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.UnbindFuture;
import cn.zjnktion.billy.service.AbstractService;
import cn.zjnktion.billy.session.SessionConfig;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractServer extends AbstractService implements Server {

    protected final Set<SocketAddress> boundAddresses = new HashSet<SocketAddress>();
    private final Set<SocketAddress> readOnlyBoundAddresses = Collections.unmodifiableSet(boundAddresses);

    private final Object bindLock = new Object();

    protected AbstractServer(SessionConfig sessionConfig, Executor executor) {
        super(sessionConfig, executor);
    }

    public final Set<SocketAddress> getBoundAddresses() {
        return readOnlyBoundAddresses;
    }

    public final BindFuture bind(SocketAddress socketAddress) throws IOException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("Can not bind a null socket address.");
        }

        List<SocketAddress> bindAddresses = new ArrayList<SocketAddress>();
        bindAddresses.add(socketAddress);

        return bind(bindAddresses);
    }

    public final BindFuture bind(Iterable<? extends SocketAddress> socketAddresses) throws IOException {
        if (isDisposing()) {
            throw new IllegalStateException("Service had been disposed.");
        }

        if (socketAddresses == null) {
            throw new IllegalArgumentException("Can not bind null socket addresses.");
        }

        List<SocketAddress> bindAddresses = new ArrayList<SocketAddress>();
        for (SocketAddress socketAddress : socketAddresses) {
            bindAddresses.add(socketAddress);
        }

        if (bindAddresses.isEmpty()) {
            throw new IllegalArgumentException("Can not bind empty socket addresses.");
        }
        synchronized (bindLock) {

            boolean activated = false;
            synchronized (boundAddresses) {
                if (boundAddresses.isEmpty()) {
                    activated =true;
                }
            }

            if (getHandler() == null) {
                throw new IllegalStateException("Can not bind without handler, please call setHandler(Handler handler) method to set a handler before binding.");
            }

            try {
                BindFuture future = bind0(bindAddresses);

                synchronized (boundAddresses) {
                    boundAddresses.addAll(future.getBindAddresses());
                }

                if (activated) {
                    fireServiceActivated();
                }

                return future;
            }
            catch (IOException e) {
                throw e;
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e) {
                throw new RuntimeIOException("Failed to bind : " + bindAddresses, e);
            }
        }
    }

    public final UnbindFuture unbind(SocketAddress socketAddress) throws IOException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("Can not unbind a null socket address.");
        }

        List<SocketAddress> bindAddesses = new ArrayList<SocketAddress>();
        bindAddesses.add(socketAddress);

        return unbind(bindAddesses);
    }

    public final UnbindFuture unbind(Iterable<? extends SocketAddress> socketAddresses) throws IOException {
        if (socketAddresses == null) {
            throw new IllegalArgumentException("Can not unbind null socket addresses.");
        }

        synchronized (bindLock) {
            synchronized (boundAddresses) {
                if (boundAddresses.isEmpty()) {
                    throw new IllegalStateException("No bound socket addresses.");
                }

                List<SocketAddress> unbindAddresses = new ArrayList<SocketAddress>();

                for (SocketAddress socketAddress : socketAddresses) {
                    if (socketAddress != null && boundAddresses.contains(socketAddress)) {
                        unbindAddresses.add(socketAddress);
                    }
                }

                if (unbindAddresses.isEmpty()) {
                    throw new IllegalArgumentException("Can not unbind empty socket addresses.");
                }

                try {
                    UnbindFuture future = unbind0(unbindAddresses);

                    boundAddresses.removeAll(unbindAddresses);

                    if (boundAddresses.isEmpty()) {
                        fireServiceDeactivated();
                    }

                    return future;
                }
                catch (IOException e) {
                    throw e;
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e) {
                    throw new RuntimeIOException("Failed to unbind : " + unbindAddresses, e);
                }
            }
        }
    }

    protected abstract BindFuture bind0(List<? extends SocketAddress> socketAddresses) throws Exception;

    protected abstract UnbindFuture unbind0(List<? extends SocketAddress> socketAddresses) throws Exception;
}
