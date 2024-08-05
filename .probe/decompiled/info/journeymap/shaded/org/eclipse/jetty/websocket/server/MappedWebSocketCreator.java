package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.websocket.server.pathmap.PathSpec;
import info.journeymap.shaded.org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public interface MappedWebSocketCreator {

    void addMapping(String var1, WebSocketCreator var2);

    @Deprecated
    void addMapping(PathSpec var1, WebSocketCreator var2);

    void addMapping(info.journeymap.shaded.org.eclipse.jetty.http.pathmap.PathSpec var1, WebSocketCreator var2);

    WebSocketCreator getMapping(String var1);

    boolean removeMapping(String var1);
}