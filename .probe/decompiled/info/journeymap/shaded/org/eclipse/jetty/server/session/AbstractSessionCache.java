package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ManagedObject
public abstract class AbstractSessionCache extends ContainerLifeCycle implements SessionCache {

    static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    protected SessionDataStore _sessionDataStore;

    protected final SessionHandler _handler;

    protected SessionContext _context;

    protected int _evictionPolicy = -1;

    protected boolean _saveOnCreate = false;

    protected boolean _saveOnInactiveEviction;

    protected boolean _removeUnloadableSessions;

    @Override
    public abstract Session newSession(SessionData var1);

    public abstract Session newSession(HttpServletRequest var1, SessionData var2);

    public abstract Session doGet(String var1);

    public abstract Session doPutIfAbsent(String var1, Session var2);

    public abstract boolean doReplace(String var1, Session var2, Session var3);

    public abstract Session doDelete(String var1);

    public AbstractSessionCache(SessionHandler handler) {
        this._handler = handler;
    }

    @Override
    public SessionHandler getSessionHandler() {
        return this._handler;
    }

    @Override
    public void initialize(SessionContext context) {
        if (this.isStarted()) {
            throw new IllegalStateException("Context set after session store started");
        } else {
            this._context = context;
        }
    }

    @Override
    protected void doStart() throws Exception {
        if (this._sessionDataStore == null) {
            throw new IllegalStateException("No session data store configured");
        } else if (this._handler == null) {
            throw new IllegalStateException("No session manager");
        } else if (this._context == null) {
            throw new IllegalStateException("No ContextId");
        } else {
            this._sessionDataStore.initialize(this._context);
            super.doStart();
        }
    }

    @Override
    protected void doStop() throws Exception {
        this._sessionDataStore.stop();
        super.doStop();
    }

    @Override
    public SessionDataStore getSessionDataStore() {
        return this._sessionDataStore;
    }

    @Override
    public void setSessionDataStore(SessionDataStore sessionStore) {
        this.updateBean(this._sessionDataStore, sessionStore);
        this._sessionDataStore = sessionStore;
    }

    @ManagedAttribute(value = "session eviction policy", readonly = true)
    @Override
    public int getEvictionPolicy() {
        return this._evictionPolicy;
    }

    @Override
    public void setEvictionPolicy(int evictionTimeout) {
        this._evictionPolicy = evictionTimeout;
    }

    @ManagedAttribute(value = "immediately save new sessions", readonly = true)
    @Override
    public boolean isSaveOnCreate() {
        return this._saveOnCreate;
    }

    @Override
    public void setSaveOnCreate(boolean saveOnCreate) {
        this._saveOnCreate = saveOnCreate;
    }

    @ManagedAttribute(value = "delete unreadable stored sessions", readonly = true)
    @Override
    public boolean isRemoveUnloadableSessions() {
        return this._removeUnloadableSessions;
    }

    @Override
    public void setRemoveUnloadableSessions(boolean removeUnloadableSessions) {
        this._removeUnloadableSessions = removeUnloadableSessions;
    }

