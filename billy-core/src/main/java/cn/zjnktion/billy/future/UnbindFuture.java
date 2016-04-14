package cn.zjnktion.billy.future;

/**
 * Created by zhengjn on 2016/4/14.
 */
public interface UnbindFuture extends Future {

    boolean isSuccess();

    void setUnbound();

    Throwable getCause();
}
