package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.DateGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpField;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpGenerator;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpHeader;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpMethod;
import info.journeymap.shaded.org.eclipse.jetty.http.HttpURI;
import info.journeymap.shaded.org.eclipse.jetty.http.PreEncodedHttpField;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ErrorHandler;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.HandlerWrapper;
import info.journeymap.shaded.org.eclipse.jetty.util.Attributes;
import info.journeymap.shaded.org.eclipse.jetty.util.AttributesMap;
import info.journeymap.shaded.org.eclipse.jetty.util.Jetty;
import info.journeymap.shaded.org.eclipse.jetty.util.MultiException;
import info.journeymap.shaded.org.eclipse.jetty.util.URIUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Uptime;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.Name;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Graceful;
import info.journeymap.shaded.org.eclipse.jetty.util.component.LifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.QueuedThreadPool;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ShutdownThread;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ThreadPool;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@ManagedObject("Jetty HTTP Servlet server")
public class Server extends HandlerWrapper implements Attributes {

    private static final Logger LOG = Log.getLogger(Server.class);

    private final AttributesMap _attributes = new AttributesMap();

    private final ThreadPool _threadPool;

    private final List<Connector> _connectors = new CopyOnWriteArrayList();

    private SessionIdManager _sessionIdManager;

    private boolean _stopAtShutdown;

    private boolean _dumpAfterStart = false;

    private boolean _dumpBeforeStop = false;

    private ErrorHandler _errorHandler;

    private RequestLog _requestLog;

    private final Locker _dateLocker = new Locker();

    private volatile Server.DateField _dateField;

    public Server() {
        this((ThreadPool) null);
    }

    public Server(@Name("port") int port) {
        this((ThreadPool) null);
        ServerConnector connector = new ServerConnector(this);
        connector.setPort(port);
        this.setConnectors(new Connector[] { connector });
    }

    public Server(@Name("address") InetSocketAddress addr) {
        this((ThreadPool) null);
        ServerConnector connector = new ServerConnector(this);
        connector.setHost(addr.getHostName());
        connector.setPort(addr.getPort());
        this.setConnectors(new Connector[] { connector });
    }

    public Server(@Name("threadpool") ThreadPool pool) {
        this._threadPool = (ThreadPool) (pool != null ? pool : new QueuedThreadPool());
        this.addBean(this._threadPool);
        this.setServer(this);
    }

    public RequestLog getRequestLog() {
        return this._requestLog;
    }

    public ErrorHandler getErrorHandler() {
        return this._errorHandler;
    }

