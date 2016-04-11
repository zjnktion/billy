package cn.zjnktion.billy.service;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.common.IdleType;
import cn.zjnktion.billy.listener.ServiceListener;
import cn.zjnktion.billy.observer.Observer;
import cn.zjnktion.billy.session.SessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhengjn on 2016/4/11.
 */
public abstract class AbstractService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    protected final Map<String, Observer> observers = new ConcurrentHashMap<String, Observer>();
    private final Map<String, Observer> readOnlyObservers = Collections.unmodifiableMap(observers);

    private final List<ServiceListener> listeners = new CopyOnWriteArrayList<ServiceListener>();
    private final ServiceListener serviceActivationListener = new ServiceListener() {
        public void serviceActivated(Service service) throws Exception {
            // We should update the service I/O time?
        }

        public void serviceIdle(Service service, IdleType idleType) throws Exception {
            // empty
        }

        public void serviceDeactivated(Service service) throws Exception {
            // empty
        }

        public void observerActivated(Observer observer) throws Exception {
            // empty
        }

        public void observerIdle(Observer observer, IdleType idleType) throws Exception {
            // empty
        }

        public void observerDeactivated(Observer observer) throws Exception {
            // empty
        }
    };

    protected final SessionConfig sessionConfig;

    // Threads pool use to deal with I/O and businesses.
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
            this.executor = new ThreadPoolExecutor(AVAILABLE_PROCESSORS + 1, AVAILABLE_PROCESSORS * 4, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
            createdDefaultExecutor = true;
        }
        else {
            this.executor = executor;
            createdDefaultExecutor = false;
        }
    }

    public final Map<String, Observer> getObservers() {
        return readOnlyObservers;
    }

    public final void setObservers(Iterable<? extends Observer> observers) {
        if (observers == null) {
            throw new IllegalArgumentException("Can not set null observers.");
        }

        Map<String, Observer> newObservers = new HashMap<String, Observer>();
        for (Observer observer : observers) {
            newObservers.put(observer.getId(), observer);
        }

        if (newObservers.isEmpty()) {
            throw new IllegalArgumentException("Can not set a empty observers.");
        }

        this.observers.clear();
        this.observers.putAll(newObservers);
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
            closeAllObservers();
        }
    }

    /**
     * 服务端和客户端有不同的实现
     * @param observer
     */
    public abstract void fireObserverActivated(Observer observer);

    /**
     * 服务端和客户端有不同的实现
     * @param observer
     * @param idleType
     */
    public abstract void fireObserverIdle(Observer observer, IdleType idleType);

    /**
     * 服务端和客户端有不同的实现
     * @param observer
     */
    public abstract void fireObserverDeactivated(Observer observer);

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
    protected abstract void closeAllObservers();
}
