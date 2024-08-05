package info.journeymap.shaded.org.eclipse.jetty.websocket.api;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

public interface Session extends Closeable {

    void close();

    void close(CloseStatus var1);

    void close(int var1, String var2);

    void disconnect() throws IOException;

    long getIdleTimeout();

    InetSocketAddress getLocalAddress();

    WebSocketPolicy getPolicy();

    String getProtocolVersion();

    RemoteEndpoint getRemote();

    InetSocketAddress getRemoteAddress();

    UpgradeRequest getUpgradeRequest();

    UpgradeResponse getUpgradeResponse();

    boolean isOpen();

    boolean isSecure();

    void setIdleTimeout(long var1);

    SuspendToken suspend();
}