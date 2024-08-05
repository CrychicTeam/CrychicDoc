package me.lucko.spark.common.monitor.tick;

import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;

public interface TickStatistics {

    double tps5Sec();

    double tps10Sec();

    double tps1Min();

    double tps5Min();

    double tps15Min();

    boolean isDurationSupported();

    DoubleAverageInfo duration10Sec();

    DoubleAverageInfo duration1Min();

    DoubleAverageInfo duration5Min();
}