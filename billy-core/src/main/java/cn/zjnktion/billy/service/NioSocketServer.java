package cn.zjnktion.billy.service;

import cn.zjnktion.billy.observer.Observer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhengjn on 2016/4/5.
 */
public class NioSocketServer {

    private final Map<String, Observer> observers = new ConcurrentHashMap<String, Observer>();
    private final Map<String, Observer> readOnlyObservers = Collections.unmodifiableMap(observers);

}
