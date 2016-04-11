package cn.zjnktion.billy.observer;

import cn.zjnktion.billy.common.TransportMetadata;
import cn.zjnktion.billy.handler.Handler;
import cn.zjnktion.billy.listener.ObserverListener;
import cn.zjnktion.billy.service.Service;
import cn.zjnktion.billy.session.Session;

import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by zhengjn on 2016/4/7.
 */
public interface Observer {

    String getId();

    Service getService();

    TransportMetadata getTransportMetadata();

    SocketAddress getObserverAddress();

    Handler getHandler();

    Map<Long, Session> getObservedSessions();

    void addListener(ObserverListener listener);

    void removeListener(ObserverListener listener);

}
