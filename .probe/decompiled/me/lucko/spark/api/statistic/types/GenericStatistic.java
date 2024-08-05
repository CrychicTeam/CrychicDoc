package me.lucko.spark.api.statistic.types;

import me.lucko.spark.api.statistic.Statistic;
import me.lucko.spark.api.statistic.StatisticWindow;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface GenericStatistic<T, W extends Enum<W> & StatisticWindow> extends Statistic<W> {

    T poll(@NonNull W var1);

    T[] poll();
}