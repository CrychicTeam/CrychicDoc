package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.websocket.common.AcceptHash;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import java.io.IOException;

public class HandshakeRFC6455 implements WebSocketHandshake {

    public static final int VERSION = 13;

    @Override
    public void doHandshakeResponse(ServletUpgradeRequest request, ServletUpgradeResponse response) throws IOException {
        String key = request.getHeader("Sec-WebSocket-Key");
        if (key == null) {
            throw new IllegalStateException("Missing request header 'Sec-WebSocket-Key'");
        } else {
            response.setHeader("Upgrade", "WebSocket");
            response.addHeader("Connection", "Upgrade");
            response.addHeader("Sec-WebSocket-Accept", AcceptHash.hashKey(key));
            request.complete();
            response.setStatusCode(101);
            response.complete();
        }
    }
}