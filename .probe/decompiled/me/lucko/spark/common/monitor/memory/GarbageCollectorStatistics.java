package me.lucko.spark.common.monitor.memory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Map;

public class GarbageCollectorStatistics {

    public static final GarbageCollectorStatistics ZERO = new GarbageCollectorStatistics(0L, 0L);

    private final long collectionCount;

    private final long collectionTime;

    public static Map<String, GarbageCollectorStatistics> pollStats() {
        Builder<String, GarbageCollectorStatistics> stats = ImmutableMap.builder();
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            stats.put(bean.getName(), new GarbageCollectorStatistics(bean));
        }
        return stats.build();
    }

    public static Map<String, GarbageCollectorStatistics> pollStatsSubtractInitial(Map<String, GarbageCollectorStatistics> initial) {
        Builder<String, GarbageCollectorStatistics> stats = ImmutableMap.builder();
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            stats.put(bean.getName(), new GarbageCollectorStatistics(bean).subtract((GarbageCollectorStatistics) initial.getOrDefault(bean.getName(), ZERO)));
        }
        return stats.build();
    }

    public GarbageCollectorStatistics(long collectionCount, long collectionTime) {
        this.collectionCount = collectionCount;
        this.collectionTime = collectionTime;
    }

    public GarbageCollectorStatistics(GarbageCollectorMXBean bean) {
        this(bean.getCollectionCount(), bean.getCollectionTime());
    }

    public long getCollectionCount() {
        return this.collectionCount;
    }

    public long getCollectionTime() {
        return this.collectionTime;
    }

    public double getAverageCollectionTime() {
        return this.collectionCount == 0L ? 0.0 : (double) this.collectionTime / (double) this.collectionCount;
    }

    public long getAverageCollectionFrequency(long serverUptime) {
        return this.collectionCount == 0L ? 0L : (long) (((double) serverUptime - (double) this.collectionTime) / (double) this.collectionCount);
    }

    public GarbageCollectorStatistics subtract(GarbageCollectorStatistics other) {
        return other != ZERO && (other.collectionCount != 0L || other.collectionTime != 0L) ? new GarbageCollectorStatistics(this.collectionCount - other.collectionCount, this.collectionTime - other.collectionTime) : this;
    }
}