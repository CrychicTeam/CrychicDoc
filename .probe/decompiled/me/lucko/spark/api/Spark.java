package me.lucko.spark.api;

import java.util.Map;
import me.lucko.spark.api.gc.GarbageCollector;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public interface Spark {

    @NonNull
    DoubleStatistic<StatisticWindow.CpuUsage> cpuProcess();

    @NonNull
    DoubleStatistic<StatisticWindow.CpuUsage> cpuSystem();

    @Nullable
    DoubleStatistic<StatisticWindow.TicksPerSecond> tps();

    @Nullable
    GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt();

    @NonNull
    @Unmodifiable
    Map<String, GarbageCollector> gc();
}