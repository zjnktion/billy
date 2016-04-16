package cn.zjnktion.billy.service;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.future.Future;
import cn.zjnktion.billy.handler.Handler;
import cn.zjnktion.billy.listener.FutureListener;
import cn.zjnktion.billy.listener.ServiceListener;
import cn.zjnktion.billy.session.Session;
import cn.zjnktion.billy.session.SessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private final int id;

    protected final Map<Long, Session> managedSessions = new ConcurrentHashMap<Long, Session>();
    private final Map<Long, Session> readOnlymanagedSessions = Collections.unmodifiableMap(managedSessions);

    private final List<ServiceListener> listeners = new CopyOnWriteArrayList<ServiceListener>();
    private final ServiceListener serviceActivationListener = new ServiceListener() {
        public void serviceActivated(Service service) throws Exception {
            // We should update the service I/O time?
            System.out.println("service activated.");
        }

        public void serviceIdle(Service service, IdleType idleType) throws Exception {
            // empty
        }

        public void serviceDeactivated(Service service) throws Exception {
            // empty
            System.out.println("service deactivated.");
        }

        public void sessionCreated(Session session) throws Exception {
            // empty
        }

        public void sessionIdle(Session session, IdleType idleType) throws Exception {
            // empty
        }

        public void sessionClosed(Session session) throws Exception {
            // empty
        }
    };

    protected final SessionConfig sessionConfig;

    private Handler handler;

    // Threads pool use to deal with Accept ,Connect or Un-accept ,Un-connect.
    private final Executor executor;
    private volatile boolean createdDefaultExecutor;

    private final AtomicBoolean activated = new AtomicBoolean(false);

    private final Object disposeLock = new Object();
    private volatile boolean disposing;
    private volatile boolean disposed;

    protected AbstractService(SessionConfig sessionConfig, Executor executor) {
        if (sessionConfig == null) {
            throw new IllegalArgumentException("Can not set a null session config.");
        }

        if (getTransportMetadata() == null) {
            throw new IllegalArgumentException("There's no TransportMetadata to be assigned.");
        }

        listeners.add(serviceActivationListener);

        this.sessionConfig = sessionConfig;

        if (executor == null) {
            this.executor = Executors.newCachedThreadPool();
            createdDefaultExecutor = true;
        }
        else {
            this.executor = executor;
            createdDefaultExecutor = false;
        }

        id = ID_GENERATOR.incrementAndGet();
    }

    public final int getId() {
        return id;
    }

    public final SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    public final Handler getHandler() {
        return handler;
    }

    public final void setHandler(Handler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Can not set a null handler.");
        }

        this.handler = handler;
    }

    public final Map<Long, Session> getManagedSessions() {
        return readOnlymanagedSessions;
    }

    public final void addListener(ServiceListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(ServiceListener listener) {
        listeners.remove(listener);
    }

    public final boolean isActive() {
        return activated.get();
    }

    public final void dispose() {
        dispose(false);
    }

    public final void dispose(boolean immediately) {
        if (disposed) {
            return;
        }

        synchronized (disposeLock) {
            if (!disposing) {
                disposing = true;
            }

            try {
                dispose0();
            }
            catch (Exception e) {
                ExceptionSupervisor.getInstance().exceptionCaught(e);
            }
        }

        if (createdDefaultExecutor) {
            ExecutorService es = (ExecutorService) executor;
            es.shutdown();

            if (!immediately) {
                try {
                    LOGGER.debug("ExecutorService awaitTermination is called on {} by Thread[{}]", this, Thread.currentThread().getName());
                    es.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                    LOGGER.debug("ExecutorService termination success on {} by Thread[{}]", this, Thread.currentThread().getName());
                }
                catch (InterruptedException e) {
                    LOGGER.warn("ExecutorService awaitTermination cause InterruptedException on {} by [{}]", this, Thread.currentThread().getName());
                    Thread.currentThread().interrupt();
                }
            }
        }

        disposed = true;
    }

    public final boolean isDisposing() {
        return disposing;
    }

    public final boolean isDisposed() {
        return disposed;
    }

    public final void fireServiceActivated() {
        if (!activated.compareAndSet(false, true)) {
            return;
        }

        for (ServiceListener listener : listeners) {
            try {
                listener.serviceActivated(this);
            }
            catch (Exception e) {
                ExceptionSupervisor.getInstance().exceptionCaught(e);
            }
        }
    }

    public final void fireServiceIdle(IdleType idleType) {
        if (!activated.get()) {
            return;
        }

        for (ServiceListener listener : listeners) {
            try {
                listener.serviceIdle(this, idleType);
            }
            catch (Exception e) {
                ExceptionSupervisor.getInstance().exceptionCaught(e);
            }
        }
    }

    public final void fireServiceDeactivated() {
        if (!activated.compareAndSet(true, false)) {
            return;
        }

        try {
            for (ServiceListener listener : listeners) {
                try {
                    listener.serviceDeactivated(this);
                } catch (Exception e) {
                    ExceptionSupervisor.getInstance().exceptionCaught(e);
                }
            }
        }
        finally {
            closeAllSessions();
        }
    }

    public final void fireSessionCreated(Session session) {
        if (managedSessions.putIfAbsent(session.getId(), session) != null) {
            return;
        }

        for (ServiceListener listener : listeners) {
            try {
                listener.sessionCreated(session);
            }
            catch (Exception e) {
                ExceptionSupervisor.getInstance().exceptionCaught(e);
            }
        }
    }

    public final void fireSessionIdle(Session session, IdleType idleType) {
        if (managedSessions.get(session.getId()) == null) {
            return;
        }

        for (ServiceListener listener : listeners) {
            try {
                listener.sessionIdle(session, idleType);
            }
            catch (Exception e) {
                ExceptionSupervisor.getInstance().exceptionCaught(e);
            }
        }
    }

    public final void fireSessionClosed(Session session) {
        if (managedSessions.remove(session.getId()) == null) {
            return;
        }

        for (ServiceListener listener : listeners) {
            try {
                listener.sessionClosed(session);
            }
            catch (Exception e) {
                ExceptionSupervisor.getInstance().exceptionCaught(e);
            }
        }
    }

    /**
     * 统一线程池调用方法，只允许{@link Service}的子类调用
     * @param worker
     */
    protected final void executeWorker(Runnable worker) {
        executor.execute(worker);
    }

    /**
     * 服务端和客户端有不同的实现
     * @throws Exception
     */
    protected abstract void dispose0() throws Exception;

    /**
     * 服务端和客户端有不同的实现
     */
    private void closeAllSessions() {
        Object lock = new Object();
        LockNotifyingListener listener = new LockNotifyingListener(lock);

        for (Session session : managedSessions.values()) {
            session.closeImmediately().addListener(listener);
        }

        try {
            synchronized (lock) {
                while (!managedSessions.isEmpty()) {
                    lock.wait(500L);
                }
            }
        }
        catch (Exception e) {
            // do nothing
        }
    }

    private static class LockNotifyingListener implements FutureListener<cn.zjnktion.billy.future.Future> {

        private final Object lock;

        public LockNotifyingListener(Object lock) {
            this.lock = lock;
        }

        public void operationCompleted(Future future) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }
}
