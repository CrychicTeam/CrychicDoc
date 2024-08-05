package info.journeymap.shaded.org.eclipse.jetty.websocket.server;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.io.Connection;
import info.journeymap.shaded.org.eclipse.jetty.io.EndPoint;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.AbstractWebSocketConnection;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class WebSocketServerConnection extends AbstractWebSocketConnection implements Connection.UpgradeTo {

    public WebSocketServerConnection(EndPoint endp, Executor executor, Scheduler scheduler, WebSocketPolicy policy, ByteBufferPool bufferPool) {
        super(endp, executor, scheduler, policy, bufferPool);
        if (policy.getIdleTimeout() > 0L) {
            endp.setIdleTimeout(policy.getIdleTimeout());
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this.getEndPoint().getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.getEndPoint().getRemoteAddress();
    }

    @Override
    public void setNextIncomingFrames(IncomingFrames incoming) {
        this.getParser().setIncomingFramesHandler(incoming);
    }
}