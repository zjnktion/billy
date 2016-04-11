package cn.zjnktion.billy.task;

import cn.zjnktion.billy.session.Session;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface WriteTaskQueue {

    Session getSession();

    void offer(WriteTask writeTask);

    WriteTask poll();

    int size();

    boolean isEmpty();

    void clear();

    void dispose();

}
