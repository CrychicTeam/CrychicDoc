package me.lucko.spark.common.api;

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.gc.GarbageCollector;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.monitor.cpu.CpuMonitor;
import me.lucko.spark.common.monitor.memory.GarbageCollectorStatistics;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SparkApi implements Spark {

    private static final Method SINGLETON_SET_METHOD;

    private final SparkPlatform platform;

    public SparkApi(SparkPlatform platform) {
        this.platform = platform;
    }

    @NonNull
    @Override
    public DoubleStatistic<StatisticWindow.CpuUsage> cpuProcess() {
        return new AbstractStatistic.Double<StatisticWindow.CpuUsage>("CPU Process Usage", StatisticWindow.CpuUsage.class) {

            public double poll(@NonNull StatisticWindow.CpuUsage window) {
                switch(window) {
                    case SECONDS_10:
                        return CpuMonitor.processLoad10SecAvg();
                    case MINUTES_1:
                        return CpuMonitor.processLoad1MinAvg();
                    case MINUTES_15:
                        return CpuMonitor.processLoad15MinAvg();
                    default:
                        throw new AssertionError(window);
                }
            }
        };
    }

    @NonNull
    @Override
    public DoubleStatistic<StatisticWindow.CpuUsage> cpuSystem() {
        return new AbstractStatistic.Double<StatisticWindow.CpuUsage>("CPU System Usage", StatisticWindow.CpuUsage.class) {

            public double poll(@NonNull StatisticWindow.CpuUsage window) {
                switch(window) {
                    case SECONDS_10:
                        return CpuMonitor.systemLoad10SecAvg();
                    case MINUTES_1:
                        return CpuMonitor.systemLoad1MinAvg();
                    case MINUTES_15:
                        return CpuMonitor.systemLoad15MinAvg();
                    default:
                        throw new AssertionError(window);
                }
            }
        };
    }

    @Nullable
    @Override
    public DoubleStatistic<StatisticWindow.TicksPerSecond> tps() {
        final TickStatistics stats = this.platform.getTickStatistics();
        return stats == null ? null : new AbstractStatistic.Double<StatisticWindow.TicksPerSecond>("Ticks Per Second", StatisticWindow.TicksPerSecond.class) {

            public double poll(@NonNull StatisticWindow.TicksPerSecond window) {
                switch(window) {
                    case SECONDS_5:
                        return stats.tps5Sec();
                    case SECONDS_10:
                        return stats.tps10Sec();
                    case MINUTES_1:
                        return stats.tps1Min();
                    case MINUTES_5:
                        return stats.tps5Min();
                    case MINUTES_15:
                        return stats.tps15Min();
                    default:
                        throw new AssertionError(window);
                }
            }
        };
    }

    @Nullable
    @Override
    public GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt() {
        final TickStatistics stats = this.platform.getTickStatistics();
        return stats != null && stats.isDurationSupported() ? new AbstractStatistic.Generic<DoubleAverageInfo, StatisticWindow.MillisPerTick>("Milliseconds Per Tick", DoubleAverageInfo.class, StatisticWindow.MillisPerTick.class) {

            public DoubleAverageInfo poll(@NonNull StatisticWindow.MillisPerTick window) {
                switch(window) {
                    case SECONDS_10:
                        return stats.duration10Sec();
                    case MINUTES_1:
                        return stats.duration1Min();
                    case MINUTES_5:
                        return stats.duration5Min();
                    default:
                        throw new AssertionError(window);
                }
            }
        } : null;
    }

    @NonNull
    @Override
    public Map<String, GarbageCollector> gc() {
        long serverUptime = System.currentTimeMillis() - this.platform.getServerNormalOperationStartTime();
        Map<String, GarbageCollectorStatistics> stats = GarbageCollectorStatistics.pollStatsSubtractInitial(this.platform.getStartupGcStatistics());
        Map<String, GarbageCollector> map = new HashMap(stats.size());
        for (Entry<String, GarbageCollectorStatistics> entry : stats.entrySet()) {
            map.put((String) entry.getKey(), new GarbageCollectorInfo((String) entry.getKey(), (GarbageCollectorStatistics) entry.getValue(), serverUptime));
        }
        return ImmutableMap.copyOf(map);
    }

    public static void register(Spark spark) {
        try {
            SINGLETON_SET_METHOD.invoke(null, spark);
        } catch (ReflectiveOperationException var2) {
            var2.printStackTrace();
        }
    }

    public static void unregister() {
        try {
            SINGLETON_SET_METHOD.invoke(null, null);
        } catch (ReflectiveOperationException var1) {
            var1.printStackTrace();
        }
    }

    static {
        try {
            SINGLETON_SET_METHOD = SparkProvider.class.getDeclaredMethod("set", Spark.class);
            SINGLETON_SET_METHOD.setAccessible(true);
        } catch (ReflectiveOperationException var1) {
            throw new RuntimeException(var1);
        }
    }
}