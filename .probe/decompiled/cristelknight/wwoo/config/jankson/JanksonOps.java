package cristelknight.wwoo.config.jankson;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonNull;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.RecordBuilder.AbstractStringBuilder;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public record JanksonOps(boolean compressed) implements DynamicOps<JsonElement> {

    public static final JanksonOps INSTANCE = new JanksonOps(false);

    public static final JanksonOps COMPRESSED = new JanksonOps(true);

    public JsonElement empty() {
        return JsonNull.INSTANCE;
    }

    public <U> U convertTo(DynamicOps<U> outOps, JsonElement input) {
        if (input instanceof JsonObject) {
            return (U) this.convertMap(outOps, input);
        } else if (input instanceof JsonArray) {
            return (U) this.convertList(outOps, input);
        } else if (input instanceof JsonNull) {
            return (U) outOps.empty();
        } else {
            JsonPrimitive primitive = (JsonPrimitive) input;
            Object value = primitive.getValue();
            if (value instanceof String) {
                return (U) outOps.createString(primitive.asString());
            } else if (value instanceof Boolean) {
                return (U) outOps.createBoolean(primitive.asBoolean(false));
            } else {
                BigDecimal bigDecimal = primitive.asBigDecimal(new BigDecimal(primitive.asString()));
                try {
                    long l = bigDecimal.longValueExact();
                    if ((long) ((byte) ((int) l)) == l) {
                        return (U) outOps.createByte((byte) ((int) l));
                    } else if ((long) ((short) ((int) l)) == l) {
                        return (U) outOps.createShort((short) ((int) l));
                    } else {
                        return (U) ((long) ((int) l) == l ? outOps.createInt((int) l) : outOps.createLong(l));
                    }
                } catch (ArithmeticException var9) {
                    double d = bigDecimal.doubleValue();
                    return (U) ((double) ((float) d) == d ? outOps.createFloat((float) d) : outOps.createDouble(d));
                }
            }
        }
    }

    public DataResult<Number> getNumberValue(JsonElement input) {
        if (input instanceof JsonPrimitive jsonPrimitive) {
            Object value = jsonPrimitive.getValue();
            if (value instanceof Number number) {
                return DataResult.success(number);
            }
            if (value instanceof Boolean bl) {
                return DataResult.success(bl ? 1 : 0);
            }
            if (this.compressed && value instanceof String s) {
                try {
                    return DataResult.success(Integer.parseInt(s));
                } catch (NumberFormatException var6) {
                    return DataResult.error(() -> "Not a number: " + var6 + " " + input);
                }
            }
        }
        if (input instanceof JsonPrimitive primitive && primitive.getValue() instanceof Boolean) {
            return DataResult.success(primitive.getValue() ? 1 : 0);
        }
        return DataResult.error(() -> "Not a number: " + input);
    }

    public JsonElement createNumeric(Number i) {
        return new JsonPrimitive(i);
    }

    public DataResult<Boolean> getBooleanValue(JsonElement input) {
        if (input instanceof JsonPrimitive) {
            Object value = ((JsonPrimitive) input).getValue();
            if (value instanceof Boolean bl) {
                return DataResult.success(bl);
            }
            if (value instanceof Number num) {
                return DataResult.success(num.byteValue() != 0);
            }
        }
        return DataResult.error(() -> "Not a boolean: " + input);
    }

    public JsonElement createBoolean(boolean value) {
        return new JsonPrimitive(value);
    }

    public DataResult<String> getStringValue(JsonElement input) {
        if (input instanceof JsonPrimitive primitive && (primitive.getValue() instanceof String || primitive.getValue() instanceof Number && this.compressed)) {
            return DataResult.success(primitive.asString());
        }
        return DataResult.error(() -> "Not a string: " + input);
    }

    public JsonElement createString(String value) {
        return new JsonPrimitive(value);
    }

    public DataResult<JsonElement> mergeToList(JsonElement list, JsonElement value) {
        if (!(list instanceof JsonArray) && list != this.empty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        } else {
            JsonArray result = new JsonArray();
            if (list != this.empty()) {
                result.addAll((Collection) list);
            }
            result.add(value);
            return DataResult.success(result);
        }
    }

    public DataResult<JsonElement> mergeToList(JsonElement list, List<JsonElement> values) {
        if (!(list instanceof JsonArray) && list != this.empty()) {
            return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
        } else {
            JsonArray result = new JsonArray();
            if (list != this.empty()) {
                result.addAll((Collection) list);
            }
            result.addAll(values);
            return DataResult.success(result);
        }
    }

    public DataResult<JsonElement> mergeToMap(JsonElement map, JsonElement key, JsonElement value) {
        if (!(map instanceof JsonObject) && map != this.empty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        } else {
            if (key instanceof JsonPrimitive primitive && (primitive.getValue() instanceof String || this.compressed)) {
                JsonObject output = new JsonObject();
                if (map != this.empty()) {
                    output.putAll((JsonObject) map);
                }
                output.put(((JsonPrimitive) key).asString(), value);
                return DataResult.success(output);
            }
            return DataResult.error(() -> "key is not a string: " + key, map);
        }
    }

    public DataResult<JsonElement> mergeToMap(JsonElement map, MapLike<JsonElement> values) {
        if (!(map instanceof JsonObject) && map != this.empty()) {
            return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
        } else {
            JsonObject output = new JsonObject();
            if (map != this.empty()) {
                output.putAll((JsonObject) map);
            }
            List<JsonElement> missed = Lists.newArrayList();
            values.entries().forEach(entry -> {
                JsonElement key = (JsonElement) entry.getFirst();
                if (key instanceof JsonPrimitive primitive && (primitive.getValue() instanceof String || this.compressed)) {
                    output.put(((JsonPrimitive) key).asString(), (JsonElement) entry.getSecond());
                    return;
                }
                missed.add(key);
            });
            return !missed.isEmpty() ? DataResult.error(() -> "some keys are not strings: " + missed, output) : DataResult.success(output);
        }
    }

    public DataResult<Stream<Pair<JsonElement, JsonElement>>> getMapValues(JsonElement input) {
        return !(input instanceof JsonObject) ? DataResult.error(() -> "Not a JSON object: " + input) : DataResult.success(((JsonObject) input).entrySet().stream().map(entry -> Pair.of(new JsonPrimitive(entry.getKey()), entry.getValue() instanceof JsonNull ? null : (JsonElement) entry.getValue())));
    }

    public DataResult<Consumer<BiConsumer<JsonElement, JsonElement>>> getMapEntries(JsonElement input) {
        return !(input instanceof JsonObject) ? DataResult.error(() -> "Not a JSON object: " + input) : DataResult.success((Consumer) c -> {
            for (Entry<String, JsonElement> entry : ((JsonObject) input).entrySet()) {
                c.accept(this.createString((String) entry.getKey()), entry.getValue() instanceof JsonNull ? null : (JsonElement) entry.getValue());
            }
        });
    }

    public DataResult<MapLike<JsonElement>> getMap(JsonElement input) {
        return input instanceof JsonObject object ? DataResult.success(new CommentsMap<JsonElement>() {

            @Nullable
            public JsonElement get(JsonElement key) {
                JsonElement element = object.get(((JsonPrimitive) key).asString());
                return element instanceof JsonNull ? null : element;
            }

            @Nullable
            public JsonElement get(String key) {
                JsonElement element = object.get(key);
                return element instanceof JsonNull ? null : element;
            }

            public Stream<Pair<JsonElement, JsonElement>> entries() {
                return object.entrySet().stream().map(e -> Pair.of(new JsonPrimitive(e.getKey()), (JsonElement) e.getValue()));
            }

            public String toString() {
                return "MapLike[" + object + "]";
            }

            @Override
            public void addComment(String key, String comment) {
                object.setComment(key, comment);
            }

            @Nullable
            @Override
            public String getComment(String key) {
                return object.getComment(key);
            }
        }) : DataResult.error(() -> "Not a JSON object: " + input);
    }

    public JsonElement createMap(Stream<Pair<JsonElement, JsonElement>> map) {
        JsonObject result = new JsonObject();
        map.forEach(p -> result.put(((JsonPrimitive) p.getFirst()).asString(), (JsonElement) p.getSecond()));
        return result;
    }

    public DataResult<Stream<JsonElement>> getStream(JsonElement input) {
        return input instanceof JsonArray ? DataResult.success(((JsonArray) input).stream().map(e -> e instanceof JsonNull ? null : e)) : DataResult.error(() -> "Not a json array: " + input);
    }

    public DataResult<Consumer<Consumer<JsonElement>>> getList(JsonElement input) {
        return input instanceof JsonArray ? DataResult.success((Consumer) c -> {
            for (JsonElement element : (JsonArray) input) {
                c.accept(element instanceof JsonNull ? null : element);
            }
        }) : DataResult.error(() -> "Not a json array: " + input);
    }

    public JsonElement createList(Stream<JsonElement> input) {
        JsonArray result = new JsonArray();
        input.forEach(result::add);
        return result;
    }

    public JsonElement remove(JsonElement input, String key) {
        if (input instanceof JsonObject) {
            JsonObject result = new JsonObject();
            ((JsonObject) input).entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), key)).forEach(entry -> result.put((String) entry.getKey(), (JsonElement) entry.getValue()));
            return result;
        } else {
            return input;
        }
    }

    public String toString() {
        return "Jankson JSON";
    }

    public ListBuilder<JsonElement> listBuilder() {
        return new JanksonOps.ArrayBuilder();
    }

    public boolean compressMaps() {
        return this.compressed;
    }

    public RecordBuilder<JsonElement> mapBuilder() {
        return new JanksonOps.JsonRecordBuilder();
    }

    private static final class ArrayBuilder implements ListBuilder<JsonElement> {

        private DataResult<JsonArray> builder = DataResult.success(new JsonArray(), Lifecycle.stable());

        public DynamicOps<JsonElement> ops() {
            return JanksonOps.INSTANCE;
        }

        public ListBuilder<JsonElement> add(JsonElement value) {
            this.builder = this.builder.map(b -> {
                b.add(value);
                return b;
            });
            return this;
        }

        public ListBuilder<JsonElement> add(DataResult<JsonElement> value) {
            this.builder = this.builder.apply2stable((b, element) -> {
                b.add(element);
                return b;
            }, value);
            return this;
        }

        public ListBuilder<JsonElement> withErrorsFrom(DataResult<?> result) {
            this.builder = this.builder.flatMap(r -> result.map(v -> r));
            return this;
        }

        public ListBuilder<JsonElement> mapError(UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }

        public DataResult<JsonElement> build(JsonElement prefix) {
            DataResult<JsonElement> result = this.builder.flatMap(b -> {
                if (!(prefix instanceof JsonArray) && prefix != this.ops().empty()) {
                    return DataResult.error(() -> "Cannot append a list to not a list: " + prefix, prefix);
                } else {
                    JsonArray array = new JsonArray();
                    if (prefix != this.ops().empty()) {
                        array.addAll((JsonArray) prefix);
                    }
                    array.addAll(b);
                    return DataResult.success(array, Lifecycle.stable());
                }
            });
            this.builder = DataResult.success(new JsonArray(), Lifecycle.stable());
            return result;
        }
    }

    private class JsonRecordBuilder extends AbstractStringBuilder<JsonElement, JsonObject> implements Comments {

        private final Map<String, String> comments = new HashMap();

        protected JsonRecordBuilder() {
            super(JanksonOps.this);
        }

        protected JsonObject initBuilder() {
            return new JsonObject();
        }

        protected JsonObject append(String key, JsonElement value, JsonObject builder) {
            builder.put(key, value);
            return builder;
        }

        protected DataResult<JsonElement> build(JsonObject builder, JsonElement prefix) {
            if (prefix != null && !(prefix instanceof JsonNull)) {
                if (prefix instanceof JsonObject prefixObject) {
                    JsonObject result = new JsonObject();
                    for (Entry<String, JsonElement> entry : prefixObject.entrySet()) {
                        String key = (String) entry.getKey();
                        result.put(key, (JsonElement) entry.getValue());
                        if (this.comments.containsKey(key)) {
                            result.setComment(key, (String) this.comments.get(key));
                        }
                    }
                    for (Entry<String, JsonElement> entryx : builder.entrySet()) {
                        String key = (String) entryx.getKey();
                        result.put(key, (JsonElement) entryx.getValue());
                        if (this.comments.containsKey(key)) {
                            result.setComment(key, (String) this.comments.get(key));
                        }
                    }
                    return DataResult.success(result);
                } else {
                    return DataResult.error(() -> "mergeToMap called with not a map: " + prefix, prefix);
                }
            } else {
                this.comments.forEach(builder::setComment);
                return DataResult.success(builder);
            }
        }

        @Override
        public void addComment(String key, String comment) {
            this.comments.put(key, comment);
        }

        @Nullable
        @Override
        public String getComment(String key) {
            return ((JsonObject) this.builder.result().orElseThrow()).getComment(key);
        }
    }
}