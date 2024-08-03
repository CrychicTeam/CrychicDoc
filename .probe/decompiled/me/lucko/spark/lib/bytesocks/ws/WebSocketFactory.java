package me.lucko.spark.lib.bytesocks.ws;

import java.util.List;
import me.lucko.spark.lib.bytesocks.ws.drafts.Draft;

public interface WebSocketFactory {

    WebSocket createWebSocket(WebSocketAdapter var1, Draft var2);

    WebSocket createWebSocket(WebSocketAdapter var1, List<Draft> var2);
}