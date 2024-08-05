package info.journeymap.shaded.org.eclipse.jetty.io;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class IdleTimeout {

    private static final Logger LOG = Log.getLogger(IdleTimeout.class);

    private final Scheduler _scheduler;

    private final AtomicReference<Scheduler.Task> _timeout = new AtomicReference();

    private volatile long _idleTimeout;

    private volatile long _idleTimestamp = System.currentTimeMillis();

    private final Runnable _idleTask = new Runnable() {

        public void run() {
            long idleLeft = IdleTimeout.this.checkIdleTimeout();
            if (idleLeft >= 0L) {
                IdleTimeout.this.scheduleIdleTimeout(idleLeft > 0L ? idleLeft : IdleTimeout.this.getIdleTimeout());
            }
        }
    };

    public IdleTimeout(Scheduler scheduler) {
        this._scheduler = scheduler;
    }

    public Scheduler getScheduler() {
        return this._scheduler;
    }

    public long getIdleTimestamp() {
        return this._idleTimestamp;
    }

    public long getIdleFor() {
        return System.currentTimeMillis() - this.getIdleTimestamp();
    }

    public long getIdleTimeout() {
        return this._idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        long old = this._idleTimeout;
        this._idleTimeout = idleTimeout;
        if (old > 0L) {
            if (old <= idleTimeout) {
                return;
            }
            this.deactivate();
        }
        if (this.isOpen()) {
            this.activate();
        }
    }

    public void notIdle() {
        this._idleTimestamp = System.currentTimeMillis();
    }

    private void scheduleIdleTimeout(long delay) {
        Scheduler.Task newTimeout = null;
        if (this.isOpen() && delay > 0L && this._scheduler != null) {
            newTimeout = this._scheduler.schedule(this._idleTask, delay, TimeUnit.MILLISECONDS);
        }
        Scheduler.Task oldTimeout = (Scheduler.Task) this._timeout.getAndSet(newTimeout);
        if (oldTimeout != null) {
            oldTimeout.cancel();
        }
    }

    public void onOpen() {
        this.activate();
    }

    private void activate() {
        if (this._idleTimeout > 0L) {
            this._idleTask.run();
        }
    }

    public void onClose() {
        this.deactivate();
    }

    private void deactivate() {
        Scheduler.Task oldTimeout = (Scheduler.Task) this._timeout.getAndSet(null);
        if (oldTimeout != null) {
            oldTimeout.cancel();
        }
    }

    protected long checkIdleTimeout() {
        if (this.isOpen()) {
            long idleTimestamp = this.getIdleTimestamp();
            long idleTimeout = this.getIdleTimeout();
            long idleElapsed = System.currentTimeMillis() - idleTimestamp;
            long idleLeft = idleTimeout - idleElapsed;
            if (LOG.isDebugEnabled()) {
                LOG.debug("{} idle timeout check, elapsed: {} ms, remaining: {} ms", this, idleElapsed, idleLeft);
            }
            if (idleTimestamp != 0L && idleTimeout > 0L && idleLeft <= 0L) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("{} idle timeout expired", this);
                }
                try {
                    this.onIdleExpired(new TimeoutException("Idle timeout expired: " + idleElapsed + "/" + idleTimeout + " ms"));
                } finally {
                    this.notIdle();
                }
            }
            return idleLeft >= 0L ? idleLeft : 0L;
        } else {
            return -1L;
        }
    }

    protected abstract void onIdleExpired(TimeoutException var1);

    public abstract boolean isOpen();
}