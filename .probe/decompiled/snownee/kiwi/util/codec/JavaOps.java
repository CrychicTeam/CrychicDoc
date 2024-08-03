package snownee.kiwi.util.codec;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.RecordBuilder.AbstractUniversalBuilder;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class JavaOps implements DynamicOps<Object> {

    public static final JavaOps INSTANCE = new JavaOps();

    private JavaOps() {
    }

    public Object empty() {
        return null;
    }

    public Object emptyMap() {
        return Map.of();
    }

    public Object emptyList() {
        return List.of();
    }

    public <U> U convertTo(DynamicOps<U> outOps, Object input) {
        if (input == null) {
            return (U) outOps.empty();
        } else if (input instanceof Map) {
            return (U) this.convertMap(outOps, input);
        } else if (input instanceof ByteList value) {
            return (U) outOps.createByteList(ByteBuffer.wrap(value.toByteArray()));
        } else if (input instanceof IntList value) {
            return (U) outOps.createIntList(value.intStream());
        } else if (input instanceof LongList value) {
            return (U) outOps.createLongList(value.longStream());
        } else if (input instanceof List) {
            return (U) this.convertList(outOps, input);
        } else if (input instanceof String value) {
            return (U) outOps.createString(value);
        } else if (input instanceof Boolean value) {
            return (U) outOps.createBoolean(value);
        } else if (input instanceof Byte value) {
            return (U) outOps.createByte(value);
        } else if (input instanceof Short value) {
            return (U) outOps.createShort(value);
        } else if (input instanceof Integer value) {
            return (U) outOps.createInt(value);
        } else if (input instanceof Long value) {
            return (U) outOps.createLong(value);
        } else if (input instanceof Float value) {
            return (U) outOps.createFloat(value);
        } else if (input instanceof Double value) {
            return (U) outOps.createDouble(value);
        } else if (input instanceof Number value) {
            return (U) outOps.createNumeric(value);
        } else {
            throw new IllegalStateException("Don't know how to convert " + input);
        }
    }

    public DataResult<Number> getNumberValue(Object input) {
        return input instanceof Number value ? DataResult.success(value) : DataResult.error(() -> "Not a number: " + input);
    }

    public Object createNumeric(Number value) {
        return value;
    }

    public Object createByte(byte value) {
        return value;
    }

    public Object createShort(short value) {
        return value;
    }

    public Object createInt(int value) {
        return value;
    }

    public Object createLong(long value) {
        return value;
    }

    public Object createFloat(float value) {
        return value;
    }

    public Object createDouble(double value) {
        return value;
    }

    public DataResult<Boolean> getBooleanValue(Object input) {
        return input instanceof Boolean value ? DataResult.success(value) : DataResult.error(() -> "Not a boolean: " + input);
    }

    public Object createBoolean(boolean value) {
        return value;
    }

    public DataResult<String> getStringValue(Object input) {
        return input instanceof String value ? DataResult.success(value) : DataResult.error(() -> "Not a string: " + input);
    }

    public Object createString(String value) {
        return value;
    }

    public DataResult<Object> mergeToList(Object input, Object value) {
        if (input == this.empty()) {
            return DataResult.success(List.of(value));
        } else if (input instanceof List<?> list) {
            return list.isEmpty() ? DataResult.success(List.of(value)) : DataResult.success(ImmutableList.builder().addAll(list).add(value).build());
        } else {
            return DataResult.error(() -> "Not a list: " + input);
        }
    }

    public DataResult<Object> mergeToList(Object input, List<Object> values) {
        if (input == this.empty()) {
            return DataResult.success(values);
        } else if (input instanceof List<?> list) {
            return list.isEmpty() ? DataResult.success(values) : DataResult.success(ImmutableList.builder().addAll(list).addAll(values).build());
        } else {
            return DataResult.error(() -> "Not a list: " + input);
        }
    }

    public DataResult<Object> mergeToMap(Object input, Object key, Object value) {
        if (input == this.empty()) {
            return DataResult.success(Map.of(key, value));
        } else if (input instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return DataResult.success(Map.of(key, value));
            } else {
                Builder<Object, Object> result = ImmutableMap.builderWithExpectedSize(map.size() + 1);
                result.putAll(map);
                result.put(key, value);
                return DataResult.success(result.buildKeepingLast());
            }
        } else {
            return DataResult.error(() -> "Not a map: " + input);
        }
    }

    public DataResult<Object> mergeToMap(Object input, Map<Object, Object> values) {
        if (input == this.empty()) {
            return DataResult.success(values);
        } else if (input instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return DataResult.success(values);
            } else {
                Builder<Object, Object> result = ImmutableMap.builderWithExpectedSize(map.size() + values.size());
                result.putAll(map);
                result.putAll(values);
                return DataResult.success(result.buildKeepingLast());
            }
        } else {
            return DataResult.error(() -> "Not a map: " + input);
        }
    }

    private static Map<Object, Object> mapLikeToMap(MapLike<Object> values) {
        return (Map<Object, Object>) values.entries().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    public DataResult<Object> mergeToMap(Object input, MapLike<Object> values) {
        if (input == this.empty()) {
            return DataResult.success(mapLikeToMap(values));
        } else if (input instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return DataResult.success(mapLikeToMap(values));
            } else {
                Builder<Object, Object> result = ImmutableMap.builderWithExpectedSize(map.size());
                result.putAll(map);
                values.entries().forEach(e -> result.put(e.getFirst(), e.getSecond()));
                return DataResult.success(result.buildKeepingLast());
            }
        } else {
            return DataResult.error(() -> "Not a map: " + input);
        }
    }

    private static Stream<Pair<Object, Object>> getMapEntries(Map<?, ?> input) {
        return input.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue()));
    }

    public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object input) {
        return input instanceof Map<?, ?> map ? DataResult.success(getMapEntries(map)) : DataResult.error(() -> "Not a map: " + input);
    }

    public DataResult<Consumer<BiConsumer<Object, Object>>> getMapEntries(Object input) {
        return input instanceof Map<?, ?> map ? DataResult.success(map::forEach) : DataResult.error(() -> "Not a map: " + input);
    }

    public Object createMap(Stream<Pair<Object, Object>> map) {
        return map.collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    public DataResult<MapLike<Object>> getMap(Object input) {
        return input instanceof Map<?, ?> map ? DataResult.success(new MapLike<Object>() {

            @Nullable
            public Object get(Object key) {
                return map.get(key);
            }

            @Nullable
            public Object get(String key) {
                return map.get(key);
            }

            public Stream<Pair<Object, Object>> entries() {
                return JavaOps.getMapEntries(map);
            }

            public String toString() {
                return "MapLike[" + map + "]";
            }
        }) : DataResult.error(() -> "Not a map: " + input);
    }

    public Object createMap(Map<Object, Object> map) {
        return map;
    }

    public DataResult<Stream<Object>> getStream(Object input) {
        return input instanceof List<?> list ? DataResult.success(list.stream().map(o -> o)) : DataResult.error(() -> "Not an list: " + input);
    }

    public DataResult<Consumer<Consumer<Object>>> getList(Object input) {
        return input instanceof List<?> list ? DataResult.success(list::forEach) : DataResult.error(() -> "Not an list: " + input);
    }

    public Object createList(Stream<Object> input) {
        return input.toList();
    }

    public DataResult<ByteBuffer> getByteBuffer(Object input) {
        return input instanceof ByteList value ? DataResult.success(ByteBuffer.wrap(value.toByteArray())) : DataResult.error(() -> "Not a byte list: " + input);
    }

    public Object createByteList(ByteBuffer input) {
        ByteBuffer wholeBuffer = input.duplicate().clear();
        ByteArrayList result = new ByteArrayList();
        result.size(wholeBuffer.capacity());
        wholeBuffer.get(0, result.elements(), 0, result.size());
        return result;
    }

    public DataResult<IntStream> getIntStream(Object input) {
        return input instanceof IntList value ? DataResult.success(value.intStream()) : DataResult.error(() -> "Not an int list: " + input);
    }

    public Object createIntList(IntStream input) {
        return IntArrayList.toList(input);
    }

    public DataResult<LongStream> getLongStream(Object input) {
        return input instanceof LongList value ? DataResult.success(value.longStream()) : DataResult.error(() -> "Not a long list: " + input);
    }

    public Object createLongList(LongStream input) {
        return LongArrayList.toList(input);
    }

    public Object remove(Object input, String key) {
        if (input instanceof Map<?, ?> map) {
            Map<Object, Object> result = new LinkedHashMap(map);
            result.remove(key);
            return Map.copyOf(result);
        } else {
            return input;
        }
    }

    public RecordBuilder<Object> mapBuilder() {
        return new JavaOps.FixedMapBuilder<Object>(this);
    }

    public String toString() {
        return "Java";
    }

    private static final class FixedMapBuilder<T> extends AbstractUniversalBuilder<T, Builder<T, T>> {

        public FixedMapBuilder(DynamicOps<T> ops) {
            super(ops);
        }

        protected Builder<T, T> initBuilder() {
            return ImmutableMap.builder();
        }

        protected Builder<T, T> append(T key, T value, Builder<T, T> builder) {
            return builder.put(key, value);
        }

        protected DataResult<T> build(Builder<T, T> builder, T prefix) {
            ImmutableMap<T, T> result = builder.buildKeepingLast();
            return this.ops().mergeToMap(prefix, result);
        }
    }
}