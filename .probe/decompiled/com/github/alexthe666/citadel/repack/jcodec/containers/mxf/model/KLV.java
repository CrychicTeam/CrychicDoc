package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.IOException;
import java.nio.ByteBuffer;

public class KLV {

    public final long offset;

    public final long dataOffset;

    public final UL key;

    public final long len;

    ByteBuffer value;

    public KLV(UL k, long len, long offset, long dataOffset) {
        this.key = k;
        this.len = len;
        this.offset = offset;
        this.dataOffset = dataOffset;
    }

    public String toString() {
        return "KLV [offset=" + this.offset + ", dataOffset=" + this.dataOffset + ", key=" + this.key + ", len=" + this.len + ", value=" + this.value + "]";
    }

    public static KLV readKL(SeekableByteChannel ch) throws IOException {
        long offset = ch.position();
        if (offset >= ch.size() - 1L) {
            return null;
        } else {
            byte[] key = new byte[16];
            ch.read(ByteBuffer.wrap(key));
            long len = BER.decodeLength(ch);
            long dataOffset = ch.position();
            return new KLV(new UL(key), len, offset, dataOffset);
        }
    }

    public int getLenByteCount() {
        int berlen = (int) (this.dataOffset - this.offset - 16L);
        return berlen <= 0 ? 4 : berlen;
    }

    public static boolean matches(byte[] key1, byte[] key2, int len) {
        for (int i = 0; i < len; i++) {
            if (key1[i] != key2[i]) {
                return false;
            }
        }
        return true;
    }

    public static KLV readKLFromBuffer(ByteBuffer buffer, long baseOffset) {
        if (buffer.remaining() < 17) {
            return null;
        } else {
            long offset = baseOffset + (long) buffer.position();
            UL ul = UL.read(buffer);
            long len = BER.decodeLengthBuf(buffer);
            return new KLV(ul, len, offset, baseOffset + (long) buffer.position());
        }
    }
}