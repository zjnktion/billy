package cn.zjnktion.billy.listener;

import cn.zjnktion.billy.future.Future;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface FutureListener<F extends Future> extends Listener {

    void operationCompleted(F future);

}
