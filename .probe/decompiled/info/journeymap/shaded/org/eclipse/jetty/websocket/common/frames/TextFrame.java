package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.StringUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class TextFrame extends DataFrame {

    public TextFrame() {
        super((byte) 1);
    }

    @Override
    public Frame.Type getType() {
        return this.getOpCode() == 0 ? Frame.Type.CONTINUATION : Frame.Type.TEXT;
    }

    public TextFrame setPayload(String str) {
        this.setPayload(ByteBuffer.wrap(StringUtil.getUtf8Bytes(str)));
        return this;
    }

    @Override
    public String getPayloadAsUTF8() {
        return this.data == null ? null : BufferUtil.toUTF8String(this.data);
    }
}