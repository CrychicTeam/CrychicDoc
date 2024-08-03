package fr.frinn.custommachinery.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.function.Function;
import java.util.stream.Stream;

public class EnhancedDispatchCodec<K, V> extends NamedMapCodec<V> {

    private final String typeKey;

    private final NamedCodec<K> keyCodec;

    private final Function<? super V, ? extends DataResult<? extends K>> type;

    private final String valueKey = "value";

    private final Function<? super K, ? extends DataResult<? extends NamedCodec<? extends V>>> valueCodec;

    private final String name;

    public static <K, V> EnhancedDispatchCodec<K, V> of(String typeKey, NamedCodec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends NamedCodec<? extends V>>> decoder, String name) {
        return new EnhancedDispatchCodec<>(typeKey, keyCodec, type, decoder, name);
    }

    private EnhancedDispatchCodec(String typeKey, NamedCodec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends NamedCodec<? extends V>>> valueCodec, String name) {
        this.typeKey = typeKey;
        this.keyCodec = keyCodec;
        this.type = type;
        this.valueCodec = valueCodec;
        this.name = name;
    }

    public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
        T elementName = (T) input.get(this.typeKey);
        if (elementName == null) {
            for (String alias : this.aliases) {
                elementName = (T) input.get(alias);
                if (elementName != null) {
                    break;
                }
            }
        }
        return elementName == null ? DataResult.error(() -> "Input does not contain a key [" + this.typeKey + "]: " + input) : this.keyCodec.decode(ops, elementName).flatMap(type -> {
            DataResult<? extends NamedCodec<? extends V>> elementDecoder = (DataResult<? extends NamedCodec<? extends V>>) this.valueCodec.apply(type.getFirst());
            return elementDecoder.flatMap(c -> {
                if (ops.compressMaps()) {
                    T value = (T) input.get(ops.createString("value"));
                    return value == null ? DataResult.error(() -> "Input does not have a \"value\" entry: " + input) : c.read(ops, value).map(Function.identity());
                } else {
                    return c.decode(ops, ops.createMap(input.entries())).map(Pair::getFirst);
                }
            });
        });
    }

    public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        DataResult<? extends NamedCodec<V>> elementEncoder = getCodec(this.type, this.valueCodec, input);
        RecordBuilder<T> builder = prefix.withErrorsFrom(elementEncoder);
        if (elementEncoder.result().isEmpty()) {
            return builder;
        } else {
            NamedCodec<V> c = (NamedCodec<V>) elementEncoder.result().get();
            if (ops.compressMaps()) {
                return prefix.add(this.typeKey, ((DataResult) this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart(ops, (K) t))).add("value", c.encodeStart(ops, input));
            } else {
                T typeString = (T) ops.createString(this.typeKey);
                DataResult<T> result = c.encodeStart(ops, input);
                DataResult<MapLike<T>> element = result.flatMap(ops::getMap);
                return (RecordBuilder<T>) element.map(map -> {
                    prefix.add(typeString, ((DataResult) this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart(ops, (K) t)));
                    map.entries().forEach(pair -> {
                        if (!pair.getFirst().equals(typeString)) {
                            prefix.add(pair.getFirst(), pair.getSecond());
                        }
                    });
                    return prefix;
                }).result().orElseGet(() -> prefix.withErrorsFrom(element));
            }
        }
    }

    private static <K, V> DataResult<? extends NamedCodec<V>> getCodec(Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends NamedCodec<? extends V>>> valueCodec, V input) {
        return ((DataResult) type.apply(input)).flatMap(k -> ((DataResult) valueCodec.apply(k)).map(Function.identity())).map(c -> c);
    }

    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.of(this.typeKey, "value").map(ops::createString);
    }

    @Override
    public String name() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}