    public void setRequestLog(RequestLog requestLog) {
        this.updateBean(this._requestLog, requestLog);
        this._requestLog = requestLog;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler instanceof ErrorHandler.ErrorPageMapper) {
            throw new IllegalArgumentException("ErrorPageMapper is applicable only to ContextHandler");
        } else {
            this.updateBean(this._errorHandler, errorHandler);
            this._errorHandler = errorHandler;
            if (errorHandler != null) {
                errorHandler.setServer(this);
            }
        }
    }

    @ManagedAttribute("version of this server")
    public static String getVersion() {
        return Jetty.VERSION;
    }

    public boolean getStopAtShutdown() {
        return this._stopAtShutdown;
    }

    @Override
    public void setStopTimeout(long stopTimeout) {
        super.setStopTimeout(stopTimeout);
    }

    public void setStopAtShutdown(boolean stop) {
        if (stop) {
            if (!this._stopAtShutdown && this.isStarted()) {
                ShutdownThread.register(this);
            }
        } else {
            ShutdownThread.deregister(this);
        }
        this._stopAtShutdown = stop;
    }

    @ManagedAttribute(value = "connectors for this server", readonly = true)
    public Connector[] getConnectors() {
        List<Connector> connectors = new ArrayList(this._connectors);
        return (Connector[]) connectors.toArray(new Connector[connectors.size()]);
    }

    public void addConnector(Connector connector) {
        if (connector.getServer() != this) {
            throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server " + connector.getServer() + " and server " + this);
        } else {
            if (this._connectors.add(connector)) {
                this.addBean(connector);
            }
        }
    }

    public void removeConnector(Connector connector) {
        if (this._connectors.remove(connector)) {
            this.removeBean(connector);
        }
    }

    public void setConnectors(Connector[] connectors) {
        if (connectors != null) {
            for (Connector connector : connectors) {
                if (connector.getServer() != this) {
                    throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server " + connector.getServer() + " and server " + this);
                }
            }
        }
        Connector[] oldConnectors = this.getConnectors();
        this.updateBeans(oldConnectors, connectors);
        this._connectors.removeAll(Arrays.asList(oldConnectors));
        if (connectors != null) {
            this._connectors.addAll(Arrays.asList(connectors));
        }
    }

    @ManagedAttribute("the server thread pool")
    public ThreadPool getThreadPool() {
        return this._threadPool;
    }

    @ManagedAttribute("dump state to stderr after start")
    public boolean isDumpAfterStart() {
        return this._dumpAfterStart;
    }

    public void setDumpAfterStart(boolean dumpAfterStart) {
        this._dumpAfterStart = dumpAfterStart;
    }

    @ManagedAttribute("dump state to stderr before stop")
    public boolean isDumpBeforeStop() {
        return this._dumpBeforeStop;
    }

    public void setDumpBeforeStop(boolean dumpBeforeStop) {
        this._dumpBeforeStop = dumpBeforeStop;
    }

    public HttpField getDateField() {
        long now = System.currentTimeMillis();
        long seconds = now / 1000L;
        Server.DateField df = this._dateField;
        if (df == null || df._seconds != seconds) {
            try (Locker.Lock lock = this._dateLocker.lock()) {
                df = this._dateField;
                if (df == null || df._seconds != seconds) {
                    HttpField field = new PreEncodedHttpField(HttpHeader.DATE, DateGenerator.formatDate(now));
                    this._dateField = new Server.DateField(seconds, field);
                    return field;
                }
            }
        }
        return df._dateField;
    }

    @Override
    protected void doStart() throws Exception {
        if (this._errorHandler == null) {
            this._errorHandler = this.getBean(ErrorHandler.class);
        }
        if (this._errorHandler == null) {
            this.setErrorHandler(new ErrorHandler());
        }
        if (this._errorHandler instanceof ErrorHandler.ErrorPageMapper) {
            LOG.warn("ErrorPageMapper not supported for Server level Error Handling");
        }
        if (this.getStopAtShutdown()) {
            ShutdownThread.register(this);
        }
        ShutdownMonitor.register(this);
        ShutdownMonitor.getInstance().start();
        LOG.info("jetty-" + getVersion());
        if (!Jetty.STABLE) {
            LOG.warn("THIS IS NOT A STABLE RELEASE! DO NOT USE IN PRODUCTION!");
            LOG.warn("Download a stable release from http://download.eclipse.org/jetty/");
        }
        HttpGenerator.setJettyVersion(HttpConfiguration.SERVER_VERSION);
        ThreadPool.SizedThreadPool pool = this.getBean(ThreadPool.SizedThreadPool.class);
        int max = pool == null ? -1 : pool.getMaxThreads();
        int selectors = 0;
        int acceptors = 0;
        for (Connector connector : this._connectors) {
            if (connector instanceof AbstractConnector) {
                AbstractConnector abstractConnector = (AbstractConnector) connector;
                Executor connectorExecutor = connector.getExecutor();
                if (connectorExecutor == pool) {
                    acceptors += abstractConnector.getAcceptors();
                    if (connector instanceof ServerConnector) {
                        selectors += 2 * ((ServerConnector) connector).getSelectorManager().getSelectorCount();
                    }
                }
            }
        }
        int needed = 1 + selectors + acceptors;
        if (max > 0 && needed > max) {
            throw new IllegalStateException(String.format("Insufficient threads: max=%d < needed(acceptors=%d + selectors=%d + request=1)", max, acceptors, selectors));
        } else {
            MultiException mex = new MultiException();
            try {
                super.doStart();
            } catch (Throwable var11) {
                mex.add(var11);
            }
            for (Connector connectorx : this._connectors) {
                try {
                    connectorx.start();
                } catch (Throwable var10) {
                    mex.add(var10);
                }
            }
            if (this.isDumpAfterStart()) {
                this.dumpStdErr();
            }
            mex.ifExceptionThrow();
            LOG.info(String.format("Started @%dms", Uptime.getUptime()));
        }
    }

    @Override
    protected void start(LifeCycle l) throws Exception {
        if (!(l instanceof Connector)) {
            super.start(l);
        }
    }

    @Override
    protected void doStop() throws Exception {
        if (this.isDumpBeforeStop()) {
            this.dumpStdErr();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("doStop {}", this);
        }
        MultiException mex = new MultiException();
        List<Future<Void>> futures = new ArrayList();
        for (Connector connector : this._connectors) {
            futures.add(connector.shutdown());
        }
        Handler[] gracefuls = this.getChildHandlersByClass(Graceful.class);
        for (Handler graceful : gracefuls) {
            futures.add(((Graceful) graceful).shutdown());
        }
        long stopTimeout = this.getStopTimeout();
        if (stopTimeout > 0L) {
            long stop_by = System.currentTimeMillis() + stopTimeout;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Graceful shutdown {} by ", this, new Date(stop_by));
            }
            for (Future<Void> future : futures) {
                try {
                    if (!future.isDone()) {
                        future.get(Math.max(1L, stop_by - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
                    }
                } catch (Exception var13) {
                    mex.add(var13);
                }
            }
        }
        for (Future<Void> future : futures) {
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
        for (Connector connector : this._connectors) {
            try {
                connector.stop();
            } catch (Throwable var12) {
                mex.add(var12);
            }
        }
        try {
            super.doStop();
        } catch (Throwable var11) {
            mex.add(var11);
        }
        if (this.getStopAtShutdown()) {
            ShutdownThread.deregister(this);
        }
        ShutdownMonitor.deregister(this);
        mex.ifExceptionThrow();
    }

    public void handle(HttpChannel channel) throws IOException, ServletException {
        String target = channel.getRequest().getPathInfo();
        Request request = channel.getRequest();
        Response response = channel.getResponse();
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} {} {} on {}", request.getDispatcherType(), request.getMethod(), target, channel);
        }
        if (!HttpMethod.OPTIONS.is(request.getMethod()) && !"*".equals(target)) {
            this.handle(target, request, request, response);
        } else {
            if (!HttpMethod.OPTIONS.is(request.getMethod())) {
                response.sendError(400);
            }
            this.handleOptions(request, response);
            if (!request.isHandled()) {
                this.handle(target, request, request, response);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("handled={} async={} committed={} on {}", request.isHandled(), request.isAsyncStarted(), response.isCommitted(), channel);
        }
    }

    protected void handleOptions(Request request, Response response) throws IOException {
    }

    public void handleAsync(HttpChannel channel) throws IOException, ServletException {
        HttpChannelState state = channel.getRequest().getHttpChannelState();
        AsyncContextEvent event = state.getAsyncContextEvent();
        Request baseRequest = channel.getRequest();
        String path = event.getPath();
        if (path != null) {
            ServletContext context = event.getServletContext();
            String query = baseRequest.getQueryString();
            baseRequest.setURIPathQuery(URIUtil.addPaths(context == null ? null : URIUtil.encodePath(context.getContextPath()), path));
            HttpURI uri = baseRequest.getHttpURI();
            baseRequest.setPathInfo(uri.getDecodedPath());
            if (uri.getQuery() != null) {
                baseRequest.mergeQueryParameters(query, uri.getQuery(), true);
            }
        }
        String target = baseRequest.getPathInfo();
        HttpServletRequest request = (HttpServletRequest) event.getSuppliedRequest();
        HttpServletResponse response = (HttpServletResponse) event.getSuppliedResponse();
        if (LOG.isDebugEnabled()) {
            LOG.debug("{} {} {} on {}", request.getDispatcherType(), request.getMethod(), target, channel);
        }
        this.handle(target, baseRequest, request, response);
        if (LOG.isDebugEnabled()) {
            LOG.debug("handledAsync={} async={} committed={} on {}", channel.getRequest().isHandled(), request.isAsyncStarted(), response.isCommitted(), channel);
        }
    }

    public void join() throws InterruptedException {
        this.getThreadPool().join();
    }

    public SessionIdManager getSessionIdManager() {
        return this._sessionIdManager;
    }

    public void setSessionIdManager(SessionIdManager sessionIdManager) {
        this.updateBean(this._sessionIdManager, sessionIdManager);
        this._sessionIdManager = sessionIdManager;
    }

    @Override
    public void clearAttributes() {
        Enumeration<String> names = this._attributes.getAttributeNames();
        while (names.hasMoreElements()) {
            this.removeBean(this._attributes.getAttribute((String) names.nextElement()));
        }
        this._attributes.clearAttributes();
    }

    @Override
    public Object getAttribute(String name) {
        return this._attributes.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return AttributesMap.getAttributeNamesCopy(this._attributes);
    }

    @Override
    public void removeAttribute(String name) {
        Object bean = this._attributes.getAttribute(name);
        if (bean != null) {
            this.removeBean(bean);
        }
        this._attributes.removeAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object attribute) {
        Object old = this._attributes.getAttribute(name);
        this.updateBean(old, attribute);
        this._attributes.setAttribute(name, attribute);
    }

    public URI getURI() {
        NetworkConnector connector = null;
        for (Connector c : this._connectors) {
            if (c instanceof NetworkConnector) {
                connector = (NetworkConnector) c;
                break;
            }
        }
        if (connector == null) {
            return null;
        } else {
            ContextHandler context = this.getChildHandlerByClass(ContextHandler.class);
            try {
                String protocol = connector.getDefaultConnectionFactory().getProtocol();
                String scheme = "http";
                if (protocol.startsWith("SSL-") || protocol.equals("SSL")) {
                    scheme = "https";
                }
                String host = connector.getHost();
                if (context != null && context.getVirtualHosts() != null && context.getVirtualHosts().length > 0) {
                    host = context.getVirtualHosts()[0];
                }
                if (host == null) {
                    host = InetAddress.getLocalHost().getHostAddress();
                }
                String path = context == null ? null : context.getContextPath();
                if (path == null) {
                    path = "/";
                }
                return new URI(scheme, null, host, connector.getLocalPort(), path, null, null);
            } catch (Exception var7) {
                LOG.warn(var7);
                return null;
            }
        }
    }

    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpBeans(out, indent, new Collection[] { Collections.singleton(new ClassLoaderDump(this.getClass().getClassLoader())) });
    }

    public static void main(String... args) throws Exception {
        System.err.println(getVersion());
    }

    private static class DateField {

        final long _seconds;

        final HttpField _dateField;

        public DateField(long seconds, HttpField dateField) {
            this._seconds = seconds;
            this._dateField = dateField;
        }
    }
}