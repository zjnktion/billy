package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface ReadFuture extends Future {

    Object getMessage();

    boolean isRead();

    void setRead(Object message);

    boolean isClosed();

    void setClesed();

    Throwable getCause();

    void setCause();

    ReadFuture await() throws InterruptedException;

    ReadFuture awaitUninterruptibly();

    ReadFuture addListener(FutureListener<?> listener);

    ReadFuture removeListener(FutureListener<?> listener);
}
