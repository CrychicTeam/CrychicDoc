package info.journeymap.shaded.org.eclipse.jetty.websocket.common.frames;

import info.journeymap.shaded.org.eclipse.jetty.util.BufferUtil;
import info.journeymap.shaded.org.eclipse.jetty.websocket.api.ProtocolException;
import info.journeymap.shaded.org.eclipse.jetty.websocket.common.WebSocketFrame;
import java.nio.ByteBuffer;
import java.util.Arrays;

public abstract class ControlFrame extends WebSocketFrame {

    public static final int MAX_CONTROL_PAYLOAD = 125;

    public ControlFrame(byte opcode) {
        super(opcode);
    }

    @Override
    public void assertValid() {
        if (this.isControlFrame()) {
            if (this.getPayloadLength() > 125) {
                throw new ProtocolException("Desired payload length [" + this.getPayloadLength() + "] exceeds maximum control payload length [" + 125 + "]");
            }
            if ((this.finRsvOp & 128) == 0) {
                throw new ProtocolException("Cannot have FIN==false on Control frames");
            }
            if ((this.finRsvOp & 64) != 0) {
                throw new ProtocolException("Cannot have RSV1==true on Control frames");
            }
            if ((this.finRsvOp & 32) != 0) {
                throw new ProtocolException("Cannot have RSV2==true on Control frames");
            }
            if ((this.finRsvOp & 16) != 0) {
                throw new ProtocolException("Cannot have RSV3==true on Control frames");
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ControlFrame other = (ControlFrame) obj;
            if (this.data == null) {
                if (other.data != null) {
                    return false;
                }
            } else if (!this.data.equals(other.data)) {
                return false;
            }
            if (this.finRsvOp != other.finRsvOp) {
                return false;
            } else {
                return !Arrays.equals(this.mask, other.mask) ? false : this.masked == other.masked;
            }
        }
    }

    @Override
    public boolean isControlFrame() {
        return true;
    }

    @Override
    public boolean isDataFrame() {
        return false;
    }

    @Override
    public WebSocketFrame setPayload(ByteBuffer buf) {
        if (buf != null && buf.remaining() > 125) {
            throw new ProtocolException("Control Payloads can not exceed 125 bytes in length.");
        } else {
            return super.setPayload(buf);
        }
    }

    @Override
    public ByteBuffer getPayload() {
        return super.getPayload() == null ? BufferUtil.EMPTY_BUFFER : super.getPayload();
    }
}