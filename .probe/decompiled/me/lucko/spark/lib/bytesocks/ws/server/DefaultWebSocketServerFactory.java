package me.lucko.spark.lib.bytesocks.ws.server;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import me.lucko.spark.lib.bytesocks.ws.WebSocketAdapter;
import me.lucko.spark.lib.bytesocks.ws.WebSocketImpl;
import me.lucko.spark.lib.bytesocks.ws.WebSocketServerFactory;
import me.lucko.spark.lib.bytesocks.ws.drafts.Draft;

public class DefaultWebSocketServerFactory implements WebSocketServerFactory {

    @Override
    public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d) {
        return new WebSocketImpl(a, d);
    }

    @Override
    public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d) {
        return new WebSocketImpl(a, d);
    }

    public SocketChannel wrapChannel(SocketChannel channel, SelectionKey key) {
        return channel;
    }

    @Override
    public void close() {
    }
}