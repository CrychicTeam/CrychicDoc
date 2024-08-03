package com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model;

import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class UL {

    private final byte[] bytes;

    private static final char[] hex = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public UL(byte[] bytes) {
        Preconditions.checkNotNull(bytes);
        this.bytes = bytes;
    }

    public static UL newULFromInts(int[] args) {
        byte[] bytes = new byte[args.length];
        for (int i = 0; i < args.length; i++) {
            bytes[i] = (byte) args[i];
        }
        return new UL(bytes);
    }

    public static UL newUL(String ul) {
        Preconditions.checkNotNull(ul);
        String[] split = StringUtils.splitS(ul, ".");
        byte[] b = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            int parseInt = Integer.parseInt(split[i], 16);
            b[i] = (byte) parseInt;
        }
        return new UL(b);
    }

    public int hashCode() {
        return (this.bytes[4] & 0xFF) << 24 | (this.bytes[5] & 0xFF) << 16 | (this.bytes[6] & 0xFF) << 8 | this.bytes[7] & 0xFF;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof UL)) {
            return false;
        } else {
            byte[] other = ((UL) obj).bytes;
            for (int i = 4; i < Math.min(this.bytes.length, other.length); i++) {
                if (this.bytes[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean maskEquals(UL o, int mask) {
        if (o == null) {
            return false;
        } else {
            byte[] other = o.bytes;
            mask >>= 4;
            for (int i = 4; i < Math.min(this.bytes.length, other.length); mask >>= 1) {
                if ((mask & 1) == 1 && this.bytes[i] != other[i]) {
                    return false;
                }
                i++;
            }
            return true;
        }
    }

    public String toString() {
        if (this.bytes.length == 0) {
            return "";
        } else {
            char[] str = new char[this.bytes.length * 3 - 1];
            int i = 0;
            int j = 0;
            for (i = 0; i < this.bytes.length - 1; i++) {
                str[j++] = hex[this.bytes[i] >> 4 & 15];
                str[j++] = hex[this.bytes[i] & 15];
                str[j++] = '.';
            }
            str[j++] = hex[this.bytes[i] >> 4 & 15];
            str[j++] = hex[this.bytes[i] & 15];
            return Platform.stringFromChars(str);
        }
    }

    public int get(int i) {
        return this.bytes[i];
    }

    public static UL read(ByteBuffer _bb) {
        byte[] umid = new byte[16];
        _bb.get(umid);
        return new UL(umid);
    }
}