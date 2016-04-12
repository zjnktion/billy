package cn.zjnktion.billy.service;

import cn.zjnktion.billy.common.TransportMetadata;
import cn.zjnktion.billy.handler.Handler;
import cn.zjnktion.billy.listener.ServiceListener;
import cn.zjnktion.billy.session.Session;
import cn.zjnktion.billy.session.SessionConfig;

import java.util.Map;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface Service {

    long getId();

    TransportMetadata getTransportMetadata();

    Map<Long, Session> getManagedSessions();

    SessionConfig getSessionConfig();

    Handler getHandler();

    void addListener(ServiceListener listener);

    void removeListener(ServiceListener listener);

    boolean isActive();

    void dispose();

    void dispose(boolean immediately);

    boolean isDisposing();

    boolean isDisposed();
}
