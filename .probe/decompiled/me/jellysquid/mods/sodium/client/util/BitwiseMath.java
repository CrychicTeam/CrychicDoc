package me.jellysquid.mods.sodium.client.util;

public class BitwiseMath {

    public static int lessThan(int a, int b) {
        return a - b >>> 31;
    }

    public static int greaterThan(int a, int b) {
        return b - a >>> 31;
    }
}