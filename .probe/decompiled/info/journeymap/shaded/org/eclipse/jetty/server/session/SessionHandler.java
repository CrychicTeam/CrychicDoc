package info.journeymap.shaded.org.eclipse.jetty.server.session;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpCookie;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.eclipse.jetty.server.SessionIdManager;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ScopedHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.ConcurrentHashSet;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedOperation;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.statistic.CounterStatistic;
import info.journeymap.shaded.org.eclipse.jetty.util.statistic.SampleStatistic;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.javax.servlet.AsyncEvent;
import info.journeymap.shaded.org.javax.servlet.AsyncListener;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.SessionCookieConfig;
import info.journeymap.shaded.org.javax.servlet.SessionTrackingMode;
import info.journeymap.shaded.org.javax.servlet.http.Cookie;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import info.journeymap.shaded.org.javax.servlet.http.HttpSession;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionAttributeListener;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionBindingEvent;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionContext;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionEvent;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionIdListener;
import info.journeymap.shaded.org.javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@ManagedObject
public class SessionHandler extends ScopedHandler {

    static final Logger LOG = Log.getLogger("info.journeymap.shaded.org.eclipse.jetty.server.session");

    public static final EnumSet<SessionTrackingMode> DEFAULT_TRACKING = EnumSet.of(SessionTrackingMode.COOKIE, SessionTrackingMode.URL);

    public static final String __SessionCookieProperty = "info.journeymap.shaded.org.eclipse.jetty.servlet.SessionCookie";

    public static final String __DefaultSessionCookie = "JSESSIONID";

    public static final String __SessionIdPathParameterNameProperty = "info.journeymap.shaded.org.eclipse.jetty.servlet.SessionIdPathParameterName";

    public static final String __DefaultSessionIdPathParameterName = "jsessionid";

    public static final String __CheckRemoteSessionEncoding = "info.journeymap.shaded.org.eclipse.jetty.servlet.CheckingRemoteSessionIdEncoding";

    public static final String __SessionDomainProperty = "info.journeymap.shaded.org.eclipse.jetty.servlet.SessionDomain";

    public static final String __DefaultSessionDomain = null;

    public static final String __SessionPathProperty = "info.journeymap.shaded.org.eclipse.jetty.servlet.SessionPath";

    public static final String __MaxAgeProperty = "info.journeymap.shaded.org.eclipse.jetty.servlet.MaxAge";

    public Set<SessionTrackingMode> __defaultSessionTrackingModes = Collections.unmodifiableSet(new HashSet(Arrays.asList(SessionTrackingMode.COOKIE, SessionTrackingMode.URL)));

    public static final Class<? extends EventListener>[] SESSION_LISTENER_TYPES = new Class[] { HttpSessionAttributeListener.class, HttpSessionIdListener.class, HttpSessionListener.class };

    public static final BigDecimal MAX_INACTIVE_MINUTES = new BigDecimal(35791394);

