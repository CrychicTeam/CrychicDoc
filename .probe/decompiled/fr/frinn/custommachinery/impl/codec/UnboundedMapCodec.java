package fr.frinn.custommachinery.impl.codec;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.Map;
import java.util.Map.Entry;

public class UnboundedMapCodec<K, V> implements NamedCodec<Map<K, V>> {

    private final NamedCodec<K> keyCodec;

    private final NamedCodec<V> valueCodec;

    private final String name;

    public static <K, V> UnboundedMapCodec<K, V> of(NamedCodec<K> keyCodec, NamedCodec<V> valueCodec, String name) {
        return new UnboundedMapCodec<>(keyCodec, valueCodec, name);
    }

    private UnboundedMapCodec(NamedCodec<K> keyCodec, NamedCodec<V> valueCodec, String name) {
        this.keyCodec = keyCodec;
        this.valueCodec = valueCodec;
        this.name = name;
    }

    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getMap(input).flatMap(map -> {
            Builder<K, V> read = ImmutableMap.builder();
            com.google.common.collect.ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();
            DataResult<Unit> result = (DataResult<Unit>) map.entries().reduce(DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (r, pair) -> {
                DataResult<K> k = this.keyCodec.read(ops, pair.getFirst());
                DataResult<V> v = this.valueCodec.read(ops, pair.getSecond());
                DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
                entry.error().ifPresent(e -> failed.add(pair));
                return r.apply2stable((u, p) -> {
                    read.put(p.getFirst(), p.getSecond());
                    return u;
                }, entry);
            }, (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2));
            Map<K, V> elements = read.build();
            T errors = (T) ops.createMap(failed.build().stream());
            return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
        }).map(r -> Pair.of(r, input));
    }

    public <T> DataResult<T> encode(DynamicOps<T> ops, Map<K, V> input, T prefix) {
        RecordBuilder<T> builder = ops.mapBuilder();
        for (Entry<K, V> entry : input.entrySet()) {
            builder.add(this.keyCodec.encodeStart(ops, (K) entry.getKey()), this.valueCodec.encodeStart(ops, (V) entry.getValue()));
        }
        return builder.build(prefix);
    }

    @Override
    public String name() {
        return this.name;
    }
}