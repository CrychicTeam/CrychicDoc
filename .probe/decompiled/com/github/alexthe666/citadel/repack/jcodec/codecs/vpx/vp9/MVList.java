package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.vp9;

public class MVList {

    private static long LO_MASK = 2147483647L;

    private static long HI_MASK = LO_MASK << 31;

    private static long HI_MASK_NEG = ~(HI_MASK | -4611686018427387904L);

    private static long LO_MASK_NEG = ~(LO_MASK | -4611686018427387904L);

    public static long create(int mv0, int mv1) {
        return Long.MIN_VALUE | (long) mv1 << 31 | (long) mv0 & LO_MASK;
    }

    public static long addUniq(long list, int mv) {
        long cnt = list >> 62 & 3L;
        if (cnt == 2L) {
            return list;
        } else if (cnt == 0L) {
            return 4611686018427387904L | list & LO_MASK_NEG | (long) mv & LO_MASK;
        } else {
            int first = (int) (list & LO_MASK);
            return first != mv ? Long.MIN_VALUE | list & HI_MASK_NEG | (long) mv << 31 & HI_MASK : list;
        }
    }

    public static long add(long list, int mv) {
        long cnt = list >> 62 & 3L;
        if (cnt == 2L) {
            return list;
        } else {
            return cnt == 0L ? 4611686018427387904L | list & LO_MASK_NEG | (long) mv & LO_MASK : Long.MIN_VALUE | list & HI_MASK_NEG | (long) mv << 31 & HI_MASK;
        }
    }

    public static int get(long list, int n) {
        return n == 0 ? (int) (list & LO_MASK) : (int) (list >> 31 & LO_MASK);
    }

    public static long set(long list, int n, int mv) {
        long cnt = list >> 62 & 3L;
        long newc = (long) (n + 1);
        cnt = newc > cnt ? newc : cnt;
        return n == 0 ? cnt << 62 | list & LO_MASK_NEG | (long) mv & LO_MASK : cnt << 62 | list & HI_MASK_NEG | (long) mv << 31 & HI_MASK;
    }

    public static int size(long list) {
        return (int) (list >> 62 & 3L);
    }
}