package net.minecraft.util.random;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

public class SimpleWeightedRandomList<E> extends WeightedRandomList<WeightedEntry.Wrapper<E>> {

    public static <E> Codec<SimpleWeightedRandomList<E>> wrappedCodecAllowingEmpty(Codec<E> codecE0) {
        return WeightedEntry.Wrapper.codec(codecE0).listOf().xmap(SimpleWeightedRandomList::new, WeightedRandomList::m_146338_);
    }

    public static <E> Codec<SimpleWeightedRandomList<E>> wrappedCodec(Codec<E> codecE0) {
        return ExtraCodecs.nonEmptyList(WeightedEntry.Wrapper.codec(codecE0).listOf()).xmap(SimpleWeightedRandomList::new, WeightedRandomList::m_146338_);
    }

    SimpleWeightedRandomList(List<? extends WeightedEntry.Wrapper<E>> listExtendsWeightedEntryWrapperE0) {
        super(listExtendsWeightedEntryWrapperE0);
    }

    public static <E> SimpleWeightedRandomList.Builder<E> builder() {
        return new SimpleWeightedRandomList.Builder<>();
    }

    public static <E> SimpleWeightedRandomList<E> empty() {
        return new SimpleWeightedRandomList<>(List.of());
    }

    public static <E> SimpleWeightedRandomList<E> single(E e0) {
        return new SimpleWeightedRandomList<>(List.of(WeightedEntry.wrap(e0, 1)));
    }

    public Optional<E> getRandomValue(RandomSource randomSource0) {
        return this.m_216829_(randomSource0).map(WeightedEntry.Wrapper::m_146310_);
    }

    public static class Builder<E> {

        private final com.google.common.collect.ImmutableList.Builder<WeightedEntry.Wrapper<E>> result = ImmutableList.builder();

        public SimpleWeightedRandomList.Builder<E> add(E e0, int int1) {
            this.result.add(WeightedEntry.wrap(e0, int1));
            return this;
        }

        public SimpleWeightedRandomList<E> build() {
            return new SimpleWeightedRandomList<>(this.result.build());
        }
    }
}