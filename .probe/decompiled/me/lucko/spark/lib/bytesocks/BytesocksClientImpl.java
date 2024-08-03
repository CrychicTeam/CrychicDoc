package me.lucko.spark.lib.bytesocks;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import me.lucko.spark.lib.bytesocks.ws.client.WebSocketClient;
import me.lucko.spark.lib.bytesocks.ws.handshake.ServerHandshake;

class BytesocksClientImpl implements BytesocksClient {

    private final String httpUrl;

    private final String wsUrl;

    private final String userAgent;

    BytesocksClientImpl(String host, String userAgent) {
        this.httpUrl = "https://" + host + "/";
        this.wsUrl = "wss://" + host + "/";
        this.userAgent = userAgent;
    }

    @Override
    public BytesocksClient.Socket createAndConnect(BytesocksClient.Listener listener) throws Exception {
        String channelId = this.create();
        return this.connect(channelId, listener);
    }

    private String create() throws IOException {
        URL url = URI.create(this.httpUrl + "create").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String var4;
        try {
            connection.setRequestProperty("User-Agent", this.userAgent);
            connection.connect();
            if (connection.getResponseCode() != 201) {
                throw new RuntimeException("Request failed: " + connection.getResponseMessage());
            }
            String channelId = connection.getHeaderField("Location");
            if (channelId == null) {
                throw new RuntimeException("Location header not returned: " + connection);
            }
            var4 = channelId;
        } finally {
            connection.disconnect();
        }
        return var4;
    }

    @Override
    public BytesocksClient.Socket connect(String channelId, final BytesocksClient.Listener listener) throws Exception {
        URI url = URI.create(this.wsUrl + channelId);
        Map<String, String> headers = new HashMap();
        headers.put("User-Agent", this.userAgent);
        WebSocketClient socket = new WebSocketClient(url, headers) {

            @Override
            public void onOpen(ServerHandshake handshake) {
                listener.onOpen();
            }

            @Override
            public void onMessage(String message) {
                listener.onText(message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                listener.onClose(code, reason);
            }

            @Override
            public void onError(Exception ex) {
                listener.onError(ex);
            }
        };
        socket.connectBlocking();
        return new BytesocksClientImpl.SocketImpl(channelId, socket);
    }

    private static final class SocketImpl implements BytesocksClient.Socket {

        private final String id;

        private final WebSocketClient ws;

        private SocketImpl(String id, WebSocketClient ws) {
            this.id = id;
            this.ws = ws;
        }

        @Override
        public String channelId() {
            return this.id;
        }

        @Override
        public boolean isOpen() {
            return this.ws.isOpen();
        }

        @Override
        public void send(String msg) {
            this.ws.send(msg);
        }

        @Override
        public void close(int statusCode, String reason) {
            this.ws.close(statusCode, reason);
        }
    }
}