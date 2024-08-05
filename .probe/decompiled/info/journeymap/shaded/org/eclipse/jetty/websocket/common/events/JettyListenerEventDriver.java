package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketConnectionListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketFrameListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPartialListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPingPongListener;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.CloseInfo;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.ReadOnlyDelegatedFrame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.SimpleBinaryMessage;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.SimpleTextMessage;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.util.Utf8PartialBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

public class JettyListenerEventDriver extends AbstractEventDriver {

    private static final Logger LOG = Log.getLogger(JettyListenerEventDriver.class);

    private final WebSocketConnectionListener listener;

    private Utf8PartialBuilder utf8Partial;

    private boolean hasCloseBeenCalled = false;

    public JettyListenerEventDriver(WebSocketPolicy policy, WebSocketConnectionListener listener) {
        super(policy, listener);
        this.listener = listener;
    }

    @Override
    public void onBinaryFrame(ByteBuffer buffer, boolean fin) throws IOException {
        if (this.listener instanceof WebSocketListener) {
            if (this.activeMessage == null) {
                this.activeMessage = new SimpleBinaryMessage(this);
            }
            this.appendMessage(buffer, fin);
        }
        if (this.listener instanceof WebSocketPartialListener) {
            ((WebSocketPartialListener) this.listener).onWebSocketPartialBinary(buffer.slice().asReadOnlyBuffer(), fin);
        }
    }

    @Override
    public void onBinaryMessage(byte[] data) {
        if (this.listener instanceof WebSocketListener) {
            ((WebSocketListener) this.listener).onWebSocketBinary(data, 0, data.length);
        }
    }

    @Override
    public void onClose(CloseInfo close) {
        if (!this.hasCloseBeenCalled) {
            this.hasCloseBeenCalled = true;
            int statusCode = close.getStatusCode();
            String reason = close.getReason();
            this.listener.onWebSocketClose(statusCode, reason);
        }
    }

    @Override
    public void onConnect() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("onConnect({})", this.session);
        }
        this.listener.onWebSocketConnect(this.session);
    }

    @Override
    public void onError(Throwable cause) {
        this.listener.onWebSocketError(cause);
    }

    @Override
    public void onFrame(Frame frame) {
        if (this.listener instanceof WebSocketFrameListener) {
            ((WebSocketFrameListener) this.listener).onWebSocketFrame(new ReadOnlyDelegatedFrame(frame));
        }
        if (this.listener instanceof WebSocketPingPongListener) {
            if (frame.getType() == Frame.Type.PING) {
                ((WebSocketPingPongListener) this.listener).onWebSocketPing(frame.getPayload().asReadOnlyBuffer());
            } else if (frame.getType() == Frame.Type.PONG) {
                ((WebSocketPingPongListener) this.listener).onWebSocketPong(frame.getPayload().asReadOnlyBuffer());
            }
        }
    }

    @Override
    public void onInputStream(InputStream stream) {
    }

    @Override
    public void onReader(Reader reader) {
    }

    @Override
    public void onTextFrame(ByteBuffer buffer, boolean fin) throws IOException {
        if (this.listener instanceof WebSocketListener) {
            if (this.activeMessage == null) {
                this.activeMessage = new SimpleTextMessage(this);
            }
            this.appendMessage(buffer, fin);
        }
        if (this.listener instanceof WebSocketPartialListener) {
            if (this.utf8Partial == null) {
                this.utf8Partial = new Utf8PartialBuilder();
            }
            String partial = this.utf8Partial.toPartialString(buffer);
            ((WebSocketPartialListener) this.listener).onWebSocketPartialText(partial, fin);
            if (fin) {
                partial = null;
            }
        }
    }

    @Override
    public void onTextMessage(String message) {
        if (this.listener instanceof WebSocketListener) {
            ((WebSocketListener) this.listener).onWebSocketText(message);
        }
    }

    public String toString() {
        return String.format("%s[%s]", JettyListenerEventDriver.class.getSimpleName(), this.listener.getClass().getName());
    }
}