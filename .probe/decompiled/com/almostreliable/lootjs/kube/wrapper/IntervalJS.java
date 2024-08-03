package com.almostreliable.lootjs.kube.wrapper;

import java.util.List;
import net.minecraft.advancements.critereon.MinMaxBounds;

public class IntervalJS {

    private MinMaxBounds.Doubles bound = MinMaxBounds.Doubles.ANY;

    public IntervalJS() {
    }

    private IntervalJS(MinMaxBounds.Doubles bound) {
        this.bound = new MinMaxBounds.Doubles((Double) bound.m_55305_(), (Double) bound.m_55326_());
    }

    public static MinMaxBounds.Doubles ofDoubles(Object o) {
        if (o instanceof List<?> list) {
            if (list.size() == 1) {
                return ofDoubles(list.get(0));
            }
            if (list.size() == 2) {
                Object min = list.get(0);
                Object max = list.get(1);
                if (min instanceof Number minN && max instanceof Number maxN) {
                    return MinMaxBounds.Doubles.between(minN.doubleValue(), maxN.doubleValue());
                }
            }
        }
        if (o instanceof Number) {
            return MinMaxBounds.Doubles.atLeast(((Number) o).doubleValue());
        } else if (o instanceof MinMaxBounds<? extends Number> minMaxBounds) {
            Double min = minMaxBounds.getMin() != null ? minMaxBounds.getMin().doubleValue() : null;
            Double max = minMaxBounds.getMax() != null ? minMaxBounds.getMax().doubleValue() : null;
            return new MinMaxBounds.Doubles(min, max);
        } else if (o instanceof IntervalJS) {
            return ((IntervalJS) o).getVanillaDoubles();
        } else {
            throw new IllegalArgumentException("Argument is not a MinMaxBound");
        }
    }

    public static MinMaxBounds.Ints ofInt(Object o) {
        if (o instanceof MinMaxBounds.Ints) {
            return (MinMaxBounds.Ints) o;
        } else {
            MinMaxBounds.Doubles doubles = ofDoubles(o);
            Integer min = doubles.m_55305_() != null ? ((Double) doubles.m_55305_()).intValue() : null;
            Integer max = doubles.m_55326_() != null ? ((Double) doubles.m_55326_()).intValue() : null;
            return new MinMaxBounds.Ints(min, max);
        }
    }

    public boolean matches(double value) {
        return this.bound.matches(value);
    }

    public boolean matchesSqr(double value) {
        return this.bound.matchesSqr(value);
    }

    public IntervalJS between(double min, double max) {
        return new IntervalJS().min(min).max(max);
    }

    public IntervalJS min(double min) {
        return new IntervalJS(new MinMaxBounds.Doubles(min, (Double) this.bound.m_55326_()));
    }

    public IntervalJS max(double max) {
        return new IntervalJS(new MinMaxBounds.Doubles((Double) this.bound.m_55305_(), max));
    }

    public MinMaxBounds.Ints getVanillaInt() {
        Integer intMin = this.bound.m_55305_() == null ? null : ((Double) this.bound.m_55305_()).intValue();
        Integer intMax = this.bound.m_55326_() == null ? null : ((Double) this.bound.m_55326_()).intValue();
        return new MinMaxBounds.Ints(intMin, intMax);
    }

    public MinMaxBounds.Doubles getVanillaDoubles() {
        return new MinMaxBounds.Doubles((Double) this.bound.m_55305_(), (Double) this.bound.m_55326_());
    }

    public String toString() {
        String minStr = this.bound.m_55305_() == null ? "-∞" : String.valueOf(this.bound.m_55305_());
        String maxStr = this.bound.m_55326_() == null ? "∞" : String.valueOf(this.bound.m_55326_());
        return "Interval[" + minStr + ";" + maxStr + "]";
    }
}