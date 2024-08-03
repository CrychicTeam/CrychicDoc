package net.minecraftforge.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.codecs.BaseMapCodec;
import java.util.Map;
import java.util.Objects;

public class LenientUnboundedMapCodec<K, V> implements BaseMapCodec<K, V>, Codec<Map<K, V>> {

    private final Codec<K> keyCodec;

    private final Codec<V> elementCodec;

    public LenientUnboundedMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
        this.keyCodec = keyCodec;
        this.elementCodec = elementCodec;
    }

    public Codec<K> keyCodec() {
        return this.keyCodec;
    }

    public Codec<V> elementCodec() {
        return this.elementCodec;
    }

    public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
        Builder<K, V> read = ImmutableMap.builder();
        com.google.common.collect.ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();
        DataResult<Unit> result = (DataResult<Unit>) input.entries().reduce(DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (r, pair) -> {
            DataResult<K> k = this.keyCodec().parse(ops, pair.getFirst());
            DataResult<V> v = this.elementCodec().parse(ops, pair.getSecond());
            DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
            entry.error().ifPresent(e -> failed.add(pair));
            entry.result().ifPresent(e -> read.put(e.getFirst(), e.getSecond()));
            return r.apply2stable((u, p) -> u, entry);
        }, (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2));
        Map<K, V> elements = read.build();
        T errors = (T) ops.createMap(failed.build().stream());
        return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
    }

    public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> this.decode(ops, map)).map(r -> Pair.of(r, input));
    }

    public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
        return this.encode(input, ops, ops.mapBuilder()).build(prefix);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            LenientUnboundedMapCodec<?, ?> that = (LenientUnboundedMapCodec<?, ?>) o;
            return Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
    }

    public String toString() {
        return "LenientUnboundedMapCodec[" + this.keyCodec + " -> " + this.elementCodec + "]";
    }
}