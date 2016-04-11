package cn.zjnktion.billy.service;

import cn.zjnktion.billy.common.TransportMetadata;
import cn.zjnktion.billy.listener.ServiceListener;
import cn.zjnktion.billy.observer.Observer;

import java.util.Map;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface Service {

    TransportMetadata getTransportMetadata();

    Map<String, Observer> getObservers();

    void addListener(ServiceListener listener);

    void removeListener(ServiceListener listener);

}
