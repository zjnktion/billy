package cn.zjnktion.billy.session;

import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.common.TransportMetadata;
import cn.zjnktion.billy.future.CloseFuture;
import cn.zjnktion.billy.future.ReadFuture;
import cn.zjnktion.billy.future.WriteFuture;
import cn.zjnktion.billy.service.Service;
import cn.zjnktion.billy.task.WriteTask;
import cn.zjnktion.billy.task.WriteTaskQueue;

import java.net.SocketAddress;

/**
 * Created by zhengjn on 2016/4/13.
 */
public final class NioSocketSession extends AbstractNioSession implements SocketSession {
    public long getId() {
        return 0;
    }

    public Service getService() {
        return null;
    }

    public TransportMetadata getTransportMetadata() {
        return null;
    }

    public SessionConfig getConfig() {
        return null;
    }

    public ReadFuture read() {
        return null;
    }

    public void suspendRead() {

    }

    public void resumeRead() {

    }

    public boolean isReadSuspended() {
        return false;
    }

    public WriteFuture write(Object message) {
        return null;
    }

    public WriteFuture write(Object message, SocketAddress destination) {
        return null;
    }

    public void suspendWrite() {

    }

    public void resumeWrite() {

    }

    public boolean isWriteSuspended() {
        return false;
    }

    public WriteTaskQueue getWriteTaskQueue() {
        return null;
    }

    public WriteTask getCurrentWriteTask() {
        return null;
    }

    public void setCurrentWriteTask(WriteTask currentWriteTask) {

    }

    public Object getCurrentWriteMessage() {
        return null;
    }

    public CloseFuture closeImmediately() {
        return null;
    }

    public CloseFuture closeOnFlush() {
        return null;
    }

    public CloseFuture getCloseFuture() {
        return null;
    }

    public SocketAddress getLocalAddress() {
        return null;
    }

    public SocketAddress getRemoteAddress() {
        return null;
    }

    public boolean isConnected() {
        return false;
    }

    public boolean isActive() {
        return false;
    }

    public long getLastIoTime() {
        return 0;
    }

    public long getLastReadTime() {
        return 0;
    }

    public long getLastWriteTime() {
        return 0;
    }

    public boolean isIdle(IdleType idleType) {
        return false;
    }

    public long getLastIdleTime(IdleType idleType) {
        return 0;
    }

    public int getIdleCount(IdleType idleType) {
        return 0;
    }

    public boolean isClosing() {
        return false;
    }

    public Object getAttr(String key) {
        return null;
    }

    public Object setAttr(String key, Object value) {
        return null;
    }

    public Object setAttrIfAbsent(String key, Object value) {
        return null;
    }

    public void removeAttr(String key) {

    }

    public boolean containsAttr(String key) {
        return false;
    }
}
