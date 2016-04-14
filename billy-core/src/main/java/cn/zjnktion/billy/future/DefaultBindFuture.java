package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhengjn on 2016/4/14.
 */
public class DefaultBindFuture extends DefaultFuture implements BindFuture {

    private static final Object BOUND = new Object();

    private final List<SocketAddress> bindAddresses;

    public DefaultBindFuture(List<? extends SocketAddress> bindAddresses) {
        super(null);
        this.bindAddresses = new ArrayList<SocketAddress>(bindAddresses);
    }

    public final Throwable getCause() {
        if (isCompleted()) {
            if (getResult() instanceof Throwable) {
                return (Throwable) getResult();
            }
        }

        return null;
    }

    public final void setCause(Throwable cause) {
        if (cause == null) {
            throw new IllegalArgumentException("Can not set a null cause.");
        }

        setResult(cause);
    }

    public final boolean isSuccess() {
        return isCompleted() && getResult() == BOUND && getCause() == null;
    }

    public final void setBound() {
        setResult(BOUND);
    }

    public final List<SocketAddress> getBindAddresses() {
        return Collections.unmodifiableList(bindAddresses);
    }

    public final BindFuture await() throws InterruptedException {
        return (BindFuture) super.await();
    }

    public final BindFuture awaitUninterruptibly() {
        return (BindFuture) super.awaitUninterruptibly();
    }

    public final BindFuture addListener(FutureListener<?> listener) {
        return (BindFuture) super.addListener(listener);
    }

    public final BindFuture removeListener(FutureListener<?> listener) {
        return (BindFuture) super.removeListener(listener);
    }
}
