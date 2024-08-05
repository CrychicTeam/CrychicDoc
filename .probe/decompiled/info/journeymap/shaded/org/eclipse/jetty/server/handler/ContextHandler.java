package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpURI;
import info.journeymap.shaded.org.eclipse.jetty.http.MimeTypes;
import info.journeymap.shaded.org.eclipse.jetty.server.ClassLoaderDump;
import info.journeymap.shaded.org.eclipse.jetty.server.Dispatcher;
import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.HandlerContainer;
import info.journeymap.shaded.org.eclipse.jetty.server.Request;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.eclipse.jetty.util.Attributes;
import info.journeymap.shaded.org.eclipse.jetty.util.AttributesMap;
import info.journeymap.shaded.org.eclipse.jetty.util.FutureCallback;
import info.journeymap.shaded.org.eclipse.jetty.util.Loader;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiException;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.DumpableCollection;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Graceful;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import info.journeymap.shaded.org.javax.servlet.DispatcherType;
import info.journeymap.shaded.org.javax.servlet.Filter;
import info.journeymap.shaded.org.javax.servlet.FilterRegistration;
import info.journeymap.shaded.org.javax.servlet.RequestDispatcher;
import info.journeymap.shaded.org.javax.servlet.Servlet;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletContextAttributeEvent;
import info.journeymap.shaded.org.javax.servlet.ServletContextAttributeListener;
import info.journeymap.shaded.org.javax.servlet.ServletContextEvent;
import info.journeymap.shaded.org.javax.servlet.ServletContextListener;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRegistration;
import info.journeymap.shaded.org.javax.servlet.ServletRequestAttributeListener;
import info.journeymap.shaded.org.javax.servlet.ServletRequestEvent;
import info.journeymap.shaded.org.javax.servlet.ServletRequestListener;
import info.journeymap.shaded.org.javax.servlet.SessionCookieConfig;
import info.journeymap.shaded.org.javax.servlet.SessionTrackingMode;
import info.journeymap.shaded.org.javax.servlet.descriptor.JspConfigDescriptor;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

@ManagedObject("URI Context")
public class ContextHandler extends ScopedHandler implements Attributes, Graceful {

    public static final int SERVLET_MAJOR_VERSION = 3;

    public static final int SERVLET_MINOR_VERSION = 1;

    public static final Class<?>[] SERVLET_LISTENER_TYPES = new Class[] { ServletContextListener.class, ServletContextAttributeListener.class, ServletRequestListener.class, ServletRequestAttributeListener.class };

    public static final int DEFAULT_LISTENER_TYPE_INDEX = 1;

    public static final int EXTENDED_LISTENER_TYPE_INDEX = 0;

    private static final String __unimplmented = "Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler";

    private static final Logger LOG = Log.getLogger(ContextHandler.class);

    private static final ThreadLocal<ContextHandler.Context> __context = new ThreadLocal();

    private static String __serverInfo = "jetty/" + Server.getVersion();

    public static final String MANAGED_ATTRIBUTES = "info.journeymap.shaded.org.eclipse.jetty.server.context.ManagedAttributes";

    protected ContextHandler.Context _scontext;

    private final AttributesMap _attributes;

    private final Map<String, String> _initParams;

    private ClassLoader _classLoader;

    private String _contextPath = "/";

    private String _contextPathEncoded = "/";

    private String _displayName;

    private Resource _baseResource;

    private MimeTypes _mimeTypes;

    private Map<String, String> _localeEncodingMap;

    private String[] _welcomeFiles;

    private ErrorHandler _errorHandler;

    private String[] _vhosts;

    private Logger _logger;

    private boolean _allowNullPathInfo;

    private int _maxFormKeys = Integer.getInteger("info.journeymap.shaded.org.eclipse.jetty.server.Request.maxFormKeys", -1);

    private int _maxFormContentSize = Integer.getInteger("info.journeymap.shaded.org.eclipse.jetty.server.Request.maxFormContentSize", -1);

    private boolean _compactPath = false;

    private boolean _usingSecurityManager = System.getSecurityManager() != null;

    private final List<EventListener> _eventListeners = new CopyOnWriteArrayList();

    private final List<EventListener> _programmaticListeners = new CopyOnWriteArrayList();

    private final List<ServletContextListener> _servletContextListeners = new CopyOnWriteArrayList();

    private final List<ServletContextListener> _destroySerletContextListeners = new ArrayList();

    private final List<ServletContextAttributeListener> _servletContextAttributeListeners = new CopyOnWriteArrayList();

    private final List<ServletRequestListener> _servletRequestListeners = new CopyOnWriteArrayList();

    private final List<ServletRequestAttributeListener> _servletRequestAttributeListeners = new CopyOnWriteArrayList();

    private final List<ContextHandler.ContextScopeListener> _contextListeners = new CopyOnWriteArrayList();

    private final List<EventListener> _durableListeners = new CopyOnWriteArrayList();

    private Map<String, Object> _managedAttributes;

    private String[] _protectedTargets;

    private final CopyOnWriteArrayList<ContextHandler.AliasCheck> _aliasChecks = new CopyOnWriteArrayList();

    private volatile ContextHandler.Availability _availability = ContextHandler.Availability.UNAVAILABLE;

    public static ContextHandler.Context getCurrentContext() {
        return (ContextHandler.Context) __context.get();
    }

    public static ContextHandler getContextHandler(ServletContext context) {
        if (context instanceof ContextHandler.Context) {
            return ((ContextHandler.Context) context).getContextHandler();
        } else {
            ContextHandler.Context c = getCurrentContext();
            return c != null ? c.getContextHandler() : null;
        }
    }

    public static String getServerInfo() {
        return __serverInfo;
    }

    public static void setServerInfo(String serverInfo) {
        __serverInfo = serverInfo;
    }

    public ContextHandler() {
        this(null, null, null);
    }

    protected ContextHandler(ContextHandler.Context context) {
        this(context, null, null);
    }

    public ContextHandler(String contextPath) {
        this(null, null, contextPath);
    }

    public ContextHandler(HandlerContainer parent, String contextPath) {
        this(null, parent, contextPath);
    }

    private ContextHandler(ContextHandler.Context context, HandlerContainer parent, String contextPath) {
        this._scontext = context == null ? new ContextHandler.Context() : context;
        this._attributes = new AttributesMap();
        this._initParams = new HashMap();
        this.addAliasCheck(new ContextHandler.ApproveNonExistentDirectoryAliases());
        if (File.separatorChar == '/') {
            this.addAliasCheck(new AllowSymLinkAliasChecker());
        }
        if (contextPath != null) {
            this.setContextPath(contextPath);
        }
        if (parent instanceof HandlerWrapper) {
            ((HandlerWrapper) parent).setHandler(this);
        } else if (parent instanceof HandlerCollection) {
            ((HandlerCollection) parent).addHandler(this);
        }
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpBeans(out, indent, new Collection[] { Collections.singletonList(new ClassLoaderDump(this.getClassLoader())), Collections.singletonList(new DumpableCollection("Handler attributes " + this, ((AttributesMap) this.getAttributes()).getAttributeEntrySet())), Collections.singletonList(new DumpableCollection("Context attributes " + this, this.getServletContext().getAttributeEntrySet())), Collections.singletonList(new DumpableCollection("Initparams " + this, this.getInitParams().entrySet())) });
    }

