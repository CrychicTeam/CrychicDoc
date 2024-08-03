package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.server.SessionIdManager;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.AbstractLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import java.util.concurrent.TimeUnit;

@ManagedObject
public class HouseKeeper extends AbstractLifeCycle {

    private static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    public static final long DEFAULT_PERIOD_MS = 600000L;

    protected SessionIdManager _sessionIdManager;

    protected Scheduler _scheduler;

    protected Scheduler.Task _task;

    protected HouseKeeper.Runner _runner;

    protected boolean _ownScheduler = false;

    private long _intervalMs = 600000L;

    public void setSessionIdManager(SessionIdManager sessionIdManager) {
        this._sessionIdManager = sessionIdManager;
    }

    @Override
    protected void doStart() throws Exception {
        if (this._sessionIdManager == null) {
            throw new IllegalStateException("No SessionIdManager for Housekeeper");
        } else {
            this.setIntervalSec(this.getIntervalSec());
            super.doStart();
        }
    }

    protected void findScheduler() throws Exception {
        if (this._scheduler == null) {
            if (this._sessionIdManager instanceof DefaultSessionIdManager) {
                this._scheduler = ((DefaultSessionIdManager) this._sessionIdManager).getServer().getBean(Scheduler.class);
            }
            if (this._scheduler == null) {
                this._scheduler = new ScheduledExecutorScheduler();
                this._ownScheduler = true;
                this._scheduler.start();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Using own scheduler for scavenging");
                }
            } else if (!this._scheduler.isStarted()) {
                throw new IllegalStateException("Shared scheduler not started");
            }
        }
    }

    protected void startScavenging() throws Exception {
        synchronized (this) {
            if (this._scheduler != null) {
                if (this._task != null) {
                    this._task.cancel();
                }
                if (this._runner == null) {
                    this._runner = new HouseKeeper.Runner();
                }
                LOG.info("Scavenging every {}ms", this._intervalMs);
                this._task = this._scheduler.schedule(this._runner, this._intervalMs, TimeUnit.MILLISECONDS);
            }
        }
    }

    protected void stopScavenging() throws Exception {
        synchronized (this) {
            if (this._task != null) {
                this._task.cancel();
                LOG.info("Stopped scavenging");
            }
            this._task = null;
            if (this._ownScheduler) {
                this._scheduler.stop();
                this._scheduler = null;
            }
        }
        this._runner = null;
    }

    @Override
    protected void doStop() throws Exception {
        synchronized (this) {
            this.stopScavenging();
            this._scheduler = null;
        }
        super.doStop();
    }

    public void setIntervalSec(long sec) throws Exception {
        if (!this.isStarted() && !this.isStarting()) {
            this._intervalMs = sec * 1000L;
        } else if (sec <= 0L) {
            this._intervalMs = 0L;
            LOG.info("Scavenging disabled");
            this.stopScavenging();
        } else {
            if (sec < 10L) {
                LOG.warn("Short interval of {}sec for session scavenging.", sec);
            }
            this._intervalMs = sec * 1000L;
            long tenPercent = this._intervalMs / 10L;
            if (System.currentTimeMillis() % 2L == 0L) {
                this._intervalMs += tenPercent;
            }
            if (this.isStarting() || this.isStarted()) {
                this.findScheduler();
                this.startScavenging();
            }
        }
    }

    @ManagedAttribute(value = "secs between scavenge cycles", readonly = true)
    public long getIntervalSec() {
        return this._intervalMs / 1000L;
    }

    public void scavenge() {
        if (!this.isStopping() && !this.isStopped()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Scavenging sessions");
            }
            for (SessionHandler manager : this._sessionIdManager.getSessionHandlers()) {
                if (manager != null) {
                    try {
                        manager.scavenge();
                    } catch (Exception var4) {
                        LOG.warn(var4);
                    }
                }
            }
        }
    }

    public String toString() {
        return super.toString() + "[interval=" + this._intervalMs + ", ownscheduler=" + this._ownScheduler + "]";
    }

    protected class Runner implements Runnable {

        public void run() {
            try {
                HouseKeeper.this.scavenge();
            } finally {
                if (HouseKeeper.this._scheduler != null && HouseKeeper.this._scheduler.isRunning()) {
                    HouseKeeper.this._task = HouseKeeper.this._scheduler.schedule(this, HouseKeeper.this._intervalMs, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}