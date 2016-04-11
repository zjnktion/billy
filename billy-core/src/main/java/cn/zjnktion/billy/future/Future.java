package cn.zjnktion.billy.future;

import cn.zjnktion.billy.listener.FutureListener;
import cn.zjnktion.billy.listener.Listener;
import cn.zjnktion.billy.session.Session;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface Future {

    Session getSession();

    boolean isCompleted();

    Future await() throws InterruptedException;

    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

    Future awaitUninterruptibly();

    boolean awaitUninterruptibly(long timeout, TimeUnit unit);

    Future addListener(FutureListener<?> listener);

    Future removeListener(FutureListener<?> listener);

}
