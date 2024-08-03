package net.minecraft.core;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;

public class RegistryCodecs {

    private static <T> MapCodec<RegistryCodecs.RegistryEntry<T>> withNameAndId(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, MapCodec<T> mapCodecT1) {
        return RecordCodecBuilder.mapCodec(p_206309_ -> p_206309_.group(ResourceKey.codec(resourceKeyExtendsRegistryT0).fieldOf("name").forGetter(RegistryCodecs.RegistryEntry::f_206354_), Codec.INT.fieldOf("id").forGetter(RegistryCodecs.RegistryEntry::f_206355_), mapCodecT1.forGetter(RegistryCodecs.RegistryEntry::f_206356_)).apply(p_206309_, RegistryCodecs.RegistryEntry::new));
    }

    public static <T> Codec<Registry<T>> networkCodec(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0, Lifecycle lifecycle1, Codec<T> codecT2) {
        return withNameAndId(resourceKeyExtendsRegistryT0, codecT2.fieldOf("element")).codec().listOf().xmap(p_258188_ -> {
            WritableRegistry<T> $$3 = new MappedRegistry<>(resourceKeyExtendsRegistryT0, lifecycle1);
            for (RegistryCodecs.RegistryEntry<T> $$4 : p_258188_) {
                $$3.registerMapping($$4.id(), $$4.key(), $$4.value(), lifecycle1);
            }
            return $$3;
        }, p_258185_ -> {
            Builder<RegistryCodecs.RegistryEntry<T>> $$1 = ImmutableList.builder();
            for (T $$2 : p_258185_) {
                $$1.add(new RegistryCodecs.RegistryEntry((ResourceKey<T>) p_258185_.getResourceKey($$2).get(), p_258185_.getId($$2), $$2));
            }
            return $$1.build();
        });
    }

    public static <E> Codec<Registry<E>> fullCodec(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Lifecycle lifecycle1, Codec<E> codecE2) {
        Codec<Map<ResourceKey<E>, E>> $$3 = Codec.unboundedMap(ResourceKey.codec(resourceKeyExtendsRegistryE0), codecE2);
        return $$3.xmap(p_258184_ -> {
            WritableRegistry<E> $$3x = new MappedRegistry<>(resourceKeyExtendsRegistryE0, lifecycle1);
            p_258184_.forEach((p_258191_, p_258192_) -> $$3x.register(p_258191_, (E) p_258192_, lifecycle1));
            return $$3x.m_203521_();
        }, p_258193_ -> ImmutableMap.copyOf(p_258193_.entrySet()));
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<E> codecE1) {
        return homogeneousList(resourceKeyExtendsRegistryE0, codecE1, false);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<E> codecE1, boolean boolean2) {
        return HolderSetCodec.create(resourceKeyExtendsRegistryE0, RegistryFileCodec.create(resourceKeyExtendsRegistryE0, codecE1), boolean2);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0) {
        return homogeneousList(resourceKeyExtendsRegistryE0, false);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, boolean boolean1) {
        return HolderSetCodec.create(resourceKeyExtendsRegistryE0, RegistryFixedCodec.create(resourceKeyExtendsRegistryE0), boolean1);
    }

    static record RegistryEntry<T>(ResourceKey<T> f_206354_, int f_206355_, T f_206356_) {

        private final ResourceKey<T> key;

        private final int id;

        private final T value;

        RegistryEntry(ResourceKey<T> f_206354_, int f_206355_, T f_206356_) {
            this.key = f_206354_;
            this.id = f_206355_;
            this.value = f_206356_;
        }
    }
}