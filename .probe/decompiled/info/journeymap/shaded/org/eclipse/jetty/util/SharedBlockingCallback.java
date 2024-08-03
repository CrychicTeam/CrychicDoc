package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Invocable;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SharedBlockingCallback {

    static final Logger LOG = Log.getLogger(SharedBlockingCallback.class);

    private static Throwable IDLE = new ConstantThrowable("IDLE");

    private static Throwable SUCCEEDED = new ConstantThrowable("SUCCEEDED");

    private static Throwable FAILED = new ConstantThrowable("FAILED");

    private final ReentrantLock _lock = new ReentrantLock();

    private final Condition _idle = this._lock.newCondition();

    private final Condition _complete = this._lock.newCondition();

    private SharedBlockingCallback.Blocker _blocker = new SharedBlockingCallback.Blocker();

    protected long getIdleTimeout() {
        return -1L;
    }

    public SharedBlockingCallback.Blocker acquire() throws IOException {
        long idle = this.getIdleTimeout();
        this._lock.lock();
        SharedBlockingCallback.Blocker x;
        try {
            while (this._blocker._state != IDLE) {
                if (idle > 0L && idle < 4611686018427387903L) {
                    if (!this._idle.await(idle * 2L, TimeUnit.MILLISECONDS)) {
                        throw new IOException(new TimeoutException());
                    }
                } else {
                    this._idle.await();
                }
            }
            this._blocker._state = null;
            x = this._blocker;
        } catch (InterruptedException var7) {
            throw new InterruptedIOException();
        } finally {
            this._lock.unlock();
        }
        return x;
    }

    protected void notComplete(SharedBlockingCallback.Blocker blocker) {
        LOG.warn("Blocker not complete {}", blocker);
        if (LOG.isDebugEnabled()) {
            LOG.debug(new Throwable());
        }
    }

    public class Blocker implements Callback, Closeable {

        private Throwable _state = SharedBlockingCallback.IDLE;

        protected Blocker() {
        }

        @Override
        public Invocable.InvocationType getInvocationType() {
            return Invocable.InvocationType.NON_BLOCKING;
        }

        @Override
        public void succeeded() {
            SharedBlockingCallback.this._lock.lock();
            try {
                if (this._state != null) {
                    throw new IllegalStateException(this._state);
                }
                this._state = SharedBlockingCallback.SUCCEEDED;
                SharedBlockingCallback.this._complete.signalAll();
            } finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }

        @Override
        public void failed(Throwable cause) {
            SharedBlockingCallback.this._lock.lock();
            try {
                if (this._state == null) {
                    if (cause == null) {
                        this._state = SharedBlockingCallback.FAILED;
                    } else if (cause instanceof SharedBlockingCallback.BlockerTimeoutException) {
                        this._state = new IOException(cause);
                    } else {
                        this._state = cause;
                    }
                    SharedBlockingCallback.this._complete.signalAll();
                } else if (!(this._state instanceof SharedBlockingCallback.BlockerTimeoutException)) {
                    throw new IllegalStateException(this._state);
                }
            } finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }

        public void block() throws IOException {
            long idle = SharedBlockingCallback.this.getIdleTimeout();
            SharedBlockingCallback.this._lock.lock();
            try {
                while (this._state == null) {
                    if (idle > 0L) {
                        long excess = Math.min(idle / 2L, 1000L);
                        if (!SharedBlockingCallback.this._complete.await(idle + excess, TimeUnit.MILLISECONDS)) {
                            this._state = new SharedBlockingCallback.BlockerTimeoutException();
                        }
                    } else {
                        SharedBlockingCallback.this._complete.await();
                    }
                }
                if (this._state != SharedBlockingCallback.SUCCEEDED) {
                    if (this._state == SharedBlockingCallback.IDLE) {
                        throw new IllegalStateException("IDLE");
                    }
                    if (this._state instanceof IOException) {
                        throw (IOException) this._state;
                    }
                    if (this._state instanceof CancellationException) {
                        throw (CancellationException) this._state;
                    }
                    if (this._state instanceof RuntimeException) {
                        throw (RuntimeException) this._state;
                    }
                    if (this._state instanceof Error) {
                        throw (Error) this._state;
                    }
                    throw new IOException(this._state);
                }
            } catch (InterruptedException var8) {
                throw new InterruptedIOException();
            } finally {
                SharedBlockingCallback.this._lock.unlock();
            }
        }

        public void close() {
            SharedBlockingCallback.this._lock.lock();
            try {
                if (this._state == SharedBlockingCallback.IDLE) {
                    throw new IllegalStateException("IDLE");
                }
                if (this._state == null) {
                    SharedBlockingCallback.this.notComplete(this);
                }
            } finally {
                try {
                    if (this._state instanceof SharedBlockingCallback.BlockerTimeoutException) {
                        SharedBlockingCallback.this._blocker = SharedBlockingCallback.this.new Blocker();
                    } else {
                        this._state = SharedBlockingCallback.IDLE;
                    }
                    SharedBlockingCallback.this._idle.signalAll();
                    SharedBlockingCallback.this._complete.signalAll();
                } finally {
                    SharedBlockingCallback.this._lock.unlock();
                }
            }
        }

        public String toString() {
            SharedBlockingCallback.this._lock.lock();
            String var1;
            try {
                var1 = String.format("%s@%x{%s}", SharedBlockingCallback.Blocker.class.getSimpleName(), this.hashCode(), this._state);
            } finally {
                SharedBlockingCallback.this._lock.unlock();
            }
            return var1;
        }
    }

    private static class BlockerTimeoutException extends TimeoutException {

        private BlockerTimeoutException() {
        }
    }
}