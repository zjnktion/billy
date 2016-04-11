package cn.zjnktion.billy.listener;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.observer.Observer;
import cn.zjnktion.billy.service.Service;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface ServiceListener extends Listener {

    void serviceActivated(Service service) throws Exception;

    void serviceIdle(Service service, IdleType idleType) throws Exception;

    void serviceDeactivated(Service service) throws Exception;

    void observerActivated(Observer observer) throws Exception;

    void observerIdle(Observer observer, IdleType idleType) throws Exception;

    void observerDeactivated(Observer observer) throws Exception;

}
