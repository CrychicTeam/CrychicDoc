package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import java.nio.ByteBuffer;

public interface WebSocketPartialListener extends WebSocketConnectionListener {

    void onWebSocketPartialBinary(ByteBuffer var1, boolean var2);

    void onWebSocketPartialText(String var1, boolean var2);
}