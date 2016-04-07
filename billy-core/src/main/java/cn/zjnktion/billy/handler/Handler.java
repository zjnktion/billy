package cn.zjnktion.billy.handler;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.session.Session;

/**
 * Created by zhengjn on 2016/4/7.
 */
public interface Handler {

    void sessionCreated(Session session) throws Exception;

    void sessionOpened(Session session) throws Exception;

    void sessionIdle(Session session, IdleType idleType) throws Exception;

    void sessionClosed(Session session) throws Exception;

    void messageRead(Session session, Object message) throws Exception;

    void messageSent(Session session, Object message) throws Exception;

    void exceptionCaught(Session session, Throwable cause) throws Exception;
}
