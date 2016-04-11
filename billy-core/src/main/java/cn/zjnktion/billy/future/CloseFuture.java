package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface CloseFuture extends Future {

    boolean isClosed();

    void setClosed();

    CloseFuture await() throws InterruptedException;

    CloseFuture awaitUninterruptibly();

    CloseFuture addListener(FutureListener<?> listener);

    CloseFuture removeListener(FutureListener<?> listener);
}
