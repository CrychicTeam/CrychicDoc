package dev.latvian.mods.kubejs.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public class TickTemporalUnit implements TemporalUnit {

    public static final TickTemporalUnit INSTANCE = new TickTemporalUnit();

    public static final Duration DURATION = Duration.ofMillis(50L);

    public Duration getDuration() {
        return DURATION;
    }

    public boolean isDurationEstimated() {
        return false;
    }

    public boolean isDateBased() {
        return false;
    }

    public boolean isTimeBased() {
        return true;
    }

    public <R extends Temporal> R addTo(R temporal, long amount) {
        return (R) temporal.plus(amount * 50L, ChronoUnit.MILLIS);
    }

    public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
        return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.MILLIS) / 50L;
    }

    public String toString() {
        return "Ticks";
    }
}