    public ContextHandler.Context getServletContext() {
        return this._scontext;
    }

    @ManagedAttribute("Checks if the /context is not redirected to /context/")
    public boolean getAllowNullPathInfo() {
        return this._allowNullPathInfo;
    }

    public void setAllowNullPathInfo(boolean allowNullPathInfo) {
        this._allowNullPathInfo = allowNullPathInfo;
    }

    @Override
    public void setServer(Server server) {
        super.setServer(server);
        if (this._errorHandler != null) {
            this._errorHandler.setServer(server);
        }
    }

    public boolean isUsingSecurityManager() {
        return this._usingSecurityManager;
    }

    public void setUsingSecurityManager(boolean usingSecurityManager) {
        this._usingSecurityManager = usingSecurityManager;
    }

    public void setVirtualHosts(String[] vhosts) {
        if (vhosts == null) {
            this._vhosts = vhosts;
        } else {
            this._vhosts = new String[vhosts.length];
            for (int i = 0; i < vhosts.length; i++) {
                this._vhosts[i] = this.normalizeHostname(vhosts[i]);
            }
        }
    }

    public void addVirtualHosts(String[] virtualHosts) {
        if (virtualHosts != null) {
            List<String> currentVirtualHosts = null;
            ArrayList var5;
            if (this._vhosts != null) {
                var5 = new ArrayList(Arrays.asList(this._vhosts));
            } else {
                var5 = new ArrayList();
            }
            for (int i = 0; i < virtualHosts.length; i++) {
                String normVhost = this.normalizeHostname(virtualHosts[i]);
                if (!var5.contains(normVhost)) {
                    var5.add(normVhost);
                }
            }
            this._vhosts = (String[]) var5.toArray(new String[0]);
        }
    }

    public void removeVirtualHosts(String[] virtualHosts) {
        if (virtualHosts != null) {
            if (this._vhosts != null && this._vhosts.length != 0) {
                List<String> existingVirtualHosts = new ArrayList(Arrays.asList(this._vhosts));
                for (int i = 0; i < virtualHosts.length; i++) {
                    String toRemoveVirtualHost = this.normalizeHostname(virtualHosts[i]);
                    if (existingVirtualHosts.contains(toRemoveVirtualHost)) {
                        existingVirtualHosts.remove(toRemoveVirtualHost);
                    }
                }
                if (existingVirtualHosts.isEmpty()) {
                    this._vhosts = null;
                } else {
                    this._vhosts = (String[]) existingVirtualHosts.toArray(new String[0]);
                }
            }
        }
    }

    @ManagedAttribute(value = "Virtual hosts accepted by the context", readonly = true)
    public String[] getVirtualHosts() {
        return this._vhosts;
    }

    @Override
    public Object getAttribute(String name) {
        return this._attributes.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return AttributesMap.getAttributeNamesCopy(this._attributes);
    }

    public Attributes getAttributes() {
        return this._attributes;
    }

    public ClassLoader getClassLoader() {
        return this._classLoader;
    }

    @ManagedAttribute("The file classpath")
    public String getClassPath() {
        if (this._classLoader != null && this._classLoader instanceof URLClassLoader) {
            URLClassLoader loader = (URLClassLoader) this._classLoader;
            URL[] urls = loader.getURLs();
            StringBuilder classpath = new StringBuilder();
            for (int i = 0; i < urls.length; i++) {
                try {
                    Resource resource = this.newResource(urls[i]);
                    File file = resource.getFile();
                    if (file != null && file.exists()) {
                        if (classpath.length() > 0) {
                            classpath.append(File.pathSeparatorChar);
                        }
                        classpath.append(file.getAbsolutePath());
                    }
                } catch (IOException var7) {
                    LOG.debug(var7);
                }
            }
            return classpath.length() == 0 ? null : classpath.toString();
        } else {
            return null;
        }
    }

    @ManagedAttribute("True if URLs are compacted to replace the multiple '/'s with a single '/'")
    public String getContextPath() {
        return this._contextPath;
    }

    public String getContextPathEncoded() {
        return this._contextPathEncoded;
    }

    public String getInitParameter(String name) {
        return (String) this._initParams.get(name);
    }

