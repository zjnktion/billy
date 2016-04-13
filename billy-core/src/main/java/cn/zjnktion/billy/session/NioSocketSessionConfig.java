package cn.zjnktion.billy.session;

import cn.zjnktion.billy.common.IdleType;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/13.
 */
public class NioSocketSessionConfig extends AbstractNioSessionConfig implements SocketSessionConfig {

    public int getReadBufferSize() {
        return 0;
    }

    public void setReadBufferSize(int size) {

    }

    public int getMinReadBufferSize() {
        return 0;
    }

    public void setMinReadBufferSize(int size) {

    }

    public int getMaxReadBufferSize() {
        return 0;
    }

    public void setMaxReadBufferSize(int size) {

    }

    public long getIdleTime(IdleType idleType, TimeUnit timeUnit) {
        return 0;
    }

    public void setIdleTime(IdleType idleType, TimeUnit timeUnit) {

    }

    public void setConfig(SessionConfig config) {

    }

    public boolean isAsyncReadEnable() {
        return false;
    }
}
