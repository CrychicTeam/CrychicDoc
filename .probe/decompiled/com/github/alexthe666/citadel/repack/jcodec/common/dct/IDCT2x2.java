package com.github.alexthe666.citadel.repack.jcodec.common.dct;

public class IDCT2x2 {

    public static void idct(int[] blk, int off) {
        int x0 = blk[off];
        int x1 = blk[off + 1];
        int x2 = blk[off + 2];
        int x3 = blk[off + 3];
        int t0 = x0 + x2;
        int t2 = x0 - x2;
        int t1 = x1 + x3;
        int t3 = x1 - x3;
        blk[off] = t0 + t1 >> 3;
        blk[off + 1] = t0 - t1 >> 3;
        blk[off + 2] = t2 + t3 >> 3;
        blk[off + 3] = t2 - t3 >> 3;
    }
}