package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import java.io.IOException;

public interface WebSocketHandshake {

    void doHandshakeResponse(ServletUpgradeRequest var1, ServletUpgradeResponse var2) throws IOException;
}