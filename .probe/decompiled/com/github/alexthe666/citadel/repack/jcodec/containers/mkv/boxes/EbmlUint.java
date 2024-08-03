package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import java.nio.ByteBuffer;

public class EbmlUint extends EbmlBin {

    public EbmlUint(byte[] id) {
        super(id);
    }

    public static EbmlUint createEbmlUint(byte[] id, long value) {
        EbmlUint e = new EbmlUint(id);
        e.setUint(value);
        return e;
    }

    public void setUint(long value) {
        this.data = ByteBuffer.wrap(longToBytes(value));
        this.dataLen = this.data.limit();
    }

    public long getUint() {
        long l = 0L;
        long tmp = 0L;
        for (int i = 0; i < this.data.limit(); i++) {
            tmp = (long) this.data.get(this.data.limit() - 1 - i) << 56;
            tmp >>>= 56 - i * 8;
            l |= tmp;
        }
        return l;
    }

    public static byte[] longToBytes(long value) {
        byte[] b = new byte[calculatePayloadSize(value)];
        for (int i = b.length - 1; i >= 0; i--) {
            b[i] = (byte) ((int) (value >>> 8 * (b.length - i - 1)));
        }
        return b;
    }

    public static int calculatePayloadSize(long value) {
        if (value == 0L) {
            return 1;
        } else {
            return value <= 2147483647L ? 4 - (Integer.numberOfLeadingZeros((int) value) >> 3) : 8 - (Long.numberOfLeadingZeros(value) >> 3);
        }
    }
}