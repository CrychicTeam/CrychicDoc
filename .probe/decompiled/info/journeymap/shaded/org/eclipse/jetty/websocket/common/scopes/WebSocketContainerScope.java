package info.journeymap.shaded.org.eclipse.jetty.websocket.common.scopes;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.util.DecoratedObjectFactory;
import info.journeymap.shaded.org.eclipse.jetty.util.ssl.SslContextFactory;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.WebSocketSession;
import java.util.concurrent.Executor;

public interface WebSocketContainerScope {

    ByteBufferPool getBufferPool();

    Executor getExecutor();

    DecoratedObjectFactory getObjectFactory();

    WebSocketPolicy getPolicy();

    SslContextFactory getSslContextFactory();

    boolean isRunning();

    void onSessionOpened(WebSocketSession var1);

    void onSessionClosed(WebSocketSession var1);
}