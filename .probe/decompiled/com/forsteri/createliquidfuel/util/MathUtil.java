package com.forsteri.createliquidfuel.util;

public class MathUtil {

    public static int gcd(int a, int b) {
        return a == 0 ? b : gcd(b % a, a);
    }
}