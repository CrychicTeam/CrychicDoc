package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.util.Utf8Appendable;
import info.journeymap.shaded.org.eclipse.jetty.util.Utf8StringBuilder;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BadPayloadException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.ProtocolException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames.CloseFrame;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class CloseInfo {

    private int statusCode;

    private byte[] reasonBytes;

    public CloseInfo() {
        this(1005, null);
    }

    public CloseInfo(ByteBuffer payload, boolean validate) {
        this.statusCode = 1005;
        if (payload != null && payload.remaining() != 0) {
            ByteBuffer data = payload.slice();
            if (data.remaining() == 1 && validate) {
                throw new ProtocolException("Invalid 1 byte payload");
            } else {
                if (data.remaining() >= 2) {
                    this.statusCode = 0;
                    this.statusCode = this.statusCode | (data.get() & 255) << 8;
                    this.statusCode = this.statusCode | data.get() & 255;
                    if (validate && (this.statusCode < 1000 || this.statusCode == 1004 || this.statusCode == 1006 || this.statusCode == 1005 || this.statusCode > 1011 && this.statusCode <= 2999 || this.statusCode >= 5000)) {
                        throw new ProtocolException("Invalid close code: " + this.statusCode);
                    }
                    if (data.remaining() > 0) {
                        int len = Math.min(data.remaining(), 123);
                        this.reasonBytes = new byte[len];
                        data.get(this.reasonBytes, 0, len);
                        if (validate) {
                            try {
                                Utf8StringBuilder utf = new Utf8StringBuilder();
                                utf.append(this.reasonBytes, 0, this.reasonBytes.length);
                            } catch (Utf8Appendable.NotUtf8Exception var6) {
                                throw new BadPayloadException("Invalid Close Reason", var6);
                            }
                        }
                    }
                }
            }
        }
    }

    public CloseInfo(Frame frame) {
        this(frame.getPayload(), false);
    }

    public CloseInfo(Frame frame, boolean validate) {
        this(frame.getPayload(), validate);
    }

    public CloseInfo(int statusCode) {
        this(statusCode, null);
    }

    public CloseInfo(int statusCode, String reason) {
        this.statusCode = statusCode;
        if (reason != null) {
            byte[] utf8Bytes = reason.getBytes(StandardCharsets.UTF_8);
            if (utf8Bytes.length > 123) {
                this.reasonBytes = new byte[123];
                System.arraycopy(utf8Bytes, 0, this.reasonBytes, 0, 123);
            } else {
                this.reasonBytes = utf8Bytes;
            }
        }
    }

    private ByteBuffer asByteBuffer() {
        if (this.statusCode != 1006 && this.statusCode != 1005 && this.statusCode != -1) {
            int len = 2;
            boolean hasReason = this.reasonBytes != null && this.reasonBytes.length > 0;
            if (hasReason) {
                len += this.reasonBytes.length;
            }
            ByteBuffer buf = BufferUtil.allocate(len);
            BufferUtil.flipToFill(buf);
            buf.put((byte) (this.statusCode >>> 8 & 0xFF));
            buf.put((byte) (this.statusCode >>> 0 & 0xFF));
            if (hasReason) {
                buf.put(this.reasonBytes, 0, this.reasonBytes.length);
            }
            BufferUtil.flipToFlush(buf, 0);
            return buf;
        } else {
            return null;
        }
    }

    public CloseFrame asFrame() {
        CloseFrame frame = new CloseFrame();
        frame.setFin(true);
        if (this.statusCode >= 1000 && this.statusCode != 1006 && this.statusCode != 1005) {
            if (this.statusCode == 1015) {
                throw new ProtocolException("Close Frame with status code " + this.statusCode + " not allowed (per RFC6455)");
            }
            frame.setPayload(this.asByteBuffer());
        }
        return frame;
    }

    public String getReason() {
        return this.reasonBytes == null ? null : new String(this.reasonBytes, StandardCharsets.UTF_8);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public boolean isHarsh() {
        return this.statusCode != 1000 && this.statusCode != 1005;
    }

    public boolean isAbnormal() {
        return this.statusCode != 1000;
    }

    public String toString() {
        return String.format("CloseInfo[code=%d,reason=%s]", this.statusCode, this.getReason());
    }
}