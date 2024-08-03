package me.lucko.spark.api.statistic.misc;

public interface DoubleAverageInfo {

    double mean();

    double max();

    double min();

    default double median() {
        return this.percentile(0.5);
    }

    default double percentile95th() {
        return this.percentile(0.95);
    }

    double percentile(double var1);
}