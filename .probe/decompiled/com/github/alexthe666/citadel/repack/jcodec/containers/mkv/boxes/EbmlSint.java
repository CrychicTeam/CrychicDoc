package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import java.nio.ByteBuffer;

public class EbmlSint extends EbmlBin {

    public static final long[] signedComplement = new long[] { 0L, 63L, 8191L, 1048575L, 134217727L, 17179869183L, 2199023255551L, 281474976710655L, 36028797018963967L };

    public EbmlSint(byte[] id) {
        super(id);
    }

    public void setLong(long value) {
        this.data = ByteBuffer.wrap(convertToBytes(value));
    }

    public long getLong() {
        if (this.data.limit() - this.data.position() == 8) {
            return this.data.duplicate().getLong();
        } else {
            byte[] b = this.data.array();
            long l = 0L;
            for (int i = b.length - 1; i >= 0; i--) {
                l |= ((long) b[i] & 255L) << 8 * (b.length - 1 - i);
            }
            return l;
        }
    }

    public static int ebmlSignedLength(long val) {
        if (val <= 64L && val >= -63L) {
            return 1;
        } else if (val <= 8192L && val >= -8191L) {
            return 2;
        } else if (val <= 1048576L && val >= -1048575L) {
            return 3;
        } else if (val <= 134217728L && val >= -134217727L) {
            return 4;
        } else if (val <= 17179869184L && val >= -17179869183L) {
            return 5;
        } else if (val <= 2199023255552L && val >= -2199023255551L) {
            return 6;
        } else {
            return val <= 281474976710656L && val >= -281474976710655L ? 7 : 8;
        }
    }

    public static byte[] convertToBytes(long val) {
        int num = ebmlSignedLength(val);
        val += signedComplement[num];
        return EbmlUtil.ebmlEncodeLen(val, num);
    }
}