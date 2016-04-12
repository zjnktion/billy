package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.session.AbstractSession;
import cn.zjnktion.billy.session.SessionConfig;

import java.util.concurrent.Executor;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractNioServer<S extends AbstractSession> extends AbstractServer {

    protected AbstractNioServer(SessionConfig sessionConfig, Executor executor) {
        super(sessionConfig, executor);
    }
}
