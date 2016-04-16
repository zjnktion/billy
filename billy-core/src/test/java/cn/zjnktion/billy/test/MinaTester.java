package cn.zjnktion.billy.test;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by zjnktion on 2016/4/16.
 */
public class MinaTester {

    public static void main(String[] args) {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new IoHandler() {
            public void sessionCreated(IoSession session) throws Exception {
                System.out.println("connect");
            }

            public void sessionOpened(IoSession session) throws Exception {

            }

            public void sessionClosed(IoSession session) throws Exception {

            }

            public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

            }

            public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

            }

            public void messageReceived(IoSession session, Object message) throws Exception {

            }

            public void messageSent(IoSession session, Object message) throws Exception {

            }

            public void inputClosed(IoSession session) throws Exception {

            }
        });
        try {
            acceptor.bind(new InetSocketAddress(5222));

            TimeUnit.MILLISECONDS.sleep(5000L);
            acceptor.bind(new InetSocketAddress(9090));

            TimeUnit.MILLISECONDS.sleep(2000L);
            acceptor.unbind(acceptor.getLocalAddress());
            System.out.println(acceptor.getLocalAddress());

            TimeUnit.MILLISECONDS.sleep(10000L);
            acceptor.dispose();
            System.out.println("all dispose");

            TimeUnit.MILLISECONDS.sleep(10000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
