package info.journeymap.shaded.org.eclipse.jetty.websocket.common.message;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.events.EventDriver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SimpleBinaryMessage implements MessageAppender {

    private static final int BUFFER_SIZE = 65535;

    private final EventDriver onEvent;

    protected final ByteArrayOutputStream out;

    private int size;

    protected boolean finished;

    public SimpleBinaryMessage(EventDriver onEvent) {
        this.onEvent = onEvent;
        this.out = new ByteArrayOutputStream(65535);
        this.finished = false;
    }

    @Override
    public void appendFrame(ByteBuffer payload, boolean isLast) throws IOException {
        if (this.finished) {
            throw new IOException("Cannot append to finished buffer");
        } else if (payload != null) {
            this.onEvent.getPolicy().assertValidBinaryMessageSize(this.size + payload.remaining());
            this.size = this.size + payload.remaining();
            BufferUtil.writeTo(payload, this.out);
        }
    }

    @Override
    public void messageComplete() {
        this.finished = true;
        byte[] data = this.out.toByteArray();
        this.onEvent.onBinaryMessage(data);
    }
}