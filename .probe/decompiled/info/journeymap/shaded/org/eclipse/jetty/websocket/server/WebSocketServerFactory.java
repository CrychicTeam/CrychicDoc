package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.http.HttpVersion;
import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.io.MappedByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.server.ConnectionFactory;
import info.journeymap.shaded.org.eclipse.jetty.server.Connector;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpConfiguration;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpConnection;
import info.journeymap.shaded.org.eclipse.jetty.server.HttpConnectionFactory;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.DecoratedObjectFactory;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.ssl.SslContextFactory;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.InvalidWebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.util.QuoteUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.LogicalConnection;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.SessionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.WebSocketSession;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.WebSocketSessionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.EventDriver;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.EventDriverFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.extensions.ExtensionStack;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.extensions.WebSocketExtensionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.AbstractWebSocketConnection;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import info.journeymap.shaded.org.javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class WebSocketServerFactory extends ContainerLifeCycle implements WebSocketCreator, WebSocketContainerScope, WebSocketServletFactory {

    private static final Logger LOG = Log.getLogger(WebSocketServerFactory.class);

    private final ClassLoader contextClassloader;

    private final Map<Integer, WebSocketHandshake> handshakes = new HashMap();

    private final Scheduler scheduler = new ScheduledExecutorScheduler();

    private final List<WebSocketSession.Listener> listeners = new CopyOnWriteArrayList();

    private final String supportedVersions;

    private final WebSocketPolicy defaultPolicy;

    private final EventDriverFactory eventDriverFactory;

    private final ByteBufferPool bufferPool;

    private final WebSocketExtensionFactory extensionFactory;

    private final ServletContext context;

    private final List<SessionFactory> sessionFactories = new ArrayList();

    private final List<Class<?>> registeredSocketClasses = new ArrayList();

    private Executor executor;

    private DecoratedObjectFactory objectFactory;

    private WebSocketCreator creator;

    public WebSocketServerFactory(ServletContext context) {
        this(context, WebSocketPolicy.newServerPolicy(), new MappedByteBufferPool());
    }

    public WebSocketServerFactory(ServletContext context, ByteBufferPool bufferPool) {
        this(context, WebSocketPolicy.newServerPolicy(), bufferPool);
    }

    public WebSocketServerFactory(ServletContext context, WebSocketPolicy policy) {
        this(context, policy, new MappedByteBufferPool());
    }

    public WebSocketServerFactory(ServletContext context, WebSocketPolicy policy, ByteBufferPool bufferPool) {
        this((ServletContext) Objects.requireNonNull(context, ServletContext.class.getName()), policy, null, null, bufferPool);
    }

    protected WebSocketServerFactory(WebSocketPolicy policy, Executor executor, ByteBufferPool bufferPool) {
        this(null, policy, new DecoratedObjectFactory(), executor, bufferPool);
    }

    private WebSocketServerFactory(ServletContext context, WebSocketPolicy policy, DecoratedObjectFactory objectFactory, Executor executor, ByteBufferPool bufferPool) {
        this.context = context;
        this.objectFactory = objectFactory;
        this.executor = executor;
        this.handshakes.put(13, new HandshakeRFC6455());
        this.addBean(this.scheduler);
        this.addBean(bufferPool);
        this.contextClassloader = Thread.currentThread().getContextClassLoader();
        this.defaultPolicy = policy;
        this.eventDriverFactory = new EventDriverFactory(this);
        this.bufferPool = bufferPool;
        this.extensionFactory = new WebSocketExtensionFactory(this);
        this.sessionFactories.add(new WebSocketSessionFactory(this));
        this.creator = this;
        List<Integer> versions = new ArrayList();
        for (int v : this.handshakes.keySet()) {
            versions.add(v);
        }
        Collections.sort(versions, Collections.reverseOrder());
        StringBuilder rv = new StringBuilder();
        for (int v : versions) {
            if (rv.length() > 0) {
                rv.append(", ");
            }
            rv.append(v);
        }
        this.supportedVersions = rv.toString();
    }

    public void addSessionListener(WebSocketSession.Listener listener) {
        this.listeners.add(listener);
    }

    public void removeSessionListener(WebSocketSession.Listener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public boolean acceptWebSocket(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return this.acceptWebSocket(this.getCreator(), request, response);
    }

    @Override
    public boolean acceptWebSocket(WebSocketCreator creator, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        boolean connection;
        try {
            Thread.currentThread().setContextClassLoader(this.contextClassloader);
            ServletUpgradeRequest sockreq = new ServletUpgradeRequest(request);
            ServletUpgradeResponse sockresp = new ServletUpgradeResponse(response);
            Object websocketPojo = creator.createWebSocket(sockreq, sockresp);
            if (sockresp.isCommitted()) {
                return false;
            }
            if (websocketPojo != null) {
                websocketPojo = this.getObjectFactory().decorate(websocketPojo);
                HttpConnection connectionx = (HttpConnection) request.getAttribute("info.journeymap.shaded.org.eclipse.jetty.server.HttpConnection");
                EventDriver driver = this.eventDriverFactory.wrap(websocketPojo);
                return this.upgrade(connectionx, sockreq, sockresp, driver);
            }
            sockresp.sendError(503, "Endpoint Creation Failed");
            connection = false;
        } catch (URISyntaxException var14) {
            throw new IOException("Unable to accept websocket due to mangled URI", var14);
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
        return connection;
    }

    public void addSessionFactory(SessionFactory sessionFactory) {
        if (!this.sessionFactories.contains(sessionFactory)) {
            this.sessionFactories.add(sessionFactory);
        }
    }

    private WebSocketSession createSession(URI requestURI, EventDriver websocket, LogicalConnection connection) {
        if (websocket == null) {
            throw new InvalidWebSocketException("Unable to create Session from null websocket");
        } else {
            for (SessionFactory impl : this.sessionFactories) {
                if (impl.supports(websocket)) {
                    try {
                        return impl.createSession(requestURI, websocket, connection);
                    } catch (Throwable var7) {
                        throw new InvalidWebSocketException("Unable to create Session", var7);
                    }
                }
            }
            throw new InvalidWebSocketException("Unable to create Session: unrecognized internal EventDriver type: " + websocket.getClass().getName());
        }
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        if (this.registeredSocketClasses.size() < 1) {
            throw new WebSocketException("No WebSockets have been registered with the factory.  Cannot use default implementation of WebSocketCreator.");
        } else {
            if (this.registeredSocketClasses.size() > 1) {
                LOG.warn("You have registered more than 1 websocket object, and are using the default WebSocketCreator! Using first registered websocket.");
            }
            Class<?> firstClass = (Class<?>) this.registeredSocketClasses.get(0);
            try {
                return this.objectFactory.createInstance(firstClass);
            } catch (IllegalAccessException | InstantiationException var5) {
                throw new WebSocketException("Unable to create instance of " + firstClass, var5);
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        if (this.objectFactory == null && this.context != null) {
            this.objectFactory = (DecoratedObjectFactory) this.context.getAttribute(DecoratedObjectFactory.ATTR);
            if (this.objectFactory == null) {
                throw new IllegalStateException("Unable to find required ServletContext attribute: " + DecoratedObjectFactory.ATTR);
            }
        }
        if (this.executor == null && this.context != null) {
            ContextHandler contextHandler = ContextHandler.getContextHandler(this.context);
            this.executor = contextHandler.getServer().getThreadPool();
        }
        Objects.requireNonNull(this.objectFactory, DecoratedObjectFactory.class.getName());
        Objects.requireNonNull(this.executor, Executor.class.getName());
        super.doStart();
    }

    @Override
    public ByteBufferPool getBufferPool() {
        return this.bufferPool;
    }

    @Override
    public WebSocketCreator getCreator() {
        return this.creator;
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }

    @Override
    public DecoratedObjectFactory getObjectFactory() {
        return this.objectFactory;
    }

    public EventDriverFactory getEventDriverFactory() {
        return this.eventDriverFactory;
    }

    @Override
    public ExtensionFactory getExtensionFactory() {
        return this.extensionFactory;
    }

    public Collection<WebSocketSession> getOpenSessions() {
        return this.getBeans(WebSocketSession.class);
    }

    @Override
    public WebSocketPolicy getPolicy() {
        return this.defaultPolicy;
    }

    @Override
    public SslContextFactory getSslContextFactory() {
        return null;
    }

    @Override
    public boolean isUpgradeRequest(HttpServletRequest request, HttpServletResponse response) {
        String upgrade = request.getHeader("Upgrade");
        if (upgrade == null) {
            return false;
        } else if (!"websocket".equalsIgnoreCase(upgrade)) {
            return false;
        } else {
            String connection = request.getHeader("Connection");
            if (connection == null) {
                return false;
            } else {
                boolean foundUpgradeToken = false;
                Iterator<String> iter = QuoteUtil.splitAt(connection, ",");
                while (iter.hasNext()) {
                    String token = (String) iter.next();
                    if ("upgrade".equalsIgnoreCase(token)) {
                        foundUpgradeToken = true;
                        break;
                    }
                }
                if (!foundUpgradeToken) {
                    return false;
                } else if (!"GET".equalsIgnoreCase(request.getMethod())) {
                    return false;
                } else if (!"HTTP/1.1".equals(request.getProtocol())) {
                    LOG.debug("Not a 'HTTP/1.1' request (was [" + request.getProtocol() + "])");
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    @Override
    public void onSessionOpened(WebSocketSession session) {
        this.addManaged(session);
        this.notifySessionListeners(listener -> listener.onOpened(session));
    }

    @Override
    public void onSessionClosed(WebSocketSession session) {
        this.removeBean(session);
        this.notifySessionListeners(listener -> listener.onClosed(session));
    }

    private void notifySessionListeners(Consumer<WebSocketSession.Listener> consumer) {
        for (WebSocketSession.Listener listener : this.listeners) {
            try {
                consumer.accept(listener);
            } catch (Throwable var5) {
                LOG.info("Exception while invoking listener " + listener, var5);
            }
        }
    }

    @Override
    public void register(Class<?> websocketPojo) {
        this.registeredSocketClasses.add(websocketPojo);
    }

    @Override
    public void setCreator(WebSocketCreator creator) {
        this.creator = creator;
    }

    private boolean upgrade(HttpConnection http, ServletUpgradeRequest request, ServletUpgradeResponse response, EventDriver driver) throws IOException {
        if (!"websocket".equalsIgnoreCase(request.getHeader("Upgrade"))) {
            throw new IllegalStateException("Not a 'WebSocket: Upgrade' request");
        } else if (!"HTTP/1.1".equals(request.getHttpVersion())) {
            throw new IllegalStateException("Not a 'HTTP/1.1' request");
        } else {
            int version = request.getHeaderInt("Sec-WebSocket-Version");
            if (version < 0) {
                version = request.getHeaderInt("Sec-WebSocket-Draft");
            }
            WebSocketHandshake handshaker = (WebSocketHandshake) this.handshakes.get(version);
            if (handshaker == null) {
                StringBuilder warn = new StringBuilder();
                warn.append("Client ").append(request.getRemoteAddress());
                warn.append(" (:").append(request.getRemotePort());
                warn.append(") User Agent: ");
                String ua = request.getHeader("User-Agent");
                if (ua == null) {
                    warn.append("[unset] ");
                } else {
                    warn.append('"').append(StringUtil.sanitizeXmlString(ua)).append("\" ");
                }
                warn.append("requested WebSocket version [").append(version);
                warn.append("], Jetty supports version");
                if (this.handshakes.size() > 1) {
                    warn.append('s');
                }
                warn.append(": [").append(this.supportedVersions).append("]");
                LOG.warn(warn.toString());
                response.setHeader("Sec-WebSocket-Version", this.supportedVersions);
                response.sendError(400, "Unsupported websocket version specification");
                return false;
            } else {
                ExtensionStack extensionStack = new ExtensionStack(this.getExtensionFactory());
                if (response.isExtensionsNegotiated()) {
                    extensionStack.negotiate(response.getExtensions());
                } else {
                    extensionStack.negotiate(request.getExtensions());
                }
                EndPoint endp = http.getEndPoint();
                Connector connector = http.getConnector();
                Executor executor = connector.getExecutor();
                ByteBufferPool bufferPool = connector.getByteBufferPool();
                AbstractWebSocketConnection wsConnection = new WebSocketServerConnection(endp, executor, this.scheduler, driver.getPolicy(), bufferPool);
                extensionStack.setPolicy(driver.getPolicy());
                extensionStack.configure(wsConnection.getParser());
                extensionStack.configure(wsConnection.getGenerator());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("HttpConnection: {}", http);
                    LOG.debug("WebSocketConnection: {}", wsConnection);
                }
                WebSocketSession session = this.createSession(request.getRequestURI(), driver, wsConnection);
                session.setUpgradeRequest(request);
                response.setExtensions(extensionStack.getNegotiatedExtensions());
                session.setUpgradeResponse(response);
                wsConnection.addListener(session);
                wsConnection.setNextIncomingFrames(extensionStack);
                extensionStack.setNextIncoming(session);
                session.setOutgoingHandler(extensionStack);
                extensionStack.setNextOutgoing(wsConnection);
                session.addManaged(extensionStack);
                this.addManaged(session);
                if (session.isFailed()) {
                    throw new IOException("Session failed to start");
                } else {
                    request.setServletAttribute("info.journeymap.shaded.org.eclipse.jetty.server.HttpConnection.UPGRADE", wsConnection);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Handshake Response: {}", handshaker);
                    }
                    if (this.getSendServerVersion(connector)) {
                        response.setHeader("Server", HttpConfiguration.SERVER_VERSION);
                    }
                    handshaker.doHandshakeResponse(request, response);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Websocket upgrade {} {} {} {}", request.getRequestURI(), version, response.getAcceptedSubProtocol(), wsConnection);
                    }
                    return true;
                }
            }
        }
    }

    private boolean getSendServerVersion(Connector connector) {
        ConnectionFactory connFactory = connector.getConnectionFactory(HttpVersion.HTTP_1_1.asString());
        if (connFactory == null) {
            return false;
        } else {
            if (connFactory instanceof HttpConnectionFactory) {
                HttpConfiguration httpConf = ((HttpConnectionFactory) connFactory).getHttpConfiguration();
                if (httpConf != null) {
                    return httpConf.getSendServerVersion();
                }
            }
            return false;
        }
    }
}