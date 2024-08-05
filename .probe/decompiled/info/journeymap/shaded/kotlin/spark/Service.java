package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.embeddedserver.EmbeddedServer;
import info.journeymap.shaded.kotlin.spark.embeddedserver.EmbeddedServers;
import info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket.WebSocketHandlerClassWrapper;
import info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket.WebSocketHandlerInstanceWrapper;
import info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;
import info.journeymap.shaded.kotlin.spark.globalstate.ServletFlag;
import info.journeymap.shaded.kotlin.spark.route.HttpMethod;
import info.journeymap.shaded.kotlin.spark.route.Routes;
import info.journeymap.shaded.kotlin.spark.route.ServletRoutes;
import info.journeymap.shaded.kotlin.spark.ssl.SslStores;
import info.journeymap.shaded.kotlin.spark.staticfiles.MimeType;
import info.journeymap.shaded.kotlin.spark.staticfiles.StaticFilesConfiguration;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Service extends Routable {

    private static final Logger LOG = LoggerFactory.getLogger("info.journeymap.shaded.kotlin.spark.Spark");

    public static final int SPARK_DEFAULT_PORT = 4567;

    protected static final String DEFAULT_ACCEPT_TYPE = "*/*";

    protected boolean initialized = false;

    protected int port = 4567;

    protected String ipAddress = "0.0.0.0";

    protected SslStores sslStores;

    protected String staticFileFolder = null;

    protected String externalStaticFileFolder = null;

    protected Map<String, WebSocketHandlerWrapper> webSocketHandlers = null;

    protected int maxThreads = -1;

    protected int minThreads = -1;

    protected int threadIdleTimeoutMillis = -1;

    protected Optional<Integer> webSocketIdleTimeoutMillis = Optional.empty();

    protected EmbeddedServer server;

    protected Deque<String> pathDeque = new ArrayDeque();

    protected Routes routes;

    private boolean servletStaticLocationSet;

    private boolean servletExternalStaticLocationSet;

    private CountDownLatch latch = new CountDownLatch(1);

    private Object embeddedServerIdentifier = null;

    public final Redirect redirect;

    public final Service.StaticFiles staticFiles;

    private final StaticFilesConfiguration staticFilesConfiguration;

    private final ExceptionMapper exceptionMapper = ExceptionMapper.getInstance();

    private Consumer<Exception> initExceptionHandler = e -> {
        LOG.error("ignite failed", (Throwable) e);
        System.exit(100);
    };

    public static Service ignite() {
        return new Service();
    }

    private Service() {
        this.redirect = Redirect.create(this);
        this.staticFiles = new Service.StaticFiles();
        if (ServletFlag.isRunningFromServlet()) {
            this.staticFilesConfiguration = StaticFilesConfiguration.servletInstance;
        } else {
            this.staticFilesConfiguration = StaticFilesConfiguration.create();
        }
    }

    public synchronized Service ipAddress(String ipAddress) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        this.ipAddress = ipAddress;
        return this;
    }

    public synchronized Service port(int port) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        this.port = port;
        return this;
    }

    public synchronized int port() {
        if (this.initialized) {
            return this.port;
        } else {
            throw new IllegalStateException("This must be done after route mapping has begun");
        }
    }

    public synchronized Service secure(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword) {
        return this.secure(keystoreFile, keystorePassword, truststoreFile, truststorePassword, false);
    }

    public synchronized Service secure(String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword, boolean needsClientCert) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        if (keystoreFile == null) {
            throw new IllegalArgumentException("Must provide a keystore file to run secured");
        } else {
            this.sslStores = SslStores.create(keystoreFile, keystorePassword, truststoreFile, truststorePassword, needsClientCert);
            return this;
        }
    }

    public synchronized Service threadPool(int maxThreads) {
        return this.threadPool(maxThreads, -1, -1);
    }

    public synchronized Service threadPool(int maxThreads, int minThreads, int idleTimeoutMillis) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        this.maxThreads = maxThreads;
        this.minThreads = minThreads;
        this.threadIdleTimeoutMillis = idleTimeoutMillis;
        return this;
    }

    public synchronized Service staticFileLocation(String folder) {
        if (this.initialized && !ServletFlag.isRunningFromServlet()) {
            this.throwBeforeRouteMappingException();
        }
        this.staticFileFolder = folder;
        if (!this.servletStaticLocationSet) {
            this.staticFilesConfiguration.configure(this.staticFileFolder);
            this.servletStaticLocationSet = true;
        } else {
            LOG.warn("Static file location has already been set");
        }
        return this;
    }

    public synchronized Service externalStaticFileLocation(String externalFolder) {
        if (this.initialized && !ServletFlag.isRunningFromServlet()) {
            this.throwBeforeRouteMappingException();
        }
        this.externalStaticFileFolder = externalFolder;
        if (!this.servletExternalStaticLocationSet) {
            this.staticFilesConfiguration.configureExternal(this.externalStaticFileFolder);
            this.servletExternalStaticLocationSet = true;
        } else {
            LOG.warn("External static file location has already been set");
        }
        return this;
    }

    public void webSocket(String path, Class<?> handlerClass) {
        this.addWebSocketHandler(path, new WebSocketHandlerClassWrapper(handlerClass));
    }

    public void webSocket(String path, Object handler) {
        this.addWebSocketHandler(path, new WebSocketHandlerInstanceWrapper(handler));
    }

    private synchronized void addWebSocketHandler(String path, WebSocketHandlerWrapper handlerWrapper) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        if (ServletFlag.isRunningFromServlet()) {
            throw new IllegalStateException("WebSockets are only supported in the embedded server");
        } else {
            Objects.requireNonNull(path, "WebSocket path cannot be null");
            if (this.webSocketHandlers == null) {
                this.webSocketHandlers = new HashMap();
            }
            this.webSocketHandlers.put(path, handlerWrapper);
        }
    }

    public synchronized Service webSocketIdleTimeoutMillis(int timeoutMillis) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        if (ServletFlag.isRunningFromServlet()) {
            throw new IllegalStateException("WebSockets are only supported in the embedded server");
        } else {
            this.webSocketIdleTimeoutMillis = Optional.of(timeoutMillis);
            return this;
        }
    }

    public synchronized void notFound(String page) {
        CustomErrorPages.add(404, page);
    }

    public synchronized void internalServerError(String page) {
        CustomErrorPages.add(500, page);
    }

    public synchronized void notFound(Route route) {
        CustomErrorPages.add(404, route);
    }

    public synchronized void internalServerError(Route route) {
        CustomErrorPages.add(500, route);
    }

    public void awaitInitialization() {
        try {
            this.latch.await();
        } catch (InterruptedException var2) {
            LOG.info("Interrupted by another thread");
            Thread.currentThread().interrupt();
        }
    }

    private void throwBeforeRouteMappingException() {
        throw new IllegalStateException("This must be done before route mapping has begun");
    }

    private boolean hasMultipleHandlers() {
        return this.webSocketHandlers != null;
    }

    public synchronized void stop() {
        new Thread(() -> {
            if (this.server != null) {
                this.server.extinguish();
                this.latch = new CountDownLatch(1);
            }
            this.routes.clear();
            this.exceptionMapper.clear();
            this.staticFilesConfiguration.clear();
            this.initialized = false;
        }).start();
    }

    public void path(String path, RouteGroup routeGroup) {
        this.pathDeque.addLast(path);
        routeGroup.addRoutes();
        this.pathDeque.removeLast();
    }

    public String getPaths() {
        return (String) this.pathDeque.stream().collect(Collectors.joining(""));
    }

    @Override
    public void addRoute(HttpMethod httpMethod, RouteImpl route) {
        this.init();
        this.routes.add(httpMethod, route.withPrefix(this.getPaths()));
    }

    @Override
    public void addFilter(HttpMethod httpMethod, FilterImpl filter) {
        this.init();
        this.routes.add(httpMethod, filter.withPrefix(this.getPaths()));
    }

    @Deprecated
    @Override
    public void addRoute(String httpMethod, RouteImpl route) {
        this.init();
        this.routes.add(httpMethod + " '" + this.getPaths() + route.getPath() + "'", route.getAcceptType(), route);
    }

    @Deprecated
    @Override
    public void addFilter(String httpMethod, FilterImpl filter) {
        this.init();
        this.routes.add(httpMethod + " '" + this.getPaths() + filter.getPath() + "'", filter.getAcceptType(), filter);
    }

    public synchronized void init() {
        if (!this.initialized) {
            this.initializeRouteMatcher();
            if (!ServletFlag.isRunningFromServlet()) {
                new Thread(() -> {
                    try {
                        EmbeddedServers.initialize();
                        if (this.embeddedServerIdentifier == null) {
                            this.embeddedServerIdentifier = EmbeddedServers.defaultIdentifier();
                        }
                        this.server = EmbeddedServers.create(this.embeddedServerIdentifier, this.routes, this.staticFilesConfiguration, this.hasMultipleHandlers());
                        this.server.configureWebSockets(this.webSocketHandlers, this.webSocketIdleTimeoutMillis);
                        this.port = this.server.ignite(this.ipAddress, this.port, this.sslStores, this.maxThreads, this.minThreads, this.threadIdleTimeoutMillis);
                    } catch (Exception var3) {
                        this.initExceptionHandler.accept(var3);
                    }
                    try {
                        this.latch.countDown();
                        this.server.join();
                    } catch (InterruptedException var2) {
                        LOG.error("server interrupted", (Throwable) var2);
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
            this.initialized = true;
        }
    }

    private void initializeRouteMatcher() {
        if (ServletFlag.isRunningFromServlet()) {
            this.routes = ServletRoutes.get();
        } else {
            this.routes = Routes.create();
        }
    }

    public synchronized <T extends Exception> void exception(Class<T> exceptionClass, final ExceptionHandler<? super T> handler) {
        ExceptionHandlerImpl wrapper = new ExceptionHandlerImpl<T>(exceptionClass) {

            @Override
            public void handle(T exception, Request request, Response response) {
                handler.handle(exception, request, response);
            }
        };
        this.exceptionMapper.map(exceptionClass, wrapper);
    }

    public HaltException halt() {
        throw new HaltException();
    }

    public HaltException halt(int status) {
        throw new HaltException(status);
    }

    public HaltException halt(String body) {
        throw new HaltException(body);
    }

    public HaltException halt(int status, String body) {
        throw new HaltException(status, body);
    }

    public void initExceptionHandler(Consumer<Exception> initExceptionHandler) {
        if (this.initialized) {
            this.throwBeforeRouteMappingException();
        }
        this.initExceptionHandler = initExceptionHandler;
    }

    public final class StaticFiles {

        public void location(String folder) {
            Service.this.staticFileLocation(folder);
        }

        public void externalLocation(String externalFolder) {
            Service.this.externalStaticFileLocation(externalFolder);
        }

        public void headers(Map<String, String> headers) {
            Service.this.staticFilesConfiguration.putCustomHeaders(headers);
        }

        public void header(String key, String value) {
            Service.this.staticFilesConfiguration.putCustomHeader(key, value);
        }

        @Experimental("Functionality will not be removed. The API might change")
        public void expireTime(long seconds) {
            Service.this.staticFilesConfiguration.setExpireTimeSeconds(seconds);
        }

        public void registerMimeType(String extension, String mimeType) {
            MimeType.register(extension, mimeType);
        }

        public void disableMimeTypeGuessing() {
            MimeType.disableGuessing();
        }
    }
}