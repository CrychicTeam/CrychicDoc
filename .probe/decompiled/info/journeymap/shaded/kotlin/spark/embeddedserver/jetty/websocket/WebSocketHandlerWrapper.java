package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.WebSocket;

public interface WebSocketHandlerWrapper {

    Object getHandler();

    static void validateHandlerClass(Class<?> handlerClass) {
        boolean valid = WebSocketListener.class.isAssignableFrom(handlerClass) || handlerClass.isAnnotationPresent(WebSocket.class);
        if (!valid) {
            throw new IllegalArgumentException("WebSocket handler must implement 'WebSocketListener' or be annotated as '@WebSocket'");
        }
    }
}