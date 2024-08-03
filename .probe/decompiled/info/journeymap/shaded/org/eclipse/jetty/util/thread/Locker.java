package info.journeymap.shaded.org.eclipse.jetty.util.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Locker {

    private static final Locker.Lock LOCKED = new Locker.Lock();

    private final ReentrantLock _lock = new ReentrantLock();

    private final Locker.Lock _unlock = new Locker.UnLock();

    public Locker.Lock lock() {
        if (this._lock.isHeldByCurrentThread()) {
            throw new IllegalStateException("Locker is not reentrant");
        } else {
            this._lock.lock();
            return this._unlock;
        }
    }

    public Locker.Lock lockIfNotHeld() {
        if (this._lock.isHeldByCurrentThread()) {
            return LOCKED;
        } else {
            this._lock.lock();
            return this._unlock;
        }
    }

    public boolean isLocked() {
        return this._lock.isLocked();
    }

    public Condition newCondition() {
        return this._lock.newCondition();
    }

    public static class Lock implements AutoCloseable {

        public void close() {
        }
    }

    public class UnLock extends Locker.Lock {

        @Override
        public void close() {
            Locker.this._lock.unlock();
        }
    }
}