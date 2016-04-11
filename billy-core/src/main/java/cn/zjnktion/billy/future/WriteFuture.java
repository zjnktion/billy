package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface WriteFuture extends Future {

    boolean isWritten();

    void setWritten();

    Throwable getCause();

    void setCause(Throwable cause);

    WriteFuture await() throws InterruptedException;

    WriteFuture awaitUninterruptibly();

    WriteFuture addListener(FutureListener<?> listener);

    WriteFuture removeListener(FutureListener<?> listener);
}
