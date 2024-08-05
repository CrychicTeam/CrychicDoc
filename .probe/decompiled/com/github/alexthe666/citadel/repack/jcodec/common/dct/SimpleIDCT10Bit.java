package com.github.alexthe666.citadel.repack.jcodec.common.dct;

public class SimpleIDCT10Bit {

    private static final int ROUND_COL = 8192;

    private static final int ROUND_ROW = 32768;

    private static final int SHIFT_COL = 14;

    private static final int SHIFT_ROW = 16;

    public static final int C0 = 23170;

    public static final int C1 = 32138;

    public static final int C2 = 27246;

    public static final int C3 = 18205;

    public static final int C4 = 6393;

    public static final int C5 = 30274;

    public static final int C6 = 12540;

    public static int W1 = 90901;

    public static int W2 = 85627;

    public static int W3 = 77062;

    public static int W4 = 65535;

    public static int W5 = 51491;

    public static int W6 = 35468;

    public static int W7 = 18081;

    public static int ROW_SHIFT = 15;

    public static int COL_SHIFT = 20;

    public static final void idct10(int[] buf, int off) {
        for (int i = 0; i < 8; i++) {
            idctRow(buf, off + (i << 3));
        }
        for (int i = 0; i < 8; i++) {
            idctCol(buf, off + i);
        }
    }

    private static final void idctCol(int[] buf, int off) {
        int a0 = W4 * (buf[off + 0] + (1 << COL_SHIFT - 1) / W4);
        int var10 = a0 + W2 * buf[off + 16];
        int var11 = a0 + W6 * buf[off + 16];
        int var12 = a0 + -W6 * buf[off + 16];
        int var13 = a0 + -W2 * buf[off + 16];
        int b0 = W1 * buf[off + 8];
        int b1 = W3 * buf[off + 8];
        int b2 = W5 * buf[off + 8];
        int b3 = W7 * buf[off + 8];
        b0 += W3 * buf[off + 24];
        b1 += -W7 * buf[off + 24];
        b2 += -W1 * buf[off + 24];
        b3 += -W5 * buf[off + 24];
        if (buf[off + 32] != 0) {
            var10 += W4 * buf[off + 32];
            var11 += -W4 * buf[off + 32];
            var12 += -W4 * buf[off + 32];
            var13 += W4 * buf[off + 32];
        }
        if (buf[off + 40] != 0) {
            b0 += W5 * buf[off + 40];
            b1 += -W1 * buf[off + 40];
            b2 += W7 * buf[off + 40];
            b3 += W3 * buf[off + 40];
        }
        if (buf[off + 48] != 0) {
            var10 += W6 * buf[off + 48];
            var11 += -W2 * buf[off + 48];
            var12 += W2 * buf[off + 48];
            var13 += -W6 * buf[off + 48];
        }
        if (buf[off + 56] != 0) {
            b0 += W7 * buf[off + 56];
            b1 += -W5 * buf[off + 56];
            b2 += W3 * buf[off + 56];
            b3 += -W1 * buf[off + 56];
        }
        buf[off] = var10 + b0 >> COL_SHIFT;
        buf[off + 8] = var11 + b1 >> COL_SHIFT;
        buf[off + 16] = var12 + b2 >> COL_SHIFT;
        buf[off + 24] = var13 + b3 >> COL_SHIFT;
        buf[off + 32] = var13 - b3 >> COL_SHIFT;
        buf[off + 40] = var12 - b2 >> COL_SHIFT;
        buf[off + 48] = var11 - b1 >> COL_SHIFT;
        buf[off + 56] = var10 - b0 >> COL_SHIFT;
    }

