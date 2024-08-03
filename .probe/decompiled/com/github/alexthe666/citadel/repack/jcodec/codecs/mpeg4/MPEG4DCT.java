package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4;

import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class MPEG4DCT {

    private static final int W1 = 2841;

    private static final int W2 = 2676;

    private static final int W3 = 2408;

    private static final int W5 = 1609;

    private static final int W6 = 1108;

    private static final int W7 = 565;

    public static void idctPut(byte[][] p, short[][] block, boolean interlacing) {
        idctRows(block[0]);
        idctRows(block[1]);
        idctRows(block[2]);
        idctRows(block[3]);
        idctRows(block[4]);
        idctRows(block[5]);
        int stride = 16;
        int stride2 = 8;
        int nextBlock = 128;
        if (interlacing) {
            nextBlock = stride;
            stride *= 2;
        }
        idctColumnsPut(block[0], p[0], 0, stride);
        idctColumnsPut(block[1], p[0], 8, stride);
        idctColumnsPut(block[2], p[0], nextBlock, stride);
        idctColumnsPut(block[3], p[0], nextBlock + 8, stride);
        idctColumnsPut(block[4], p[1], 0, stride2);
        idctColumnsPut(block[5], p[2], 0, stride2);
    }

    public static void idctAdd(byte[][] p, short[] block, int index, boolean interlacing) {
        idctRows(block);
        switch(index) {
            case 0:
                idctColumnsAdd(block, p[0], 0, 16);
                break;
            case 1:
                idctColumnsAdd(block, p[0], 8, 16);
                break;
            case 2:
                if (interlacing) {
                    idctColumnsAdd(block, p[0], 16, 32);
                } else {
                    idctColumnsAdd(block, p[0], 128, 16);
                }
                break;
            case 3:
                if (interlacing) {
                    idctColumnsAdd(block, p[0], 24, 32);
                } else {
                    idctColumnsAdd(block, p[0], 136, 16);
                }
                break;
            case 4:
                idctColumnsAdd(block, p[1], 0, 8);
                break;
            case 5:
                idctColumnsAdd(block, p[2], 0, 8);
        }
    }

    private static final byte clamp255(int val) {
        val -= 255;
        val = -(255 + (val >> 31 & val));
        return (byte) (-(val >> 31 & val) - 128);
    }

    public static void idctColumnsPut(short[] block, byte[] dst, int dstOffset, int stride) {
        for (int i = 0; i < 8; i++) {
            int offset = dstOffset + i;
            int X1;
            int X2;
            int X3;
            int X4;
            int X5;
            int X6;
            int X7;
            if (((X1 = block[i + 32] << 8) | (X2 = block[i + 48]) | (X3 = block[i + 16]) | (X4 = block[i + 8]) | (X5 = block[i + 56]) | (X6 = block[i + 40]) | (X7 = block[i + 24])) == 0) {
                dst[offset + stride * 0] = dst[offset + stride * 1] = dst[offset + stride * 2] = dst[offset + stride * 3] = dst[offset + stride * 4] = dst[offset + stride * 5] = dst[offset + stride * 6] = dst[offset + stride * 7] = clamp255(block[i + 0] + 32 >> 6);
            } else {
                int X0 = (block[i + 0] << 8) + 8192;
                int X8 = 565 * (X4 + X5) + 4;
                X4 = X8 + 2276 * X4 >> 3;
                X5 = X8 - 3406 * X5 >> 3;
                X8 = 2408 * (X6 + X7) + 4;
                X6 = X8 - 799 * X6 >> 3;
                X7 = X8 - 4017 * X7 >> 3;
                X8 = X0 + X1;
                X0 -= X1;
                X1 = 1108 * (X3 + X2) + 4;
                X2 = X1 - 3784 * X2 >> 3;
                X3 = X1 + 1568 * X3 >> 3;
                X1 = X4 + X6;
                X4 -= X6;
                X6 = X5 + X7;
                X5 -= X7;
                X7 = X8 + X3;
                X8 -= X3;
                X3 = X0 + X2;
                X0 -= X2;
                X2 = 181 * (X4 + X5) + 128 >> 8;
                X4 = 181 * (X4 - X5) + 128 >> 8;
                dst[offset + stride * 0] = clamp255(X7 + X1 >> 14);
                dst[offset + stride * 1] = clamp255(X3 + X2 >> 14);
                dst[offset + stride * 2] = clamp255(X0 + X4 >> 14);
                dst[offset + stride * 3] = clamp255(X8 + X6 >> 14);
                dst[offset + stride * 4] = clamp255(X8 - X6 >> 14);
                dst[offset + stride * 5] = clamp255(X0 - X4 >> 14);
                dst[offset + stride * 6] = clamp255(X3 - X2 >> 14);
                dst[offset + stride * 7] = clamp255(X7 - X1 >> 14);
            }
        }
    }

    public static void idctColumnsAdd(short[] block, byte[] dst, int dstOffset, int stride) {
        for (int i = 0; i < 8; i++) {
            int offset = dstOffset + i;
            int X1;
            int X2;
            int X3;
            int X4;
            int X5;
            int X6;
            int X7;
            if (((X1 = block[i + 32] << 8) | (X2 = block[i + 48]) | (X3 = block[i + 16]) | (X4 = block[i + 8]) | (X5 = block[i + 56]) | (X6 = block[i + 40]) | (X7 = block[i + 24])) == 0) {
                int pixel = block[i + 0] + 32 >> 6;
                dst[offset + stride * 0] = (byte) MathUtil.clip(dst[offset + stride * 0] + pixel, -128, 127);
                dst[offset + stride * 1] = (byte) MathUtil.clip(dst[offset + stride * 1] + pixel, -128, 127);
                dst[offset + stride * 2] = (byte) MathUtil.clip(dst[offset + stride * 2] + pixel, -128, 127);
                dst[offset + stride * 3] = (byte) MathUtil.clip(dst[offset + stride * 3] + pixel, -128, 127);
                dst[offset + stride * 4] = (byte) MathUtil.clip(dst[offset + stride * 4] + pixel, -128, 127);
                dst[offset + stride * 5] = (byte) MathUtil.clip(dst[offset + stride * 5] + pixel, -128, 127);
                dst[offset + stride * 6] = (byte) MathUtil.clip(dst[offset + stride * 6] + pixel, -128, 127);
                dst[offset + stride * 7] = (byte) MathUtil.clip(dst[offset + stride * 7] + pixel, -128, 127);
            } else {
                int X0 = (block[i + 0] << 8) + 8192;
                int X8 = 565 * (X4 + X5) + 4;
                X4 = X8 + 2276 * X4 >> 3;
                X5 = X8 - 3406 * X5 >> 3;
                X8 = 2408 * (X6 + X7) + 4;
                X6 = X8 - 799 * X6 >> 3;
                X7 = X8 - 4017 * X7 >> 3;
                X8 = X0 + X1;
                X0 -= X1;
                X1 = 1108 * (X3 + X2) + 4;
                X2 = X1 - 3784 * X2 >> 3;
                X3 = X1 + 1568 * X3 >> 3;
                X1 = X4 + X6;
                X4 -= X6;
                X6 = X5 + X7;
                X5 -= X7;
                X7 = X8 + X3;
                X8 -= X3;
                X3 = X0 + X2;
                X0 -= X2;
                X2 = 181 * (X4 + X5) + 128 >> 8;
                X4 = 181 * (X4 - X5) + 128 >> 8;
                dst[offset + stride * 0] = (byte) MathUtil.clip(dst[offset + stride * 0] + (X7 + X1 >> 14), -128, 127);
                dst[offset + stride * 1] = (byte) MathUtil.clip(dst[offset + stride * 1] + (X3 + X2 >> 14), -128, 127);
                dst[offset + stride * 2] = (byte) MathUtil.clip(dst[offset + stride * 2] + (X0 + X4 >> 14), -128, 127);
                dst[offset + stride * 3] = (byte) MathUtil.clip(dst[offset + stride * 3] + (X8 + X6 >> 14), -128, 127);
                dst[offset + stride * 4] = (byte) MathUtil.clip(dst[offset + stride * 4] + (X8 - X6 >> 14), -128, 127);
                dst[offset + stride * 5] = (byte) MathUtil.clip(dst[offset + stride * 5] + (X0 - X4 >> 14), -128, 127);
                dst[offset + stride * 6] = (byte) MathUtil.clip(dst[offset + stride * 6] + (X3 - X2 >> 14), -128, 127);
                dst[offset + stride * 7] = (byte) MathUtil.clip(dst[offset + stride * 7] + (X7 - X1 >> 14), -128, 127);
            }
        }
    }

    public static void idctRows(short[] block) {
        for (int i = 0; i < 8; i++) {
            int offset = i << 3;
            int X1;
            int X2;
            int X3;
            int X4;
            int X5;
            int X6;
            int X7;
            if (((X1 = block[offset + 4] << 11) | (X2 = block[offset + 6]) | (X3 = block[offset + 2]) | (X4 = block[offset + 1]) | (X5 = block[offset + 7]) | (X6 = block[offset + 5]) | (X7 = block[offset + 3])) == 0) {
                block[offset] = block[offset + 1] = block[offset + 2] = block[offset + 3] = block[offset + 4] = block[offset + 5] = block[offset + 6] = block[offset + 7] = (short) (block[offset] << 3);
            } else {
                int X0 = (block[offset] << 11) + 128;
                int X8 = 565 * (X4 + X5);
                X4 = X8 + 2276 * X4;
                X5 = X8 - 3406 * X5;
                X8 = 2408 * (X6 + X7);
                X6 = X8 - 799 * X6;
                X7 = X8 - 4017 * X7;
                X8 = X0 + X1;
                X0 -= X1;
                X1 = 1108 * (X3 + X2);
                X2 = X1 - 3784 * X2;
                X3 = X1 + 1568 * X3;
                X1 = X4 + X6;
                X4 -= X6;
                X6 = X5 + X7;
                X5 -= X7;
                X7 = X8 + X3;
                X8 -= X3;
                X3 = X0 + X2;
                X0 -= X2;
                X2 = 181 * (X4 + X5) + 128 >> 8;
                X4 = 181 * (X4 - X5) + 128 >> 8;
                block[offset] = (short) (X7 + X1 >> 8);
                block[offset + 1] = (short) (X3 + X2 >> 8);
                block[offset + 2] = (short) (X0 + X4 >> 8);
                block[offset + 3] = (short) (X8 + X6 >> 8);
                block[offset + 4] = (short) (X8 - X6 >> 8);
                block[offset + 5] = (short) (X0 - X4 >> 8);
                block[offset + 6] = (short) (X3 - X2 >> 8);
                block[offset + 7] = (short) (X7 - X1 >> 8);
            }
        }
    }
}