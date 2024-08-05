package harmonised.pmmo.config.readers;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.NullObject;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.DataResult.PartialResult;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public record TomlConfigHelper(ForgeConfigSpec.Builder builder) {

    static final Logger LOGGER = LogManager.getLogger();

    public static <T> T register(Type configType, Function<ForgeConfigSpec.Builder, T> configFactory) {
        return register(configType, configFactory, null);
    }

    public static <T> T register(Type configType, Function<ForgeConfigSpec.Builder, T> configFactory, @Nullable String configName) {
        ModLoadingContext modContext = ModLoadingContext.get();
        Pair<T, ForgeConfigSpec> entry = new ForgeConfigSpec.Builder().configure(configFactory);
        T config = (T) entry.getLeft();
        ForgeConfigSpec spec = (ForgeConfigSpec) entry.getRight();
        if (configName == null) {
            modContext.registerConfig(configType, spec);
        } else {
            modContext.registerConfig(configType, spec, configName + ".toml");
        }
        return config;
    }

    public static <T> TomlConfigHelper.ConfigObject<T> defineObject(ForgeConfigSpec.Builder builder, String name, Codec<T> codec, T defaultObject) {
        DataResult<Object> encodeResult = codec.encodeStart(TomlConfigHelper.TomlConfigOps.INSTANCE, defaultObject);
        Object encodedObject = encodeResult.getOrThrow(false, s -> LOGGER.error("Unable to encode default value: {}", s));
        ForgeConfigSpec.ConfigValue<Object> value = builder.define(name, encodedObject);
        return new TomlConfigHelper.ConfigObject<>(value, codec, defaultObject, encodedObject);
    }

    public static class ConfigObject<T> implements Supplier<T> {

        @Nonnull
        private final ForgeConfigSpec.ConfigValue<Object> value;

        @Nonnull
        private final Codec<T> codec;

        @Nonnull
        private Object cachedObject;

        @Nonnull
        private T parsedObject;

        @Nonnull
        private T defaultObject;

        private ConfigObject(ForgeConfigSpec.ConfigValue<Object> value, Codec<T> codec, T defaultObject, Object encodedDefaultObject) {
            this.value = value;
            this.codec = codec;
            this.defaultObject = defaultObject;
            this.parsedObject = defaultObject;
            this.cachedObject = encodedDefaultObject;
        }

        @Nonnull
        public T get() {
            Object freshObject = this.value.get();
            if (!Objects.equals(this.cachedObject, freshObject)) {
                this.cachedObject = freshObject;
                this.parsedObject = this.getReparsedObject(freshObject);
            }
            return this.parsedObject;
        }

        private T getReparsedObject(Object obj) {
            DataResult<T> parseResult = this.codec.parse(TomlConfigHelper.TomlConfigOps.INSTANCE, obj);
            return (T) parseResult.get().map(result -> result, failure -> {
                TomlConfigHelper.LOGGER.error("Config failure: Using default config value due to parsing error: {}", failure.message());
                return this.defaultObject;
            });
        }
    }

    public static class TomlConfigOps implements DynamicOps<Object> {

        public static final TomlConfigHelper.TomlConfigOps INSTANCE = new TomlConfigHelper.TomlConfigOps();

        public Object empty() {
            return NullObject.NULL_OBJECT;
        }

        public <U> U convertTo(DynamicOps<U> outOps, Object input) {
            if (input instanceof Config) {
                return (U) this.convertMap(outOps, input);
            } else if (input instanceof Collection) {
                return (U) this.convertList(outOps, input);
            } else if (input == null || input instanceof NullObject) {
                return (U) outOps.empty();
            } else if (input instanceof Enum) {
                return (U) outOps.createString(((Enum) input).name());
            } else if (input instanceof Temporal) {
                return (U) outOps.createString(input.toString());
            } else if (input instanceof String s) {
                return (U) outOps.createString(s);
            } else if (input instanceof Boolean b) {
                return (U) outOps.createBoolean(b);
            } else if (input instanceof Number n) {
                return (U) outOps.createNumeric(n);
            } else {
                throw new UnsupportedOperationException("TomlConfigOps was unable to convert toml value: " + input);
            }
        }

        public DataResult<Number> getNumberValue(Object input) {
            return input instanceof Number n ? DataResult.success(n) : DataResult.error(() -> "Not a number: " + input);
        }

        public DataResult<Boolean> getBooleanValue(Object input) {
            if (input instanceof Boolean b) {
                return DataResult.success(b);
            } else {
                return input instanceof Number n ? DataResult.success(n.intValue() > 0) : DataResult.error(() -> "Not a boolean: " + input);
            }
        }

        public Object createBoolean(boolean value) {
            return value;
        }

        public boolean compressMaps() {
            return false;
        }

        public Object createNumeric(Number i) {
            return i;
        }

        public DataResult<String> getStringValue(Object input) {
            return !(input instanceof Config) && !(input instanceof Collection) ? DataResult.success(String.valueOf(input)) : DataResult.error(() -> "Not a string: " + input);
        }

        public Object createString(String value) {
            return value;
        }

        public DataResult<Object> mergeToList(Object list, Object value) {
            if (!(list instanceof Collection) && list != this.empty()) {
                return DataResult.error(() -> "mergeToList called with not a list: " + list, list);
            } else {
                Collection<Object> result = new ArrayList();
                if (list != this.empty()) {
                    Collection<Object> listAsCollection = (Collection<Object>) list;
                    result.addAll(listAsCollection);
                }
                result.add(value);
                return DataResult.success(result);
            }
        }

        public DataResult<Object> mergeToList(Object list, List<Object> values) {
            return super.mergeToList(list, values).map(obj -> obj == this.empty() ? new ArrayList() : obj);
        }

        public DataResult<Object> mergeToMap(Object map, Object key, Object value) {
            if (!(map instanceof Config) && map != this.empty()) {
                return DataResult.error(() -> "mergeToMap called with not a map: " + map, map);
            } else {
                DataResult<String> stringResult = this.getStringValue(key);
                Optional<PartialResult<String>> badResult = stringResult.error();
                return badResult.isPresent() ? DataResult.error(() -> "key is not a string: " + key, map) : stringResult.flatMap(s -> {
                    Config output = TomlFormat.newConfig();
                    if (map != this.empty()) {
                        Config oldConfig = (Config) map;
                        output.addAll(oldConfig);
                    }
                    output.add(s, value);
                    return DataResult.success(output);
                });
            }
        }

        public DataResult<Stream<com.mojang.datafixers.util.Pair<Object, Object>>> getMapValues(Object input) {
            return !(input instanceof Config config) ? DataResult.error(() -> "Not a Config: " + input) : DataResult.success(config.entrySet().stream().map(entry -> com.mojang.datafixers.util.Pair.of(entry.getKey(), entry.getValue())));
        }

        public Object createMap(Stream<com.mojang.datafixers.util.Pair<Object, Object>> map) {
            Config result = TomlFormat.newConfig();
            map.forEach(p -> result.add((String) this.getStringValue(p.getFirst()).getOrThrow(false, s -> {
            }), p.getSecond()));
            return result;
        }

        public DataResult<Stream<Object>> getStream(Object input) {
            return input instanceof Collection<Object> collection ? DataResult.success(collection.stream()) : DataResult.error(() -> "Not a collection: " + input);
        }

        public Object createList(Stream<Object> input) {
            return input.toList();
        }

        public Object remove(Object input, String key) {
            if (input instanceof Config oldConfig) {
                Config result = TomlFormat.newConfig();
                oldConfig.entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), key)).forEach(entry -> result.add(entry.getKey(), entry.getValue()));
                return result;
            } else {
                return input;
            }
        }

        public String toString() {
            return "TOML";
        }
    }
}