package cn.zjnktion.billy.filter;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.session.Session;
import cn.zjnktion.billy.task.WriteTask;

import java.util.List;

/**
 * Created by zhengjn on 2016/4/22.
 */
public interface FilterChain {

    Session getSession();

    Entry getEntry(String name);

    Entry getEntry(Filter filter);

    /**
     * get by filter class type, if has several entries, return the first one
     * @param filterType
     * @return
     */
    Entry getEntry(Class<? extends Filter> filterType);

    Filter get(String name);

    /**
     * get by filter class type, if has several filters, return the first one
     * @param filterType
     * @return
     */
    Filter get(Class<? extends Filter> filterType);

    Filter.NextFilter getNextFilter(String name);

    /**
     * get by filter class type, if has several next filters, return the first one
     * @param filter
     * @return
     */
    Filter.NextFilter getNextFilter(Filter filter);

    List<Entry> getAllEntries();

    boolean contains(String name);

    boolean contains(Filter filter);

    boolean contains(Class<? extends Filter> filterType);

    void addFirst(String name, Filter filter);

    void addLast(String name, Filter filter);

    void addBefore(String beforeName, String name, Filter newFilter);

    Filter replace(String name, Filter newFilter);

    boolean replace(Filter oldFilter, Filter newFilter);

    /**
     * replace by filter class type, if has several filters, replace the first one
     * @param oldFilterType
     * @param newFilter
     * @return
     */
    Filter replace(Class<? extends Filter> oldFilterType, Filter newFilter);

    Filter remove(String name);

    Filter remove(Filter filter);

    /**
     * remove by filter class type, if has several filters, remove the first one
     * @param filterType
     * @return
     */
    Filter remove(Class<? extends Filter> filterType);

    void clear() throws Exception;

    void fireSessionCreated();

    void fireSessionOpened();

    void fireSessionIdle(IdleType idleType);

    void fireSessionClosed();

    void fireMessageRead(Object message);

    void fireMessageSent(WriteTask writeTask);

    void fireExceptionCaught(Throwable cause);

    void fireFilterWrite(WriteTask writeTask);

    void fireFilterClose();

    /**
     *
     * @param filterType
     * @return
     */
    Filter.NextFilter getNextFilter(Class<? extends Filter> filterType);

    interface Entry {

        String getName();

        Filter getFilter();

        Filter.NextFilter getNextFilter();

        void remove();
    }
}
