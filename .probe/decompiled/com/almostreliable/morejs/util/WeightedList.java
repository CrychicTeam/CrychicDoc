package com.almostreliable.morejs.util;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.util.RandomSource;

public class WeightedList<T> {

    private static final RandomSource RANDOM = RandomSource.create();

    private final List<WeightedList.Entry<T>> entries;

    private final int totalWeight;

    private WeightedList(List<WeightedList.Entry<T>> entries) {
        Preconditions.checkNotNull(entries, "entries are null");
        Preconditions.checkArgument(!entries.isEmpty(), "entries cannot be empty");
        this.entries = entries;
        this.totalWeight = entries.stream().mapToInt(WeightedList.Entry::weight).sum();
    }

    public T roll() {
        return this.roll(RANDOM);
    }

    public T roll(RandomSource random) {
        int i = random.nextInt(this.totalWeight);
        for (WeightedList.Entry<T> e : this.entries) {
            i -= e.weight;
            if (i < 0) {
                return e.value;
            }
        }
        throw new IllegalStateException("Rolled past end of list");
    }

    public <T2> WeightedList<T2> map(Function<T, T2> mapper) {
        List<WeightedList.Entry<T2>> newEntries = new ArrayList(this.entries.size());
        for (WeightedList.Entry<T> entry : this.entries) {
            T2 newValue = (T2) mapper.apply(entry.value);
            if (newValue != null) {
                newEntries.add(new WeightedList.Entry<>(entry.weight, newValue));
            }
        }
        return new WeightedList((List<WeightedList.Entry<T>>) newEntries);
    }

    public static class Builder<T> {

        private final List<WeightedList.Entry<T>> entries = new ArrayList();

        public WeightedList.Builder<T> add(int weight, T value) {
            this.entries.add(new WeightedList.Entry(weight, value));
            return this;
        }

        public WeightedList<T> build() {
            return new WeightedList<>(this.entries);
        }
    }

    private static record Entry<T>(int weight, T value) {
    }
}