package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class BinaryFrame extends DataFrame {

    public BinaryFrame() {
        super((byte) 2);
    }

    public BinaryFrame setPayload(ByteBuffer buf) {
        super.setPayload(buf);
        return this;
    }

    public BinaryFrame setPayload(byte[] buf) {
        this.setPayload(ByteBuffer.wrap(buf));
        return this;
    }

    public BinaryFrame setPayload(String payload) {
        this.setPayload(StringUtil.getUtf8Bytes(payload));
        return this;
    }

    @Override
    public Frame.Type getType() {
        return this.getOpCode() == 0 ? Frame.Type.CONTINUATION : Frame.Type.BINARY;
    }
}