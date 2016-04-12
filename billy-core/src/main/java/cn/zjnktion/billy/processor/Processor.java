package cn.zjnktion.billy.processor;

import cn.zjnktion.billy.session.Session;
import cn.zjnktion.billy.task.WriteTask;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface Processor<S extends Session> {

    void add(S session);

    void remove(S session);

    void write(S session, WriteTask writeTask);

    void flush(S session);

    void dispose();

    boolean isDisposing();

    boolean isDisposed();
}
