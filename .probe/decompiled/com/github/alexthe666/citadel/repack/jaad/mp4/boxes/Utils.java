package com.github.alexthe666.citadel.repack.jaad.mp4.boxes;

public final class Utils {

    private static final long UNDETERMINED = 4294967295L;

    public static String getLanguageCode(long l) {
        char[] c = new char[] { (char) ((int) ((l >> 10 & 31L) + 96L)), (char) ((int) ((l >> 5 & 31L) + 96L)), (char) ((int) ((l & 31L) + 96L)) };
        return new String(c);
    }

    public static long detectUndetermined(long l) {
        long x;
        if (l == 4294967295L) {
            x = -1L;
        } else {
            x = l;
        }
        return x;
    }
}