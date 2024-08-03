package me.lucko.spark.api.gc;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface GarbageCollector {

    @NonNull
    String name();

    long totalCollections();

    long totalTime();

    double avgTime();

    long avgFrequency();
}