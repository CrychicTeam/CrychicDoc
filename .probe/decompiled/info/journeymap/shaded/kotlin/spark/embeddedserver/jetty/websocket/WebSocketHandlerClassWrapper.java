package info.journeymap.shaded.kotlin.spark.embeddedserver.jetty.websocket;

import java.util.Objects;

public class WebSocketHandlerClassWrapper implements WebSocketHandlerWrapper {

    private final Class<?> handlerClass;

    public WebSocketHandlerClassWrapper(Class<?> handlerClass) {
        Objects.requireNonNull(handlerClass, "WebSocket handler class cannot be null");
        WebSocketHandlerWrapper.validateHandlerClass(handlerClass);
        this.handlerClass = handlerClass;
    }

    @Override
    public Object getHandler() {
        try {
            return this.handlerClass.newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw new RuntimeException("Could not instantiate websocket handler", var2);
        }
    }
}