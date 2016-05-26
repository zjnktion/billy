package cn.zjnktion.billy.session;

import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

/**
 * Created by zhengjn on 2016/4/13.
 */
public class NioSocketSessionConfig extends AbstractNioSessionConfig implements SocketSessionConfig {

    private final Socket channelSocket;

    protected NioSocketSessionConfig(SocketChannel channel) {
        channelSocket = channel.socket();
    }

    @Override
    protected void setConfig0(SessionConfig config) {

    }

    public boolean isKeepAlive() {
        try {
            return channelSocket.getKeepAlive();
        }
        catch (SocketException e) {
            return false;
        }
    }

    public void setKeepAlive(boolean keepAlive) {
        try {
            channelSocket.setKeepAlive(keepAlive);
        }
        catch (SocketException e) {
            // do nothing
        }
    }

    public boolean isReuseAddress() {
        try {
            return channelSocket.getReuseAddress();
        }
        catch (SocketException e) {
            return false;
        }
    }

    public void setReuseAddress(boolean reuseAddress) {
        try {
            channelSocket.setReuseAddress(reuseAddress);
        }
        catch (SocketException e) {
            // do nothing
        }
    }

    public boolean isTcpNoDelay() {
        try {
            return channelSocket.getTcpNoDelay();
        }
        catch (SocketException e) {
            return false;
        }
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        try {
            channelSocket.setTcpNoDelay(tcpNoDelay);
        }
        catch (SocketException e) {
            // do nothing
        }
    }

    public int getReceiveBufferSize() {
        try {
            return channelSocket.getReceiveBufferSize();
        }
        catch (SocketException e) {
            return 1024;
        }
    }

    public void setReceiveBufferSize(int receiveBufferSize) {

    }

    public int getSendBufferSize() {
        return 0;
    }

    public void setSendBufferSize(int sendBufferSize) {

    }

    public int getTrafficClass() {
        return 0;
    }

    public void setTrafficClass(int trafficClass) {

    }

    public boolean isOobInline() {
        return false;
    }

    public void setOobInline(boolean oobInline) {

    }

    public int getSoLinger() {
        return 0;
    }

    public void setSoLinger(int soLinger) {

    }
}
