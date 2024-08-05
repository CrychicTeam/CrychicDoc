package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.vp9;

public class MV {

    public static int create(int x, int y, int ref) {
        return ref << 28 | (y & 16383) << 14 | x & 16383;
    }

    public static int x(int mv) {
        return mv << 18 >> 18;
    }

    public static int y(int mv) {
        return mv << 4 >> 18;
    }

    public static int ref(int mv) {
        return mv >> 28 & 3;
    }
}