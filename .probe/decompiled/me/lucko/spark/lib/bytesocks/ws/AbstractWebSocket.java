package me.lucko.spark.lib.bytesocks.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import me.lucko.spark.lib.bytesocks.ws.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebSocket extends WebSocketAdapter {

    private final Logger log = LoggerFactory.getLogger(AbstractWebSocket.class);

    private boolean tcpNoDelay;

    private boolean reuseAddr;

    private ScheduledExecutorService connectionLostCheckerService;

    private ScheduledFuture<?> connectionLostCheckerFuture;

    private long connectionLostTimeout = TimeUnit.SECONDS.toNanos(60L);

    private boolean websocketRunning = false;

    private final Object syncConnectionLost = new Object();

    public int getConnectionLostTimeout() {
        synchronized (this.syncConnectionLost) {
            return (int) TimeUnit.NANOSECONDS.toSeconds(this.connectionLostTimeout);
        }
    }

    public void setConnectionLostTimeout(int connectionLostTimeout) {
        synchronized (this.syncConnectionLost) {
            this.connectionLostTimeout = TimeUnit.SECONDS.toNanos((long) connectionLostTimeout);
            if (this.connectionLostTimeout <= 0L) {
                this.log.trace("Connection lost timer stopped");
                this.cancelConnectionLostTimer();
            } else {
                if (this.websocketRunning) {
                    this.log.trace("Connection lost timer restarted");
                    try {
                        for (WebSocket conn : new ArrayList(this.getConnections())) {
                            if (conn instanceof WebSocketImpl) {
                                WebSocketImpl webSocketImpl = (WebSocketImpl) conn;
                                webSocketImpl.updateLastPong();
                            }
                        }
                    } catch (Exception var8) {
                        this.log.error("Exception during connection lost restart", var8);
                    }
                    this.restartConnectionLostTimer();
                }
            }
        }
    }

    protected void stopConnectionLostTimer() {
        synchronized (this.syncConnectionLost) {
            if (this.connectionLostCheckerService != null || this.connectionLostCheckerFuture != null) {
                this.websocketRunning = false;
                this.log.trace("Connection lost timer stopped");
                this.cancelConnectionLostTimer();
            }
        }
    }

    protected void startConnectionLostTimer() {
        synchronized (this.syncConnectionLost) {
            if (this.connectionLostTimeout <= 0L) {
                this.log.trace("Connection lost timer deactivated");
            } else {
                this.log.trace("Connection lost timer started");
                this.websocketRunning = true;
                this.restartConnectionLostTimer();
            }
        }
    }

    private void restartConnectionLostTimer() {
        this.cancelConnectionLostTimer();
        this.connectionLostCheckerService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("connectionLostChecker"));
        Runnable connectionLostChecker = new Runnable() {

            private ArrayList<WebSocket> connections = new ArrayList();

            public void run() {
                this.connections.clear();
                try {
                    this.connections.addAll(AbstractWebSocket.this.getConnections());
                    long minimumPongTime;
                    synchronized (AbstractWebSocket.this.syncConnectionLost) {
                        minimumPongTime = (long) ((double) System.nanoTime() - (double) AbstractWebSocket.this.connectionLostTimeout * 1.5);
                    }
                    for (WebSocket conn : this.connections) {
                        AbstractWebSocket.this.executeConnectionLostDetection(conn, minimumPongTime);
                    }
                } catch (Exception var6) {
                }
                this.connections.clear();
            }
        };
        this.connectionLostCheckerFuture = this.connectionLostCheckerService.scheduleAtFixedRate(connectionLostChecker, this.connectionLostTimeout, this.connectionLostTimeout, TimeUnit.NANOSECONDS);
    }

    private void executeConnectionLostDetection(WebSocket webSocket, long minimumPongTime) {
        if (webSocket instanceof WebSocketImpl) {
            WebSocketImpl webSocketImpl = (WebSocketImpl) webSocket;
            if (webSocketImpl.getLastPong() < minimumPongTime) {
                this.log.trace("Closing connection due to no pong received: {}", webSocketImpl);
                webSocketImpl.closeConnection(1006, "The connection was closed because the other endpoint did not respond with a pong in time. For more information check: https://github.com/TooTallNate/Java-WebSocket/wiki/Lost-connection-detection");
            } else if (webSocketImpl.isOpen()) {
                webSocketImpl.sendPing();
            } else {
                this.log.trace("Trying to ping a non open connection: {}", webSocketImpl);
            }
        }
    }

    protected abstract Collection<WebSocket> getConnections();

    private void cancelConnectionLostTimer() {
        if (this.connectionLostCheckerService != null) {
            this.connectionLostCheckerService.shutdownNow();
            this.connectionLostCheckerService = null;
        }
        if (this.connectionLostCheckerFuture != null) {
            this.connectionLostCheckerFuture.cancel(false);
            this.connectionLostCheckerFuture = null;
        }
    }

    public boolean isTcpNoDelay() {
        return this.tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isReuseAddr() {
        return this.reuseAddr;
    }

    public void setReuseAddr(boolean reuseAddr) {
        this.reuseAddr = reuseAddr;
    }
}