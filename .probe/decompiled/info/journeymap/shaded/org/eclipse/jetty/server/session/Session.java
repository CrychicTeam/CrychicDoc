package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.io.IdleTimeout;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionActivationListener;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionBindingEvent;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionBindingListener;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionContext;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Session implements SessionHandler.SessionIf {

    private static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    public static final String SESSION_CREATED_SECURE = "info.journeymap.shaded.org.eclipse.jetty.security.sessionCreatedSecure";

    protected SessionData _sessionData;

    protected SessionHandler _handler;

    protected String _extendedId;

    protected long _requests;

    protected boolean _idChanged;

    protected boolean _newSession;

    protected Session.State _state = Session.State.VALID;

    protected Locker _lock = new Locker();

    protected boolean _resident = false;

    protected Session.SessionInactivityTimeout _sessionInactivityTimer = null;

    public Session(SessionHandler handler, HttpServletRequest request, SessionData data) {
        this._handler = handler;
        this._sessionData = data;
        this._newSession = true;
        this._sessionData.setDirty(true);
        this._requests = 1L;
    }

    public Session(SessionHandler handler, SessionData data) {
        this._handler = handler;
        this._sessionData = data;
    }

    public long getRequests() {
        long var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = this._requests;
        }
        return var3;
    }

    public void setExtendedId(String extendedId) {
        this._extendedId = extendedId;
    }

    protected void cookieSet() {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this._sessionData.setCookieSet(this._sessionData.getAccessed());
        }
    }

    protected boolean access(long time) {
        boolean var7;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            if (!this.isValid()) {
                return false;
            }
            this._newSession = false;
            long lastAccessed = this._sessionData.getAccessed();
            if (this._sessionInactivityTimer != null) {
                this._sessionInactivityTimer.notIdle();
            }
            this._sessionData.setAccessed(time);
            this._sessionData.setLastAccessed(lastAccessed);
            this._sessionData.calcAndSetExpiry(time);
            if (!this.isExpiredAt(time)) {
                this._requests++;
                return true;
            }
            this.invalidate();
            var7 = false;
        }
        return var7;
    }

    protected void complete() {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this._requests--;
        }
    }

    protected boolean isExpiredAt(long time) {
        boolean var5;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var5 = this._sessionData.isExpiredAt(time);
        }
        return var5;
    }

    protected boolean isIdleLongerThan(int sec) {
        long now = System.currentTimeMillis();
        boolean var6;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var6 = this._sessionData.getAccessed() + (long) (sec * 1000) <= now;
        }
        return var6;
    }

    protected void callSessionAttributeListeners(String name, Object newValue, Object oldValue) {
        if (newValue == null || !newValue.equals(oldValue)) {
            if (oldValue != null) {
                this.unbindValue(name, oldValue);
            }
            if (newValue != null) {
                this.bindValue(name, newValue);
            }
            if (this._handler == null) {
                throw new IllegalStateException("No session manager for session " + this._sessionData.getId());
            }
            this._handler.doSessionAttributeListeners(this, name, oldValue, newValue);
        }
    }

    public void unbindValue(String name, Object value) {
        if (value != null && value instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this, name));
        }
    }

    public void bindValue(String name, Object value) {
        if (value != null && value instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener) value).valueBound(new HttpSessionBindingEvent(this, name));
        }
    }

    public void didActivate() {
        HttpSessionEvent event = new HttpSessionEvent(this);
        Iterator<String> iter = this._sessionData.getKeys().iterator();
        while (iter.hasNext()) {
            Object value = this._sessionData.getAttribute((String) iter.next());
            if (value instanceof HttpSessionActivationListener) {
                HttpSessionActivationListener listener = (HttpSessionActivationListener) value;
                listener.sessionDidActivate(event);
            }
        }
    }

    public void willPassivate() {
        HttpSessionEvent event = new HttpSessionEvent(this);
        Iterator<String> iter = this._sessionData.getKeys().iterator();
        while (iter.hasNext()) {
            Object value = this._sessionData.getAttribute((String) iter.next());
            if (value instanceof HttpSessionActivationListener) {
                HttpSessionActivationListener listener = (HttpSessionActivationListener) value;
                listener.sessionWillPassivate(event);
            }
        }
    }

    public boolean isValid() {
        boolean var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = this._state == Session.State.VALID;
        }
        return var3;
    }

    public long getCookieSetTime() {
        long var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = this._sessionData.getCookieSet();
        }
        return var3;
    }

    @Override
    public long getCreationTime() throws IllegalStateException {
        long var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this.checkValidForRead();
            var3 = this._sessionData.getCreated();
        }
        return var3;
    }

    @Override
    public String getId() {
        String var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = this._sessionData.getId();
        }
        return var3;
    }

    public String getExtendedId() {
        return this._extendedId;
    }

    public String getContextPath() {
        return this._sessionData.getContextPath();
    }

    public String getVHost() {
        return this._sessionData.getVhost();
    }

    @Override
    public long getLastAccessedTime() {
        long var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = this._sessionData.getLastAccessed();
        }
        return var3;
    }

    @Override
    public ServletContext getServletContext() {
        if (this._handler == null) {
            throw new IllegalStateException("No session manager for session " + this._sessionData.getId());
        } else {
            return this._handler._context;
        }
    }

    @Override
    public void setMaxInactiveInterval(int secs) {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this._sessionData.setMaxInactiveMs((long) secs * 1000L);
            this._sessionData.calcAndSetExpiry();
            this._sessionData.setDirty(true);
            this.updateInactivityTimer();
            if (LOG.isDebugEnabled()) {
                if (secs <= 0) {
                    LOG.debug("Session {} is now immortal (maxInactiveInterval={})", this._sessionData.getId(), secs);
                } else {
                    LOG.debug("Session {} maxInactiveInterval={}", this._sessionData.getId(), secs);
                }
            }
        }
    }

    public void updateInactivityTimer() {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateInactivityTimer");
            }
            long maxInactive = this._sessionData.getMaxInactiveMs();
            int evictionPolicy = this.getSessionHandler().getSessionCache().getEvictionPolicy();
            if (maxInactive <= 0L) {
                if (evictionPolicy < 1) {
                    this.setInactivityTimer(-1L);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Session is immortal && no inactivity eviction: timer cancelled");
                    }
                } else {
                    this.setInactivityTimer(TimeUnit.SECONDS.toMillis((long) evictionPolicy));
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Session is immortal; evict after {} sec inactivity", (long) evictionPolicy);
                    }
                }
            } else if (evictionPolicy < 1) {
                this.setInactivityTimer(this._sessionData.getMaxInactiveMs());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No inactive session eviction");
                }
            } else {
                this.setInactivityTimer(Math.min(maxInactive, TimeUnit.SECONDS.toMillis((long) evictionPolicy)));
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Inactivity timer set to lesser of maxInactive={} and inactivityEvict={}", maxInactive, evictionPolicy);
                }
            }
        }
    }

    private void setInactivityTimer(long ms) {
        if (this._sessionInactivityTimer == null) {
            this._sessionInactivityTimer = new Session.SessionInactivityTimeout();
        }
        this._sessionInactivityTimer.setIdleTimeout(ms);
    }

    public void stopInactivityTimer() {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            if (this._sessionInactivityTimer != null) {
                this._sessionInactivityTimer.setIdleTimeout(-1L);
                this._sessionInactivityTimer = null;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Session timer stopped");
                }
            }
        }
    }

    @Override
    public int getMaxInactiveInterval() {
        int var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = (int) (this._sessionData.getMaxInactiveMs() / 1000L);
        }
        return var3;
    }

    @Deprecated
    @Override
    public HttpSessionContext getSessionContext() {
        this.checkValidForRead();
        return SessionHandler.__nullSessionContext;
    }

    public SessionHandler getSessionHandler() {
        return this._handler;
    }

    protected void checkValidForWrite() throws IllegalStateException {
        this.checkLocked();
        if (this._state == Session.State.INVALID) {
            throw new IllegalStateException("Not valid for write: id=" + this._sessionData.getId() + " created=" + this._sessionData.getCreated() + " accessed=" + this._sessionData.getAccessed() + " lastaccessed=" + this._sessionData.getLastAccessed() + " maxInactiveMs=" + this._sessionData.getMaxInactiveMs() + " expiry=" + this._sessionData.getExpiry());
        } else if (this._state != Session.State.INVALIDATING) {
            if (!this.isResident()) {
                throw new IllegalStateException("Not valid for write: id=" + this._sessionData.getId() + " not resident");
            }
        }
    }

    protected void checkValidForRead() throws IllegalStateException {
        this.checkLocked();
        if (this._state == Session.State.INVALID) {
            throw new IllegalStateException("Invalid for read: id=" + this._sessionData.getId() + " created=" + this._sessionData.getCreated() + " accessed=" + this._sessionData.getAccessed() + " lastaccessed=" + this._sessionData.getLastAccessed() + " maxInactiveMs=" + this._sessionData.getMaxInactiveMs() + " expiry=" + this._sessionData.getExpiry());
        } else if (this._state != Session.State.INVALIDATING) {
            if (!this.isResident()) {
                throw new IllegalStateException("Invalid for read: id=" + this._sessionData.getId() + " not resident");
            }
        }
    }

    protected void checkLocked() throws IllegalStateException {
        if (!this._lock.isLocked()) {
            throw new IllegalStateException("Session not locked");
        }
    }

    @Override
    public Object getAttribute(String name) {
        Object var4;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this.checkValidForRead();
            var4 = this._sessionData.getAttribute(name);
        }
        return var4;
    }

    @Deprecated
    @Override
    public Object getValue(String name) {
        Object var4;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var4 = this._sessionData.getAttribute(name);
        }
        return var4;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Enumeration var4;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this.checkValidForRead();
            final Iterator<String> itor = this._sessionData.getKeys().iterator();
            var4 = new Enumeration<String>() {

                public boolean hasMoreElements() {
                    return itor.hasNext();
                }

                public String nextElement() {
                    return (String) itor.next();
                }
            };
        }
        return var4;
    }

    public int getAttributes() {
        return this._sessionData.getKeys().size();
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(this._sessionData.getKeys());
    }

    @Deprecated
    @Override
    public String[] getValueNames() throws IllegalStateException {
        String[] names;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this.checkValidForRead();
            Iterator<String> itor = this._sessionData.getKeys().iterator();
            if (itor.hasNext()) {
                ArrayList<String> namesx = new ArrayList();
                while (itor.hasNext()) {
                    namesx.add(itor.next());
                }
                return (String[]) namesx.toArray(new String[namesx.size()]);
            }
            names = new String[0];
        }
        return names;
    }

    @Override
    public void setAttribute(String name, Object value) {
        Object old = null;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this.checkValidForWrite();
            old = this._sessionData.setAttribute(name, value);
        }
        if (value != null || old != null) {
            this.callSessionAttributeListeners(name, value, old);
        }
    }

    @Deprecated
    @Override
    public void putValue(String name, Object value) {
        this.setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        this.setAttribute(name, null);
    }

    @Deprecated
    @Override
    public void removeValue(String name) {
        this.setAttribute(name, null);
    }

    public void renewId(HttpServletRequest request) {
        if (this._handler == null) {
            throw new IllegalStateException("No session manager for session " + this._sessionData.getId());
        } else {
            String id = null;
            String extendedId = null;
            try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
                this.checkValidForWrite();
                id = this._sessionData.getId();
                extendedId = this.getExtendedId();
            }
            String newId = this._handler._sessionIdManager.renewSessionId(id, extendedId, request);
            try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
                this.checkValidForWrite();
                this._sessionData.setId(newId);
                this.setExtendedId(this._handler._sessionIdManager.getExtendedId(newId, request));
            }
            this.setIdChanged(true);
        }
    }

    @Override
    public void invalidate() {
        if (this._handler == null) {
            throw new IllegalStateException("No session manager for session " + this._sessionData.getId());
        } else {
            boolean result = this.beginInvalidate();
            try {
                if (result) {
                    this._handler.getSessionIdManager().invalidateAll(this._sessionData.getId());
                }
            } catch (Exception var3) {
                LOG.warn(var3);
            }
        }
    }

    public Locker.Lock lock() {
        return this._lock.lock();
    }

    public Locker.Lock lockIfNotHeld() {
        return this._lock.lockIfNotHeld();
    }

    protected boolean beginInvalidate() {
        boolean result = false;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            switch(this._state) {
                case INVALID:
                    throw new IllegalStateException();
                case VALID:
                    result = true;
                    this._state = Session.State.INVALIDATING;
                    break;
                default:
                    LOG.info("Session {} already being invalidated", this._sessionData.getId());
            }
        }
        return result;
    }

    @Deprecated
    protected void doInvalidate() throws IllegalStateException {
        this.finishInvalidate();
    }

    protected void finishInvalidate() throws IllegalStateException {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            try {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("invalidate {}", this._sessionData.getId());
                }
                if (this._state == Session.State.VALID || this._state == Session.State.INVALIDATING) {
                    Set<String> keys = null;
                    do {
                        keys = this._sessionData.getKeys();
                        for (String key : keys) {
                            Object old = this._sessionData.setAttribute(key, null);
                            if (old == null) {
                                return;
                            }
                            this.callSessionAttributeListeners(key, null, old);
                        }
                    } while (!keys.isEmpty());
                }
            } finally {
                this._state = Session.State.INVALID;
            }
        }
    }

    @Override
    public boolean isNew() throws IllegalStateException {
        boolean var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this.checkValidForRead();
            var3 = this._newSession;
        }
        return var3;
    }

    public void setIdChanged(boolean changed) {
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            this._idChanged = changed;
        }
    }

    public boolean isIdChanged() {
        boolean var3;
        try (Locker.Lock lock = this._lock.lockIfNotHeld()) {
            var3 = this._idChanged;
        }
        return var3;
    }

    @Override
    public Session getSession() {
        return this;
    }

    protected SessionData getSessionData() {
        return this._sessionData;
    }

    public void setResident(boolean resident) {
        this._resident = resident;
    }

    public boolean isResident() {
        return this._resident;
    }

    public class SessionInactivityTimeout extends IdleTimeout {

        public SessionInactivityTimeout() {
            super(Session.this.getSessionHandler().getScheduler());
        }

        @Override
        protected void onIdleExpired(TimeoutException timeout) {
            if (Session.LOG.isDebugEnabled()) {
                Session.LOG.debug("Timer expired for session {}", Session.this.getId());
            }
            Session.this.getSessionHandler().sessionInactivityTimerExpired(Session.this);
        }

        @Override
        public boolean isOpen() {
            boolean var3;
            try (Locker.Lock lock = Session.this._lock.lockIfNotHeld()) {
                var3 = Session.this.isValid() && Session.this.isResident();
            }
            return var3;
        }

        @Override
        public void setIdleTimeout(long idleTimeout) {
            if (Session.LOG.isDebugEnabled()) {
                Session.LOG.debug("setIdleTimeout called: old=" + this.getIdleTimeout() + " new=" + idleTimeout);
            }
            super.setIdleTimeout(idleTimeout);
        }
    }

    public static enum State {

        VALID, INVALID, INVALIDATING
    }
}