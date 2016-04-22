package cn.zjnktion.billy.filter;

/**
 * Created by zhengjn on 2016/4/22.
 */
public interface FilterChainBuilder {

    void buildFilterChain(FilterChain filterChain) throws Exception;
}
