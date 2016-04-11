package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.service.AbstractService;
import cn.zjnktion.billy.session.SessionConfig;

import java.util.concurrent.Executor;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractServer extends AbstractService implements Server {

    protected AbstractServer(SessionConfig sessionConfig, Executor executor) {
        super(sessionConfig, executor);
    }


}
