package info.journeymap.shaded.org.eclipse.jetty.websocket.common;

import info.journeymap.shaded.org.eclipse.jetty.io.ByteBufferPool;
import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.ProtocolException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketBehavior;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.WebSocketPolicy;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Extension;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;
import java.util.List;

public class Generator {

    public static final int MAX_HEADER_LENGTH = 28;

    private final WebSocketBehavior behavior;

    private final ByteBufferPool bufferPool;

    private final boolean validating;

    private final boolean readOnly;

    private byte flagsInUse = 0;

    public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool) {
        this(policy, bufferPool, true, false);
    }

    public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool, boolean validating) {
        this(policy, bufferPool, validating, false);
    }

    public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool, boolean validating, boolean readOnly) {
        this.behavior = policy.getBehavior();
        this.bufferPool = bufferPool;
        this.validating = validating;
        this.readOnly = readOnly;
    }

    public void assertFrameValid(Frame frame) {
        if (this.validating) {
            if (frame.isRsv1() && !this.isRsv1InUse()) {
                throw new ProtocolException("RSV1 not allowed to be set");
            } else if (frame.isRsv2() && !this.isRsv2InUse()) {
                throw new ProtocolException("RSV2 not allowed to be set");
            } else if (frame.isRsv3() && !this.isRsv3InUse()) {
                throw new ProtocolException("RSV3 not allowed to be set");
            } else {
                if (OpCode.isControlFrame(frame.getOpCode())) {
                    if (frame.getPayloadLength() > 125) {
                        throw new ProtocolException("Invalid control frame payload length");
                    }
                    if (!frame.isFin()) {
                        throw new ProtocolException("Control Frames must be FIN=true");
                    }
                    if (frame.getOpCode() == 8) {
                        ByteBuffer payload = frame.getPayload();
                        if (payload != null) {
                            new CloseInfo(payload, true);
                        }
                    }
                }
            }
        }
    }

    public void configureFromExtensions(List<? extends Extension> exts) {
        this.flagsInUse = 0;
        for (Extension ext : exts) {
            if (ext.isRsv1User()) {
                this.flagsInUse = (byte) (this.flagsInUse | 64);
            }
            if (ext.isRsv2User()) {
                this.flagsInUse = (byte) (this.flagsInUse | 32);
            }
            if (ext.isRsv3User()) {
                this.flagsInUse = (byte) (this.flagsInUse | 16);
            }
        }
    }

    public ByteBuffer generateHeaderBytes(Frame frame) {
        ByteBuffer buffer = this.bufferPool.acquire(28, true);
        this.generateHeaderBytes(frame, buffer);
        return buffer;
    }

    public void generateHeaderBytes(Frame frame, ByteBuffer buffer) {
        int p = BufferUtil.flipToFill(buffer);
        this.assertFrameValid(frame);
        byte b = (byte) 0;
        if (frame.isFin()) {
            b = (byte) (b | 128);
        }
        if (frame.isRsv1()) {
            b = (byte) (b | 64);
        }
        if (frame.isRsv2()) {
            b = (byte) (b | 32);
        }
        if (frame.isRsv3()) {
            b = (byte) (b | 16);
        }
        byte opcode = frame.getOpCode();
        if (frame.getOpCode() == 0) {
            opcode = 0;
        }
        b = (byte) (b | opcode & 15);
        buffer.put(b);
        b = (byte) (frame.isMasked() ? -128 : 0);
        int payloadLength = frame.getPayloadLength();
        if (payloadLength > 65535) {
            byte var16 = (byte) (b | 127);
            buffer.put(var16);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) (payloadLength >> 24 & 0xFF));
            buffer.put((byte) (payloadLength >> 16 & 0xFF));
            buffer.put((byte) (payloadLength >> 8 & 0xFF));
            buffer.put((byte) (payloadLength & 0xFF));
        } else if (payloadLength >= 126) {
            byte var17 = (byte) (b | 126);
            buffer.put(var17);
            buffer.put((byte) (payloadLength >> 8));
            buffer.put((byte) (payloadLength & 0xFF));
        } else {
            byte var18 = (byte) (b | payloadLength & 127);
            buffer.put(var18);
        }
        if (frame.isMasked() && !this.readOnly) {
            byte[] mask = frame.getMask();
            buffer.put(mask);
            int maskInt = 0;
            for (byte maskByte : mask) {
                maskInt = (maskInt << 8) + (maskByte & 255);
            }
            ByteBuffer payload = frame.getPayload();
            if (payload != null && payload.remaining() > 0) {
                int maskOffset = 0;
                int start = payload.position();
                int end = payload.limit();
                int remaining;
                while ((remaining = end - start) > 0) {
                    if (remaining >= 4) {
                        payload.putInt(start, payload.getInt(start) ^ maskInt);
                        start += 4;
                    } else {
                        payload.put(start, (byte) (payload.get(start) ^ mask[maskOffset & 3]));
                        start++;
                        maskOffset++;
                    }
                }
            }
        }
        BufferUtil.flipToFlush(buffer, p);
    }

    public void generateWholeFrame(Frame frame, ByteBuffer buf) {
        buf.put(this.generateHeaderBytes(frame));
        if (frame.hasPayload()) {
            if (this.readOnly) {
                buf.put(frame.getPayload().slice());
            } else {
                buf.put(frame.getPayload());
            }
        }
    }

    public ByteBufferPool getBufferPool() {
        return this.bufferPool;
    }

    public void setRsv1InUse(boolean rsv1InUse) {
        if (this.readOnly) {
            throw new RuntimeException("Not allowed to modify read-only frame");
        } else {
            this.flagsInUse = (byte) (this.flagsInUse & 191 | (rsv1InUse ? 64 : 0));
        }
    }

    public void setRsv2InUse(boolean rsv2InUse) {
        if (this.readOnly) {
            throw new RuntimeException("Not allowed to modify read-only frame");
        } else {
            this.flagsInUse = (byte) (this.flagsInUse & 223 | (rsv2InUse ? 32 : 0));
        }
    }

    public void setRsv3InUse(boolean rsv3InUse) {
        if (this.readOnly) {
            throw new RuntimeException("Not allowed to modify read-only frame");
        } else {
            this.flagsInUse = (byte) (this.flagsInUse & 239 | (rsv3InUse ? 16 : 0));
        }
    }

    public boolean isRsv1InUse() {
        return (this.flagsInUse & 64) != 0;
    }

    public boolean isRsv2InUse() {
        return (this.flagsInUse & 32) != 0;
    }

    public boolean isRsv3InUse() {
        return (this.flagsInUse & 16) != 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Generator[");
        builder.append(this.behavior);
        if (this.validating) {
            builder.append(",validating");
        }
        if (this.isRsv1InUse()) {
            builder.append(",+rsv1");
        }
        if (this.isRsv2InUse()) {
            builder.append(",+rsv2");
        }
        if (this.isRsv3InUse()) {
            builder.append(",+rsv3");
        }
        builder.append("]");
        return builder.toString();
    }
}