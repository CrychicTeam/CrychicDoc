package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class PongFrame extends ControlFrame {

    public PongFrame() {
        super((byte) 10);
    }

    public PongFrame setPayload(byte[] bytes) {
        this.setPayload(ByteBuffer.wrap(bytes));
        return this;
    }

    public PongFrame setPayload(String payload) {
        this.setPayload(StringUtil.getUtf8Bytes(payload));
        return this;
    }

    @Override
    public Frame.Type getType() {
        return Frame.Type.PONG;
    }
}