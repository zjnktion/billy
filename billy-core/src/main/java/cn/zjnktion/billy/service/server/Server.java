package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.service.Service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Set;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface Server extends Service {

    Set<SocketAddress> getBoundAddresses();

    void bind(SocketAddress socketAddress) throws IOException;

    void bind(Iterable<? extends SocketAddress> socketAddresses) throws IOException;

    void unbind(SocketAddress socketAddress) throws IOException;

    void unbind(Iterable<? extends SocketAddress> socketAddresses) throws IOException;
}
