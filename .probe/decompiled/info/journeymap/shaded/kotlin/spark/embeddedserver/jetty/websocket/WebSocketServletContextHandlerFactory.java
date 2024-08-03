package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket;

import info.journeymap.shaded.org.eclipse.jetty.http.pathmap.ServletPathSpec;
import info.journeymap.shaded.org.eclipse.jetty.servlet.ServletContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.websocket.server.NativeWebSocketConfiguration;
import info.journeymap.shaded.org.eclipse.jetty.websocket.server.WebSocketUpgradeFilter;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Optional;

public class WebSocketServletContextHandlerFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServletContextHandlerFactory.class);

    public static ServletContextHandler create(Map<String, WebSocketHandlerWrapper> webSocketHandlers, Optional<Integer> webSocketIdleTimeoutMillis) {
        ServletContextHandler webSocketServletContextHandler = null;
        if (webSocketHandlers != null) {
            try {
                webSocketServletContextHandler = new ServletContextHandler(null, "/", true, false);
                WebSocketUpgradeFilter webSocketUpgradeFilter = WebSocketUpgradeFilter.configureContext(webSocketServletContextHandler);
                if (webSocketIdleTimeoutMillis.isPresent()) {
                    webSocketUpgradeFilter.getFactory().getPolicy().setIdleTimeout((long) ((Integer) webSocketIdleTimeoutMillis.get()).intValue());
                }
                NativeWebSocketConfiguration webSocketConfiguration = (NativeWebSocketConfiguration) webSocketServletContextHandler.getServletContext().getAttribute(NativeWebSocketConfiguration.class.getName());
                for (String path : webSocketHandlers.keySet()) {
                    WebSocketCreator webSocketCreator = WebSocketCreatorFactory.create((WebSocketHandlerWrapper) webSocketHandlers.get(path));
                    webSocketConfiguration.addMapping(new ServletPathSpec(path), webSocketCreator);
                }
            } catch (Exception var8) {
                logger.error("creation of websocket context handler failed.", (Throwable) var8);
                webSocketServletContextHandler = null;
            }
        }
        return webSocketServletContextHandler;
    }
}