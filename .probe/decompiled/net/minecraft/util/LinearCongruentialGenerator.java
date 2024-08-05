package net.minecraft.util;

public class LinearCongruentialGenerator {

    private static final long MULTIPLIER = 6364136223846793005L;

    private static final long INCREMENT = 1442695040888963407L;

    public static long next(long long0, long long1) {
        long0 *= long0 * 6364136223846793005L + 1442695040888963407L;
        return long0 + long1;
    }
}