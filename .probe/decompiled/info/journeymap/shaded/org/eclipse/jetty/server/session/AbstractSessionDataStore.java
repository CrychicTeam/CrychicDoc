package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@ManagedObject
public abstract class AbstractSessionDataStore extends ContainerLifeCycle implements SessionDataStore {

    static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    protected SessionContext _context;

    protected int _gracePeriodSec = 3600;

    protected long _lastExpiryCheckTime = 0L;

    protected int _savePeriodSec = 0;

    public abstract void doStore(String var1, SessionData var2, long var3) throws Exception;

    public abstract Set<String> doGetExpired(Set<String> var1);

    @Override
    public void initialize(SessionContext context) throws Exception {
        if (this.isStarted()) {
            throw new IllegalStateException("Context set after SessionDataStore started");
        } else {
            this._context = context;
        }
    }

    @Override
    public void store(String id, SessionData data) throws Exception {
        if (data != null) {
            long lastSave = data.getLastSaved();
            long savePeriodMs = this._savePeriodSec <= 0 ? 0L : TimeUnit.SECONDS.toMillis((long) this._savePeriodSec);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Store: id={}, dirty={}, lsave={}, period={}, elapsed={}", id, data.isDirty(), data.getLastSaved(), savePeriodMs, System.currentTimeMillis() - lastSave);
            }
            if (data.isDirty() || lastSave <= 0L || System.currentTimeMillis() - lastSave > savePeriodMs) {
                data.setLastSaved(System.currentTimeMillis());
                try {
                    this.doStore(id, data, lastSave);
                    data.setDirty(false);
                } catch (Exception var8) {
                    data.setLastSaved(lastSave);
                    throw var8;
                }
            }
        }
    }

    @Override
    public Set<String> getExpired(Set<String> candidates) {
        Set var2;
        try {
            var2 = this.doGetExpired(candidates);
        } finally {
            this._lastExpiryCheckTime = System.currentTimeMillis();
        }
        return var2;
    }

    @Override
    public SessionData newSessionData(String id, long created, long accessed, long lastAccessed, long maxInactiveMs) {
        return new SessionData(id, this._context.getCanonicalContextPath(), this._context.getVhost(), created, accessed, lastAccessed, maxInactiveMs);
    }

    protected void checkStarted() throws IllegalStateException {
        if (this.isStarted()) {
            throw new IllegalStateException("Already started");
        }
    }

    @Override
    protected void doStart() throws Exception {
        if (this._context == null) {
            throw new IllegalStateException("No SessionContext");
        } else {
            super.doStart();
        }
    }

    @ManagedAttribute(value = "interval in secs to prevent too eager session scavenging", readonly = true)
    public int getGracePeriodSec() {
        return this._gracePeriodSec;
    }

    public void setGracePeriodSec(int sec) {
        this._gracePeriodSec = sec;
    }

    @ManagedAttribute(value = "min secs between saves", readonly = true)
    public int getSavePeriodSec() {
        return this._savePeriodSec;
    }

    public void setSavePeriodSec(int savePeriodSec) {
        this._savePeriodSec = savePeriodSec;
    }

    public String toString() {
        return String.format("%s@%x[passivating=%b,graceSec=%d]", this.getClass().getName(), this.hashCode(), this.isPassivating(), this.getGracePeriodSec());
    }
}