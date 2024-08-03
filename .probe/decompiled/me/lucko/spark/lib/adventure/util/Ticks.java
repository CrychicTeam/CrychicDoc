package me.lucko.spark.lib.adventure.util;

import java.time.Duration;
import org.jetbrains.annotations.NotNull;

public interface Ticks {

    int TICKS_PER_SECOND = 20;

    long SINGLE_TICK_DURATION_MS = 50L;

    @NotNull
    static Duration duration(final long ticks) {
        return Duration.ofMillis(ticks * 50L);
    }
}