    private static final void idctRow(int[] buf, int off) {
        int a0 = W4 * buf[off] + (1 << ROW_SHIFT - 1);
        int var10 = a0 + W2 * buf[off + 2];
        int var11 = a0 + W6 * buf[off + 2];
        int var12 = a0 - W6 * buf[off + 2];
        int var13 = a0 - W2 * buf[off + 2];
        int b0 = W1 * buf[off + 1];
        b0 += W3 * buf[off + 3];
        int b1 = W3 * buf[off + 1];
        b1 += -W7 * buf[off + 3];
        int b2 = W5 * buf[off + 1];
        b2 += -W1 * buf[off + 3];
        int b3 = W7 * buf[off + 1];
        b3 += -W5 * buf[off + 3];
        if (buf[off + 4] != 0 || buf[off + 5] != 0 || buf[off + 6] != 0 || buf[off + 7] != 0) {
            var10 += W4 * buf[off + 4] + W6 * buf[off + 6];
            var11 += -W4 * buf[off + 4] - W2 * buf[off + 6];
            var12 += -W4 * buf[off + 4] + W2 * buf[off + 6];
            var13 += W4 * buf[off + 4] - W6 * buf[off + 6];
            b0 += W5 * buf[off + 5];
            b0 += W7 * buf[off + 7];
            b1 += -W1 * buf[off + 5];
            b1 += -W5 * buf[off + 7];
            b2 += W7 * buf[off + 5];
            b2 += W3 * buf[off + 7];
            b3 += W3 * buf[off + 5];
            b3 += -W1 * buf[off + 7];
        }
        buf[off + 0] = var10 + b0 >> ROW_SHIFT;
        buf[off + 7] = var10 - b0 >> ROW_SHIFT;
        buf[off + 1] = var11 + b1 >> ROW_SHIFT;
        buf[off + 6] = var11 - b1 >> ROW_SHIFT;
        buf[off + 2] = var12 + b2 >> ROW_SHIFT;
        buf[off + 5] = var12 - b2 >> ROW_SHIFT;
        buf[off + 3] = var13 + b3 >> ROW_SHIFT;
        buf[off + 4] = var13 - b3 >> ROW_SHIFT;
    }

    public static void fdctProres10(int[] block, int off) {
        for (int j = 0; j < 8; j++) {
            fdctCol(block, off + j);
        }
        for (int i = 0; i < 64; i += 8) {
            fdctRow(block, off + i);
        }
    }

    private static void fdctRow(int[] block, int off) {
        int z0 = block[off + 0] - block[off + 7];
        int z1 = block[off + 1] - block[off + 6];
        int z2 = block[off + 2] - block[off + 5];
        int z3 = block[off + 3] - block[off + 4];
        int z4 = block[off + 0] + block[off + 7];
        int z5 = block[off + 3] + block[off + 4];
        int z6 = block[off + 1] + block[off + 6];
        int z7 = block[off + 2] + block[off + 5];
        int u0 = z4 - z5;
        int u1 = z6 - z7;
        int c0 = (z4 + z5) * 23170;
        int c1 = (z6 + z7) * 23170;
        int c2 = u0 * 30274;
        int c3 = u1 * 12540;
        int c4 = u0 * 12540;
        int c5 = u1 * 30274;
        block[1 + off] = z0 * 32138 + z1 * 27246 + z2 * 18205 + z3 * 6393 + 32768 >> 16;
        block[3 + off] = z0 * 27246 - z1 * 6393 - z2 * 32138 - z3 * 18205 + 32768 >> 16;
        block[5 + off] = z0 * 18205 - z1 * 32138 + z2 * 6393 + z3 * 27246 + 32768 >> 16;
        block[7 + off] = z0 * 6393 - z1 * 18205 + z2 * 27246 - z3 * 32138 + 32768 >> 16;
        block[0 + off] = c0 + c1 + 32768 >> 16;
        block[2 + off] = c2 + c3 + 32768 >> 16;
        block[4 + off] = c0 - c1 + 32768 >> 16;
        block[6 + off] = c4 - c5 + 32768 >> 16;
    }

    private static void fdctCol(int[] block, int off) {
        int z0 = block[off + 0] - block[off + 56];
        int z1 = block[off + 8] - block[off + 48];
        int z2 = block[off + 16] - block[off + 40];
        int z3 = block[off + 24] - block[off + 32];
        int z4 = block[off + 0] + block[off + 56];
        int z5 = block[off + 24] + block[off + 32];
        int z6 = block[off + 8] + block[off + 48];
        int z7 = block[off + 16] + block[off + 40];
        int u0 = z4 - z5;
        int u1 = z6 - z7;
        int c0 = (z4 + z5) * 23170;
        int c1 = (z6 + z7) * 23170;
        int c2 = u0 * 30274;
        int c3 = u1 * 12540;
        int c4 = u0 * 12540;
        int c5 = u1 * 30274;
        block[8 + off] = z0 * 32138 + z1 * 27246 + z2 * 18205 + z3 * 6393 + 8192 >> 14;
        block[24 + off] = z0 * 27246 - z1 * 6393 - z2 * 32138 - z3 * 18205 + 8192 >> 14;
        block[40 + off] = z0 * 18205 - z1 * 32138 + z2 * 6393 + z3 * 27246 + 8192 >> 14;
        block[56 + off] = z0 * 6393 - z1 * 18205 + z2 * 27246 - z3 * 32138 + 8192 >> 14;
        block[0 + off] = c0 + c1 + 8192 >> 14;
        block[16 + off] = c2 + c3 + 8192 >> 14;
        block[32 + off] = c0 - c1 + 8192 >> 14;
        block[48 + off] = c4 - c5 + 8192 >> 14;
    }
}