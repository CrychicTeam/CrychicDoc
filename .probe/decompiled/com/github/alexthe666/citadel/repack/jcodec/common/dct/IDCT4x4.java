package com.github.alexthe666.citadel.repack.jcodec.common.dct;

public class IDCT4x4 {

    public static final int CN_SHIFT = 12;

    public static final int C1 = C_FIX(0.6532814824);

    public static final int C2 = C_FIX(0.2705980501);

    public static final int C3 = C_FIX(0.5);

    public static final int C_SHIFT = 18;

    public static final int RN_SHIFT = 15;

    public static final int R1 = R_FIX(0.6532814824);

    public static final int R2 = R_FIX(0.2705980501);

    public static final int R3 = R_FIX(0.5);

    public static final int R_SHIFT = 11;

    public static void idct(int[] blk, int off) {
        for (int i = 0; i < 4; i++) {
            idct4row(blk, off + (i << 2));
        }
        for (int var3 = 0; var3 < 4; var3++) {
            idct4col_add(blk, off + var3);
        }
    }

    public static final int C_FIX(double x) {
        return (int) (x * 1.414213562 * 4096.0 + 0.5);
    }

    private static void idct4col_add(int[] blk, int off) {
        int a0 = blk[off];
        int a1 = blk[off + 4];
        int a2 = blk[off + 8];
        int a3 = blk[off + 12];
        int c0 = (a0 + a2) * C3 + 131072;
        int c2 = (a0 - a2) * C3 + 131072;
        int c1 = a1 * C1 + a3 * C2;
        int c3 = a1 * C2 - a3 * C1;
        blk[off] = c0 + c1 >> 18;
        blk[off + 4] = c2 + c3 >> 18;
        blk[off + 8] = c2 - c3 >> 18;
        blk[off + 12] = c0 - c1 >> 18;
    }

    public static final int R_FIX(double x) {
        return (int) (x * 1.414213562 * 32768.0 + 0.5);
    }

    private static void idct4row(int[] blk, int off) {
        int a0 = blk[off];
        int a1 = blk[off + 1];
        int a2 = blk[off + 2];
        int a3 = blk[off + 3];
        int c0 = (a0 + a2) * R3 + 1024;
        int c2 = (a0 - a2) * R3 + 1024;
        int c1 = a1 * R1 + a3 * R2;
        int c3 = a1 * R2 - a3 * R1;
        blk[off] = c0 + c1 >> 11;
        blk[off + 1] = c2 + c3 >> 11;
        blk[off + 2] = c2 - c3 >> 11;
        blk[off + 3] = c0 - c1 >> 11;
    }
}