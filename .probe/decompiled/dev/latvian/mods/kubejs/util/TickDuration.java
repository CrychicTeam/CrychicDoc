package dev.latvian.mods.kubejs.util;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;

public record TickDuration(long ticks) implements TemporalAmount {

    public static final TickDuration ZERO = new TickDuration(0L);

    private static final List<TemporalUnit> UNITS = List.of(TickTemporalUnit.INSTANCE);

    public long get(TemporalUnit unit) {
        return 0L;
    }

    public List<TemporalUnit> getUnits() {
        return UNITS;
    }

    public Temporal addTo(Temporal temporal) {
        return this.ticks != 0L ? temporal.plus(this.ticks, TickTemporalUnit.INSTANCE) : temporal;
    }

    public Temporal subtractFrom(Temporal temporal) {
        return this.ticks != 0L ? temporal.minus(this.ticks, TickTemporalUnit.INSTANCE) : temporal;
    }
}