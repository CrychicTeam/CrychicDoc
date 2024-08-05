package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.util.RandomSource;

public class ShufflingList<U> implements Iterable<U> {

    protected final List<ShufflingList.WeightedEntry<U>> entries;

    private final RandomSource random = RandomSource.create();

    public ShufflingList() {
        this.entries = Lists.newArrayList();
    }

    private ShufflingList(List<ShufflingList.WeightedEntry<U>> listShufflingListWeightedEntryU0) {
        this.entries = Lists.newArrayList(listShufflingListWeightedEntryU0);
    }

    public static <U> Codec<ShufflingList<U>> codec(Codec<U> codecU0) {
        return ShufflingList.WeightedEntry.codec(codecU0).listOf().xmap(ShufflingList::new, p_147926_ -> p_147926_.entries);
    }

    public ShufflingList<U> add(U u0, int int1) {
        this.entries.add(new ShufflingList.WeightedEntry<>(u0, int1));
        return this;
    }

    public ShufflingList<U> shuffle() {
        this.entries.forEach(p_147924_ -> p_147924_.setRandom(this.random.nextFloat()));
        this.entries.sort(Comparator.comparingDouble(ShufflingList.WeightedEntry::m_147946_));
        return this;
    }

    public Stream<U> stream() {
        return this.entries.stream().map(ShufflingList.WeightedEntry::m_147940_);
    }

    public Iterator<U> iterator() {
        return Iterators.transform(this.entries.iterator(), ShufflingList.WeightedEntry::m_147940_);
    }

    public String toString() {
        return "ShufflingList[" + this.entries + "]";
    }

    public static class WeightedEntry<T> {

        final T data;

        final int weight;

        private double randWeight;

        WeightedEntry(T t0, int int1) {
            this.weight = int1;
            this.data = t0;
        }

        private double getRandWeight() {
            return this.randWeight;
        }

        void setRandom(float float0) {
            this.randWeight = -Math.pow((double) float0, (double) (1.0F / (float) this.weight));
        }

        public T getData() {
            return this.data;
        }

        public int getWeight() {
            return this.weight;
        }

        public String toString() {
            return this.weight + ":" + this.data;
        }

        public static <E> Codec<ShufflingList.WeightedEntry<E>> codec(final Codec<E> codecE0) {
            return new Codec<ShufflingList.WeightedEntry<E>>() {

                public <T> DataResult<Pair<ShufflingList.WeightedEntry<E>, T>> decode(DynamicOps<T> p_147962_, T p_147963_) {
                    Dynamic<T> $$2 = new Dynamic(p_147962_, p_147963_);
                    return $$2.get("data").flatMap(codecE0::parse).map(p_147957_ -> new ShufflingList.WeightedEntry<>(p_147957_, $$2.get("weight").asInt(1))).map(p_147960_ -> Pair.of(p_147960_, p_147962_.empty()));
                }

                public <T> DataResult<T> encode(ShufflingList.WeightedEntry<E> p_147952_, DynamicOps<T> p_147953_, T p_147954_) {
                    return p_147953_.mapBuilder().add("weight", p_147953_.createInt(p_147952_.weight)).add("data", codecE0.encodeStart(p_147953_, p_147952_.data)).build(p_147954_);
                }
            };
        }
    }
}