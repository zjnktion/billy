package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.observer.Observer;
import cn.zjnktion.billy.service.Service;

import java.io.IOException;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface Server extends Service {

    void bind() throws IOException;

    void bind(Observer observer) throws IOException;

    void bind(Iterable<? extends Observer> observers) throws IOException;

    void unbind() throws IOException;
}
