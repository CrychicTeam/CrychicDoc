package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class PingFrame extends ControlFrame {

    public PingFrame() {
        super((byte) 9);
    }

    public PingFrame setPayload(byte[] bytes) {
        this.setPayload(ByteBuffer.wrap(bytes));
        return this;
    }

    public PingFrame setPayload(String payload) {
        this.setPayload(ByteBuffer.wrap(StringUtil.getUtf8Bytes(payload)));
        return this;
    }

    @Override
    public Frame.Type getType() {
        return Frame.Type.PING;
    }
}