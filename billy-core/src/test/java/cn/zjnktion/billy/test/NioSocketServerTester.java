package cn.zjnktion.billy.test;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.future.UnbindFuture;
import cn.zjnktion.billy.handler.Handler;
import cn.zjnktion.billy.service.server.NioSocketServer;
import cn.zjnktion.billy.session.Session;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/15.
 */
public class NioSocketServerTester {

    public static void main(String[] args) {
        NioSocketServer server = new NioSocketServer();
        server.setHandler(new Handler() {
            public void sessionCreated(Session session) throws Exception {

            }

            public void sessionOpened(Session session) throws Exception {

            }

            public void sessionIdle(Session session, IdleType idleType) throws Exception {

            }

            public void sessionClosed(Session session) throws Exception {

            }

            public void messageRead(Session session, Object message) throws Exception {

            }

            public void messageSent(Session session, Object message) throws Exception {

            }

            public void exceptionCaught(Session session, Throwable cause) throws Exception {

            }
        });
        try {
            int num = 1000;

            BindFuture[] future = new BindFuture[num];

            for (int i = 0; i < num; i++) {
                future[i] = server.bind(new InetSocketAddress(5222 + i));
            }

            for (int i = 0; i < num; i++) {
                future[i].awaitUninterruptibly();
            }

            for (int i = 0; i < num; i++) {
                if (future[i].isSuccess()) {
                    System.out.println(future[i].getBoundAddresses() + " bind success.");
                }
            }

            /*List<SocketAddress> list = new ArrayList<SocketAddress>();
            for (int i = 0; i < num; i++) {
                list.add(new InetSocketAddress(8080 + i));
            }
            BindFuture future = server.bind(list).awaitUninterruptibly();
            if (future.isSuccess()) {
                System.out.println(future.getBoundAddresses() + " bind success.");
            }
            else {
                System.out.println(future.getCause());
            }*/

            TimeUnit.MILLISECONDS.sleep(10000L);
            server.dispose();

            //TimeUnit.MILLISECONDS.sleep(10000L);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("...");
        }
    }
}
