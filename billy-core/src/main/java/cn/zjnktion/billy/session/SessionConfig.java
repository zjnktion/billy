package cn.zjnktion.billy.session;

import cn.zjnktion.billy.common.IdleType;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface SessionConfig {

    int getReadBufferSize();

    void setReadBufferSize(int size);

    int getMinReadBufferSize();

    void setMinReadBufferSize(int size);

    int getMaxReadBufferSize();

    void setMaxReadBufferSize(int size);

    long getIdleTime(IdleType idleType, TimeUnit timeUnit);

    void setIdleTime(IdleType idleType, TimeUnit timeUnit);

    void setConfig(SessionConfig config);

    /**
     * This option is false default.
     * @return
     */
    boolean isAsyncReadEnable();
}
