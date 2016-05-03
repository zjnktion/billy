package cn.zjnktion.billy.session;

import cn.zjnktion.billy.common.IdleType;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/13.
 */
public abstract class AbstractSessionConfig implements SessionConfig {

    private int minReadBufferSize = 64;
    private int readBufferSize = 2048;
    private int maxReadBufferSize = 65536;

    private static final TimeUnit IDLE_TIME_UNIT = TimeUnit.MILLISECONDS;
    private long idleTimeForRead;
    private long idleTimeForWrite;
    private long idleTimeForBoth;

    private boolean asyncReadEnable;

    protected AbstractSessionConfig() {
        // do nothing
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public int getMinReadBufferSize() {
        return minReadBufferSize;
    }

    public void setMinReadBufferSize(int minReadBufferSize) {
        if (minReadBufferSize <= 0) {
            throw new IllegalArgumentException("Can not set " + minReadBufferSize + " to min read buffer size." + " Expected 1+.");
        }

        if (minReadBufferSize > maxReadBufferSize) {
            throw new IllegalArgumentException("Can not set " + minReadBufferSize + " to min read buffer size." + " Expected smaller than " + maxReadBufferSize + ".");
        }

        this.minReadBufferSize = minReadBufferSize;
    }

    public int getMaxReadBufferSize() {
        return maxReadBufferSize;
    }

    public void setMaxReadBufferSize(int maxReadBufferSize) {
        if (maxReadBufferSize <= 0) {
            throw new IllegalArgumentException("Can not set " + maxReadBufferSize + " to max read buffer size." + " Expected 1+.");
        }

        if (maxReadBufferSize < minReadBufferSize) {
            throw new IllegalArgumentException("Can not set " + maxReadBufferSize + " to max read buffer size." + " Expected greater than " + minReadBufferSize + ".");
        }

        this.maxReadBufferSize = maxReadBufferSize;
    }

    public long getIdleTime(IdleType idleType, TimeUnit timeUnit) {
        if (timeUnit == null) {
            throw new IllegalArgumentException("Can not convert to a null time unit.");
        }

        if (idleType == IdleType.READ_IDLE) {
            return timeUnit.convert(idleTimeForRead, IDLE_TIME_UNIT);
        }

        if (idleType == IdleType.WRITE_IDLE) {
            return timeUnit.convert(idleTimeForWrite, IDLE_TIME_UNIT);
        }

        if (idleType == IdleType.BOTH_IDLE) {
            return timeUnit.convert(idleTimeForBoth, IDLE_TIME_UNIT);
        }

        throw new IllegalArgumentException("Illegal idle type " + idleType + ".");
    }

    public void setIdleTime(IdleType idleType, TimeUnit timeUnit, long idleTime) {
        if (timeUnit == null) {
            throw new IllegalArgumentException("Can not convert to a null time unit.");
        }

        if (idleTime < 0) {
            throw new IllegalArgumentException("Illegal idle time " + idleTime + ".");
        }

        if (idleType == IdleType.READ_IDLE) {
            idleTimeForRead = IDLE_TIME_UNIT.convert(idleTime, timeUnit);
        }

        if (idleType == IdleType.WRITE_IDLE) {
            idleTimeForWrite = IDLE_TIME_UNIT.convert(idleTime, timeUnit);
        }

        if (idleType == IdleType.BOTH_IDLE) {
            idleTimeForBoth = IDLE_TIME_UNIT.convert(idleTime, timeUnit);
        }

        throw new IllegalArgumentException("Illegal idle type " + idleType + ".");
    }

    public final void setConfig(SessionConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Can not set a null session config.");
        }

        setReadBufferSize(config.getReadBufferSize());
        setMinReadBufferSize(config.getMinReadBufferSize());
        setMaxReadBufferSize(config.getMaxReadBufferSize());

        setIdleTime(IdleType.READ_IDLE, IDLE_TIME_UNIT, config.getIdleTime(IdleType.READ_IDLE, IDLE_TIME_UNIT));
        setIdleTime(IdleType.WRITE_IDLE, IDLE_TIME_UNIT, config.getIdleTime(IdleType.WRITE_IDLE, IDLE_TIME_UNIT));
        setIdleTime(IdleType.BOTH_IDLE, IDLE_TIME_UNIT, config.getIdleTime(IdleType.BOTH_IDLE, IDLE_TIME_UNIT));

        setAsyncReadEnable(config.isAsyncReadEnable());

        setConfig0(config);
    }

    public boolean isAsyncReadEnable() {
        return asyncReadEnable;
    }

    public void setAsyncReadEnable(boolean asyncReadEnable) {
        this.asyncReadEnable = asyncReadEnable;
    }

    protected abstract void setConfig0(SessionConfig config);
}
