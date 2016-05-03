package cn.zjnktion.billy.session;

/**
 * Created by zhengjn on 2016/4/13.
 */
public interface SocketSessionConfig extends SessionConfig {

    boolean isKeepAlive();

    void setKeepAlive();

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

    void setSoLinger();
}
