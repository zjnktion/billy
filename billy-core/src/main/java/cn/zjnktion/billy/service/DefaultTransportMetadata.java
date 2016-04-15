package cn.zjnktion.billy.service;

/**
 * Created by zhengjn on 2016/4/15.
 */
public class DefaultTransportMetadata implements TransportMetadata {

    private String provider;

    private String type;

    private boolean connectionless;

    private boolean fragment;

    public DefaultTransportMetadata(String provider, String type, boolean connectionless, boolean fragment) {
        this.provider = provider;
        this.type = type;
        this.connectionless = connectionless;
        this.fragment = fragment;
    }

    public String getProvider() {
        return provider;
    }

    public String getType() {
        return type;
    }

    public boolean isConnectionless() {
        return connectionless;
    }

    public boolean canFragment() {
        return fragment;
    }
}
