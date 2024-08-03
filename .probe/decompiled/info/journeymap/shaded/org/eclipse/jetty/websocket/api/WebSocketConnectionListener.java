package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

public interface WebSocketConnectionListener {

    void onWebSocketClose(int var1, String var2);

    void onWebSocketConnect(Session var1);

    void onWebSocketError(Throwable var1);
}