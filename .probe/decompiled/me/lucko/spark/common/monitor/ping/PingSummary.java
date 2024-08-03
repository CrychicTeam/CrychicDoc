package me.lucko.spark.common.monitor.ping;

import java.util.Arrays;

public final class PingSummary {

    private final int[] values;

    private final int total;

    private final int max;

    private final int min;

    private final double mean;

    public PingSummary(int[] values) {
        Arrays.sort(values);
        this.values = values;
        int total = 0;
        for (int value : values) {
            total += value;
        }
        this.total = total;
        this.mean = (double) total / (double) values.length;
        this.max = values[values.length - 1];
        this.min = values[0];
    }

    public int total() {
        return this.total;
    }

    public double mean() {
        return this.mean;
    }

    public int max() {
        return this.max;
    }

    public int min() {
        return this.min;
    }

    public int percentile(double percentile) {
        if (!(percentile < 0.0) && !(percentile > 1.0)) {
            int rank = (int) Math.ceil(percentile * (double) (this.values.length - 1));
            return this.values[rank];
        } else {
            throw new IllegalArgumentException("Invalid percentile " + percentile);
        }
    }

    public double median() {
        return (double) this.percentile(0.5);
    }

    public double percentile95th() {
        return (double) this.percentile(0.95);
    }
}