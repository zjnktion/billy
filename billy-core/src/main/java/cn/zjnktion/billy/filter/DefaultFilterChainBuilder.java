package cn.zjnktion.billy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhengjn on 2016/4/22.
 */
public class DefaultFilterChainBuilder implements FilterChainBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFilterChainBuilder.class);

    private List<FilterChain.Entry> entries;

    public DefaultFilterChainBuilder() {
        entries = new CopyOnWriteArrayList<FilterChain.Entry>();
    }

    public void buildFilterChain(FilterChain filterChain) throws Exception {
        for (FilterChain.Entry entry : entries) {
            filterChain.addLast(entry.getName(), entry.getFilter());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DefaultFilterChainBuilder : {");

        boolean empty = true;

        for (FilterChain.Entry entry : entries) {
            if (!empty) {
                sb.append(",");
            }
            else {
                empty = false;
            }

            sb.append("(");
            sb.append(entry.getName());
            sb.append(":");
            sb.append(entry.getFilter());
            sb.append(")");
        }

        if (empty) {
            sb.append("empty");
        }

        sb.append("}");

        return sb.toString();
    }
}
