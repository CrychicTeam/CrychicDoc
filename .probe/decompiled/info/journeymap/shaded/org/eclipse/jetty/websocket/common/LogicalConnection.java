package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.SuspendToken;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.IOState;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public interface LogicalConnection extends OutgoingFrames, SuspendToken {

    void close();

    void close(int var1, String var2);

    void disconnect();

    ByteBufferPool getBufferPool();

    Executor getExecutor();

    long getIdleTimeout();

    IOState getIOState();

    InetSocketAddress getLocalAddress();

    long getMaxIdleTimeout();

    WebSocketPolicy getPolicy();

    InetSocketAddress getRemoteAddress();

    boolean isOpen();

    boolean isReading();

    void setMaxIdleTimeout(long var1);

    void setNextIncomingFrames(IncomingFrames var1);

    SuspendToken suspend();

    String getId();
}