    static final HttpSessionContext __nullSessionContext = new HttpSessionContext() {

        @Override
        public HttpSession getSession(String sessionId) {
            return null;
        }

        @Override
        public Enumeration getIds() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }
    };

    protected int _dftMaxIdleSecs = -1;

    protected boolean _httpOnly = false;

    protected SessionIdManager _sessionIdManager;

    protected boolean _secureCookies = false;

    protected boolean _secureRequestOnly = true;

    protected final List<HttpSessionAttributeListener> _sessionAttributeListeners = new CopyOnWriteArrayList();

    protected final List<HttpSessionListener> _sessionListeners = new CopyOnWriteArrayList();

    protected final List<HttpSessionIdListener> _sessionIdListeners = new CopyOnWriteArrayList();

    protected ClassLoader _loader;

    protected ContextHandler.Context _context;

    protected SessionContext _sessionContext;

    protected String _sessionCookie = "JSESSIONID";

    protected String _sessionIdPathParameterName = "jsessionid";

    protected String _sessionIdPathParameterNamePrefix = ";" + this._sessionIdPathParameterName + "=";

    protected String _sessionDomain;

    protected String _sessionPath;

    protected int _maxCookieAge = -1;

    protected int _refreshCookieAge;

    protected boolean _nodeIdInSessionId;

    protected boolean _checkingRemoteSessionIdEncoding;

    protected String _sessionComment;

    protected SessionCache _sessionCache;

    protected final SampleStatistic _sessionTimeStats = new SampleStatistic();

    protected final CounterStatistic _sessionsCreatedStats = new CounterStatistic();

    public Set<SessionTrackingMode> _sessionTrackingModes;

    protected boolean _usingURLs;

    protected boolean _usingCookies = true;

    protected ConcurrentHashSet<String> _candidateSessionIdsForExpiry = new ConcurrentHashSet<>();

    protected Scheduler _scheduler;

    protected boolean _ownScheduler = false;

    private SessionCookieConfig _cookieConfig = new SessionHandler.CookieConfig();

    public SessionHandler() {
        this.setSessionTrackingModes(this.__defaultSessionTrackingModes);
    }

    @ManagedAttribute("path of the session cookie, or null for default")
    public String getSessionPath() {
        return this._sessionPath;
    }

    @ManagedAttribute("if greater the zero, the time in seconds a session cookie will last for")
    public int getMaxCookieAge() {
        return this._maxCookieAge;
    }

    public HttpCookie access(HttpSession session, boolean secure) {
        long now = System.currentTimeMillis();
        Session s = ((SessionHandler.SessionIf) session).getSession();
        if (s.access(now) && this.isUsingCookies() && (s.isIdChanged() || this.getSessionCookieConfig().getMaxAge() > 0 && this.getRefreshCookieAge() > 0 && (now - s.getCookieSetTime()) / 1000L > (long) this.getRefreshCookieAge())) {
            HttpCookie cookie = this.getSessionCookie(session, this._context == null ? "/" : this._context.getContextPath(), secure);
            s.cookieSet();
            s.setIdChanged(false);
            return cookie;
        } else {
            return null;
        }
    }

    public void addEventListener(EventListener listener) {
        if (listener instanceof HttpSessionAttributeListener) {
            this._sessionAttributeListeners.add((HttpSessionAttributeListener) listener);
        }
        if (listener instanceof HttpSessionListener) {
            this._sessionListeners.add((HttpSessionListener) listener);
        }
        if (listener instanceof HttpSessionIdListener) {
            this._sessionIdListeners.add((HttpSessionIdListener) listener);
        }
        this.addBean(listener, false);
    }

    public void clearEventListeners() {
        for (EventListener e : this.getBeans(EventListener.class)) {
            this.removeBean(e);
        }
        this._sessionAttributeListeners.clear();
        this._sessionListeners.clear();
        this._sessionIdListeners.clear();
    }

    public void complete(HttpSession session) {
        if (session != null) {
            Session s = ((SessionHandler.SessionIf) session).getSession();
            try {
                s.complete();
                this._sessionCache.put(s.getId(), s);
            } catch (Exception var4) {
                LOG.warn(var4);
            }
        }
    }

    public void complete(Session session, Request request) {
        if (request.isAsyncStarted() && request.getDispatcherType() == DispatcherType.REQUEST) {
            request.getAsyncContext().addListener(new SessionHandler.SessionAsyncListener(session));
        } else {
            this.complete(session);
        }
    }

    @Override
    protected void doStart() throws Exception {
        Server server = this.getServer();
        this._context = ContextHandler.getCurrentContext();
        this._loader = Thread.currentThread().getContextClassLoader();
        synchronized (server) {
            if (this._sessionCache == null) {
                SessionCacheFactory ssFactory = server.getBean(SessionCacheFactory.class);
                this.setSessionCache((SessionCache) (ssFactory != null ? ssFactory.getSessionCache(this) : new DefaultSessionCache(this)));
                SessionDataStore sds = null;
                SessionDataStoreFactory sdsFactory = server.getBean(SessionDataStoreFactory.class);
                if (sdsFactory != null) {
                    sds = sdsFactory.getSessionDataStore(this);
                } else {
                    sds = new NullSessionDataStore();
                }
                this._sessionCache.setSessionDataStore(sds);
            }
            if (this._sessionIdManager == null) {
                this._sessionIdManager = server.getSessionIdManager();
                if (this._sessionIdManager == null) {
                    ClassLoader serverLoader = server.getClass().getClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader(serverLoader);
                        this._sessionIdManager = new DefaultSessionIdManager(server);
                        server.setSessionIdManager(this._sessionIdManager);
                        server.manage(this._sessionIdManager);
                        this._sessionIdManager.start();
                    } finally {
                        Thread.currentThread().setContextClassLoader(this._loader);
                    }
                }
                this.addBean(this._sessionIdManager, false);
            }
            this._scheduler = server.getBean(Scheduler.class);
            if (this._scheduler == null) {
                this._scheduler = new ScheduledExecutorScheduler();
                this._ownScheduler = true;
                this._scheduler.start();
            }
        }
        if (this._context != null) {
            String tmp = this._context.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.servlet.SessionCookie");
            if (tmp != null) {
                this._sessionCookie = tmp;
            }
            tmp = this._context.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.servlet.SessionIdPathParameterName");
            if (tmp != null) {
                this.setSessionIdPathParameterName(tmp);
            }
            if (this._maxCookieAge == -1) {
                tmp = this._context.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.servlet.MaxAge");
                if (tmp != null) {
                    this._maxCookieAge = Integer.parseInt(tmp.trim());
                }
            }
            if (this._sessionDomain == null) {
                this._sessionDomain = this._context.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.servlet.SessionDomain");
            }
            if (this._sessionPath == null) {
                this._sessionPath = this._context.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.servlet.SessionPath");
            }
            tmp = this._context.getInitParameter("info.journeymap.shaded.org.eclipse.jetty.servlet.CheckingRemoteSessionIdEncoding");
            if (tmp != null) {
                this._checkingRemoteSessionIdEncoding = Boolean.parseBoolean(tmp);
            }
        }
        this._sessionContext = new SessionContext(this._sessionIdManager.getWorkerName(), this._context);
        this._sessionCache.initialize(this._sessionContext);
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        this.shutdownSessions();
        this._sessionCache.stop();
        if (this._ownScheduler && this._scheduler != null) {
            this._scheduler.stop();
        }
        this._scheduler = null;
        super.doStop();
        this._loader = null;
    }

    @ManagedAttribute("true if cookies use the http only flag")
    public boolean getHttpOnly() {
        return this._httpOnly;
    }

    public HttpSession getHttpSession(String extendedId) {
        String id = this.getSessionIdManager().getId(extendedId);
        Session session = this.getSession(id);
        if (session != null && !session.getExtendedId().equals(extendedId)) {
            session.setIdChanged(true);
        }
        return session;
    }

    @ManagedAttribute("Session ID Manager")
    public SessionIdManager getSessionIdManager() {
        return this._sessionIdManager;
    }

    @ManagedAttribute("default maximum time a session may be idle for (in s)")
    public int getMaxInactiveInterval() {
        return this._dftMaxIdleSecs;
    }

    @ManagedAttribute("time before a session cookie is re-set (in s)")
    public int getRefreshCookieAge() {
        return this._refreshCookieAge;
    }

    @ManagedAttribute("if true, secure cookie flag is set on session cookies")
    public boolean getSecureCookies() {
        return this._secureCookies;
    }

    public boolean isSecureRequestOnly() {
        return this._secureRequestOnly;
    }

    public void setSecureRequestOnly(boolean secureRequestOnly) {
        this._secureRequestOnly = secureRequestOnly;
    }

    @ManagedAttribute("the set session cookie")
    public String getSessionCookie() {
        return this._sessionCookie;
    }

    public HttpCookie getSessionCookie(HttpSession session, String contextPath, boolean requestIsSecure) {
        if (this.isUsingCookies()) {
            String sessionPath = this._cookieConfig.getPath() == null ? contextPath : this._cookieConfig.getPath();
            sessionPath = sessionPath != null && sessionPath.length() != 0 ? sessionPath : "/";
            String id = this.getExtendedId(session);
            HttpCookie cookie = null;
            if (this._sessionComment == null) {
                cookie = new HttpCookie(this._cookieConfig.getName(), id, this._cookieConfig.getDomain(), sessionPath, (long) this._cookieConfig.getMaxAge(), this._cookieConfig.isHttpOnly(), this._cookieConfig.isSecure() || this.isSecureRequestOnly() && requestIsSecure);
            } else {
                cookie = new HttpCookie(this._cookieConfig.getName(), id, this._cookieConfig.getDomain(), sessionPath, (long) this._cookieConfig.getMaxAge(), this._cookieConfig.isHttpOnly(), this._cookieConfig.isSecure() || this.isSecureRequestOnly() && requestIsSecure, this._sessionComment, 1);
            }
            return cookie;
        } else {
            return null;
        }
    }

    @ManagedAttribute("domain of the session cookie, or null for the default")
    public String getSessionDomain() {
        return this._sessionDomain;
    }

    @ManagedAttribute("number of sessions created by this node")
    public int getSessionsCreated() {
        return (int) this._sessionsCreatedStats.getCurrent();
    }

    @ManagedAttribute("name of use for URL session tracking")
    public String getSessionIdPathParameterName() {
        return this._sessionIdPathParameterName;
    }

    public String getSessionIdPathParameterNamePrefix() {
        return this._sessionIdPathParameterNamePrefix;
    }

    public boolean isUsingCookies() {
        return this._usingCookies;
    }

    public boolean isValid(HttpSession session) {
        Session s = ((SessionHandler.SessionIf) session).getSession();
        return s.isValid();
    }

    public String getId(HttpSession session) {
        Session s = ((SessionHandler.SessionIf) session).getSession();
        return s.getId();
    }

    public String getExtendedId(HttpSession session) {
        Session s = ((SessionHandler.SessionIf) session).getSession();
        return s.getExtendedId();
    }

    public HttpSession newHttpSession(HttpServletRequest request) {
        long created = System.currentTimeMillis();
        String id = this._sessionIdManager.newSessionId(request, created);
        Session session = this._sessionCache.newSession(request, id, created, this._dftMaxIdleSecs > 0 ? (long) this._dftMaxIdleSecs * 1000L : -1L);
        session.setExtendedId(this._sessionIdManager.getExtendedId(id, request));
        session.getSessionData().setLastNode(this._sessionIdManager.getWorkerName());
        try {
            this._sessionCache.put(id, session);
            this._sessionsCreatedStats.increment();
            if (request.isSecure()) {
                session.setAttribute("info.journeymap.shaded.org.eclipse.jetty.security.sessionCreatedSecure", Boolean.TRUE);
            }
            if (this._sessionListeners != null) {
                HttpSessionEvent event = new HttpSessionEvent(session);
                for (HttpSessionListener listener : this._sessionListeners) {
                    listener.sessionCreated(event);
                }
            }
            return session;
        } catch (Exception var9) {
            LOG.warn(var9);
            return null;
        }
    }

    public void removeEventListener(EventListener listener) {
        if (listener instanceof HttpSessionAttributeListener) {
            this._sessionAttributeListeners.remove(listener);
        }
        if (listener instanceof HttpSessionListener) {
            this._sessionListeners.remove(listener);
        }
        if (listener instanceof HttpSessionIdListener) {
            this._sessionIdListeners.remove(listener);
        }
        this.removeBean(listener);
    }

    @ManagedOperation(value = "reset statistics", impact = "ACTION")
    public void statsReset() {
        this._sessionsCreatedStats.reset();
        this._sessionTimeStats.reset();
    }

    public void setHttpOnly(boolean httpOnly) {
        this._httpOnly = httpOnly;
    }

    public void setSessionIdManager(SessionIdManager metaManager) {
        this.updateBean(this._sessionIdManager, metaManager);
        this._sessionIdManager = metaManager;
    }

    public void setMaxInactiveInterval(int seconds) {
        this._dftMaxIdleSecs = seconds;
        if (LOG.isDebugEnabled()) {
            if (this._dftMaxIdleSecs <= 0) {
                LOG.debug("Sessions created by this manager are immortal (default maxInactiveInterval={})", (long) this._dftMaxIdleSecs);
            } else {
                LOG.debug("SessionManager default maxInactiveInterval={}", (long) this._dftMaxIdleSecs);
            }
        }
    }

    public void setRefreshCookieAge(int ageInSeconds) {
        this._refreshCookieAge = ageInSeconds;
    }

    public void setSessionCookie(String cookieName) {
        this._sessionCookie = cookieName;
    }

    public void setSessionIdPathParameterName(String param) {
        this._sessionIdPathParameterName = param != null && !"none".equals(param) ? param : null;
        this._sessionIdPathParameterNamePrefix = param != null && !"none".equals(param) ? ";" + this._sessionIdPathParameterName + "=" : null;
    }

    public void setUsingCookies(boolean usingCookies) {
        this._usingCookies = usingCookies;
    }

    public Session getSession(String id) {
        try {
            Session session = this._sessionCache.get(id);
            if (session != null) {
                if (session.isExpiredAt(System.currentTimeMillis())) {
                    try {
                        session.invalidate();
                    } catch (Exception var5) {
                        LOG.warn("Invalidating session {} found to be expired when requested", id, var5);
                    }
                    return null;
                }
                session.setExtendedId(this._sessionIdManager.getExtendedId(id, null));
            }
            return session;
        } catch (UnreadableSessionDataException var6) {
            LOG.warn(var6);
            try {
                this.getSessionIdManager().invalidateAll(id);
            } catch (Exception var4) {
                LOG.warn("Error cross-context invalidating unreadable session {}", id, var4);
            }
            return null;
        } catch (Exception var7) {
            LOG.warn(var7);
            return null;
        }
    }

    protected void shutdownSessions() throws Exception {
        this._sessionCache.shutdown();
    }

    public SessionCache getSessionCache() {
        return this._sessionCache;
    }

    public void setSessionCache(SessionCache cache) {
        this.updateBean(this._sessionCache, cache);
        this._sessionCache = cache;
    }

    public boolean isNodeIdInSessionId() {
        return this._nodeIdInSessionId;
    }

    public void setNodeIdInSessionId(boolean nodeIdInSessionId) {
        this._nodeIdInSessionId = nodeIdInSessionId;
    }

    public Session removeSession(String id, boolean invalidate) {
        try {
            Session session = this._sessionCache.delete(id);
            if (session != null && invalidate) {
                session.beginInvalidate();
                if (this._sessionListeners != null) {
                    HttpSessionEvent event = new HttpSessionEvent(session);
                    for (int i = this._sessionListeners.size() - 1; i >= 0; i--) {
                        ((HttpSessionListener) this._sessionListeners.get(i)).sessionDestroyed(event);
                    }
                }
            }
            return session;
        } catch (Exception var6) {
            LOG.warn(var6);
            return null;
        }
    }

    @ManagedAttribute("maximum amount of time sessions have remained active (in s)")
    public long getSessionTimeMax() {
        return this._sessionTimeStats.getMax();
    }

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return this.__defaultSessionTrackingModes;
    }

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return Collections.unmodifiableSet(this._sessionTrackingModes);
    }

    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        this._sessionTrackingModes = new HashSet(sessionTrackingModes);
        this._usingCookies = this._sessionTrackingModes.contains(SessionTrackingMode.COOKIE);
        this._usingURLs = this._sessionTrackingModes.contains(SessionTrackingMode.URL);
    }

    public boolean isUsingURLs() {
        return this._usingURLs;
    }

    public SessionCookieConfig getSessionCookieConfig() {
        return this._cookieConfig;
    }

    @ManagedAttribute("total time sessions have remained valid")
    public long getSessionTimeTotal() {
        return this._sessionTimeStats.getTotal();
    }

    @ManagedAttribute("mean time sessions remain valid (in s)")
    public double getSessionTimeMean() {
        return this._sessionTimeStats.getMean();
    }

    @ManagedAttribute("standard deviation a session remained valid (in s)")
    public double getSessionTimeStdDev() {
        return this._sessionTimeStats.getStdDev();
    }

    @ManagedAttribute("check remote session id encoding")
    public boolean isCheckingRemoteSessionIdEncoding() {
        return this._checkingRemoteSessionIdEncoding;
    }

    public void setCheckingRemoteSessionIdEncoding(boolean remote) {
        this._checkingRemoteSessionIdEncoding = remote;
    }

    public void renewSessionId(String oldId, String oldExtendedId, String newId, String newExtendedId) {
        try {
            Session session = this._sessionCache.renewSessionId(oldId, newId);
            if (session == null) {
                return;
            }
            session.setExtendedId(newExtendedId);
            if (!this._sessionIdListeners.isEmpty()) {
                HttpSessionEvent event = new HttpSessionEvent(session);
                for (HttpSessionIdListener l : this._sessionIdListeners) {
                    l.sessionIdChanged(event, oldId);
                }
            }
        } catch (Exception var9) {
            LOG.warn(var9);
        }
    }

    public void invalidate(String id) {
        if (!StringUtil.isBlank(id)) {
            try {
                Session session = this.removeSession(id, true);
                if (session != null) {
                    this._sessionTimeStats.set(Math.round((double) (System.currentTimeMillis() - session.getSessionData().getCreated()) / 1000.0));
                    session.finishInvalidate();
                }
            } catch (Exception var3) {
                LOG.warn(var3);
            }
        }
    }

    public void scavenge() {
        if (!this.isStopping() && !this.isStopped()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Scavenging sessions");
            }
            String[] ss = this._candidateSessionIdsForExpiry.toArray(new String[0]);
            Set<String> candidates = new HashSet(Arrays.asList(ss));
            this._candidateSessionIdsForExpiry.removeAll(candidates);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Scavenging session ids {}", candidates);
            }
            try {
                for (String id : this._sessionCache.checkExpiration(candidates)) {
                    try {
                        this.getSessionIdManager().expireAll(id);
                    } catch (Exception var6) {
                        LOG.warn(var6);
                    }
                }
            } catch (Exception var7) {
                LOG.warn(var7);
            }
        }
    }

    public void sessionInactivityTimerExpired(Session session) {
        if (session != null) {
            boolean expired = false;
            try (Locker.Lock lock = session.lockIfNotHeld()) {
                if (session.getRequests() > 0L) {
                    return;
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Inspecting session {}, valid={}", session.getId(), session.isValid());
                }
                if (!session.isValid()) {
                    return;
                }
                if (session.isExpiredAt(System.currentTimeMillis()) && session.getRequests() <= 0L) {
                    expired = true;
                }
            }
            if (expired) {
                if (this._sessionIdManager.getSessionHouseKeeper() != null && this._sessionIdManager.getSessionHouseKeeper().getIntervalSec() > 0L) {
                    this._candidateSessionIdsForExpiry.add(session.getId());
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Session {} is candidate for expiry", session.getId());
                    }
                }
            } else {
                this._sessionCache.checkInactiveSession(session);
            }
        }
    }

    public boolean isIdInUse(String id) throws Exception {
        return this._sessionCache.exists(id);
    }

    public Scheduler getScheduler() {
        return this._scheduler;
    }

    public void doSessionAttributeListeners(Session session, String name, Object old, Object value) {
        if (!this._sessionAttributeListeners.isEmpty()) {
            HttpSessionBindingEvent event = new HttpSessionBindingEvent(session, name, old == null ? value : old);
            for (HttpSessionAttributeListener l : this._sessionAttributeListeners) {
                if (old == null) {
                    l.attributeAdded(event);
                } else if (value == null) {
                    l.attributeRemoved(event);
                } else {
                    l.attributeReplaced(event);
                }
            }
        }
    }

    @Override
    public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        SessionHandler old_session_manager = null;
        HttpSession old_session = null;
        HttpSession existingSession = null;
        try {
            old_session_manager = baseRequest.getSessionHandler();
            old_session = baseRequest.getSession(false);
            if (old_session_manager != this) {
                baseRequest.setSessionHandler(this);
                baseRequest.setSession(null);
                this.checkRequestedSessionId(baseRequest, request);
            }
            existingSession = baseRequest.getSession(false);
            if (existingSession != null && old_session_manager != this) {
                HttpCookie cookie = this.access(existingSession, request.isSecure());
                if (cookie != null && (request.getDispatcherType() == DispatcherType.ASYNC || request.getDispatcherType() == DispatcherType.REQUEST)) {
                    baseRequest.getResponse().addCookie(cookie);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("sessionHandler=" + this);
                LOG.debug("session=" + existingSession);
            }
            if (this._nextScope != null) {
                this._nextScope.doScope(target, baseRequest, request, response);
            } else if (this._outerScope != null) {
                this._outerScope.doHandle(target, baseRequest, request, response);
            } else {
                this.doHandle(target, baseRequest, request, response);
            }
        } finally {
            HttpSession finalSession = baseRequest.getSession(false);
            if (LOG.isDebugEnabled()) {
                LOG.debug("FinalSession=" + finalSession + " old_session_manager=" + old_session_manager + " this=" + this);
            }
            if (finalSession != null && old_session_manager != this) {
                this.complete((Session) finalSession, baseRequest);
            }
            if (old_session_manager != null && old_session_manager != this) {
                baseRequest.setSessionHandler(old_session_manager);
                baseRequest.setSession(old_session);
            }
        }
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.nextHandle(target, baseRequest, request, response);
    }

    protected void checkRequestedSessionId(Request baseRequest, HttpServletRequest request) {
        String requested_session_id = request.getRequestedSessionId();
        if (requested_session_id != null) {
            HttpSession session = this.getHttpSession(requested_session_id);
            if (session != null && this.isValid(session)) {
                baseRequest.setSession(session);
            }
        } else if (DispatcherType.REQUEST.equals(baseRequest.getDispatcherType())) {
            boolean requested_session_id_from_cookie = false;
            HttpSession session = null;
            if (this.isUsingCookies()) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null && cookies.length > 0) {
                    String sessionCookie = this.getSessionCookieConfig().getName();
                    for (int i = 0; i < cookies.length; i++) {
                        if (sessionCookie.equalsIgnoreCase(cookies[i].getName())) {
                            requested_session_id = cookies[i].getValue();
                            requested_session_id_from_cookie = true;
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Got Session ID {} from cookie", requested_session_id);
                            }
                            if (requested_session_id != null) {
                                session = this.getHttpSession(requested_session_id);
                                if (session != null && this.isValid(session)) {
                                    break;
                                }
                            } else {
                                LOG.warn("null session id from cookie");
                            }
                        }
                    }
                }
            }
            if (requested_session_id == null || session == null) {
                String uri = request.getRequestURI();
                String prefix = this.getSessionIdPathParameterNamePrefix();
                if (prefix != null) {
                    int s = uri.indexOf(prefix);
                    if (s >= 0) {
                        s += prefix.length();
                        int ix;
                        for (ix = s; ix < uri.length(); ix++) {
                            char c = uri.charAt(ix);
                            if (c == ';' || c == '#' || c == '?' || c == '/') {
                                break;
                            }
                        }
                        requested_session_id = uri.substring(s, ix);
                        requested_session_id_from_cookie = false;
                        session = this.getHttpSession(requested_session_id);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Got Session ID {} from URL", requested_session_id);
                        }
                    }
                }
            }
            baseRequest.setRequestedSessionId(requested_session_id);
            baseRequest.setRequestedSessionIdFromCookie(requested_session_id != null && requested_session_id_from_cookie);
            if (session != null && this.isValid(session)) {
                baseRequest.setSession(session);
            }
        }
    }

    public String toString() {
        return String.format("%s%d==dftMaxIdleSec=%d", this.getClass().getName(), this.hashCode(), this._dftMaxIdleSecs);
    }

    public final class CookieConfig implements SessionCookieConfig {

        @Override
        public String getComment() {
            return SessionHandler.this._sessionComment;
        }

        @Override
        public String getDomain() {
            return SessionHandler.this._sessionDomain;
        }

        @Override
        public int getMaxAge() {
            return SessionHandler.this._maxCookieAge;
        }

        @Override
        public String getName() {
            return SessionHandler.this._sessionCookie;
        }

        @Override
        public String getPath() {
            return SessionHandler.this._sessionPath;
        }

        @Override
        public boolean isHttpOnly() {
            return SessionHandler.this._httpOnly;
        }

        @Override
        public boolean isSecure() {
            return SessionHandler.this._secureCookies;
        }

        @Override
        public void setComment(String comment) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._sessionComment = comment;
            }
        }

        @Override
        public void setDomain(String domain) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._sessionDomain = domain;
            }
        }

        @Override
        public void setHttpOnly(boolean httpOnly) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._httpOnly = httpOnly;
            }
        }

        @Override
        public void setMaxAge(int maxAge) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._maxCookieAge = maxAge;
            }
        }

        @Override
        public void setName(String name) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._sessionCookie = name;
            }
        }

        @Override
        public void setPath(String path) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._sessionPath = path;
            }
        }

        @Override
        public void setSecure(boolean secure) {
            if (SessionHandler.this._context != null && SessionHandler.this._context.getContextHandler().isAvailable()) {
                throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
            } else {
                SessionHandler.this._secureCookies = secure;
            }
        }
    }

    public class SessionAsyncListener implements AsyncListener {

        private Session _session;

        public SessionAsyncListener(Session session) {
            this._session = session;
        }

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            SessionHandler.this.complete(((HttpServletRequest) event.getAsyncContext().getRequest()).getSession(false));
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
            SessionHandler.this.complete(((HttpServletRequest) event.getAsyncContext().getRequest()).getSession(false));
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
            event.getAsyncContext().addListener(this);
        }
    }

    public interface SessionIf extends HttpSession {

        Session getSession();
    }
}