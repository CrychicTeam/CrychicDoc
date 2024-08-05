package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty;

import info.journeymap.shaded.kotlin.spark.embeddedserver.EmbeddedServer;
import info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;
import info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket.WebSocketServletContextHandlerFactory;
import info.journeymap.shaded.kotlin.spark.ssl.SslStores;
import info.journeymap.shaded.org.eclipse.jetty.server.Connector;
import info.journeymap.shaded.org.eclipse.jetty.server.Handler;
import info.journeymap.shaded.org.eclipse.jetty.server.Server;
import info.journeymap.shaded.org.eclipse.jetty.server.ServerConnector;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.HandlerList;
import info.journeymap.shaded.org.eclipse.jetty.servlet.ServletContextHandler;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmbeddedJettyServer implements EmbeddedServer {

    private static final int SPARK_DEFAULT_PORT = 4567;

    private static final String NAME = "Spark";

    private final JettyServerFactory serverFactory;

    private final Handler handler;

    private Server server;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, WebSocketHandlerWrapper> webSocketHandlers;

    private Optional<Integer> webSocketIdleTimeoutMillis;

    public EmbeddedJettyServer(JettyServerFactory serverFactory, Handler handler) {
        this.serverFactory = serverFactory;
        this.handler = handler;
    }

    @Override
    public void configureWebSockets(Map<String, WebSocketHandlerWrapper> webSocketHandlers, Optional<Integer> webSocketIdleTimeoutMillis) {
        this.webSocketHandlers = webSocketHandlers;
        this.webSocketIdleTimeoutMillis = webSocketIdleTimeoutMillis;
    }

    @Override
    public int ignite(String host, int port, SslStores sslStores, int maxThreads, int minThreads, int threadIdleTimeoutMillis) throws Exception {
        if (port == 0) {
            try {
                ServerSocket s = new ServerSocket(0);
                Throwable webSocketServletContextHandler = null;
                try {
                    port = s.getLocalPort();
                } catch (Throwable var18) {
                    webSocketServletContextHandler = var18;
                    throw var18;
                } finally {
                    if (s != null) {
                        if (webSocketServletContextHandler != null) {
                            try {
                                s.close();
                            } catch (Throwable var17) {
                                webSocketServletContextHandler.addSuppressed(var17);
                            }
                        } else {
                            s.close();
                        }
                    }
                }
            } catch (IOException var20) {
                this.logger.error("Could not get first available port (port set to 0), using default: {}", 4567);
                port = 4567;
            }
        }
        this.server = this.serverFactory.create(maxThreads, minThreads, threadIdleTimeoutMillis);
        ServerConnector connector;
        if (sslStores == null) {
            connector = SocketConnectorFactory.createSocketConnector(this.server, host, port);
        } else {
            connector = SocketConnectorFactory.createSecureSocketConnector(this.server, host, port, sslStores);
        }
        this.server = connector.getServer();
        this.server.setConnectors(new Connector[] { connector });
        ServletContextHandler webSocketServletContextHandler = WebSocketServletContextHandlerFactory.create(this.webSocketHandlers, this.webSocketIdleTimeoutMillis);
        if (webSocketServletContextHandler == null) {
            this.server.setHandler(this.handler);
        } else {
            List<Handler> handlersInList = new ArrayList();
            handlersInList.add(this.handler);
            if (webSocketServletContextHandler != null) {
                handlersInList.add(webSocketServletContextHandler);
            }
            HandlerList handlers = new HandlerList();
            handlers.setHandlers((Handler[]) handlersInList.toArray(new Handler[handlersInList.size()]));
            this.server.setHandler(handlers);
        }
        this.logger.info("== {} has ignited ...", "Spark");
        this.logger.info(">> Listening on {}:{}", host, port);
        this.server.start();
        return port;
    }

    @Override
    public void join() throws InterruptedException {
        this.server.join();
    }

    @Override
    public void extinguish() {
        this.logger.info(">>> {} shutting down ...", "Spark");
        try {
            if (this.server != null) {
                this.server.stop();
            }
        } catch (Exception var2) {
            this.logger.error("stop failed", (Throwable) var2);
            System.exit(100);
        }
        this.logger.info("done");
    }

    @Override
    public int activeThreadCount() {
        return this.server == null ? 0 : this.server.getThreadPool().getThreads() - this.server.getThreadPool().getIdleThreads();
    }
}