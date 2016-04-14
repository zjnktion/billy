package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;

import java.net.SocketAddress;
import java.util.List;

/**
 * Created by zhengjn on 2016/4/14.
 */
public interface BindFuture extends Future {

    boolean isSuccess();

    void setBound();

    Throwable getCause();

    void setCause(Throwable cause);

    List<SocketAddress> getBindAddresses();

    BindFuture await() throws InterruptedException;

    BindFuture awaitUninterruptibly();

    BindFuture addListener(FutureListener<?> listener);

    BindFuture removeListener(FutureListener<?> listener);
}
