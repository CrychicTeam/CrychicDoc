package fr.frinn.custommachinery.api.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import dev.architectury.registry.registries.Registrar;
import fr.frinn.custommachinery.impl.codec.DefaultOptionalFieldCodec;
import fr.frinn.custommachinery.impl.codec.EnhancedDispatchCodec;
import fr.frinn.custommachinery.impl.codec.EnhancedEitherCodec;
import fr.frinn.custommachinery.impl.codec.EnhancedListCodec;
import fr.frinn.custommachinery.impl.codec.EnumCodec;
import fr.frinn.custommachinery.impl.codec.EnumMapCodec;
import fr.frinn.custommachinery.impl.codec.FieldCodec;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import fr.frinn.custommachinery.impl.codec.NamedRecordCodec;
import fr.frinn.custommachinery.impl.codec.NumberCodec;
import fr.frinn.custommachinery.impl.codec.OptionalFieldCodec;
import fr.frinn.custommachinery.impl.codec.PairCodec;
import fr.frinn.custommachinery.impl.codec.RegistrarCodec;
import fr.frinn.custommachinery.impl.codec.UnboundedMapCodec;
import io.netty.handler.codec.EncoderException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface NamedCodec<A> {

    NamedCodec<Boolean> BOOL = new NamedCodec<Boolean>() {

        @Override
        public <T> DataResult<Pair<Boolean, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<Boolean> result = ops.getBooleanValue(input);
            if (result.result().isPresent()) {
                return result.map(b -> Pair.of(b, ops.empty()));
            } else {
                DataResult<String> stringResult = ops.getStringValue(input);
                if (stringResult.result().isPresent()) {
                    String s = (String) stringResult.result().get();
                    if (s.equalsIgnoreCase("true")) {
                        return DataResult.success(Pair.of(true, ops.empty()));
                    }
                    if (s.equalsIgnoreCase("false")) {
                        return DataResult.success(Pair.of(false, ops.empty()));
                    }
                }
                return result.map(b -> Pair.of(b, input));
            }
        }

        public <T> DataResult<T> encode(DynamicOps<T> ops, Boolean input, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createBoolean(input));
        }

        @Override
        public String name() {
            return "Boolean";
        }
    };

    NamedCodec<Byte> BYTE = new NumberCodec<Byte>() {

        @Override
        public <T> DataResult<Byte> parse(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::byteValue);
        }

        public Byte fromString(String s) throws NumberFormatException {
            return Byte.parseByte(s);
        }

        @Override
        public String name() {
            return "Byte";
        }
    };

    NamedCodec<Short> SHORT = new NumberCodec<Short>() {

        @Override
        public <T> DataResult<Short> parse(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::shortValue);
        }

        public Short fromString(String s) throws NumberFormatException {
            return Short.parseShort(s);
        }

        @Override
        public String name() {
            return "Short";
        }
    };

    NamedCodec<Integer> INT = new NumberCodec<Integer>() {

        @Override
        public <T> DataResult<Integer> parse(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::intValue);
        }

        public Integer fromString(String s) throws NumberFormatException {
            return Integer.parseInt(s);
        }

        @Override
        public String name() {
            return "Integer";
        }
    };

    NamedCodec<Long> LONG = new NumberCodec<Long>() {

        @Override
        public <T> DataResult<Long> parse(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::longValue);
        }

        public Long fromString(String s) throws NumberFormatException {
            return Long.parseLong(s);
        }

        @Override
        public String name() {
            return "Long";
        }
    };

    NamedCodec<Float> FLOAT = new NumberCodec<Float>() {

        @Override
        public <T> DataResult<Float> parse(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::floatValue);
        }

        public Float fromString(String s) throws NumberFormatException {
            return Float.parseFloat(s);
        }

        @Override
        public String name() {
            return "Float";
        }
    };

    NamedCodec<Double> DOUBLE = new NumberCodec<Double>() {

        @Override
        public <T> DataResult<Double> parse(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input).map(Number::doubleValue);
        }

        public Double fromString(String s) throws NumberFormatException {
            return Double.parseDouble(s);
        }

        @Override
        public String name() {
            return "Double";
        }
    };

    NamedCodec<String> STRING = of(Codec.STRING);

    NamedCodec<ByteBuffer> BYTE_BUFFER = of(Codec.BYTE_BUFFER);

    NamedCodec<IntStream> INT_STREAM = of(Codec.INT_STREAM);

    NamedCodec<LongStream> LONG_STREAM = of(Codec.LONG_STREAM);

    NamedCodec<DoubleStream> DOUBLE_STREAM = new NamedCodec<DoubleStream>() {

        @Override
        public <T> DataResult<Pair<DoubleStream, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getStream(input).flatMap(stream -> {
                List<T> list = stream.toList();
                return list.stream().allMatch(element -> ops.getNumberValue(element).result().isPresent()) ? DataResult.success(list.stream().mapToDouble(element -> ((Number) ops.getNumberValue(element).result().get()).doubleValue())) : DataResult.error(() -> "Some elements are not doubles: " + input);
            }).map(r -> Pair.of(r, ops.empty()));
        }

        public <T> DataResult<T> encode(DynamicOps<T> ops, DoubleStream input, T prefix) {
            return ops.mergeToPrimitive(prefix, ops.createList(input.mapToObj(ops::createDouble)));
        }

        @Override
        public String name() {
            return "DoubleStream";
        }
    };

    NamedCodec<Dynamic<?>> PASSTHROUGH = new NamedCodec<Dynamic<?>>() {

        @Override
        public <T> DataResult<Pair<Dynamic<?>, T>> decode(DynamicOps<T> ops, T input) {
            return DataResult.success(Pair.of(new Dynamic(ops, input), ops.empty()));
        }

        public <T> DataResult<T> encode(DynamicOps<T> ops, Dynamic<?> input, T prefix) {
            if (input.getValue() == input.getOps().empty()) {
                return DataResult.success(prefix, Lifecycle.experimental());
            } else {
                T casted = (T) input.convert(ops).getValue();
                if (prefix == ops.empty()) {
                    return DataResult.success(casted, Lifecycle.experimental());
                } else {
                    DataResult<T> toMap = ops.getMap(casted).flatMap(map -> ops.mergeToMap(prefix, map));
                    return (DataResult<T>) toMap.result().map(DataResult::success).orElseGet(() -> {
                        DataResult<T> toList = ops.getStream(casted).flatMap(stream -> ops.mergeToList(prefix, (List) stream.collect(Collectors.toList())));
                        return (DataResult) toList.result().map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Don't know how to merge " + prefix + " and " + casted, prefix, Lifecycle.experimental()));
                    });
                }
            }
        }

        @Override
        public String name() {
            return "passthrough";
        }
    };

    static <A> NamedCodec<A> of(Codec<A> codec) {
        return of(codec, codec.toString());
    }

    static <A> NamedCodec<A> of(Codec<A> codec, String name) {
        return new NamedCodec<A>() {

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                return codec.decode(ops, input);
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, A input, T prefix) {
                return codec.encode(input, ops, prefix);
            }

            @Override
            public String name() {
                return name;
            }

            public String toString() {
                return name;
            }
        };
    }

    static <A> NamedCodec<List<A>> list(NamedCodec<A> codec) {
        return EnhancedListCodec.of(codec);
    }

    static <A> NamedCodec<List<A>> list(NamedCodec<A> codec, String name) {
        return EnhancedListCodec.of(codec, name);
    }

    static <F, S> NamedCodec<Either<F, S>> either(NamedCodec<F> first, NamedCodec<S> second) {
        return EnhancedEitherCodec.of(first, second);
    }

    static <F, S> NamedCodec<Either<F, S>> either(NamedCodec<F> first, NamedCodec<S> second, String name) {
        return EnhancedEitherCodec.of(first, second, name);
    }

    static <E extends Enum<E>> NamedCodec<E> enumCodec(Class<E> enumClass) {
        return EnumCodec.of(enumClass);
    }

    static <E extends Enum<E>> NamedCodec<E> enumCodec(Class<E> enumClass, String name) {
        return EnumCodec.of(enumClass, name);
    }

    static <A> NamedCodec<A> registrar(Registrar<A> registrar) {
        return RegistrarCodec.of(registrar, false);
    }

    static <K extends Enum<K>, V> NamedMapCodec<Map<K, V>> enumMap(Class<K> keyEnumClass, NamedCodec<V> valueCodec) {
        return EnumMapCodec.of(keyEnumClass, valueCodec);
    }

    static <K extends Enum<K>, V> NamedMapCodec<Map<K, V>> enumMap(Class<K> keyEnumClass, NamedCodec<V> valueCodec, @Nullable V defaultValue) {
        return EnumMapCodec.of(keyEnumClass, valueCodec, defaultValue);
    }

    static <K extends Enum<K>, V> NamedMapCodec<Map<K, V>> enumMap(Class<K> keyEnumClass, NamedCodec<V> valueCodec, String name) {
        return EnumMapCodec.of(keyEnumClass, valueCodec, name);
    }

    static <K extends Enum<K>, V> NamedMapCodec<Map<K, V>> enumMap(Class<K> keyEnumClass, NamedCodec<V> valueCodec, @Nullable V defaultValue, String name) {
        return EnumMapCodec.of(keyEnumClass, valueCodec, defaultValue, name);
    }

    static <A> NamedCodec<A> unit(A defaultValue) {
        return unit(defaultValue, defaultValue.toString());
    }

    static <A> NamedCodec<A> unit(A defaultValue, String name) {
        return unit((Supplier<A>) (() -> defaultValue), name);
    }

    static <A> NamedCodec<A> unit(Supplier<A> defaultValue, String name) {
        return new NamedCodec<A>() {

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                return DataResult.success(Pair.of(defaultValue.get(), ops.empty()));
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, A input, T prefix) {
                return ops.mapBuilder().build(prefix);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    static <O> NamedMapCodec<O> record(Function<NamedRecordCodec.Instance<O>, ? extends App<NamedRecordCodec.Mu<O>, O>> builder, String name) {
        return NamedRecordCodec.create(builder, name);
    }

    static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRange(N minInclusive, N maxInclusive) {
        return value -> ((Comparable) value).compareTo(minInclusive) >= 0 && ((Comparable) value).compareTo(maxInclusive) <= 0 ? DataResult.success(value) : DataResult.error(() -> "Value " + value + " outside of range [" + minInclusive + ":" + maxInclusive + "]", value);
    }

    static NamedCodec<Integer> intRange(int minInclusive, int maxInclusive) {
        Function<Integer, DataResult<Integer>> checker = checkRange(minInclusive, maxInclusive);
        return INT.flatXmap(checker, checker, "Range: [" + minInclusive + "," + maxInclusive + "]");
    }

    static NamedCodec<Float> floatRange(float minInclusive, float maxInclusive) {
        Function<Float, DataResult<Float>> checker = checkRange(minInclusive, maxInclusive);
        return FLOAT.flatXmap(checker, checker, "Range: [" + minInclusive + "," + maxInclusive + "]");
    }

    static NamedCodec<Double> doubleRange(double minInclusive, double maxInclusive) {
        Function<Double, DataResult<Double>> checker = checkRange(minInclusive, maxInclusive);
        return DOUBLE.flatXmap(checker, checker, "Range: [" + minInclusive + "," + maxInclusive + "]");
    }

    static NamedCodec<Long> longRange(long minInclusive, long maxInclusive) {
        Function<Long, DataResult<Long>> checker = checkRange(minInclusive, maxInclusive);
        return LONG.flatXmap(checker, checker, "Range: [" + minInclusive + "," + maxInclusive + "]");
    }

    static <A> NamedCodec<A> lazy(Supplier<NamedCodec<A>> supplier, String name) {
        return new NamedCodec<A>() {

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                return ((NamedCodec) supplier.get()).decode(ops, input);
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, A input, T prefix) {
                return ((NamedCodec) supplier.get()).encode(ops, input, prefix);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    static <K, V> UnboundedMapCodec<K, V> unboundedMap(NamedCodec<K> keyCodec, NamedCodec<V> valueCodec, String name) {
        return UnboundedMapCodec.of(keyCodec, valueCodec, name);
    }

    static <F, S> PairCodec<F, S> pair(NamedCodec<F> first, NamedCodec<S> second) {
        return PairCodec.of(first, second);
    }

    static <T> NamedCodec<T> fromJson(Function<JsonElement, T> parser, Function<T, JsonElement> encoder, String name) {
        return PASSTHROUGH.flatXmap(d -> {
            JsonElement json = getJson(d);
            try {
                return DataResult.success(parser.apply(json));
            } catch (JsonSyntaxException var4) {
                return DataResult.error(var4::getMessage);
            }
        }, t -> {
            try {
                JsonElement json = (JsonElement) encoder.apply(t);
                return DataResult.success(new Dynamic(JsonOps.INSTANCE, json));
            } catch (JsonSyntaxException var3) {
                return DataResult.error(var3::getMessage);
            }
        }, name);
    }

    static <U> JsonElement getJson(Dynamic<?> dynamic) {
        return dynamic.getValue() instanceof JsonElement ? (JsonElement) dynamic.getValue() : (JsonElement) dynamic.getOps().convertTo(JsonOps.INSTANCE, dynamic.getValue());
    }

    static DataResult<double[]> validateDoubleStreamSize(DoubleStream stream, int size) {
        double[] array = stream.limit((long) (size + 1)).toArray();
        if (array.length != size) {
            String s = "Input is not a list of " + size + " doubles";
            return array.length >= size ? DataResult.error(() -> s, Arrays.copyOf(array, size)) : DataResult.error(() -> s);
        } else {
            return DataResult.success(array);
        }
    }

    <T> DataResult<Pair<A, T>> decode(DynamicOps<T> var1, T var2);

    default <T> DataResult<A> read(DynamicOps<T> ops, T input) {
        return this.decode(ops, input).map(Pair::getFirst);
    }

    <T> DataResult<T> encode(DynamicOps<T> var1, A var2, T var3);

    default <T> DataResult<T> encodeStart(DynamicOps<T> ops, A input) {
        return this.encode(ops, input, (T) ops.empty());
    }

    String name();

    default Codec<A> codec() {
        return new Codec<A>() {

            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                return NamedCodec.this.decode(ops, input);
            }

            public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
                return NamedCodec.this.encode(ops, input, prefix);
            }

            public String toString() {
                return NamedCodec.this.name();
            }
        };
    }

    default NamedCodec<List<A>> listOf() {
        return list(this);
    }

    default NamedCodec<List<A>> listOf(String name) {
        return list(this, name);
    }

    default <E> NamedMapCodec<E> dispatch(Function<? super E, ? extends A> type, Function<? super A, ? extends NamedCodec<? extends E>> valueCodecGetter, String name) {
        return this.dispatch("type", type, valueCodecGetter, name);
    }

    default <E> NamedMapCodec<E> dispatch(String typeKey, Function<? super E, ? extends A> type, Function<? super A, ? extends NamedCodec<? extends E>> valueCodecGetter, String name) {
        return EnhancedDispatchCodec.of(typeKey, this, type.andThen(DataResult::success), valueCodecGetter.andThen(DataResult::success), name);
    }

    default <S> NamedCodec<S> xmap(Function<? super A, ? extends S> to, Function<? super S, ? extends A> from, String name) {
        return new NamedCodec<S>() {

            @Override
            public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
                return NamedCodec.this.decode(ops, input).map(p -> p.mapFirst(to));
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, S input, T prefix) {
                return NamedCodec.this.encode(ops, from.apply(input), prefix);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    default <S> NamedCodec<S> comapFlatMap(Function<? super A, ? extends DataResult<? extends S>> to, Function<? super S, ? extends A> from, String name) {
        return new NamedCodec<S>() {

            @Override
            public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
                return NamedCodec.this.decode(ops, input).flatMap(p -> ((DataResult) to.apply(p.getFirst())).map(r -> Pair.of(r, p.getSecond())));
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, S input, T prefix) {
                return NamedCodec.this.encode(ops, from.apply(input), prefix);
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    default <S> NamedCodec<S> flatComapMap(Function<? super A, ? extends S> to, Function<? super S, ? extends DataResult<? extends A>> from, String name) {
        return new NamedCodec<S>() {

            @Override
            public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
                return NamedCodec.this.decode(ops, input).map(p -> p.mapFirst(to));
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, S input, T prefix) {
                return ((DataResult) from.apply(input)).flatMap(a -> NamedCodec.this.encode(ops, a, prefix));
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    default <S> NamedCodec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> to, Function<? super S, ? extends DataResult<? extends A>> from, String name) {
        return new NamedCodec<S>() {

            @Override
            public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T input) {
                return NamedCodec.this.decode(ops, input).flatMap(p -> ((DataResult) to.apply(p.getFirst())).map(r -> Pair.of(r, p.getSecond())));
            }

            @Override
            public <T> DataResult<T> encode(DynamicOps<T> ops, S input, T prefix) {
                return ((DataResult) from.apply(input)).flatMap(a -> NamedCodec.this.encode(ops, a, prefix));
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    default NamedMapCodec<A> fieldOf(String fieldName) {
        return FieldCodec.of(fieldName, this, this.name());
    }

    default NamedMapCodec<Optional<A>> optionalFieldOf(String fieldName) {
        return OptionalFieldCodec.of(fieldName, this, this.name());
    }

    default NamedMapCodec<A> optionalFieldOf(String fieldName, A defaultValue) {
        return this.optionalFieldOf(fieldName, (Supplier<A>) (() -> defaultValue));
    }

    default NamedMapCodec<A> optionalFieldOf(String fieldName, Supplier<A> defaultValue) {
        return DefaultOptionalFieldCodec.of(fieldName, this, defaultValue, this.name());
    }

    default void toNetwork(A input, FriendlyByteBuf buf) {
        DataResult<Tag> result = this.encodeStart(NbtOps.INSTANCE, input);
        result.error().ifPresent(error -> {
            throw new EncoderException(String.format("Failed to encode: %s\nError: %s\nInput: %s", this.name(), error.message(), input.toString()));
        });
        Tag tag = (Tag) result.result().orElseThrow();
        if (tag instanceof CompoundTag compoundTag) {
            buf.writeNbt(compoundTag);
        } else {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("custommachinery$special_nbt_key", tag);
            buf.writeNbt(compoundTag);
        }
    }

    default A fromNetwork(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readAnySizeNbt();
        DataResult<A> result;
        if (tag != null && tag.contains("custommachinery$special_nbt_key")) {
            result = this.read(NbtOps.INSTANCE, tag.get("custommachinery$special_nbt_key"));
        } else {
            result = this.read(NbtOps.INSTANCE, tag);
        }
        result.error().ifPresent(error -> {
            throw new EncoderException(String.format("Failed to decode: %s\nError: %s\nInput: %S", this.name(), error.message(), tag));
        });
        return (A) result.result().orElseThrow();
    }

    default A copy(A input) {
        return (A) this.read(JsonOps.INSTANCE, (JsonElement) this.encodeStart(JsonOps.INSTANCE, input).result().orElseThrow()).result().orElseThrow();
    }
}