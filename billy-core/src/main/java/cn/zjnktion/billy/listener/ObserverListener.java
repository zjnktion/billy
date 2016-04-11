package cn.zjnktion.billy.listener;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.observer.Observer;
import cn.zjnktion.billy.session.Session;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface ObserverListener extends Listener {

    void observerActivated(Observer observer) throws Exception;

    void observerIdle(Observer observer, IdleType idleType) throws Exception;

    void observerDeactivated(Observer observer) throws Exception;

    void sessionCreated(Session session) throws Exception;

    void sessionOpened(Session session) throws Exception;

    void sessionClosed(Session session) throws Exception;

}
