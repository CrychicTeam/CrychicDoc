package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.statistic.CounterStatistic;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;

@ManagedObject
public class DefaultSessionCache extends AbstractSessionCache {

    private static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    protected ConcurrentHashMap<String, Session> _sessions = new ConcurrentHashMap();

    private final CounterStatistic _stats = new CounterStatistic();

    public DefaultSessionCache(SessionHandler manager) {
        super(manager);
    }

    @ManagedAttribute(value = "current sessions in cache", readonly = true)
    public long getSessionsCurrent() {
        return this._stats.getCurrent();
    }

    @ManagedAttribute(value = "max sessions in cache", readonly = true)
    public long getSessionsMax() {
        return this._stats.getMax();
    }

    @ManagedAttribute(value = "total sessions in cache", readonly = true)
    public long getSessionsTotal() {
        return this._stats.getTotal();
    }

    @ManagedOperation(value = "reset statistics", impact = "ACTION")
    public void resetStats() {
        this._stats.reset();
    }

    @Override
    public Session doGet(String id) {
        return id == null ? null : (Session) this._sessions.get(id);
    }

    @Override
    public Session doPutIfAbsent(String id, Session session) {
        Session s = (Session) this._sessions.putIfAbsent(id, session);
        if (s == null && !(session instanceof AbstractSessionCache.PlaceHolderSession)) {
            this._stats.increment();
        }
        return s;
    }

    @Override
    public Session doDelete(String id) {
        Session s = (Session) this._sessions.remove(id);
        if (s != null && !(s instanceof AbstractSessionCache.PlaceHolderSession)) {
            this._stats.decrement();
        }
        return s;
    }

    @Override
    public void shutdown() {
        int loop = 100;
        while (!this._sessions.isEmpty() && loop-- > 0) {
            for (Session session : this._sessions.values()) {
                if (this._sessionDataStore != null) {
                    session.willPassivate();
                    try {
                        this._sessionDataStore.store(session.getId(), session.getSessionData());
                    } catch (Exception var6) {
                        LOG.warn(var6);
                    }
                    this.doDelete(session.getId());
                } else {
                    try {
                        session.invalidate();
                    } catch (Exception var5) {
                        LOG.ignore(var5);
                    }
                }
            }
        }
    }

    @Override
    public Session newSession(HttpServletRequest request, SessionData data) {
        return new Session(this.getSessionHandler(), request, data);
    }

    @Override
    public Session newSession(SessionData data) {
        return new Session(this.getSessionHandler(), data);
    }

    @Override
    public boolean doReplace(String id, Session oldValue, Session newValue) {
        boolean result = this._sessions.replace(id, oldValue, newValue);
        if (result && oldValue instanceof AbstractSessionCache.PlaceHolderSession) {
            this._stats.increment();
        }
        return result;
    }
}