package com.simibubi.create.foundation.utility;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class Couple<T> extends Pair<T, T> implements Iterable<T> {

    private static final Couple<Boolean> TRUE_AND_FALSE = create(true, false);

    protected Couple(T first, T second) {
        super(first, second);
    }

    public static <T> Couple<T> create(T first, T second) {
        return new Couple<>(first, second);
    }

    public static <T> Couple<T> create(Supplier<T> factory) {
        return new Couple<>((T) factory.get(), (T) factory.get());
    }

    public static <T> Couple<T> createWithContext(Function<Boolean, T> factory) {
        return new Couple<>((T) factory.apply(true), (T) factory.apply(false));
    }

    public T get(boolean first) {
        return first ? this.getFirst() : this.getSecond();
    }

    public void set(boolean first, T value) {
        if (first) {
            this.setFirst(value);
        } else {
            this.setSecond(value);
        }
    }

    public Couple<T> copy() {
        return create(this.first, this.second);
    }

    public <S> Couple<S> map(Function<T, S> function) {
        return (Couple<S>) create(function.apply(this.first), function.apply(this.second));
    }

    public <S> Couple<S> mapWithContext(BiFunction<T, Boolean, S> function) {
        return (Couple<S>) create(function.apply(this.first, true), function.apply(this.second, false));
    }

    public <S, R> Couple<S> mapWithParams(BiFunction<T, R, S> function, Couple<R> values) {
        return (Couple<S>) create(function.apply(this.first, values.first), function.apply(this.second, values.second));
    }

    public <S, R> Couple<S> mapNotNullWithParam(BiFunction<T, R, S> function, R value) {
        return (Couple<S>) create(this.first != null ? function.apply(this.first, value) : null, this.second != null ? function.apply(this.second, value) : null);
    }

    public boolean both(Predicate<T> test) {
        return test.test(this.getFirst()) && test.test(this.getSecond());
    }

    public boolean either(Predicate<T> test) {
        return test.test(this.getFirst()) || test.test(this.getSecond());
    }

    public void replace(Function<T, T> function) {
        this.setFirst((T) function.apply(this.getFirst()));
        this.setSecond((T) function.apply(this.getSecond()));
    }

    public void replaceWithContext(BiFunction<T, Boolean, T> function) {
        this.replaceWithParams(function, TRUE_AND_FALSE);
    }

    public <S> void replaceWithParams(BiFunction<T, S, T> function, Couple<S> values) {
        this.setFirst((T) function.apply(this.getFirst(), values.getFirst()));
        this.setSecond((T) function.apply(this.getSecond(), values.getSecond()));
    }

    public void forEach(Consumer<? super T> consumer) {
        consumer.accept(this.getFirst());
        consumer.accept(this.getSecond());
    }

    public void forEachWithContext(BiConsumer<T, Boolean> consumer) {
        this.forEachWithParams(consumer, TRUE_AND_FALSE);
    }

    public <S> void forEachWithParams(BiConsumer<T, S> function, Couple<S> values) {
        function.accept(this.getFirst(), values.getFirst());
        function.accept(this.getSecond(), values.getSecond());
    }

    public Couple<T> swap() {
        return create(this.second, this.first);
    }

    public ListTag serializeEach(Function<T, CompoundTag> serializer) {
        return NBTHelper.writeCompoundList(ImmutableList.of(this.first, this.second), serializer);
    }

    public static <S> Couple<S> deserializeEach(ListTag list, Function<CompoundTag, S> deserializer) {
        List<S> readCompoundList = NBTHelper.readCompoundList(list, (Function<CompoundTag, T>) deserializer);
        return (Couple<S>) (new Couple<>(readCompoundList.get(0), readCompoundList.get(1)));
    }

    public Iterator<T> iterator() {
        return new Couple.Couplerator<>(this);
    }

    public Stream<T> stream() {
        return Stream.of(this.first, this.second);
    }

    private static class Couplerator<T> implements Iterator<T> {

        int state;

        private final Couple<T> couple;

        public Couplerator(Couple<T> couple) {
            this.couple = couple;
            this.state = 0;
        }

        public boolean hasNext() {
            return this.state != 2;
        }

        public T next() {
            this.state++;
            if (this.state == 1) {
                return this.couple.first;
            } else {
                return this.state == 2 ? this.couple.second : null;
            }
        }
    }
}