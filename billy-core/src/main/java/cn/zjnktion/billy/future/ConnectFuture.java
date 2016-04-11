package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;
import cn.zjnktion.billy.session.Session;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface ConnectFuture extends Future {

    boolean isConnected();

    boolean isCanceled();

    boolean cancel();

    Session getSession();

    void setSession(Session session);

    Throwable getCause();

    void setCause(Throwable cause);

    ConnectFuture await() throws InterruptedException;

    ConnectFuture awaitUninterruptibly();

    ConnectFuture addListener(FutureListener<?> listener);

    ConnectFuture removeListener(FutureListener<?> listener);
}
