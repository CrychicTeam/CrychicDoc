package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MPEGUtil {

    public static final ByteBuffer gotoNextMarker(ByteBuffer buf) {
        return gotoMarker(buf, 0, 256, 511);
    }

    public static final ByteBuffer gotoMarker(ByteBuffer buf, int n, int mmin, int mmax) {
        if (!buf.hasRemaining()) {
            return null;
        } else {
            int from = buf.position();
            ByteBuffer result = buf.slice();
            result.order(ByteOrder.BIG_ENDIAN);
            int val = -1;
            while (buf.hasRemaining()) {
                val = val << 8 | buf.get() & 255;
                if (val >= mmin && val <= mmax) {
                    if (n == 0) {
                        buf.position(buf.position() - 4);
                        result.limit(buf.position() - from);
                        break;
                    }
                    n--;
                }
            }
            return result;
        }
    }

    public static final ByteBuffer nextSegment(ByteBuffer buf) {
        gotoMarker(buf, 0, 256, 511);
        return gotoMarker(buf, 1, 256, 511);
    }
}