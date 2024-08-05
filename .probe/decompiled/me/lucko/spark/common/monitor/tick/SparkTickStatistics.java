package me.lucko.spark.common.monitor.tick;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.common.tick.TickReporter;
import me.lucko.spark.common.util.RollingAverage;

public class SparkTickStatistics implements TickHook.Callback, TickReporter.Callback, TickStatistics {

    private static final long SEC_IN_NANO = TimeUnit.SECONDS.toNanos(1L);

    private static final int TPS = 20;

    private static final int TPS_SAMPLE_INTERVAL = 20;

    private static final BigDecimal TPS_BASE = new BigDecimal(SEC_IN_NANO).multiply(new BigDecimal(20));

    private final SparkTickStatistics.TpsRollingAverage tps5Sec = new SparkTickStatistics.TpsRollingAverage(5);

    private final SparkTickStatistics.TpsRollingAverage tps10Sec = new SparkTickStatistics.TpsRollingAverage(10);

    private final SparkTickStatistics.TpsRollingAverage tps1Min = new SparkTickStatistics.TpsRollingAverage(60);

    private final SparkTickStatistics.TpsRollingAverage tps5Min = new SparkTickStatistics.TpsRollingAverage(300);

    private final SparkTickStatistics.TpsRollingAverage tps15Min = new SparkTickStatistics.TpsRollingAverage(900);

    private final SparkTickStatistics.TpsRollingAverage[] tpsAverages = new SparkTickStatistics.TpsRollingAverage[] { this.tps5Sec, this.tps10Sec, this.tps1Min, this.tps5Min, this.tps15Min };

    private boolean durationSupported = false;

    private final RollingAverage tickDuration10Sec = new RollingAverage(200);

    private final RollingAverage tickDuration1Min = new RollingAverage(1200);

    private final RollingAverage tickDuration5Min = new RollingAverage(6000);

    private final RollingAverage[] tickDurationAverages = new RollingAverage[] { this.tickDuration10Sec, this.tickDuration1Min, this.tickDuration5Min };

    private long last = 0L;

    @Override
    public boolean isDurationSupported() {
        return this.durationSupported;
    }

    @Override
    public void onTick(int currentTick) {
        if (currentTick % 20 == 0) {
            long now = System.nanoTime();
            if (this.last == 0L) {
                this.last = now;
            } else {
                long diff = now - this.last;
                BigDecimal currentTps = TPS_BASE.divide(new BigDecimal(diff), 30, RoundingMode.HALF_UP);
                BigDecimal total = currentTps.multiply(new BigDecimal(diff));
                for (SparkTickStatistics.TpsRollingAverage rollingAverage : this.tpsAverages) {
                    rollingAverage.add(currentTps, diff, total);
                }
                this.last = now;
            }
        }
    }

    @Override
    public void onTick(double duration) {
        this.durationSupported = true;
        BigDecimal decimal = new BigDecimal(duration);
        for (RollingAverage rollingAverage : this.tickDurationAverages) {
            rollingAverage.add(decimal);
        }
    }

    @Override
    public double tps5Sec() {
        return this.tps5Sec.getAverage();
    }

    @Override
    public double tps10Sec() {
        return this.tps10Sec.getAverage();
    }

    @Override
    public double tps1Min() {
        return this.tps1Min.getAverage();
    }

    @Override
    public double tps5Min() {
        return this.tps5Min.getAverage();
    }

    @Override
    public double tps15Min() {
        return this.tps15Min.getAverage();
    }

    @Override
    public DoubleAverageInfo duration10Sec() {
        return !this.durationSupported ? null : this.tickDuration10Sec;
    }

    @Override
    public DoubleAverageInfo duration1Min() {
        return !this.durationSupported ? null : this.tickDuration1Min;
    }

    @Override
    public DoubleAverageInfo duration5Min() {
        return !this.durationSupported ? null : this.tickDuration5Min;
    }

    public static final class TpsRollingAverage {

        private final int size;

        private long time;

        private BigDecimal total;

        private int index = 0;

        private final BigDecimal[] samples;

        private final long[] times;

        TpsRollingAverage(int size) {
            this.size = size;
            this.time = (long) size * SparkTickStatistics.SEC_IN_NANO;
            this.total = new BigDecimal(20).multiply(new BigDecimal(SparkTickStatistics.SEC_IN_NANO)).multiply(new BigDecimal(size));
            this.samples = new BigDecimal[size];
            this.times = new long[size];
            for (int i = 0; i < size; i++) {
                this.samples[i] = new BigDecimal(20);
                this.times[i] = SparkTickStatistics.SEC_IN_NANO;
            }
        }

        public void add(BigDecimal x, long t, BigDecimal total) {
            this.time = this.time - this.times[this.index];
            this.total = this.total.subtract(this.samples[this.index].multiply(new BigDecimal(this.times[this.index])));
            this.samples[this.index] = x;
            this.times[this.index] = t;
            this.time += t;
            this.total = this.total.add(total);
            if (++this.index == this.size) {
                this.index = 0;
            }
        }

        public double getAverage() {
            return this.total.divide(new BigDecimal(this.time), 30, RoundingMode.HALF_UP).doubleValue();
        }
    }
}