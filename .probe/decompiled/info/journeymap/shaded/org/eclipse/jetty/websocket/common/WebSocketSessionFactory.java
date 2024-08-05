package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.EventDriver;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.JettyAnnotatedEventDriver;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.JettyListenerEventDriver;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
import java.net.URI;

public class WebSocketSessionFactory implements SessionFactory {

    private final WebSocketContainerScope containerScope;

    public WebSocketSessionFactory(WebSocketContainerScope containerScope) {
        this.containerScope = containerScope;
    }

    @Override
    public boolean supports(EventDriver websocket) {
        return websocket instanceof JettyAnnotatedEventDriver || websocket instanceof JettyListenerEventDriver;
    }

    @Override
    public WebSocketSession createSession(URI requestURI, EventDriver websocket, LogicalConnection connection) {
        return new WebSocketSession(this.containerScope, requestURI, websocket, connection);
    }
}