package cn.zjnktion.billy.processor;

import cn.zjnktion.billy.session.NioSocketSession;
import cn.zjnktion.billy.task.WriteTask;

/**
 * Created by zhengjn on 2016/4/13.
 */
public final class NioSocketProcessor extends AbstractNioProcessor<NioSocketSession> implements SocketProcessor<NioSocketSession> {
    public void add(NioSocketSession session) {

    }

    public void remove(NioSocketSession session) {

    }

    public void write(NioSocketSession session, WriteTask writeTask) {

    }

    public void flush(NioSocketSession session) {

    }

    public void dispose() {

    }

    public boolean isDisposing() {
        return false;
    }

    public boolean isDisposed() {
        return false;
    }
}
