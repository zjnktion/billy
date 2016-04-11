package cn.zjnktion.billy.service;

import cn.zjnktion.billy.common.TransportMetadata;
import cn.zjnktion.billy.listener.ServiceListener;
import cn.zjnktion.billy.observer.Observer;
import cn.zjnktion.billy.session.SessionConfig;

import java.util.Map;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface Service {

    TransportMetadata getTransportMetadata();

    Map<String, Observer> getObservers();

    SessionConfig getSessionConfig();

    void addListener(ServiceListener listener);

    void removeListener(ServiceListener listener);

    boolean isActive();

    void dispose();

    void dispose(boolean immediately);

    boolean isDisposing();

    boolean isDisposed();
}
