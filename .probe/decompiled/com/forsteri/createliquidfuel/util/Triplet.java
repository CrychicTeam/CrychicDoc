package com.forsteri.createliquidfuel.util;

public record Triplet<T, U, V>(T getFirst, U getSecond, V getThird) {

    public static <T, U, V> Triplet<T, U, V> of(T first, U second, V third) {
        return new Triplet<>(first, second, third);
    }
}