package info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes;

import info.journeymap.shaded.org.eclipse.jetty.websocket.common.WebSocketSession;

public interface WebSocketSessionScope {

    WebSocketSession getWebSocketSession();

    WebSocketContainerScope getContainerScope();
}