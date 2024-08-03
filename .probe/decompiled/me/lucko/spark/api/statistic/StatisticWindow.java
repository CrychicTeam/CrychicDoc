package me.lucko.spark.api.statistic;

import java.time.Duration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public interface StatisticWindow {

    @NonNull
    Duration length();

    public static enum CpuUsage implements StatisticWindow {

        SECONDS_10(Duration.ofSeconds(10L)), MINUTES_1(Duration.ofMinutes(1L)), MINUTES_15(Duration.ofMinutes(15L));

        private final Duration value;

        private CpuUsage(Duration value) {
            this.value = value;
        }

        @NotNull
        @Override
        public Duration length() {
            return this.value;
        }
    }

    public static enum MillisPerTick implements StatisticWindow {

        SECONDS_10(Duration.ofSeconds(10L)), MINUTES_1(Duration.ofMinutes(1L)), MINUTES_5(Duration.ofMinutes(5L));

        private final Duration value;

        private MillisPerTick(Duration value) {
            this.value = value;
        }

        @NotNull
        @Override
        public Duration length() {
            return this.value;
        }
    }

    public static enum TicksPerSecond implements StatisticWindow {

        SECONDS_5(Duration.ofSeconds(5L)), SECONDS_10(Duration.ofSeconds(10L)), MINUTES_1(Duration.ofMinutes(1L)), MINUTES_5(Duration.ofMinutes(5L)), MINUTES_15(Duration.ofMinutes(15L));

        private final Duration value;

        private TicksPerSecond(Duration value) {
            this.value = value;
        }

        @NotNull
        @Override
        public Duration length() {
            return this.value;
        }
    }
}