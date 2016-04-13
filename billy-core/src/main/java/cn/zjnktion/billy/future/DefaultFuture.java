package cn.zjnktion.billy.future;

import cn.zjnktion.billy.common.ExceptionSupervisor;
import cn.zjnktion.billy.listener.FutureListener;
import cn.zjnktion.billy.service.server.AbstractNioServer;
import cn.zjnktion.billy.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengjn on 2016/4/8.
 */
public class DefaultFuture implements Future {

    private final long FUTURE_WAIT_INTERVAL = 5000L;

    private final Session session;

    private List<FutureListener<?>> listeners = new ArrayList<FutureListener<?>>();

    private boolean completed;

    private Object result;

    private final Object lock;

    private int waitThreads = 0;

    public DefaultFuture(Session session) {
        this.session = session;
        lock = this;
    }

    public Session getSession() {
        return session;
    }

    public boolean isCompleted() {
        synchronized (lock) {
            return completed;
        }
    }

    public Object getResult() {
        synchronized (lock) {
            return result;
        }
    }

    public boolean setResult(Object result) {
        synchronized (lock) {
            if (completed) {
                return false;
            }

            this.result = result;
            completed=true;

            if (waitThreads > 0) {
                lock.notifyAll();
            }
        }

        notifyListeners();

        return true;
    }

    public Future await() throws InterruptedException {
        synchronized (lock) {
            while (!completed) {
                waitThreads++;
                try {
                    lock.wait(FUTURE_WAIT_INTERVAL);
                }
                finally {
                    waitThreads--;
                }
            }
        }

        return this;
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return wait0(unit.toMillis(timeout), true);
    }

    public Future awaitUninterruptibly() {
        try {
            wait0(Long.MAX_VALUE, false);
        }
        catch (InterruptedException e) {
            // do nothing
        }

        return this;
    }

    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        try {
            return wait0(unit.toMillis(timeout), false);
        }
        catch (InterruptedException e) {
            throw new InternalError();
        }
    }

    public Future addListener(FutureListener<?> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Can not set a null listener.");
        }

        synchronized (lock) {
            if (isCompleted()) {
                // If this future had completed, we just need to notify immediately the to be added listener.
                notifyListener(listener);
            }
            else {
                listeners.add(listener);
            }
        }

        return this;
    }

    public Future removeListener(FutureListener<?> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Can not remove a null listener.");
        }

        synchronized (lock) {
            if (!completed) {
                listeners.remove(listener);
            }
        }

        return this;
    }

    private boolean wait0(long timeoutMillis, boolean interruptable) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeoutMillis;

        if (endTime < 0) {
            endTime = Long.MAX_VALUE;
        }

        synchronized (lock) {
            // If this future had completed or timeout, we could return immediately.
            if (completed || timeoutMillis <= 0) {
                return completed;
            }

            // The future is not completed, have to wait
            waitThreads++;

            try {
                while(true) {
                    try {
                        long timeOut = Math.max(timeoutMillis, FUTURE_WAIT_INTERVAL);

                        lock.wait(timeOut);
                    }
                    catch (InterruptedException e) {
                        if (interruptable) {
                            throw e;
                        }
                    }

                    if (completed || endTime <= System.currentTimeMillis()) {
                        return completed;
                    }
                }
            }
            finally {
                // three possible to reach here :
                // 1) we have been notified (operation has been completed by this future or other thread)
                // 2) timeout
                // 3) this thread has been interrupted
                waitThreads--;
            }
        }
    }

    private void notifyListeners() {
        for (FutureListener<?> listener : listeners) {
            notifyListener(listener);
        }
        listeners.clear();
    }

    private void notifyListener(FutureListener listener) {
        try {
            listener.operationCompleted(this);
        }
        catch (Exception e) {
            ExceptionSupervisor.getInstance().exceptionCaught(e);
        }
    }
}
