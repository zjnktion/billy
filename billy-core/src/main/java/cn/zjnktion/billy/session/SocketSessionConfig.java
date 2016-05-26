package cn.zjnktion.billy.session;

/**
 * Created by zhengjn on 2016/4/13.
 */
public interface SocketSessionConfig extends SessionConfig {

    boolean DEFAULT_SOCKET_KEEPALIVE = false;
    boolean DEFAULT_SOCKET_REUSEADDRESS = false;
    boolean DEFAULT_SOCKET_TCPNODELAY = false;
    int DEFAULT_SOCKET_RECETVEBUFFERSIZE = 1024;
    int DEFAULT_SOCKET_SENDBUFFERSIZE = 1024;
    int DEFAULT_SOCKET_TRAFFICCLASS = 64;
    boolean DEFAULT_SOCKET_OOBINLINE = false;
    int DEFAULT_SOCKET_SOLINER = 1;

    boolean isKeepAlive();

    void setKeepAlive(boolean keepAlive);

    boolean isReuseAddress();

    void setReuseAddress(boolean reuseAddress);

    boolean isTcpNoDelay();

    void setTcpNoDelay(boolean tcpNoDelay);

    int getReceiveBufferSize();

    void setReceiveBufferSize(int receiveBufferSize);

    int getSendBufferSize();

    void setSendBufferSize(int sendBufferSize);

    int getTrafficClass();

    void setTrafficClass(int trafficClass);

    boolean isOobInline();

    void setOobInline(boolean oobInline);

    int getSoLinger();

    void setSoLinger(int soLinger);
}
