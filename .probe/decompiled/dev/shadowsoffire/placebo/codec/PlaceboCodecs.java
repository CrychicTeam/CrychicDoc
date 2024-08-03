package dev.shadowsoffire.placebo.codec;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

public class PlaceboCodecs {

    public static <T extends CodecProvider<T>> Codec<T> mapBackedDefaulted(String name, BiMap<ResourceLocation, Codec<? extends T>> reg, Codec<? extends T> defaultCodec) {
        return new MapBackedCodec<>(name, reg, () -> defaultCodec);
    }

    public static <T extends CodecProvider<? super T>> Codec<T> mapBacked(String name, BiMap<ResourceLocation, Codec<? extends T>> reg) {
        return new MapBackedCodec<>(name, reg);
    }

    public static <T> Codec<Set<T>> setOf(Codec<T> elementCodec) {
        return setFromList(elementCodec.listOf());
    }

    public static <T> Codec<Set<T>> setFromList(Codec<List<T>> listCodec) {
        return listCodec.xmap(HashSet::new, ArrayList::new);
    }

    public static <E extends Enum<E>> Codec<E> enumCodec(Class<E> clazz) {
        return ExtraCodecs.stringResolverCodec(e -> e.name().toLowerCase(Locale.ROOT), name -> Enum.valueOf(clazz, name.toUpperCase(Locale.ROOT)));
    }

    public static <T extends StringRepresentable> Codec<T> stringResolver(Function<String, T> decoder) {
        return ExtraCodecs.stringResolverCodec(StringRepresentable::m_7912_, decoder);
    }

    public static <A> MapCodec<Optional<A>> nullableField(Codec<A> elementCodec, String name) {
        return new NullableFieldCodec<Optional<A>>(name, elementCodec);
    }

    public static <A> MapCodec<A> nullableField(Codec<A> elementCodec, String name, A defaultValue) {
        return nullableField(elementCodec, name).xmap(o -> o.orElse(defaultValue), Optional::ofNullable);
    }
}