package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

import java.net.SocketAddress;
import java.util.*;

/**
 * Created by zhengjn on 2016/4/15.
 */
public class DefaultUnbindFuture extends DefaultFuture implements UnbindFuture {

    private static final Object UNBOUND = new Object();

    private final List<SocketAddress> unbindAddresses;

    private Set<SocketAddress> unboundAddresses;

    public DefaultUnbindFuture(List<? extends SocketAddress> unbindAddresses) {
        super(null);
        this.unbindAddresses = new ArrayList<SocketAddress>(unbindAddresses);
    }

    public final boolean isSuccess() {
        return isCompleted() && getResult() == UNBOUND;
    }

    public final void setUnbound(Set<? extends SocketAddress> socketAddresses) {
        this.unboundAddresses = new HashSet<SocketAddress>(socketAddresses);
        setResult(UNBOUND);
    }

    public final List<SocketAddress> getUnbindAddresses() {
        return Collections.unmodifiableList(unbindAddresses);
    }

    public final Set<SocketAddress> getUnboundAddresses() {
        return Collections.unmodifiableSet(unboundAddresses);
    }

    public final UnbindFuture await() throws InterruptedException {
        return (UnbindFuture) super.await();
    }

    public final UnbindFuture awaitUninterruptibly() {
        return (UnbindFuture) super.awaitUninterruptibly();
    }

    public UnbindFuture addListener(FutureListener<?> listener) {
        return (UnbindFuture) super.addListener(listener);
    }

    public UnbindFuture removeListener(FutureListener<?> listener) {
        return (UnbindFuture) super.removeListener(listener);
    }
}