    @Override
    public Session get(String id) throws Exception {
        Session session = null;
        Exception ex = null;
        while (true) {
            session = this.doGet(id);
            if (this._sessionDataStore == null) {
                break;
            }
            if (session == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Session {} not found locally, attempting to load", id);
                }
                AbstractSessionCache.PlaceHolderSession phs = new AbstractSessionCache.PlaceHolderSession(new SessionData(id, null, null, 0L, 0L, 0L, 0L));
                Locker.Lock phsLock = phs.lock();
                Session s = this.doPutIfAbsent(id, phs);
                if (s == null) {
                    try {
                        session = this.loadSession(id);
                        if (session == null) {
                            this.doDelete(id);
                            phsLock.close();
                        } else {
                            try (Locker.Lock lock = session.lock()) {
                                boolean success = this.doReplace(id, phs, session);
                                if (!success) {
                                    this.doDelete(id);
                                    session = null;
                                    LOG.warn("Replacement of placeholder for session {} failed", id);
                                    phsLock.close();
                                } else {
                                    session.setResident(true);
                                    session.updateInactivityTimer();
                                    phsLock.close();
                                }
                            }
                        }
                    } catch (Exception var64) {
                        ex = var64;
                        this.doDelete(id);
                        phsLock.close();
                        session = null;
                    }
                    break;
                }
                phsLock.close();
                try (Locker.Lock lockx = s.lock()) {
                    if (s.isResident() && !(s instanceof AbstractSessionCache.PlaceHolderSession)) {
                        session = s;
                        break;
                    }
                    session = null;
                }
            } else {
                try (Locker.Lock lockx = session.lock()) {
                    if (session.isResident() && !(session instanceof AbstractSessionCache.PlaceHolderSession)) {
                        break;
                    }
                    session = null;
                }
            }
        }
        if (ex != null) {
            throw ex;
        } else {
            return session;
        }
    }

    private Session loadSession(String id) throws Exception {
        SessionData data = null;
        Session session = null;
        if (this._sessionDataStore == null) {
            return null;
        } else {
            try {
                data = this._sessionDataStore.load(id);
                if (data == null) {
                    return null;
                } else {
                    data.setLastNode(this._context.getWorkerName());
                    return this.newSession(data);
                }
            } catch (UnreadableSessionDataException var5) {
                if (this.isRemoveUnloadableSessions()) {
                    this._sessionDataStore.delete(id);
                }
                throw var5;
            }
        }
    }

    @Override
    public void put(String id, Session session) throws Exception {
        if (id != null && session != null) {
            try (Locker.Lock lock = session.lock()) {
                if (session.getSessionHandler() == null) {
                    throw new IllegalStateException("Session " + id + " is not managed");
                }
                if (!session.isValid()) {
                    return;
                }
                if (this._sessionDataStore == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No SessionDataStore, putting into SessionCache only id={}", id);
                    }
                    session.setResident(true);
                    if (this.doPutIfAbsent(id, session) == null) {
                        session.updateInactivityTimer();
                    }
                    return;
                }
                if (session.getRequests() <= 0L) {
                    if (!this._sessionDataStore.isPassivating()) {
                        this._sessionDataStore.store(id, session.getSessionData());
                        if (this.getEvictionPolicy() == 0) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Eviction on request exit id={}", id);
                            }
                            this.doDelete(session.getId());
                            session.setResident(false);
                        } else {
                            session.setResident(true);
                            if (this.doPutIfAbsent(id, session) == null) {
                                session.updateInactivityTimer();
                            }
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Non passivating SessionDataStore, session in SessionCache only id={}", id);
                            }
                        }
                    } else {
                        session.willPassivate();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Session passivating id={}", id);
                        }
                        this._sessionDataStore.store(id, session.getSessionData());
                        if (this.getEvictionPolicy() == 0) {
                            this.doDelete(id);
                            session.setResident(false);
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Evicted on request exit id={}", id);
                            }
                        } else {
                            session.didActivate();
                            session.setResident(true);
                            if (this.doPutIfAbsent(id, session) == null) {
                                session.updateInactivityTimer();
                            }
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Session reactivated id={}", id);
                            }
                        }
                    }
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Req count={} for id={}", session.getRequests(), id);
                    }
                    session.setResident(true);
                    if (this.doPutIfAbsent(id, session) == null) {
                        session.updateInactivityTimer();
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Put key=" + id + " session=" + (session == null ? "null" : session.getId()));
        }
    }

    @Override
    public boolean exists(String id) throws Exception {
        Session s = this.doGet(id);
        if (s != null) {
            boolean var5;
            try (Locker.Lock lock = s.lock()) {
                var5 = s.isValid();
            }
            return var5;
        } else {
            return this._sessionDataStore.exists(id);
        }
    }

    @Override
    public boolean contains(String id) throws Exception {
        return this.doGet(id) != null;
    }

    @Override
    public Session delete(String id) throws Exception {
        Session session = this.get(id);
        if (this._sessionDataStore != null) {
            boolean dsdel = this._sessionDataStore.delete(id);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Session {} deleted in db {}", id, dsdel);
            }
        }
        if (session != null) {
            session.stopInactivityTimer();
            session.setResident(false);
        }
        return this.doDelete(id);
    }

    @Override
    public Set<String> checkExpiration(Set<String> candidates) {
        if (!this.isStarted()) {
            return Collections.emptySet();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("SessionDataStore checking expiration on {}", candidates);
            }
            Set<String> allCandidates = this._sessionDataStore.getExpired(candidates);
            Set<String> sessionsInUse = new HashSet();
            if (allCandidates != null) {
                for (String c : allCandidates) {
                    Session s = this.doGet(c);
                    if (s != null && s.getRequests() > 0L) {
                        sessionsInUse.add(c);
                    }
                }
                try {
                    allCandidates.removeAll(sessionsInUse);
                } catch (UnsupportedOperationException var7) {
                    Set<String> tmp = new HashSet(allCandidates);
                    tmp.removeAll(sessionsInUse);
                    allCandidates = tmp;
                }
            }
            return allCandidates;
        }
    }

    @Override
    public void checkInactiveSession(Session session) {
        if (session != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Checking for idle {}", session.getId());
            }
            try (Locker.Lock s = session.lock()) {
                if (this.getEvictionPolicy() > 0 && session.isIdleLongerThan(this.getEvictionPolicy()) && session.isValid() && session.isResident() && session.getRequests() <= 0L) {
                    try {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Evicting idle session {}", session.getId());
                        }
                        if (this.isSaveOnInactiveEviction() && this._sessionDataStore != null) {
                            if (this._sessionDataStore.isPassivating()) {
                                session.willPassivate();
                            }
                            this._sessionDataStore.store(session.getId(), session.getSessionData());
                        }
                        this.doDelete(session.getId());
                        session.setResident(false);
                    } catch (Exception var13) {
                        LOG.warn("Passivation of idle session {} failed", session.getId(), var13);
                        session.updateInactivityTimer();
                    }
                }
            }
        }
    }

    @Override
    public Session renewSessionId(String oldId, String newId) throws Exception {
        if (StringUtil.isBlank(oldId)) {
            throw new IllegalArgumentException("Old session id is null");
        } else if (StringUtil.isBlank(newId)) {
            throw new IllegalArgumentException("New session id is null");
        } else {
            Session session = this.get(oldId);
            if (session == null) {
                return null;
            } else {
                Session var6;
                try (Locker.Lock lock = session.lock()) {
                    session.checkValidForWrite();
                    session.getSessionData().setId(newId);
                    session.getSessionData().setLastSaved(0L);
                    session.getSessionData().setDirty(true);
                    this.doPutIfAbsent(newId, session);
                    this.doDelete(oldId);
                    if (this._sessionDataStore != null) {
                        this._sessionDataStore.delete(oldId);
                        this._sessionDataStore.store(newId, session.getSessionData());
                    }
                    LOG.info("Session id {} swapped for new id {}", oldId, newId);
                    var6 = session;
                }
                return var6;
            }
        }
    }

    @Override
    public void setSaveOnInactiveEviction(boolean saveOnEvict) {
        this._saveOnInactiveEviction = saveOnEvict;
    }

    @ManagedAttribute(value = "save sessions before evicting from cache", readonly = true)
    @Override
    public boolean isSaveOnInactiveEviction() {
        return this._saveOnInactiveEviction;
    }

    @Override
    public Session newSession(HttpServletRequest request, String id, long time, long maxInactiveMs) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new session id=" + id);
        }
        Session session = this.newSession(request, this._sessionDataStore.newSessionData(id, time, time, time, maxInactiveMs));
        session.getSessionData().setLastNode(this._context.getWorkerName());
        try {
            if (this.isSaveOnCreate() && this._sessionDataStore != null) {
                this._sessionDataStore.store(id, session.getSessionData());
            }
        } catch (Exception var9) {
            LOG.warn("Save of new session {} failed", id, var9);
        }
        return session;
    }

    public String toString() {
        return String.format("%s@%x[evict=%d,removeUnloadable=%b,saveOnCreate=%b,saveOnInactiveEvict=%b]", this.getClass().getName(), this.hashCode(), this._evictionPolicy, this._removeUnloadableSessions, this._saveOnCreate, this._saveOnInactiveEviction);
    }

    protected class PlaceHolderSession extends Session {

        public PlaceHolderSession(SessionData data) {
            super(null, data);
        }
    }
}