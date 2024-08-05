package me.lucko.spark.lib.adventure.util;

@FunctionalInterface
public interface IntFunction2<R> {

    R apply(final int first, final int second);
}