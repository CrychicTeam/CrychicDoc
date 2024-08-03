package info.journeymap.shaded.org.eclipse.jetty.util;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class FutureCallback implements Future<Void>, Callback {

    private static Throwable COMPLETED = new ConstantThrowable();

    private final AtomicBoolean _done = new AtomicBoolean(false);

    private final CountDownLatch _latch = new CountDownLatch(1);

    private Throwable _cause;

    public FutureCallback() {
    }

    public FutureCallback(boolean completed) {
        if (completed) {
            this._cause = COMPLETED;
            this._done.set(true);
            this._latch.countDown();
        }
    }

    public FutureCallback(Throwable failed) {
        this._cause = failed;
        this._done.set(true);
        this._latch.countDown();
    }

    @Override
    public void succeeded() {
        if (this._done.compareAndSet(false, true)) {
            this._cause = COMPLETED;
            this._latch.countDown();
        }
    }

    @Override
    public void failed(Throwable cause) {
        if (this._done.compareAndSet(false, true)) {
            this._cause = cause;
            this._latch.countDown();
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this._done.compareAndSet(false, true)) {
            this._cause = new CancellationException();
            this._latch.countDown();
            return true;
        } else {
            return false;
        }
    }

    public boolean isCancelled() {
        if (this._done.get()) {
            try {
                this._latch.await();
            } catch (InterruptedException var2) {
                throw new RuntimeException(var2);
            }
            return this._cause instanceof CancellationException;
        } else {
            return false;
        }
    }

    public boolean isDone() {
        return this._done.get() && this._latch.getCount() == 0L;
    }

    public Void get() throws InterruptedException, ExecutionException {
        this._latch.await();
        if (this._cause == COMPLETED) {
            return null;
        } else if (this._cause instanceof CancellationException) {
            throw (CancellationException) new CancellationException().initCause(this._cause);
        } else {
            throw new ExecutionException(this._cause);
        }
    }

    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!this._latch.await(timeout, unit)) {
            throw new TimeoutException();
        } else if (this._cause == COMPLETED) {
            return null;
        } else if (this._cause instanceof TimeoutException) {
            throw (TimeoutException) this._cause;
        } else if (this._cause instanceof CancellationException) {
            throw (CancellationException) new CancellationException().initCause(this._cause);
        } else {
            throw new ExecutionException(this._cause);
        }
    }

    public static void rethrow(ExecutionException e) throws IOException {
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
            throw (IOException) cause;
        } else if (cause instanceof Error) {
            throw (Error) cause;
        } else if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
        } else {
            throw new RuntimeException(cause);
        }
    }

    public String toString() {
        return String.format("FutureCallback@%x{%b,%b}", this.hashCode(), this._done.get(), this._cause == COMPLETED);
    }
}