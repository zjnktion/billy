package cn.zjnktion.billy.service.server;

import cn.zjnktion.billy.common.TransportMetadata;
import cn.zjnktion.billy.processor.NioSocketProcessor;
import cn.zjnktion.billy.processor.Processor;
import cn.zjnktion.billy.session.NioSocketSession;
import cn.zjnktion.billy.session.NioSocketSessionConfig;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executor;

/**
 * Created by zhengjn on 2016/4/5.
 */
public final class NioSocketServer extends AbstractNioServer<NioSocketSession, ServerSocketChannel> implements SocketServer {

    private boolean keepAlive = false;

    private boolean reuseAddress = false;

    private int backlog = 50;

    private boolean tcpNoDelay = false;

    private int sendBufferSize = 1024;

    private int receiveBufferSize = 1024;

    public NioSocketServer() {
        this(null, new NioSocketProcessor());
    }

    public NioSocketServer(Executor executor) {
        this(executor, new NioSocketProcessor());
    }

    public NioSocketServer(Processor<NioSocketSession> processor) {
        this(null, processor);
    }

    public NioSocketServer(Executor executor, Processor<NioSocketSession> processor) {
        super(new NioSocketSessionConfig(), executor, processor);
    }

    @Override
    protected void work() throws InterruptedException {

    }

    @Override
    protected SocketAddress localAddress(ServerSocketChannel channel) throws Exception {
        return channel.socket().getLocalSocketAddress();
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isReuseAddress() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public TransportMetadata getTransportMetadata() {
        return null;
    }
}
