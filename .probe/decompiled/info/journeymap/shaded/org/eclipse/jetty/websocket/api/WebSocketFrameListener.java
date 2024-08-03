package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;

public interface WebSocketFrameListener extends WebSocketConnectionListener {

    void onWebSocketFrame(Frame var1);
}