package cn.zjnktion.billy.test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjn on 2016/4/12.
 */
public class GeniricTypeTest {

    public static void main(String[] args) {
        List<InetSocketAddress> list = new ArrayList<InetSocketAddress>();
        list.add(new InetSocketAddress("localhost", 5222));

        new GeniricTypeTest().bind(list);

        System.out.println(Object.class.isAssignableFrom(String.class));
    }

    public void bind(Iterable<? extends SocketAddress> localAddresses) {
        List<SocketAddress> list = new ArrayList<SocketAddress>();

        for (SocketAddress localAddress : localAddresses) {
            list.add(localAddress);
        }

        for (SocketAddress address : list) {
            System.out.println(address.getClass().getName() + ":" + address.toString());
        }
    }
}
