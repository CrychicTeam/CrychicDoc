package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket;

import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import java.util.Objects;

public class WebSocketCreatorFactory {

    public static WebSocketCreator create(WebSocketHandlerWrapper handlerWrapper) {
        return new WebSocketCreatorFactory.SparkWebSocketCreator(handlerWrapper.getHandler());
    }

    static class SparkWebSocketCreator implements WebSocketCreator {

        private final Object handler;

        private SparkWebSocketCreator(Object handler) {
            this.handler = Objects.requireNonNull(handler, "handler cannot be null");
        }

        @Override
        public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
            return this.handler;
        }

        Object getHandler() {
            return this.handler;
        }
    }
}