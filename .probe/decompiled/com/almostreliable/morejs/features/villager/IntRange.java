package com.almostreliable.morejs.features.villager;

import java.util.function.IntPredicate;
import net.minecraft.util.RandomSource;

public class IntRange implements IntPredicate {

    private final int min;

    private final int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntRange(int level) {
        this(level, level);
    }

    public static IntRange all() {
        return new IntRange(0, Integer.MAX_VALUE);
    }

    public boolean test(int value) {
        return this.min <= value && value <= this.max;
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public int getRandom(RandomSource random) {
        return this.min == this.max ? this.min : random.nextIntBetweenInclusive(this.min, this.max);
    }
}