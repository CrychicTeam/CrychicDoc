package com.cupboard.util;

public class MathUtil {

    public static int limitToMinMax(int value, int min, int max) {
        return Math.min(max, Math.max(min, value));
    }

    public static int limitToMax(int value, int max) {
        return Math.min(value, max);
    }

    public static int limitToMin(int value, int min) {
        return Math.min(value, min);
    }

    public static double minMax(double value, double min, double max) {
        return Math.min(max, Math.max(min, value));
    }

    public static double limitToMax(double value, double max) {
        return Math.min(value, max);
    }

    public static double limitToMin(double value, double min) {
        return Math.max(value, min);
    }
}