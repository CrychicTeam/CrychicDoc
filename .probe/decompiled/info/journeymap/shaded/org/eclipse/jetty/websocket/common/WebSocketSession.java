package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import info.journeymap.shaded.org.eclipse.jetty.util.component.ContainerLifeCycle;
import info.journeymap.shaded.org.eclipse.jetty.util.component.Dumpable;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.ThreadClassLoaderScope;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.CloseException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.CloseStatus;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.RemoteEndpoint;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.Session;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.SuspendToken;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.UpgradeRequest;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.UpgradeResponse;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketBehavior;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.EventDriver;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.IOState;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketSessionScope;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ManagedObject("A Jetty WebSocket Session")
public class WebSocketSession extends ContainerLifeCycle implements Session, RemoteEndpointFactory, WebSocketSessionScope, IncomingFrames, Connection.Listener, IOState.ConnectionStateListener {

    private static final Logger LOG = Log.getLogger(WebSocketSession.class);

    private static final Logger LOG_OPEN = Log.getLogger(WebSocketSession.class.getName() + "_OPEN");

    private final WebSocketContainerScope containerScope;

    private final URI requestURI;

    private final LogicalConnection connection;

    private final EventDriver websocket;

    private final Executor executor;

    private final WebSocketPolicy policy;

    private ClassLoader classLoader;

    private ExtensionFactory extensionFactory;

    private RemoteEndpointFactory remoteEndpointFactory;

    private String protocolVersion;

    private Map<String, String[]> parameterMap = new HashMap();

    private RemoteEndpoint remote;

    private IncomingFrames incomingHandler;

    private OutgoingFrames outgoingHandler;

    private UpgradeRequest upgradeRequest;

    private UpgradeResponse upgradeResponse;

    private CompletableFuture<Session> openFuture;

    public WebSocketSession(WebSocketContainerScope containerScope, URI requestURI, EventDriver websocket, LogicalConnection connection) {
        Objects.requireNonNull(containerScope, "Container Scope cannot be null");
        Objects.requireNonNull(requestURI, "Request URI cannot be null");
        this.classLoader = Thread.currentThread().getContextClassLoader();
        this.containerScope = containerScope;
        this.requestURI = requestURI;
        this.websocket = websocket;
        this.connection = connection;
        this.executor = connection.getExecutor();
        this.outgoingHandler = connection;
        this.incomingHandler = websocket;
        this.connection.getIOState().addListener(this);
        this.policy = websocket.getPolicy();
        this.addBean(this.connection);
        this.addBean(this.websocket);
    }

    @Override
    public void close() {
        this.close(1000, null);
    }

    @Override
    public void close(CloseStatus closeStatus) {
        this.close(closeStatus.getCode(), closeStatus.getPhrase());
    }

    @Override
    public void close(int statusCode, String reason) {
        this.connection.close(statusCode, reason);
    }

    @Override
    public void disconnect() {
        this.connection.disconnect();
        this.notifyClose(1006, "Harsh disconnect");
    }

    public void dispatch(Runnable runnable) {
        this.executor.execute(runnable);
    }

