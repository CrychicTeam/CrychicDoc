package me.lucko.spark.api;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class SparkProvider {

    private static Spark instance;

    @NonNull
    public static Spark get() {
        Spark instance = SparkProvider.instance;
        if (instance == null) {
            throw new IllegalStateException("spark has not loaded yet!");
        } else {
            return instance;
        }
    }

    static void set(Spark impl) {
        instance = impl;
    }

    private SparkProvider() {
        throw new AssertionError();
    }
}