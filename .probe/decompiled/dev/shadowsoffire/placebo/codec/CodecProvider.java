package dev.shadowsoffire.placebo.codec;

import com.mojang.serialization.Codec;

public interface CodecProvider<T> {

    Codec<? extends T> getCodec();
}