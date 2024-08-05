package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public record KeyDispatchDataCodec<A>(Codec<A> f_216232_) {

    private final Codec<A> codec;

    public KeyDispatchDataCodec(Codec<A> f_216232_) {
        this.codec = f_216232_;
    }

    public static <A> KeyDispatchDataCodec<A> of(Codec<A> p_216237_) {
        return new KeyDispatchDataCodec<>(p_216237_);
    }

    public static <A> KeyDispatchDataCodec<A> of(MapCodec<A> p_216239_) {
        return new KeyDispatchDataCodec<>(p_216239_.codec());
    }
}