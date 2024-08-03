package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

public class Packed4BitList {

    private static int[] CLEAR_MASK = new int[] { 268435440, -16, -16, -16, -16, -16, -16 };

    public static int _7(int val0, int val1, int val2, int val3, int val4, int val5, int val6) {
        return 1879048192 | (val0 & 15) << 24 | (val1 & 15) << 20 | (val2 & 15) << 16 | (val3 & 15) << 12 | (val4 & 15) << 8 | (val5 & 15) << 4 | val6 & 15;
    }

    public static int _3(int val0, int val1, int val2) {
        return _7(val0, val1, val2, 0, 0, 0, 0);
    }

    public static int set(int list, int val, int n) {
        int cnt = list >> 28 & 15;
        int newc = n + 1;
        cnt = newc > cnt ? newc : cnt;
        return list & CLEAR_MASK[n] | (val & 0xFF) << (n << 2) | cnt << 28;
    }

    public static int get(int list, int n) {
        return n > 6 ? 0 : list >> (n << 2) & 0xFF;
    }
}