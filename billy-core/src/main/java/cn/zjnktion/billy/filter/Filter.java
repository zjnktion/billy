package cn.zjnktion.billy.filter;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.session.Session;
import cn.zjnktion.billy.task.WriteTask;

/**
 * Created by zhengjn on 2016/4/22.
 */
public interface Filter {

    void init() throws Exception;

    void destory() throws Exception;

    void preAdd(FilterChain filterChain, String name, NextFilter nextFilter) throws Exception;

    void postAdd(FilterChain filterChain, String name, NextFilter nextFilter) throws Exception;

    void preRemove(FilterChain filterChain, String name, NextFilter nextFilter) throws Exception;

    void postRemove(FilterChain filterChain, String name, NextFilter nextFilter) throws Exception;

    void sessionCreated(Session session, NextFilter nextFilter) throws Exception;

    void sessionOpened(Session session, NextFilter nextFilter) throws Exception;

    void sessionIdle(Session session, IdleType idleType, NextFilter nextFilter);

    void sessionClosed(Session session, NextFilter nextFilter) throws Exception;

    void messageRead(Session session, Object message, NextFilter nextFilter) throws Exception;

    void messageWrote(Session session, WriteTask writeTask, NextFilter nextFilter) throws Exception;

    void exceptionCaught(Session session, Throwable cause, NextFilter nextFilter) throws Exception;

    void filterWrite(Session session, WriteTask writeTask, NextFilter nextFilter) throws Exception;

    void filterClose(Session session, NextFilter nextFilter) throws Exception;

    interface NextFilter {

        void sessionCreated(Session session);

        void sessionOpened(Session session);

        void sessionIdle(Session session, IdleType idleType);

        void sessionClosed(Session session);

        void messageRead(Session session, Object message);

        void messageWrote(Session session, WriteTask writeTask);

        void exceptionCaught(Session session, Throwable cause);

        void filterWrite(Session session, WriteTask writeTask);

        void filterClose(Session session);
    }
}
