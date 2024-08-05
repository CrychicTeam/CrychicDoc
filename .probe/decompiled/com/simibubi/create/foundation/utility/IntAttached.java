package com.simibubi.create.foundation.utility;

import java.util.Comparator;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class IntAttached<V> extends Pair<Integer, V> {

    protected IntAttached(Integer first, V second) {
        super(first, second);
    }

    public static <V> IntAttached<V> with(int number, V value) {
        return new IntAttached<>(number, value);
    }

    public static <V> IntAttached<V> withZero(V value) {
        return new IntAttached<>(0, value);
    }

    public boolean isZero() {
        return this.first == 0;
    }

    public boolean exceeds(int value) {
        return this.first > value;
    }

    public boolean isOrBelowZero() {
        return this.first <= 0;
    }

    public void increment() {
        Integer var1 = this.first;
        this.first = this.first + 1;
    }

    public void decrement() {
        Integer var1 = this.first;
        this.first = this.first - 1;
    }

    public V getValue() {
        return this.getSecond();
    }

    public CompoundTag serializeNBT(Function<V, CompoundTag> serializer) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Item", (Tag) serializer.apply(this.getValue()));
        nbt.putInt("Location", this.getFirst());
        return nbt;
    }

    public static Comparator<? super IntAttached<?>> comparator() {
        return (i1, i2) -> Integer.compare(i2.getFirst(), i1.getFirst());
    }

    public static <T> IntAttached<T> read(CompoundTag nbt, Function<CompoundTag, T> deserializer) {
        return (IntAttached<T>) with(nbt.getInt("Location"), deserializer.apply(nbt.getCompound("Item")));
    }
}