    @Override
    protected void doStart() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("starting - {}", this);
        }
        Iterator<RemoteEndpointFactory> iter = ServiceLoader.load(RemoteEndpointFactory.class).iterator();
        if (iter.hasNext()) {
            this.remoteEndpointFactory = (RemoteEndpointFactory) iter.next();
        }
        if (this.remoteEndpointFactory == null) {
            this.remoteEndpointFactory = this;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Using RemoteEndpointFactory: {}", this.remoteEndpointFactory);
        }
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("stopping - {}", this);
        }
        try {
            this.close(1001, "Shutdown");
        } catch (Throwable var2) {
            LOG.debug("During Connection Shutdown", var2);
        }
        super.doStop();
    }

    @Override
    public void dump(Appendable out, String indent) throws IOException {
        this.dumpThis(out);
        out.append(indent).append(" +- incomingHandler : ");
        if (this.incomingHandler instanceof Dumpable) {
            ((Dumpable) this.incomingHandler).dump(out, indent + "    ");
        } else {
            out.append(this.incomingHandler.toString()).append(System.lineSeparator());
        }
        out.append(indent).append(" +- outgoingHandler : ");
        if (this.outgoingHandler instanceof Dumpable) {
            ((Dumpable) this.outgoingHandler).dump(out, indent + "    ");
        } else {
            out.append(this.outgoingHandler.toString()).append(System.lineSeparator());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            WebSocketSession other = (WebSocketSession) obj;
            if (this.connection == null) {
                if (other.connection != null) {
                    return false;
                }
            } else if (!this.connection.equals(other.connection)) {
                return false;
            }
            return true;
        }
    }

    public ByteBufferPool getBufferPool() {
        return this.connection.getBufferPool();
    }

    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }

    public LogicalConnection getConnection() {
        return this.connection;
    }

    @Override
    public WebSocketContainerScope getContainerScope() {
        return this.containerScope;
    }

    public ExtensionFactory getExtensionFactory() {
        return this.extensionFactory;
    }

    @Override
    public long getIdleTimeout() {
        return this.connection.getMaxIdleTimeout();
    }

    @ManagedAttribute(readonly = true)
    public IncomingFrames getIncomingHandler() {
        return this.incomingHandler;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this.connection.getLocalAddress();
    }

    @ManagedAttribute(readonly = true)
    public OutgoingFrames getOutgoingHandler() {
        return this.outgoingHandler;
    }

    @Override
    public WebSocketPolicy getPolicy() {
        return this.policy;
    }

    @Override
    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public RemoteEndpoint getRemote() {
        if (LOG_OPEN.isDebugEnabled()) {
            LOG_OPEN.debug("[{}] {}.getRemote()", this.policy.getBehavior(), this.getClass().getSimpleName());
        }
        ConnectionState state = this.connection.getIOState().getConnectionState();
        if (state != ConnectionState.OPEN && state != ConnectionState.CONNECTED) {
            throw new WebSocketException("RemoteEndpoint unavailable, current state [" + state + "], expecting [OPEN or CONNECTED]");
        } else {
            return this.remote;
        }
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.remote.getInetSocketAddress();
    }

    public URI getRequestURI() {
        return this.requestURI;
    }

    @Override
    public UpgradeRequest getUpgradeRequest() {
        return this.upgradeRequest;
    }

    @Override
    public UpgradeResponse getUpgradeResponse() {
        return this.upgradeResponse;
    }

    @Override
    public WebSocketSession getWebSocketSession() {
        return this;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        return 31 * result + (this.connection == null ? 0 : this.connection.hashCode());
    }

    @Override
    public void incomingError(Throwable t) {
        this.websocket.incomingError(t);
    }

    @Override
    public void incomingFrame(Frame frame) {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.classLoader);
            if (this.connection.getIOState().isInputAvailable()) {
                this.incomingHandler.incomingFrame(frame);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }

    @Override
    public boolean isOpen() {
        return this.connection == null ? false : this.connection.isOpen();
    }

    @Override
    public boolean isSecure() {
        if (this.upgradeRequest == null) {
            throw new IllegalStateException("No valid UpgradeRequest yet");
        } else {
            URI requestURI = this.upgradeRequest.getRequestURI();
            return "wss".equalsIgnoreCase(requestURI.getScheme());
        }
    }

    public void notifyClose(int statusCode, String reason) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("notifyClose({},{})", statusCode, reason);
        }
        this.websocket.onClose(new CloseInfo(statusCode, reason));
    }

    public void notifyError(Throwable cause) {
        if (this.openFuture != null && !this.openFuture.isDone()) {
            this.openFuture.completeExceptionally(cause);
        }
        this.incomingError(cause);
    }

    @Override
    public void onClosed(Connection connection) {
    }

    @Override
    public void onOpened(Connection connection) {
        if (LOG_OPEN.isDebugEnabled()) {
            LOG_OPEN.debug("[{}] {}.onOpened()", this.policy.getBehavior(), this.getClass().getSimpleName());
        }
        this.open();
    }

    @Override
    public void onConnectionStateChange(ConnectionState state) {
        switch(state) {
            case CLOSED:
                IOState ioState = this.connection.getIOState();
                CloseInfo close = ioState.getCloseInfo();
                this.notifyClose(close.getStatusCode(), close.getReason());
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("{}.onSessionClosed()", this.containerScope.getClass().getSimpleName());
                    }
                    this.containerScope.onSessionClosed(this);
                } catch (Throwable var6) {
                    LOG.ignore(var6);
                }
                break;
            case CONNECTED:
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("{}.onSessionOpened()", this.containerScope.getClass().getSimpleName());
                    }
                    this.containerScope.onSessionOpened(this);
                } catch (Throwable var5) {
                    LOG.ignore(var5);
                }
        }
    }

    public WebSocketRemoteEndpoint newRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoingFrames, BatchMode batchMode) {
        return new WebSocketRemoteEndpoint(connection, this.outgoingHandler, this.getBatchMode());
    }

    public void open() {
        if (LOG_OPEN.isDebugEnabled()) {
            LOG_OPEN.debug("[{}] {}.open()", this.policy.getBehavior(), this.getClass().getSimpleName());
        }
        if (this.remote == null) {
            try {
                ThreadClassLoaderScope scope = new ThreadClassLoaderScope(this.classLoader);
                Throwable var17 = null;
                try {
                    this.connection.getIOState().onConnected();
                    this.remote = this.remoteEndpointFactory.newRemoteEndpoint(this.connection, this.outgoingHandler, this.getBatchMode());
                    if (LOG_OPEN.isDebugEnabled()) {
                        LOG_OPEN.debug("[{}] {}.open() remote={}", this.policy.getBehavior(), this.getClass().getSimpleName(), this.remote);
                    }
                    this.websocket.openSession(this);
                    this.connection.getIOState().onOpened();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("open -> {}", this.dump());
                    }
                    if (this.openFuture != null) {
                        this.openFuture.complete(this);
                    }
                } catch (Throwable var13) {
                    var17 = var13;
                    throw var13;
                } finally {
                    if (scope != null) {
                        if (var17 != null) {
                            try {
                                scope.close();
                            } catch (Throwable var12) {
                                var17.addSuppressed(var12);
                            }
                        } else {
                            scope.close();
                        }
                    }
                }
            } catch (CloseException var15) {
                LOG.warn(var15);
                this.close(var15.getStatusCode(), var15.getMessage());
            } catch (Throwable var16) {
                LOG.warn(var16);
                int statusCode = 1011;
                if (this.policy.getBehavior() == WebSocketBehavior.CLIENT) {
                    statusCode = 1008;
                }
                this.close(statusCode, var16.getMessage());
            }
        }
    }

    public void setExtensionFactory(ExtensionFactory extensionFactory) {
        this.extensionFactory = extensionFactory;
    }

    public void setFuture(CompletableFuture<Session> fut) {
        this.openFuture = fut;
    }

    @Override
    public void setIdleTimeout(long ms) {
        this.connection.setMaxIdleTimeout(ms);
    }

    public void setOutgoingHandler(OutgoingFrames outgoing) {
        this.outgoingHandler = outgoing;
    }

    @Deprecated
    public void setPolicy(WebSocketPolicy policy) {
    }

    public void setUpgradeRequest(UpgradeRequest request) {
        this.upgradeRequest = request;
        this.protocolVersion = request.getProtocolVersion();
        this.parameterMap.clear();
        if (request.getParameterMap() != null) {
            for (Entry<String, List<String>> entry : request.getParameterMap().entrySet()) {
                List<String> values = (List<String>) entry.getValue();
                if (values != null) {
                    this.parameterMap.put(entry.getKey(), values.toArray(new String[values.size()]));
                } else {
                    this.parameterMap.put(entry.getKey(), new String[0]);
                }
            }
        }
    }

    public void setUpgradeResponse(UpgradeResponse response) {
        this.upgradeResponse = response;
    }

    @Override
    public SuspendToken suspend() {
        return this.connection.suspend();
    }

    public BatchMode getBatchMode() {
        return BatchMode.AUTO;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WebSocketSession[");
        builder.append("websocket=").append(this.websocket);
        builder.append(",behavior=").append(this.policy.getBehavior());
        builder.append(",connection=").append(this.connection);
        builder.append(",remote=").append(this.remote);
        builder.append(",incoming=").append(this.incomingHandler);
        builder.append(",outgoing=").append(this.outgoingHandler);
        builder.append("]");
        return builder.toString();
    }

    public interface Listener {

        void onOpened(WebSocketSession var1);

        void onClosed(WebSocketSession var1);
    }
}