package dev.architectury.utils;

public class Amount {

    public static int toInt(long amount) {
        if (amount >= 2147483647L) {
            return Integer.MAX_VALUE;
        } else {
            return amount <= -2147483648L ? Integer.MIN_VALUE : (int) amount;
        }
    }
}