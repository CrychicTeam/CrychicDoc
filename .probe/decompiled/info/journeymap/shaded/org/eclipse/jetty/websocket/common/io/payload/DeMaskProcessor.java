package info.journeymap.shaded.org.eclipse.jetty.websocket.common.io.payload;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.extensions.Frame;
import java.nio.ByteBuffer;

public class DeMaskProcessor implements PayloadProcessor {

    private byte[] maskBytes;

    private int maskInt;

    private int maskOffset;

    @Override
    public void process(ByteBuffer payload) {
        if (this.maskBytes != null) {
            int maskInt = this.maskInt;
            int start = payload.position();
            int end = payload.limit();
            int offset = this.maskOffset;
            int remaining;
            while ((remaining = end - start) > 0) {
                if (remaining >= 4 && (offset & 3) == 0) {
                    payload.putInt(start, payload.getInt(start) ^ maskInt);
                    start += 4;
                    offset += 4;
                } else {
                    payload.put(start, (byte) (payload.get(start) ^ this.maskBytes[offset & 3]));
                    start++;
                    offset++;
                }
            }
            this.maskOffset = offset;
        }
    }

    public void reset(byte[] mask) {
        this.maskBytes = mask;
        int maskInt = 0;
        if (mask != null) {
            for (byte maskByte : mask) {
                maskInt = (maskInt << 8) + (maskByte & 255);
            }
        }
        this.maskInt = maskInt;
        this.maskOffset = 0;
    }

    @Override
    public void reset(Frame frame) {
        this.reset(frame.getMask());
    }
}