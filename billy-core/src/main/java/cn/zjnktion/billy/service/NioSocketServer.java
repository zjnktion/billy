package cn.zjnktion.billy.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.*;

/**
 * Created by zhengjn on 2016/4/5.
 */
public class NioSocketServer {

    private final Set<SocketAddress> boundAddress = new HashSet<SocketAddress>();

    public void bind(SocketAddress address) throws IOException {
        if (address == null) {
            throw new IllegalArgumentException("Can not bind a null socket address.");
        }

        List<SocketAddress> toBindAddresses = new ArrayList<SocketAddress>(1);
        toBindAddresses.add(address);

        bind(toBindAddresses);
    }

    public void bind(Iterable<? extends SocketAddress> addresses) throws IOException {
        if (addresses == null) {
            throw new IllegalArgumentException("Can not bind null socket addresses.")
        }

        List<SocketAddress> toBind
    }

    private Set<SocketAddress> bind0(List<? extends SocketAddress> addresses) throws Exception {

    }

}
