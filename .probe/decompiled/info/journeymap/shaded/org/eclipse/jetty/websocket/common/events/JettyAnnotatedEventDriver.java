package info.journeymap.shaded.org.eclipse.jetty.websocket.common.events;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations.WebSocket;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.CloseInfo;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.MessageAppender;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.MessageInputStream;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.MessageReader;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.SimpleBinaryMessage;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.message.SimpleTextMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

public class JettyAnnotatedEventDriver extends AbstractEventDriver {

    private final JettyAnnotatedMetadata events;

    private boolean hasCloseBeenCalled = false;

    private BatchMode batchMode;

    public JettyAnnotatedEventDriver(WebSocketPolicy policy, Object websocket, JettyAnnotatedMetadata events) {
        super(policy, websocket);
        this.events = events;
        WebSocket anno = (WebSocket) websocket.getClass().getAnnotation(WebSocket.class);
        if (anno.maxTextMessageSize() > 0) {
            this.policy.setMaxTextMessageSize(anno.maxTextMessageSize());
        }
        if (anno.maxBinaryMessageSize() > 0) {
            this.policy.setMaxBinaryMessageSize(anno.maxBinaryMessageSize());
        }
        if (anno.inputBufferSize() > 0) {
            this.policy.setInputBufferSize(anno.inputBufferSize());
        }
        if (anno.maxIdleTime() > 0) {
            this.policy.setIdleTimeout((long) anno.maxIdleTime());
        }
        this.batchMode = anno.batchMode();
    }

    @Override
    public BatchMode getBatchMode() {
        return this.batchMode;
    }

    @Override
    public void onBinaryFrame(ByteBuffer buffer, boolean fin) throws IOException {
        if (this.events.onBinary != null) {
            if (this.activeMessage == null) {
                if (this.events.onBinary.isStreaming()) {
                    this.activeMessage = new MessageInputStream();
                    final MessageAppender msg = this.activeMessage;
                    this.dispatch(new Runnable() {

                        public void run() {
                            try {
                                JettyAnnotatedEventDriver.this.events.onBinary.call(JettyAnnotatedEventDriver.this.websocket, JettyAnnotatedEventDriver.this.session, msg);
                            } catch (Throwable var2) {
                                JettyAnnotatedEventDriver.this.onError(var2);
                            }
                        }
                    });
                } else {
                    this.activeMessage = new SimpleBinaryMessage(this);
                }
            }
            this.appendMessage(buffer, fin);
        }
    }

    @Override
    public void onBinaryMessage(byte[] data) {
        if (this.events.onBinary != null) {
            this.events.onBinary.call(this.websocket, this.session, data, 0, data.length);
        }
    }

    @Override
    public void onClose(CloseInfo close) {
        if (!this.hasCloseBeenCalled) {
            this.hasCloseBeenCalled = true;
            if (this.events.onClose != null) {
                this.events.onClose.call(this.websocket, this.session, close.getStatusCode(), close.getReason());
            }
        }
    }

    @Override
    public void onConnect() {
        if (this.events.onConnect != null) {
            this.events.onConnect.call(this.websocket, this.session);
        }
    }

    @Override
    public void onError(Throwable cause) {
        if (this.events.onError != null) {
            this.events.onError.call(this.websocket, this.session, cause);
        }
    }

    @Override
    public void onFrame(Frame frame) {
        if (this.events.onFrame != null) {
            this.events.onFrame.call(this.websocket, this.session, frame);
        }
    }

    @Override
    public void onInputStream(InputStream stream) {
        if (this.events.onBinary != null) {
            this.events.onBinary.call(this.websocket, this.session, stream);
        }
    }

    @Override
    public void onReader(Reader reader) {
        if (this.events.onText != null) {
            this.events.onText.call(this.websocket, this.session, reader);
        }
    }

    @Override
    public void onTextFrame(ByteBuffer buffer, boolean fin) throws IOException {
        if (this.events.onText != null) {
            if (this.activeMessage == null) {
                if (this.events.onText.isStreaming()) {
                    this.activeMessage = new MessageReader(new MessageInputStream());
                    final MessageAppender msg = this.activeMessage;
                    this.dispatch(new Runnable() {

                        public void run() {
                            try {
                                JettyAnnotatedEventDriver.this.events.onText.call(JettyAnnotatedEventDriver.this.websocket, JettyAnnotatedEventDriver.this.session, msg);
                            } catch (Throwable var2) {
                                JettyAnnotatedEventDriver.this.onError(var2);
                            }
                        }
                    });
                } else {
                    this.activeMessage = new SimpleTextMessage(this);
                }
            }
            this.appendMessage(buffer, fin);
        }
    }

    @Override
    public void onTextMessage(String message) {
        if (this.events.onText != null) {
            this.events.onText.call(this.websocket, this.session, message);
        }
    }

    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.websocket);
    }
}