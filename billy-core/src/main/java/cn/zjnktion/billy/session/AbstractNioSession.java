package cn.zjnktion.billy.session;

import cn.zjnktion.billy.service.Service;

/**
 * Created by zhengjn on 2016/4/13.
 */
public abstract class AbstractNioSession extends AbstractSession {
    protected AbstractNioSession(Service service) {
        super(service);
    }
}
