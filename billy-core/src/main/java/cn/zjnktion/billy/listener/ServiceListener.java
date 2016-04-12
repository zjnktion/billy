package cn.zjnktion.billy.listener;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.service.Service;
import cn.zjnktion.billy.session.Session;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface ServiceListener extends Listener {

    void serviceActivated(Service service) throws Exception;

    void serviceIdle(Service service, IdleType idleType) throws Exception;

    void serviceDeactivated(Service service) throws Exception;

    void sessionCreated(Session session) throws Exception;

    void sessionIdle(Session session, IdleType idleType) throws Exception;

    void sessionClosed(Session session) throws Exception;

}
