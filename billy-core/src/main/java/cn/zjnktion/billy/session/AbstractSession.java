package cn.zjnktion.billy.session;

import cn.zjnktion.billy.handler.Handler;
import cn.zjnktion.billy.service.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractSession implements Session {

    private final Service service;

    private final Handler handler;

    private static final AtomicLong idGenerator = new AtomicLong(0);
    private long sessionId;

    protected AbstractSession(Service service) {
        this.service = service;
        this.handler = service.getHandler();

        sessionId = idGenerator.getAndIncrement();
    }

    public final long getId() {
        return sessionId;
    }

    public final Service getService() {
        return service;
    }


}