    public String setInitParameter(String name, String value) {
        return (String) this._initParams.put(name, value);
    }

    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(this._initParams.keySet());
    }

    @ManagedAttribute("Initial Parameter map for the context")
    public Map<String, String> getInitParams() {
        return this._initParams;
    }

    @ManagedAttribute(value = "Display name of the Context", readonly = true)
    public String getDisplayName() {
        return this._displayName;
    }

    public EventListener[] getEventListeners() {
        return (EventListener[]) this._eventListeners.toArray(new EventListener[this._eventListeners.size()]);
    }

    public void setEventListeners(EventListener[] eventListeners) {
        this._contextListeners.clear();
        this._servletContextListeners.clear();
        this._servletContextAttributeListeners.clear();
        this._servletRequestListeners.clear();
        this._servletRequestAttributeListeners.clear();
        this._eventListeners.clear();
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                this.addEventListener(listener);
            }
        }
    }

    public void addEventListener(EventListener listener) {
        this._eventListeners.add(listener);
        if (!this.isStarted() && !this.isStarting()) {
            this._durableListeners.add(listener);
        }
        if (listener instanceof ContextHandler.ContextScopeListener) {
            this._contextListeners.add((ContextHandler.ContextScopeListener) listener);
        }
        if (listener instanceof ServletContextListener) {
            this._servletContextListeners.add((ServletContextListener) listener);
        }
        if (listener instanceof ServletContextAttributeListener) {
            this._servletContextAttributeListeners.add((ServletContextAttributeListener) listener);
        }
        if (listener instanceof ServletRequestListener) {
            this._servletRequestListeners.add((ServletRequestListener) listener);
        }
        if (listener instanceof ServletRequestAttributeListener) {
            this._servletRequestAttributeListeners.add((ServletRequestAttributeListener) listener);
        }
    }

    public void removeEventListener(EventListener listener) {
        this._eventListeners.remove(listener);
        if (listener instanceof ContextHandler.ContextScopeListener) {
            this._contextListeners.remove(listener);
        }
        if (listener instanceof ServletContextListener) {
            this._servletContextListeners.remove(listener);
        }
        if (listener instanceof ServletContextAttributeListener) {
            this._servletContextAttributeListeners.remove(listener);
        }
        if (listener instanceof ServletRequestListener) {
            this._servletRequestListeners.remove(listener);
        }
        if (listener instanceof ServletRequestAttributeListener) {
            this._servletRequestAttributeListeners.remove(listener);
        }
    }

    protected void addProgrammaticListener(EventListener listener) {
        this._programmaticListeners.add(listener);
    }

    protected boolean isProgrammaticListener(EventListener listener) {
        return this._programmaticListeners.contains(listener);
    }

    @ManagedAttribute("true for graceful shutdown, which allows existing requests to complete")
    public boolean isShutdown() {
        return this._availability == ContextHandler.Availability.SHUTDOWN;
    }

    @Override
    public Future<Void> shutdown() {
        this._availability = this.isRunning() ? ContextHandler.Availability.SHUTDOWN : ContextHandler.Availability.UNAVAILABLE;
        return new FutureCallback(true);
    }

    public boolean isAvailable() {
        return this._availability == ContextHandler.Availability.AVAILABLE;
    }

    public void setAvailable(boolean available) {
        synchronized (this) {
            if (available && this.isRunning()) {
                this._availability = ContextHandler.Availability.AVAILABLE;
            } else if (!available || !this.isRunning()) {
                this._availability = ContextHandler.Availability.UNAVAILABLE;
            }
        }
    }

    public Logger getLogger() {
        return this._logger;
    }

    public void setLogger(Logger logger) {
        this._logger = logger;
    }

    @Override
    protected void doStart() throws Exception {
        this._availability = ContextHandler.Availability.STARTING;
        if (this._contextPath == null) {
            throw new IllegalStateException("Null contextPath");
        } else {
            if (this._logger == null) {
                this._logger = Log.getLogger(ContextHandler.class.getName() + this.getLogNameSuffix());
            }
            ClassLoader old_classloader = null;
            Thread current_thread = null;
            ContextHandler.Context old_context = null;
            this._attributes.setAttribute("info.journeymap.shaded.org.eclipse.jetty.server.Executor", this.getServer().getThreadPool());
            if (this._mimeTypes == null) {
                this._mimeTypes = new MimeTypes();
            }
            try {
                if (this._classLoader != null) {
                    current_thread = Thread.currentThread();
                    old_classloader = current_thread.getContextClassLoader();
                    current_thread.setContextClassLoader(this._classLoader);
                }
                old_context = (ContextHandler.Context) __context.get();
                __context.set(this._scontext);
                this.enterScope(null, this.getState());
                this.startContext();
                this._availability = ContextHandler.Availability.AVAILABLE;
                LOG.info("Started {}", this);
            } finally {
                if (this._availability == ContextHandler.Availability.STARTING) {
                    this._availability = ContextHandler.Availability.UNAVAILABLE;
                }
                this.exitScope(null);
                __context.set(old_context);
                if (this._classLoader != null && current_thread != null) {
                    current_thread.setContextClassLoader(old_classloader);
                }
            }
        }
    }

    private String getLogNameSuffix() {
        String log_name = this.getDisplayName();
        if (StringUtil.isBlank(log_name)) {
            log_name = this.getContextPath();
            if (log_name != null && log_name.startsWith("/")) {
                log_name = log_name.substring(1);
            }
            if (StringUtil.isBlank(log_name)) {
                log_name = "ROOT";
            }
        }
        return '.' + log_name.replaceAll("\\W", "_");
    }

    protected void startContext() throws Exception {
        String managedAttributes = (String) this._initParams.get("info.journeymap.shaded.org.eclipse.jetty.server.context.ManagedAttributes");
        if (managedAttributes != null) {
            this.addEventListener(new ManagedAttributeListener(this, StringUtil.csvSplit(managedAttributes)));
        }
        super.doStart();
        this._destroySerletContextListeners.clear();
        if (!this._servletContextListeners.isEmpty()) {
            ServletContextEvent event = new ServletContextEvent(this._scontext);
            for (ServletContextListener listener : this._servletContextListeners) {
                this.callContextInitialized(listener, event);
                this._destroySerletContextListeners.add(listener);
            }
        }
    }

    protected void stopContext() throws Exception {
        super.doStop();
        ServletContextEvent event = new ServletContextEvent(this._scontext);
        Collections.reverse(this._destroySerletContextListeners);
        MultiException ex = new MultiException();
        for (ServletContextListener listener : this._destroySerletContextListeners) {
            try {
                this.callContextDestroyed(listener, event);
            } catch (Exception var6) {
                ex.add(var6);
            }
        }
        ex.ifExceptionThrow();
    }

    protected void callContextInitialized(ServletContextListener l, ServletContextEvent e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("contextInitialized: {}->{}", e, l);
        }
        l.contextInitialized(e);
    }

    protected void callContextDestroyed(ServletContextListener l, ServletContextEvent e) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("contextDestroyed: {}->{}", e, l);
        }
        l.contextDestroyed(e);
    }

    @Override
    protected void doStop() throws Exception {
        this._availability = ContextHandler.Availability.UNAVAILABLE;
        ClassLoader old_classloader = null;
        ClassLoader old_webapploader = null;
        Thread current_thread = null;
        ContextHandler.Context old_context = (ContextHandler.Context) __context.get();
        this.enterScope(null, "doStop");
        __context.set(this._scontext);
        try {
            if (this._classLoader != null) {
                old_webapploader = this._classLoader;
                current_thread = Thread.currentThread();
                old_classloader = current_thread.getContextClassLoader();
                current_thread.setContextClassLoader(this._classLoader);
            }
            this.stopContext();
            this.setEventListeners((EventListener[]) this._durableListeners.toArray(new EventListener[this._durableListeners.size()]));
            this._durableListeners.clear();
            if (this._errorHandler != null) {
                this._errorHandler.stop();
            }
            for (EventListener l : this._programmaticListeners) {
                this.removeEventListener(l);
                if (l instanceof ContextHandler.ContextScopeListener) {
                    try {
                        ((ContextHandler.ContextScopeListener) l).exitScope(this._scontext, null);
                    } catch (Throwable var11) {
                        LOG.warn(var11);
                    }
                }
            }
            this._programmaticListeners.clear();
        } finally {
            __context.set(old_context);
            this.exitScope(null);
            LOG.info("Stopped {}", this);
            if ((old_classloader == null || old_classloader != old_webapploader) && current_thread != null) {
                current_thread.setContextClassLoader(old_classloader);
            }
        }
        this._scontext.clearAttributes();
    }

    public boolean checkVirtualHost(Request baseRequest) {
        if (this._vhosts != null && this._vhosts.length > 0) {
            String vhost = this.normalizeHostname(baseRequest.getServerName());
            boolean match = false;
            boolean connectorName = false;
            boolean connectorMatch = false;
            for (String contextVhost : this._vhosts) {
                if (contextVhost != null && contextVhost.length() != 0) {
                    char c = contextVhost.charAt(0);
                    switch(c) {
                        case '*':
                            if (contextVhost.startsWith("*.")) {
                                match = match || contextVhost.regionMatches(true, 2, vhost, vhost.indexOf(".") + 1, contextVhost.length() - 2);
                            }
                            break;
                        case '@':
                            connectorName = true;
                            String name = baseRequest.getHttpChannel().getConnector().getName();
                            boolean m = name != null && contextVhost.length() == name.length() + 1 && contextVhost.endsWith(name);
                            match = match || m;
                            connectorMatch = connectorMatch || m;
                            break;
                        default:
                            match = match || contextVhost.equalsIgnoreCase(vhost);
                    }
                }
            }
            if (!match || connectorName && !connectorMatch) {
                return false;
            }
        }
        return true;
    }

    public boolean checkContextPath(String uri) {
        if (this._contextPath.length() > 1) {
            if (!uri.startsWith(this._contextPath)) {
                return false;
            }
            if (uri.length() > this._contextPath.length() && uri.charAt(this._contextPath.length()) != '/') {
                return false;
            }
        }
        return true;
    }

    public boolean checkContext(String target, Request baseRequest, HttpServletResponse response) throws IOException {
        DispatcherType dispatch = baseRequest.getDispatcherType();
        if (!this.checkVirtualHost(baseRequest)) {
            return false;
        } else if (!this.checkContextPath(target)) {
            return false;
        } else if (!this._allowNullPathInfo && this._contextPath.length() == target.length() && this._contextPath.length() > 1) {
            baseRequest.setHandled(true);
            if (baseRequest.getQueryString() != null) {
                response.sendRedirect(URIUtil.addPaths(baseRequest.getRequestURI(), "/") + "?" + baseRequest.getQueryString());
            } else {
                response.sendRedirect(URIUtil.addPaths(baseRequest.getRequestURI(), "/"));
            }
            return false;
        } else {
            switch(this._availability) {
                case SHUTDOWN:
                case UNAVAILABLE:
                    baseRequest.setHandled(true);
                    response.sendError(503);
                    return false;
                default:
                    return !DispatcherType.REQUEST.equals(dispatch) || !baseRequest.isHandled();
            }
        }
    }

    @Override
    public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("scope {}|{}|{} @ {}", baseRequest.getContextPath(), baseRequest.getServletPath(), baseRequest.getPathInfo(), this);
        }
        ContextHandler.Context old_context = null;
        String old_context_path = null;
        String old_servlet_path = null;
        String old_path_info = null;
        ClassLoader old_classloader = null;
        Thread current_thread = null;
        String pathInfo = target;
        DispatcherType dispatch = baseRequest.getDispatcherType();
        old_context = baseRequest.getContext();
        if (old_context != this._scontext) {
            if (DispatcherType.REQUEST.equals(dispatch) || DispatcherType.ASYNC.equals(dispatch) || DispatcherType.ERROR.equals(dispatch) && baseRequest.getHttpChannelState().isAsync()) {
                if (this._compactPath) {
                    target = URIUtil.compactPath(target);
                }
                if (!this.checkContext(target, baseRequest, response)) {
                    return;
                }
                if (target.length() > this._contextPath.length()) {
                    if (this._contextPath.length() > 1) {
                        target = target.substring(this._contextPath.length());
                    }
                    pathInfo = target;
                } else if (this._contextPath.length() == 1) {
                    target = "/";
                    pathInfo = "/";
                } else {
                    target = "/";
                    pathInfo = null;
                }
            }
            if (this._classLoader != null) {
                current_thread = Thread.currentThread();
                old_classloader = current_thread.getContextClassLoader();
                current_thread.setContextClassLoader(this._classLoader);
            }
        }
        try {
            old_context_path = baseRequest.getContextPath();
            old_servlet_path = baseRequest.getServletPath();
            old_path_info = baseRequest.getPathInfo();
            baseRequest.setContext(this._scontext);
            __context.set(this._scontext);
            if (!DispatcherType.INCLUDE.equals(dispatch) && target.startsWith("/")) {
                if (this._contextPath.length() == 1) {
                    baseRequest.setContextPath("");
                } else {
                    baseRequest.setContextPath(this._contextPathEncoded);
                }
                baseRequest.setServletPath(null);
                baseRequest.setPathInfo(pathInfo);
            }
            if (old_context != this._scontext) {
                this.enterScope(baseRequest, dispatch);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("context={}|{}|{} @ {}", baseRequest.getContextPath(), baseRequest.getServletPath(), baseRequest.getPathInfo(), this);
            }
            this.nextScope(target, baseRequest, request, response);
        } finally {
            if (old_context != this._scontext) {
                this.exitScope(baseRequest);
                if (this._classLoader != null && current_thread != null) {
                    current_thread.setContextClassLoader(old_classloader);
                }
                baseRequest.setContext(old_context);
                __context.set(old_context);
                baseRequest.setContextPath(old_context_path);
                baseRequest.setServletPath(old_servlet_path);
                baseRequest.setPathInfo(old_path_info);
            }
        }
    }

    protected void requestInitialized(Request baseRequest, HttpServletRequest request) {
        if (!this._servletRequestAttributeListeners.isEmpty()) {
            for (ServletRequestAttributeListener l : this._servletRequestAttributeListeners) {
                baseRequest.addEventListener(l);
            }
        }
        if (!this._servletRequestListeners.isEmpty()) {
            ServletRequestEvent sre = new ServletRequestEvent(this._scontext, request);
            for (ServletRequestListener l : this._servletRequestListeners) {
                l.requestInitialized(sre);
            }
        }
    }

    protected void requestDestroyed(Request baseRequest, HttpServletRequest request) {
        if (!this._servletRequestListeners.isEmpty()) {
            ServletRequestEvent sre = new ServletRequestEvent(this._scontext, request);
            int i = this._servletRequestListeners.size();
            while (i-- > 0) {
                ((ServletRequestListener) this._servletRequestListeners.get(i)).requestDestroyed(sre);
            }
        }
        if (!this._servletRequestAttributeListeners.isEmpty()) {
            int i = this._servletRequestAttributeListeners.size();
            while (i-- > 0) {
                baseRequest.removeEventListener((EventListener) this._servletRequestAttributeListeners.get(i));
            }
        }
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DispatcherType dispatch = baseRequest.getDispatcherType();
        boolean new_context = baseRequest.takeNewContext();
        try {
            if (new_context) {
                this.requestInitialized(baseRequest, request);
            }
            switch(dispatch) {
                case REQUEST:
                    if (this.isProtectedTarget(target)) {
                        response.sendError(404);
                        baseRequest.setHandled(true);
                        return;
                    }
                    break;
                case ERROR:
                    if (!Boolean.TRUE.equals(baseRequest.getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.Dispatcher.ERROR"))) {
                        this.doError(target, baseRequest, request, response);
                        return;
                    }
            }
            this.nextHandle(target, baseRequest, request, response);
        } finally {
            if (new_context) {
                this.requestDestroyed(baseRequest, request);
            }
        }
    }

    protected void enterScope(Request request, Object reason) {
        if (!this._contextListeners.isEmpty()) {
            for (ContextHandler.ContextScopeListener listener : this._contextListeners) {
                try {
                    listener.enterScope(this._scontext, request, reason);
                } catch (Throwable var6) {
                    LOG.warn(var6);
                }
            }
        }
    }

    protected void exitScope(Request request) {
        if (!this._contextListeners.isEmpty()) {
            int i = this._contextListeners.size();
            while (i-- > 0) {
                try {
                    ((ContextHandler.ContextScopeListener) this._contextListeners.get(i)).exitScope(this._scontext, request);
                } catch (Throwable var4) {
                    LOG.warn(var4);
                }
            }
        }
    }

    public void handle(Request request, Runnable runnable) {
        ClassLoader old_classloader = null;
        Thread current_thread = null;
        ContextHandler.Context old_context = (ContextHandler.Context) __context.get();
        if (old_context == this._scontext) {
            runnable.run();
        } else {
            try {
                __context.set(this._scontext);
                if (this._classLoader != null) {
                    current_thread = Thread.currentThread();
                    old_classloader = current_thread.getContextClassLoader();
                    current_thread.setContextClassLoader(this._classLoader);
                }
                this.enterScope(request, runnable);
                runnable.run();
            } finally {
                this.exitScope(request);
                __context.set(old_context);
                if (old_classloader != null) {
                    current_thread.setContextClassLoader(old_classloader);
                }
            }
        }
    }

    public void handle(Runnable runnable) {
        this.handle(null, runnable);
    }

    public boolean isProtectedTarget(String target) {
        if (target != null && this._protectedTargets != null) {
            while (target.startsWith("//")) {
                target = URIUtil.compactPath(target);
            }
            for (int i = 0; i < this._protectedTargets.length; i++) {
                String t = this._protectedTargets[i];
                if (StringUtil.startsWithIgnoreCase(target, t)) {
                    if (target.length() == t.length()) {
                        return true;
                    }
                    char c = target.charAt(t.length());
                    if (c == '/' || c == '?' || c == '#' || c == ';') {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public void setProtectedTargets(String[] targets) {
        if (targets == null) {
            this._protectedTargets = null;
        } else {
            this._protectedTargets = (String[]) Arrays.copyOf(targets, targets.length);
        }
    }

    public String[] getProtectedTargets() {
        return this._protectedTargets == null ? null : (String[]) Arrays.copyOf(this._protectedTargets, this._protectedTargets.length);
    }

    @Override
    public void removeAttribute(String name) {
        this._attributes.removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this._attributes.setAttribute(name, value);
    }

    public void setAttributes(Attributes attributes) {
        this._attributes.clearAttributes();
        this._attributes.addAll(attributes);
    }

    @Override
    public void clearAttributes() {
        this._attributes.clearAttributes();
    }

    public void setManagedAttribute(String name, Object value) {
        Object old = this._managedAttributes.put(name, value);
        this.updateBean(old, value);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this._classLoader = classLoader;
    }

    public void setContextPath(String contextPath) {
        if (contextPath == null) {
            throw new IllegalArgumentException("null contextPath");
        } else {
            if (contextPath.endsWith("/*")) {
                LOG.warn(this + " contextPath ends with /*");
                contextPath = contextPath.substring(0, contextPath.length() - 2);
            } else if (contextPath.length() > 1 && contextPath.endsWith("/")) {
                LOG.warn(this + " contextPath ends with /");
                contextPath = contextPath.substring(0, contextPath.length() - 1);
            }
            if (contextPath.length() == 0) {
                LOG.warn("Empty contextPath");
                contextPath = "/";
            }
            this._contextPath = contextPath;
            this._contextPathEncoded = URIUtil.encodePath(contextPath);
            if (this.getServer() != null && (this.getServer().isStarting() || this.getServer().isStarted())) {
                Handler[] contextCollections = this.getServer().getChildHandlersByClass(ContextHandlerCollection.class);
                for (int h = 0; contextCollections != null && h < contextCollections.length; h++) {
                    ((ContextHandlerCollection) contextCollections[h]).mapContexts();
                }
            }
        }
    }

    public void setDisplayName(String servletContextName) {
        this._displayName = servletContextName;
    }

    public Resource getBaseResource() {
        return this._baseResource == null ? null : this._baseResource;
    }

    @ManagedAttribute("document root for context")
    public String getResourceBase() {
        return this._baseResource == null ? null : this._baseResource.toString();
    }

    public void setBaseResource(Resource base) {
        this._baseResource = base;
    }

    public void setResourceBase(String resourceBase) {
        try {
            this.setBaseResource(this.newResource(resourceBase));
        } catch (Exception var3) {
            LOG.warn(var3.toString());
            LOG.debug(var3);
            throw new IllegalArgumentException(resourceBase);
        }
    }

    public MimeTypes getMimeTypes() {
        if (this._mimeTypes == null) {
            this._mimeTypes = new MimeTypes();
        }
        return this._mimeTypes;
    }

    public void setMimeTypes(MimeTypes mimeTypes) {
        this._mimeTypes = mimeTypes;
    }

    public void setWelcomeFiles(String[] files) {
        this._welcomeFiles = files;
    }

    @ManagedAttribute(value = "Partial URIs of directory welcome files", readonly = true)
    public String[] getWelcomeFiles() {
        return this._welcomeFiles;
    }

    @ManagedAttribute("The error handler to use for the context")
    public ErrorHandler getErrorHandler() {
        return this._errorHandler;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler != null) {
            errorHandler.setServer(this.getServer());
        }
        this.updateBean(this._errorHandler, errorHandler, true);
        this._errorHandler = errorHandler;
    }

    @ManagedAttribute("The maximum content size")
    public int getMaxFormContentSize() {
        return this._maxFormContentSize;
    }

    public void setMaxFormContentSize(int maxSize) {
        this._maxFormContentSize = maxSize;
    }

    public int getMaxFormKeys() {
        return this._maxFormKeys;
    }

    public void setMaxFormKeys(int max) {
        this._maxFormKeys = max;
    }

    public boolean isCompactPath() {
        return this._compactPath;
    }

    public void setCompactPath(boolean compactPath) {
        this._compactPath = compactPath;
    }

    public String toString() {
        String[] vhosts = this.getVirtualHosts();
        StringBuilder b = new StringBuilder();
        Package pkg = this.getClass().getPackage();
        if (pkg != null) {
            String p = pkg.getName();
            if (p != null && p.length() > 0) {
                String[] ss = p.split("\\.");
                for (String s : ss) {
                    b.append(s.charAt(0)).append('.');
                }
            }
        }
        b.append(this.getClass().getSimpleName()).append('@').append(Integer.toString(this.hashCode(), 16));
        b.append('{').append(this.getContextPath()).append(',').append(this.getBaseResource()).append(',').append(this._availability);
        if (vhosts != null && vhosts.length > 0) {
            b.append(',').append(vhosts[0]);
        }
        b.append('}');
        return b.toString();
    }

    public synchronized Class<?> loadClass(String className) throws ClassNotFoundException {
        if (className == null) {
            return null;
        } else {
            return this._classLoader == null ? Loader.loadClass(className) : this._classLoader.loadClass(className);
        }
    }

    public void addLocaleEncoding(String locale, String encoding) {
        if (this._localeEncodingMap == null) {
            this._localeEncodingMap = new HashMap();
        }
        this._localeEncodingMap.put(locale, encoding);
    }

    public String getLocaleEncoding(String locale) {
        return this._localeEncodingMap == null ? null : (String) this._localeEncodingMap.get(locale);
    }

    public String getLocaleEncoding(Locale locale) {
        if (this._localeEncodingMap == null) {
            return null;
        } else {
            String encoding = (String) this._localeEncodingMap.get(locale.toString());
            if (encoding == null) {
                encoding = (String) this._localeEncodingMap.get(locale.getLanguage());
            }
            return encoding;
        }
    }

    public Map<String, String> getLocaleEncodings() {
        return this._localeEncodingMap == null ? null : Collections.unmodifiableMap(this._localeEncodingMap);
    }

    public Resource getResource(String path) throws MalformedURLException {
        if (path == null || !path.startsWith("/")) {
            throw new MalformedURLException(path);
        } else if (this._baseResource == null) {
            return null;
        } else {
            try {
                path = URIUtil.canonicalPath(path);
                Resource resource = this._baseResource.addPath(path);
                return this.checkAlias(path, resource) ? resource : null;
            } catch (Exception var3) {
                LOG.ignore(var3);
                return null;
            }
        }
    }

    public boolean checkAlias(String path, Resource resource) {
        if (resource.isAlias()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Aliased resource: " + resource + "~=" + resource.getAlias());
            }
            for (ContextHandler.AliasCheck check : this._aliasChecks) {
                if (check.check(path, resource)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Aliased resource: " + resource + " approved by " + check);
                    }
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    public Resource newResource(URL url) throws IOException {
        return Resource.newResource(url);
    }

    public Resource newResource(URI uri) throws IOException {
        return Resource.newResource(uri);
    }

    public Resource newResource(String urlOrPath) throws IOException {
        return Resource.newResource(urlOrPath);
    }

    public Set<String> getResourcePaths(String path) {
        try {
            path = URIUtil.canonicalPath(path);
            Resource resource = this.getResource(path);
            if (resource != null && resource.exists()) {
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                String[] l = resource.list();
                if (l != null) {
                    HashSet<String> set = new HashSet();
                    for (int i = 0; i < l.length; i++) {
                        set.add(path + l[i]);
                    }
                    return set;
                }
            }
        } catch (Exception var6) {
            LOG.ignore(var6);
        }
        return Collections.emptySet();
    }

    private String normalizeHostname(String host) {
        if (host == null) {
            return null;
        } else {
            return host.endsWith(".") ? host.substring(0, host.length() - 1) : host;
        }
    }

    public void addAliasCheck(ContextHandler.AliasCheck check) {
        this._aliasChecks.add(check);
    }

    public List<ContextHandler.AliasCheck> getAliasChecks() {
        return this._aliasChecks;
    }

    public void setAliasChecks(List<ContextHandler.AliasCheck> checks) {
        this._aliasChecks.clear();
        this._aliasChecks.addAll(checks);
    }

    public void clearAliasChecks() {
        this._aliasChecks.clear();
    }

    public interface AliasCheck {

        boolean check(String var1, Resource var2);
    }

    public static class ApproveAliases implements ContextHandler.AliasCheck {

        @Override
        public boolean check(String path, Resource resource) {
            return true;
        }
    }

    public static class ApproveNonExistentDirectoryAliases implements ContextHandler.AliasCheck {

        @Override
        public boolean check(String path, Resource resource) {
            if (resource.exists()) {
                return false;
            } else {
                String a = resource.getAlias().toString();
                String r = resource.getURI().toString();
                if (a.length() > r.length()) {
                    return a.startsWith(r) && a.length() == r.length() + 1 && a.endsWith("/");
                } else {
                    return a.length() >= r.length() ? a.equals(r) : r.startsWith(a) && r.length() == a.length() + 1 && r.endsWith("/");
                }
            }
        }
    }

    public static enum Availability {

        UNAVAILABLE, STARTING, AVAILABLE, SHUTDOWN
    }

    public class Context extends ContextHandler.StaticContext {

        protected boolean _enabled = true;

        protected boolean _extendedListenerTypes = false;

        protected Context() {
        }

        public ContextHandler getContextHandler() {
            return ContextHandler.this;
        }

        @Override
        public ServletContext getContext(String uripath) {
            List<ContextHandler> contexts = new ArrayList();
            Handler[] handlers = ContextHandler.this.getServer().getChildHandlersByClass(ContextHandler.class);
            String matched_path = null;
            for (Handler handler : handlers) {
                if (handler != null) {
                    ContextHandler ch = (ContextHandler) handler;
                    String context_path = ch.getContextPath();
                    if (uripath.equals(context_path) || uripath.startsWith(context_path) && uripath.charAt(context_path.length()) == '/' || "/".equals(context_path)) {
                        if (ContextHandler.this.getVirtualHosts() != null && ContextHandler.this.getVirtualHosts().length > 0) {
                            if (ch.getVirtualHosts() != null && ch.getVirtualHosts().length > 0) {
                                for (String h1 : ContextHandler.this.getVirtualHosts()) {
                                    for (String h2 : ch.getVirtualHosts()) {
                                        if (h1.equals(h2)) {
                                            if (matched_path == null || context_path.length() > matched_path.length()) {
                                                contexts.clear();
                                                matched_path = context_path;
                                            }
                                            if (matched_path.equals(context_path)) {
                                                contexts.add(ch);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (matched_path == null || context_path.length() > matched_path.length()) {
                                contexts.clear();
                                matched_path = context_path;
                            }
                            if (matched_path.equals(context_path)) {
                                contexts.add(ch);
                            }
                        }
                    }
                }
            }
            if (contexts.size() > 0) {
                return ((ContextHandler) contexts.get(0))._scontext;
            } else {
                matched_path = null;
                for (Handler handlerx : handlers) {
                    if (handlerx != null) {
                        ContextHandler ch = (ContextHandler) handlerx;
                        String context_path = ch.getContextPath();
                        if (uripath.equals(context_path) || uripath.startsWith(context_path) && uripath.charAt(context_path.length()) == '/' || "/".equals(context_path)) {
                            if (matched_path == null || context_path.length() > matched_path.length()) {
                                contexts.clear();
                                matched_path = context_path;
                            }
                            if (matched_path != null && matched_path.equals(context_path)) {
                                contexts.add(ch);
                            }
                        }
                    }
                }
                return contexts.size() > 0 ? ((ContextHandler) contexts.get(0))._scontext : null;
            }
        }

        @Override
        public String getMimeType(String file) {
            return ContextHandler.this._mimeTypes == null ? null : ContextHandler.this._mimeTypes.getMimeByExtension(file);
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String uriInContext) {
            if (uriInContext == null) {
                return null;
            } else if (!uriInContext.startsWith("/")) {
                return null;
            } else {
                try {
                    HttpURI uri = new HttpURI(null, null, 0, uriInContext);
                    String pathInfo = URIUtil.canonicalPath(uri.getDecodedPath());
                    if (pathInfo == null) {
                        return null;
                    } else {
                        String contextPath = this.getContextPath();
                        if (contextPath != null && contextPath.length() > 0) {
                            uri.setPath(URIUtil.addPaths(contextPath, uri.getPath()));
                        }
                        return new Dispatcher(ContextHandler.this, uri, pathInfo);
                    }
                } catch (Exception var5) {
                    ContextHandler.LOG.ignore(var5);
                    return null;
                }
            }
        }

        @Override
        public String getRealPath(String path) {
            if (path == null) {
                return null;
            } else {
                if (path.length() == 0) {
                    path = "/";
                } else if (path.charAt(0) != '/') {
                    path = "/" + path;
                }
                try {
                    Resource resource = ContextHandler.this.getResource(path);
                    if (resource != null) {
                        File file = resource.getFile();
                        if (file != null) {
                            return file.getCanonicalPath();
                        }
                    }
                } catch (Exception var4) {
                    ContextHandler.LOG.ignore(var4);
                }
                return null;
            }
        }

        @Override
        public URL getResource(String path) throws MalformedURLException {
            Resource resource = ContextHandler.this.getResource(path);
            return resource != null && resource.exists() ? resource.getURI().toURL() : null;
        }

        @Override
        public InputStream getResourceAsStream(String path) {
            try {
                URL url = this.getResource(path);
                if (url == null) {
                    return null;
                } else {
                    Resource r = Resource.newResource(url);
                    return r.isDirectory() ? null : r.getInputStream();
                }
            } catch (Exception var4) {
                ContextHandler.LOG.ignore(var4);
                return null;
            }
        }

        @Override
        public Set<String> getResourcePaths(String path) {
            return ContextHandler.this.getResourcePaths(path);
        }

        @Override
        public void log(Exception exception, String msg) {
            ContextHandler.this._logger.warn(msg, exception);
        }

        @Override
        public void log(String msg) {
            ContextHandler.this._logger.info(msg);
        }

        @Override
        public void log(String message, Throwable throwable) {
            ContextHandler.this._logger.warn(message, throwable);
        }

        @Override
        public String getInitParameter(String name) {
            return ContextHandler.this.getInitParameter(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return ContextHandler.this.getInitParameterNames();
        }

        @Override
        public synchronized Object getAttribute(String name) {
            Object o = ContextHandler.this.getAttribute(name);
            if (o == null) {
                o = super.getAttribute(name);
            }
            return o;
        }

        @Override
        public synchronized Enumeration<String> getAttributeNames() {
            HashSet<String> set = new HashSet();
            Enumeration<String> e = super.getAttributeNames();
            while (e.hasMoreElements()) {
                set.add(e.nextElement());
            }
            e = ContextHandler.this._attributes.getAttributeNames();
            while (e.hasMoreElements()) {
                set.add(e.nextElement());
            }
            return Collections.enumeration(set);
        }

        @Override
        public synchronized void setAttribute(String name, Object value) {
            Object old_value = super.getAttribute(name);
            if (value == null) {
                super.removeAttribute(name);
            } else {
                super.setAttribute(name, value);
            }
            if (!ContextHandler.this._servletContextAttributeListeners.isEmpty()) {
                ServletContextAttributeEvent event = new ServletContextAttributeEvent(ContextHandler.this._scontext, name, old_value == null ? value : old_value);
                for (ServletContextAttributeListener l : ContextHandler.this._servletContextAttributeListeners) {
                    if (old_value == null) {
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
        public synchronized void removeAttribute(String name) {
            Object old_value = super.getAttribute(name);
            super.removeAttribute(name);
            if (old_value != null && !ContextHandler.this._servletContextAttributeListeners.isEmpty()) {
                ServletContextAttributeEvent event = new ServletContextAttributeEvent(ContextHandler.this._scontext, name, old_value);
                for (ServletContextAttributeListener l : ContextHandler.this._servletContextAttributeListeners) {
                    l.attributeRemoved(event);
                }
            }
        }

        @Override
        public String getServletContextName() {
            String name = ContextHandler.this.getDisplayName();
            if (name == null) {
                name = ContextHandler.this.getContextPath();
            }
            return name;
        }

        @Override
        public String getContextPath() {
            return ContextHandler.this._contextPath != null && ContextHandler.this._contextPath.equals("/") ? "" : ContextHandler.this._contextPath;
        }

        @Override
        public String toString() {
            return "ServletContext@" + ContextHandler.this.toString();
        }

        @Override
        public boolean setInitParameter(String name, String value) {
            if (ContextHandler.this.getInitParameter(name) != null) {
                return false;
            } else {
                ContextHandler.this.getInitParams().put(name, value);
                return true;
            }
        }

        @Override
        public void addListener(String className) {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                try {
                    Class<? extends EventListener> clazz = ContextHandler.this._classLoader == null ? Loader.loadClass(className) : ContextHandler.this._classLoader.loadClass(className);
                    this.addListener(clazz);
                } catch (ClassNotFoundException var3) {
                    throw new IllegalArgumentException(var3);
                }
            }
        }

        @Override
        public <T extends EventListener> void addListener(T t) {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                this.checkListener(t.getClass());
                ContextHandler.this.addEventListener(t);
                ContextHandler.this.addProgrammaticListener(t);
            }
        }

        @Override
        public void addListener(Class<? extends EventListener> listenerClass) {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else {
                try {
                    EventListener e = this.createListener(listenerClass);
                    this.addListener(e);
                } catch (ServletException var3) {
                    throw new IllegalArgumentException(var3);
                }
            }
        }

        @Override
        public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
            try {
                return this.createInstance(clazz);
            } catch (Exception var3) {
                throw new ServletException(var3);
            }
        }

        public void checkListener(Class<? extends EventListener> listener) throws IllegalStateException {
            boolean ok = false;
            int startIndex = this.isExtendedListenerTypes() ? 0 : 1;
            for (int i = startIndex; i < ContextHandler.SERVLET_LISTENER_TYPES.length; i++) {
                if (ContextHandler.SERVLET_LISTENER_TYPES[i].isAssignableFrom(listener)) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                throw new IllegalArgumentException("Inappropriate listener class " + listener.getName());
            }
        }

        public void setExtendedListenerTypes(boolean extended) {
            this._extendedListenerTypes = extended;
        }

        public boolean isExtendedListenerTypes() {
            return this._extendedListenerTypes;
        }

        @Override
        public ClassLoader getClassLoader() {
            if (!this._enabled) {
                throw new UnsupportedOperationException();
            } else if (!ContextHandler.this._usingSecurityManager) {
                return ContextHandler.this._classLoader;
            } else {
                try {
                    Class<?> reflect = Loader.loadClass("sun.reflect.Reflection");
                    Method getCallerClass = reflect.getMethod("getCallerClass", int.class);
                    Class<?> caller = (Class<?>) getCallerClass.invoke(null, 2);
                    boolean ok = false;
                    ClassLoader callerLoader = caller.getClassLoader();
                    while (!ok && callerLoader != null) {
                        if (callerLoader == ContextHandler.this._classLoader) {
                            ok = true;
                        } else {
                            callerLoader = callerLoader.getParent();
                        }
                    }
                    if (ok) {
                        return ContextHandler.this._classLoader;
                    }
                } catch (Exception var6) {
                    ContextHandler.LOG.warn("Unable to check classloader of caller", var6);
                }
                AccessController.checkPermission(new RuntimePermission("getClassLoader"));
                return ContextHandler.this._classLoader;
            }
        }

        @Override
        public JspConfigDescriptor getJspConfigDescriptor() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        public void setJspConfigDescriptor(JspConfigDescriptor d) {
        }

        @Override
        public void declareRoles(String... roleNames) {
            if (!ContextHandler.this.isStarting()) {
                throw new IllegalStateException();
            } else if (!this._enabled) {
                throw new UnsupportedOperationException();
            }
        }

        public void setEnabled(boolean enabled) {
            this._enabled = enabled;
        }

        public boolean isEnabled() {
            return this._enabled;
        }

        public <T> T createInstance(Class<T> clazz) throws Exception {
            return (T) clazz.newInstance();
        }

        @Override
        public String getVirtualServerName() {
            String[] hosts = ContextHandler.this.getVirtualHosts();
            return hosts != null && hosts.length > 0 ? hosts[0] : null;
        }
    }

    public interface ContextScopeListener extends EventListener {

        void enterScope(ContextHandler.Context var1, Request var2, Object var3);

        void exitScope(ContextHandler.Context var1, Request var2);
    }

    public static class StaticContext extends AttributesMap implements ServletContext {

        private int _effectiveMajorVersion = 3;

        private int _effectiveMinorVersion = 1;

        @Override
        public ServletContext getContext(String uripath) {
            return null;
        }

        @Override
        public int getMajorVersion() {
            return 3;
        }

        @Override
        public String getMimeType(String file) {
            return null;
        }

        @Override
        public int getMinorVersion() {
            return 1;
        }

        @Override
        public RequestDispatcher getNamedDispatcher(String name) {
            return null;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String uriInContext) {
            return null;
        }

        @Override
        public String getRealPath(String path) {
            return null;
        }

        @Override
        public URL getResource(String path) throws MalformedURLException {
            return null;
        }

        @Override
        public InputStream getResourceAsStream(String path) {
            return null;
        }

        @Override
        public Set<String> getResourcePaths(String path) {
            return null;
        }

        @Override
        public String getServerInfo() {
            return ContextHandler.__serverInfo;
        }

        @Deprecated
        @Override
        public Servlet getServlet(String name) throws ServletException {
            return null;
        }

        @Deprecated
        @Override
        public Enumeration<String> getServletNames() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        @Deprecated
        @Override
        public Enumeration<Servlet> getServlets() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        @Override
        public void log(Exception exception, String msg) {
            ContextHandler.LOG.warn(msg, exception);
        }

        @Override
        public void log(String msg) {
            ContextHandler.LOG.info(msg);
        }

        @Override
        public void log(String message, Throwable throwable) {
            ContextHandler.LOG.warn(message, throwable);
        }

        @Override
        public String getInitParameter(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        @Override
        public String getServletContextName() {
            return "No Context";
        }

        @Override
        public String getContextPath() {
            return null;
        }

        @Override
        public boolean setInitParameter(String name, String value) {
            return false;
        }

        @Override
        public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public FilterRegistration.Dynamic addFilter(String filterName, String className) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public ServletRegistration.Dynamic addServlet(String servletName, String className) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public FilterRegistration getFilterRegistration(String filterName) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public ServletRegistration getServletRegistration(String servletName) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public Map<String, ? extends ServletRegistration> getServletRegistrations() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public SessionCookieConfig getSessionCookieConfig() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
        }

        @Override
        public void addListener(String className) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
        }

        @Override
        public <T extends EventListener> void addListener(T t) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
        }

        @Override
        public void addListener(Class<? extends EventListener> listenerClass) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
        }

        @Override
        public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
            try {
                return (T) clazz.newInstance();
            } catch (InstantiationException var3) {
                throw new ServletException(var3);
            } catch (IllegalAccessException var4) {
                throw new ServletException(var4);
            }
        }

        @Override
        public ClassLoader getClassLoader() {
            return ContextHandler.class.getClassLoader();
        }

        @Override
        public int getEffectiveMajorVersion() {
            return this._effectiveMajorVersion;
        }

        @Override
        public int getEffectiveMinorVersion() {
            return this._effectiveMinorVersion;
        }

        public void setEffectiveMajorVersion(int v) {
            this._effectiveMajorVersion = v;
        }

        public void setEffectiveMinorVersion(int v) {
            this._effectiveMinorVersion = v;
        }

        @Override
        public JspConfigDescriptor getJspConfigDescriptor() {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
            return null;
        }

        @Override
        public void declareRoles(String... roleNames) {
            ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler");
        }

        @Override
        public String getVirtualServerName() {
            return null;
        }
    }
}