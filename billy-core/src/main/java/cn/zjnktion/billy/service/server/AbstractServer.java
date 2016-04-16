package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.DefaultBindFuture;
import cn.zjnktion.billy.future.DefaultUnbindFuture;
import cn.zjnktion.billy.future.UnbindFuture;
import cn.zjnktion.billy.service.AbstractService;
import cn.zjnktion.billy.session.SessionConfig;

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

    public final BindFuture bind(SocketAddress socketAddress) {
        if (socketAddress == null) {
            BindFuture future = new DefaultBindFuture(null);
            future.setCause(new IllegalArgumentException("Can not bind a null socket address."));
            return future;
        }

        List<SocketAddress> bindAddresses = new ArrayList<SocketAddress>();
        bindAddresses.add(socketAddress);

        return bind(bindAddresses);
    }

    public final BindFuture bind(Iterable<? extends SocketAddress> socketAddresses) {
        BindFuture future;

        if (socketAddresses == null) {
            future = new DefaultBindFuture(null);
            future.setCause(new IllegalArgumentException("Can not bind null socket addresses."));
            return future;
        }

        List<SocketAddress> bindAddresses = new ArrayList<SocketAddress>();
        for (SocketAddress socketAddress : socketAddresses) {
            bindAddresses.add(socketAddress);
        }

        if (isDisposing()) {
            future = new DefaultBindFuture(bindAddresses);
            future.setCause(new IllegalStateException("Service had been disposed."));
            return future;
        }

        if (bindAddresses.isEmpty()) {
            future = new DefaultBindFuture(bindAddresses);
            future.setCause(new IllegalArgumentException("Can not bind empty socket addresses."));
            return future;
        }

        if (getHandler() == null) {
            future = new DefaultBindFuture(bindAddresses);
            future.setCause(new IllegalStateException("Can not bind without handler, please call setHandler(Handler handler) method to set a handler before binding."));
            return future;
        }

        synchronized (bindLock) {
            return bind0(bindAddresses);
        }
    }

    public final UnbindFuture unbind(SocketAddress socketAddress) {
        if (socketAddress == null) {
            UnbindFuture future = new DefaultUnbindFuture(null);
            future.setUnbound(null);
            return future;
        }

        List<SocketAddress> bindAddesses = new ArrayList<SocketAddress>();
        bindAddesses.add(socketAddress);

        return unbind(bindAddesses);
    }

    public final UnbindFuture unbind(Iterable<? extends SocketAddress> socketAddresses) {
        UnbindFuture future;

        if (socketAddresses == null) {
            future = new DefaultUnbindFuture(null);
            future.setUnbound(null);
            return future;
        }

        List<SocketAddress> unbindAddresses = new ArrayList<SocketAddress>();

        for (SocketAddress socketAddress : socketAddresses) {
            unbindAddresses.add(socketAddress);
        }

        if (unbindAddresses.isEmpty()) {
            future = new DefaultUnbindFuture(unbindAddresses);
            future.setUnbound(new HashSet<SocketAddress>());
            return future;
        }

        synchronized (bindLock) {
            return unbind0(unbindAddresses);
        }
    }

    protected abstract BindFuture bind0(List<? extends SocketAddress> socketAddresses);

    protected abstract UnbindFuture unbind0(List<? extends SocketAddress> socketAddresses);
}
