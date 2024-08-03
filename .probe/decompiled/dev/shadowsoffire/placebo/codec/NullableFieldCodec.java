package dev.shadowsoffire.placebo.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.OptionalFieldCodec;
import java.util.Optional;

public class NullableFieldCodec<A> extends OptionalFieldCodec<A> {

    private final String name;

    private final Codec<A> elementCodec;

    protected NullableFieldCodec(String name, Codec<A> elementCodec) {
        super(name, elementCodec);
        this.name = name;
        this.elementCodec = elementCodec;
    }

    public <T> DataResult<Optional<A>> decode(DynamicOps<T> ops, MapLike<T> input) {
        T value = (T) input.get(this.name);
        return value == null ? DataResult.success(Optional.empty()) : this.elementCodec.parse(ops, value).map(Optional::of);
    }
}