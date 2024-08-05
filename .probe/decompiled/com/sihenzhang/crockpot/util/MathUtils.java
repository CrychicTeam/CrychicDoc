package com.sihenzhang.crockpot.util;

public final class MathUtils {

    public static boolean fuzzyEquals(double a, double b) {
        return Math.abs(a - b) * 1.0E12 <= Math.min(Math.abs(a), Math.abs(b));
    }

    public static boolean fuzzyEquals(float a, float b) {
        return Math.abs(a - b) * 100000.0F <= Math.min(Math.abs(a), Math.abs(b));
    }

    public static boolean fuzzyIsZero(double d) {
        return Math.abs(d) <= 1.0E-12;
    }

    public static boolean fuzzyIsZero(float f) {
        return Math.abs(f) <= 1.0E-5F;
    }
}