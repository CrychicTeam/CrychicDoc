package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util;

public class EbmlUtil {

    public static final byte[] lengthOptions = new byte[] { 0, -128, 64, 32, 16, 8, 4, 2, 1 };

    public static final long one = 127L;

    public static final long two = 16256L;

    public static final long three = 2080768L;

    public static final long four = 266338304L;

    public static final long five = 34091302912L;

    public static final long six = 4363686772736L;

    public static final long seven = 558551906910208L;

    public static final long eight = 71494644084506624L;

    public static final long[] ebmlLengthMasks = new long[] { 0L, 127L, 16256L, 2080768L, 266338304L, 34091302912L, 4363686772736L, 558551906910208L, 71494644084506624L };

    public static byte[] ebmlEncodeLen(long value, int length) {
        byte[] b = new byte[length];
        for (int idx = 0; idx < length; idx++) {
            b[length - idx - 1] = (byte) ((int) (value >>> 8 * idx & 255L));
        }
        b[0] = (byte) (b[0] | 128 >>> length - 1);
        return b;
    }

    public static byte[] ebmlEncode(long value) {
        return ebmlEncodeLen(value, ebmlLength(value));
    }

    public static int computeLength(byte b) {
        if (b == 0) {
            throw new RuntimeException("Invalid head element for ebml sequence");
        } else {
            int i = 1;
            while ((b & lengthOptions[i]) == 0) {
                i++;
            }
            return i;
        }
    }

    public static int ebmlLength(long v) {
        if (v == 0L) {
            return 1;
        } else {
            int length = 8;
            while (length > 0 && (v & ebmlLengthMasks[length]) == 0L) {
                length--;
            }
            return length;
        }
    }

    public static String toHexString(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (byte b : a) {
            sb.append(String.format("0x%02x ", b & 255));
        }
        return sb.toString();
    }
}