package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

import java.net.SocketAddress;
import java.util.List;
import java.util.Set;

/**
 * Created by zhengjn on 2016/4/14.
 */
public interface UnbindFuture extends Future {

    boolean isSuccess();

    void setUnbound(Set<? extends SocketAddress> socketAddresses);

    List<SocketAddress> getUnbindAddresses();

    Set<SocketAddress> getUnboundAddresses();

    UnbindFuture await() throws InterruptedException;

    UnbindFuture awaitUninterruptibly();

    UnbindFuture addListener(FutureListener<?> listener);

    UnbindFuture removeListener(FutureListener<?> listener);
}
