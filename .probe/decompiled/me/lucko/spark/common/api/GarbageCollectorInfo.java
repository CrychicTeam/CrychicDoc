package me.lucko.spark.common.api;

import me.lucko.spark.api.gc.GarbageCollector;
import me.lucko.spark.common.monitor.memory.GarbageCollectorStatistics;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GarbageCollectorInfo implements GarbageCollector {

    private final String name;

    private final long totalCollections;

    private final long totalTime;

    private final double averageTime;

    private final long averageFrequency;

    public GarbageCollectorInfo(String name, GarbageCollectorStatistics stats, long serverUptime) {
        this.name = name;
        this.totalCollections = stats.getCollectionCount();
        this.totalTime = stats.getCollectionTime();
        this.averageTime = stats.getAverageCollectionTime();
        this.averageFrequency = stats.getAverageCollectionFrequency(serverUptime);
    }

    @NonNull
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public long totalCollections() {
        return this.totalCollections;
    }

    @Override
    public long totalTime() {
        return this.totalTime;
    }

    @Override
    public double avgTime() {
        return this.averageTime;
    }

    @Override
    public long avgFrequency() {
        return this.averageFrequency;
    }
}