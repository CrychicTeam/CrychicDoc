package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public interface WebSocketListener extends WebSocketConnectionListener {

    void onWebSocketBinary(byte[] var1, int var2, int var3);

    void onWebSocketText(String var1);
}