package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import java.nio.ByteBuffer;

public interface WebSocketPingPongListener extends WebSocketConnectionListener {

    void onWebSocketPing(ByteBuffer var1);

    void onWebSocketPong(ByteBuffer var1);
}