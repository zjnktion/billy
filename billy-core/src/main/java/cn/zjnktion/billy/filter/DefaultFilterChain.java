package cn.zjnktion.billy.filter;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.session.AbstractSession;
import cn.zjnktion.billy.session.Session;
import cn.zjnktion.billy.task.WriteTask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhengjn on 2016/4/22.
 */
public class DefaultFilterChain implements FilterChain {

    private final Map<String, Entry> name2Entry = new ConcurrentHashMap<String, Entry>();

    public Session getSession() {
        return null;
    }

    public Entry getEntry(String name) {
        return null;
    }

    public Entry getEntry(Filter filter) {
        return null;
    }

    public Entry getEntry(Class<? extends Filter> filterType) {
        return null;
    }

    public Filter get(String name) {
        return null;
    }

    public Filter get(Class<? extends Filter> filterType) {
        return null;
    }

    public Filter.NextFilter getNextFilter(String name) {
        return null;
    }

    public Filter.NextFilter getNextFilter(Filter filter) {
        return null;
    }

    public List<Entry> getAllEntries() {
        return null;
    }

    public boolean contains(String name) {
        return false;
    }

    public boolean contains(Filter filter) {
        return false;
    }

    public boolean contains(Class<? extends Filter> filterType) {
        return false;
    }

    public void addFirst(String name, Filter filter) {

    }

    public void addLast(String name, Filter filter) {

    }

    public void addBefore(String beforeName, String name, Filter newFilter) {

    }

    public Filter replace(String name, Filter newFilter) {
        return null;
    }

    public boolean replace(Filter oldFilter, Filter newFilter) {
        return false;
    }

    public Filter replace(Class<? extends Filter> oldFilterType, Filter newFilter) {
        return null;
    }

    public Filter remove(String name) {
        return null;
    }

    public Filter remove(Filter filter) {
        return null;
    }

    public Filter remove(Class<? extends Filter> filterType) {
        return null;
    }

    public void clear() throws Exception {

    }

    public void fireSessionCreated() {

    }

    public void fireSessionOpened() {

    }

    public void fireSessionIdle(IdleType idleType) {

    }

    public void fireSessionClosed() {

    }

    public void fireMessageRead(Object message) {

    }

    public void fireMessageSent(WriteTask writeTask) {

    }

    public void fireExceptionCaught(Throwable cause) {

    }

    public void fireFilterWrite(WriteTask writeTask) {

    }

    public void fireFilterClose() {

    }

    public Filter.NextFilter getNextFilter(Class<? extends Filter> filterType) {
        return null;
    }
}
