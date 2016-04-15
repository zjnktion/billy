package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.UnbindFuture;
import cn.zjnktion.billy.service.Service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Set;

/**
 * Created by zhengjn on 2016/4/11.
 */
public interface Server extends Service {

    Set<SocketAddress> getBoundAddresses();

    BindFuture bind(SocketAddress socketAddress) throws IOException;

    BindFuture bind(Iterable<? extends SocketAddress> socketAddresses) throws IOException;

    UnbindFuture unbind(SocketAddress socketAddress) throws IOException;

    UnbindFuture unbind(Iterable<? extends SocketAddress> socketAddresses) throws IOException;
}
