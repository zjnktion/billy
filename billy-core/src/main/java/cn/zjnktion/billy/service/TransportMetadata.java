package cn.zjnktion.billy.service;

/**
 * Created by zhengjn on 2016/4/8.
 */
public interface TransportMetadata {

    String getProvider();

    String getType();

    boolean isConnectionless();

    boolean canFragment();

}
