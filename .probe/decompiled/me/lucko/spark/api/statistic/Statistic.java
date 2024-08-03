package me.lucko.spark.api.statistic;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface Statistic<W extends Enum<W> & StatisticWindow> {

    @NonNull
    String name();

    W[] getWindows();
}