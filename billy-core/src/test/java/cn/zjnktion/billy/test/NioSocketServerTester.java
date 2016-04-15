package cn.zjnktion.billy.test;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.future.BindFuture;
import cn.zjnktion.billy.handler.Handler;
import cn.zjnktion.billy.service.server.NioSocketServer;
import cn.zjnktion.billy.session.Session;

import java.net.InetSocketAddress;

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
            BindFuture future = server.bind(new InetSocketAddress(5222));
            future.awaitUninterruptibly();

            if (future.isCompleted()) {
                System.out.println("bind success.");
            }
        }
        catch (Exception e) {
            System.out.println("...");
        }
    }
}
