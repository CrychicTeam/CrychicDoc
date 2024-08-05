package net.minecraft.util.profiling.jfr.stats;

import java.time.Duration;
import java.time.Instant;
import jdk.jfr.consumer.RecordedEvent;

public record TickTimeStat(Instant f_185819_, Duration f_185820_) {

    private final Instant timestamp;

    private final Duration currentAverage;

    public TickTimeStat(Instant f_185819_, Duration f_185820_) {
        this.timestamp = f_185819_;
        this.currentAverage = f_185820_;
    }

    public static TickTimeStat from(RecordedEvent p_185826_) {
        return new TickTimeStat(p_185826_.getStartTime(), p_185826_.getDuration("averageTickDuration"));
    }
}