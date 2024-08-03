package me.jellysquid.mods.sodium.client.util;

public class MathUtil {

    public static boolean isPowerOfTwo(int n) {
        return (n & n - 1) == 0;
    }

    public static long toMib(long bytes) {
        return bytes / 1048576L;
    }
}