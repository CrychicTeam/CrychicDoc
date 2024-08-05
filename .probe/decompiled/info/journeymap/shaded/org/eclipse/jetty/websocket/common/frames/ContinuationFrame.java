package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class ContinuationFrame extends DataFrame {

    public ContinuationFrame() {
        super((byte) 0);
    }

    public ContinuationFrame setPayload(ByteBuffer buf) {
        super.setPayload(buf);
        return this;
    }

    public ContinuationFrame setPayload(byte[] buf) {
        return this.setPayload(ByteBuffer.wrap(buf));
    }

    public ContinuationFrame setPayload(String message) {
        return this.setPayload(StringUtil.getUtf8Bytes(message));
    }

    @Override
    public Frame.Type getType() {
        return Frame.Type.CONTINUATION;
    }
}