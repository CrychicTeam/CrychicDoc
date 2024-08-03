package com.github.alexthe666.citadel.repack.jcodec.algo;

import com.github.alexthe666.citadel.repack.jcodec.api.NotSupportedException;

public class DataConvert {

    public static int[] from16BE(byte[] b) {
        int[] result = new int[b.length >> 1];
        int off = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (b[off++] & 255) << 8 | b[off++] & 255;
        }
        return result;
    }

    public static int[] from24BE(byte[] b) {
        int[] result = new int[b.length / 3];
        int off = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (b[off++] & 255) << 16 | (b[off++] & 255) << 8 | b[off++] & 255;
        }
        return result;
    }

    public static int[] from16LE(byte[] b) {
        int[] result = new int[b.length >> 1];
        int off = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = b[off++] & 255 | (b[off++] & 255) << 8;
        }
        return result;
    }

    public static int[] from24LE(byte[] b) {
        int[] result = new int[b.length / 3];
        int off = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = b[off++] & 255 | (b[off++] & 255) << 8 | (b[off++] & 255) << 16;
        }
        return result;
    }

    public static byte[] to16BE(int[] ia) {
        byte[] result = new byte[ia.length << 1];
        int off = 0;
        for (int i = 0; i < ia.length; i++) {
            result[off++] = (byte) (ia[i] >> 8 & 0xFF);
            result[off++] = (byte) (ia[i] & 0xFF);
        }
        return result;
    }

    public static byte[] to24BE(int[] ia) {
        byte[] result = new byte[ia.length * 3];
        int off = 0;
        for (int i = 0; i < ia.length; i++) {
            result[off++] = (byte) (ia[i] >> 16 & 0xFF);
            result[off++] = (byte) (ia[i] >> 8 & 0xFF);
            result[off++] = (byte) (ia[i] & 0xFF);
        }
        return result;
    }

    public static byte[] to16LE(int[] ia) {
        byte[] result = new byte[ia.length << 1];
        int off = 0;
        for (int i = 0; i < ia.length; i++) {
            result[off++] = (byte) (ia[i] & 0xFF);
            result[off++] = (byte) (ia[i] >> 8 & 0xFF);
        }
        return result;
    }

    public static byte[] to24LE(int[] ia) {
        byte[] result = new byte[ia.length * 3];
        int off = 0;
        for (int i = 0; i < ia.length; i++) {
            result[off++] = (byte) (ia[i] & 0xFF);
            result[off++] = (byte) (ia[i] >> 8 & 0xFF);
            result[off++] = (byte) (ia[i] >> 16 & 0xFF);
        }
        return result;
    }

    public static int[] fromByte(byte[] b, int depth, boolean isBe) {
        if (depth == 24) {
            return isBe ? from24BE(b) : from24LE(b);
        } else if (depth == 16) {
            return isBe ? from16BE(b) : from16LE(b);
        } else {
            throw new NotSupportedException("Conversion from " + depth + "bit " + (isBe ? "big endian" : "little endian") + " is not supported.");
        }
    }

    public static byte[] toByte(int[] ia, int depth, boolean isBe) {
        if (depth == 24) {
            return isBe ? to24BE(ia) : to24LE(ia);
        } else if (depth == 16) {
            return isBe ? to16BE(ia) : to16LE(ia);
        } else {
            throw new NotSupportedException("Conversion to " + depth + "bit " + (isBe ? "big endian" : "little endian") + " is not supported.");
        }
    }
}