package net.mehvahdjukaar.moonlight.api.platform.configs.forge;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.mehvahdjukaar.moonlight.core.databuddy.ConfigHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ConfigBuilderImpl extends ConfigBuilder {

    private final List<ForgeConfigSpec.ConfigValue<?>> requireGameRestart = new ArrayList();

    private boolean currentGameRestart;

    private ForgeConfigSpec.ConfigValue<?> currentValue;

    private final List<ConfigBuilderImpl.SpecialValue<?, ?>> specialValues = new ArrayList();

    private final ForgeConfigSpec.Builder builder;

    private final Deque<String> cat = new ArrayDeque();

    public static ConfigBuilder create(ResourceLocation name, ConfigType type) {
        return new ConfigBuilderImpl(name, type);
    }

    public ConfigBuilderImpl(ResourceLocation name, ConfigType type) {
        super(name, type);
        this.builder = new ForgeConfigSpec.Builder();
    }

    @Override
    public String currentCategory() {
        return (String) this.cat.peekFirst();
    }

    public ConfigSpecWrapper build() {
        return new ConfigSpecWrapper(this.getName(), this.builder.build(), this.type, this.synced, this.changeCallback, this.requireGameRestart, this.specialValues);
    }

    public ConfigBuilderImpl push(String category) {
        this.builder.push(category);
        this.cat.push(category);
        return this;
    }

    public ConfigBuilderImpl pop() {
        this.builder.pop();
        this.cat.pop();
        return this;
    }

    @Override
    public Supplier<Boolean> define(String name, boolean defaultValue) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.BooleanValue value = this.builder.define(name, defaultValue);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return value;
    }

    @Override
    public Supplier<Double> define(String name, double defaultValue, double min, double max) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.DoubleValue value = this.builder.defineInRange(name, defaultValue, min, max);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return value;
    }

    @Override
    public Supplier<Integer> define(String name, int defaultValue, int min, int max) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.IntValue value = this.builder.defineInRange(name, defaultValue, min, max);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return value;
    }

    @Override
    public Supplier<Integer> defineColor(String name, int defaultValue) {
        this.maybeAddTranslationString(name);
        String def = ((JsonElement) ColorUtils.CODEC.encodeStart(JsonOps.INSTANCE, defaultValue).result().get()).getAsString();
        ForgeConfigSpec.ConfigValue<String> value = this.builder.define(name, def, o -> {
            if (o instanceof String s && ColorUtils.isValidString(s)) {
                return true;
            }
            return false;
        });
        this.currentValue = value;
        this.maybeAddGameRestart();
        var wrapper = new ConfigBuilderImpl.SpecialValue<Integer, String>(value) {

            Integer map(String value) {
                return (Integer) ((Pair) ColorUtils.CODEC.decode(JsonOps.INSTANCE, new JsonPrimitive(value)).get().left().get()).getFirst();
            }
        };
        this.specialValues.add(wrapper);
        return wrapper;
    }

    @Override
    public Supplier<String> define(String name, String defaultValue, Predicate<Object> validator) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.ConfigValue<String> value = this.builder.define(name, defaultValue, validator);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return value;
    }

    public <T> Supplier<T> define(String name, Supplier<T> defaultValue, Predicate<Object> validator) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.ConfigValue<T> value = this.builder.define(name, defaultValue, validator);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return value;
    }

    @Override
    public <T extends String> Supplier<List<String>> define(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.ConfigValue<? extends List<? extends T>> value = this.builder.defineList(name, defaultValue, predicate);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return () -> value.get();
    }

    @Override
    public <T> Supplier<T> defineObject(String name, com.google.common.base.Supplier<T> defaultSupplier, Codec<T> codec) {
        return (Supplier<T>) (this.usesDataBuddy ? ConfigHelper.defineObject(this.builder, name, codec, defaultSupplier) : ConfigBuilderImpl.StringCodecConfigValue.define(this, name, defaultSupplier, codec));
    }

    @Override
    public <T> Supplier<List<T>> defineObjectList(String name, com.google.common.base.Supplier<List<T>> defaultSupplier, Codec<T> codec) {
        this.builder.comment("This is a list. Add more entries with syntax [[...]]");
        return super.defineObjectList(name, defaultSupplier, codec);
    }

    public ConfigBuilderImpl.StringJsonConfigValue defineJson(String path, JsonElement defaultValue) {
        return ConfigBuilderImpl.StringJsonConfigValue.define(this, path, defaultValue);
    }

    public ConfigBuilderImpl.StringJsonConfigValue defineJson(String path, Supplier<JsonElement> defaultValue) {
        return ConfigBuilderImpl.StringJsonConfigValue.define(this, path, defaultValue);
    }

    @Override
    public <V extends Enum<V>> Supplier<V> define(String name, V defaultValue) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.EnumValue<V> value = this.builder.defineEnum(name, defaultValue);
        this.currentValue = value;
        this.maybeAddGameRestart();
        return value;
    }

    private void maybeAddGameRestart() {
        if (this.currentGameRestart && this.currentValue != null) {
            this.requireGameRestart.add(this.currentValue);
            this.currentGameRestart = false;
            this.currentValue = null;
        }
    }

    @Override
    public ConfigBuilder gameRestart() {
        this.currentGameRestart = true;
        return this;
    }

    @Override
    public ConfigBuilder worldReload() {
        this.builder.worldRestart();
        return this;
    }

    @Override
    public ConfigBuilder comment(String comment) {
        this.builder.comment(comment);
        return super.comment(comment);
    }

    abstract class SpecialValue<T, C> implements Supplier<T> {

        private final ForgeConfigSpec.ConfigValue<C> original;

        private T cachedValue = (T) null;

        SpecialValue(ForgeConfigSpec.ConfigValue<C> original) {
            this.original = original;
        }

        abstract T map(C var1);

        public void clearCache() {
            this.cachedValue = null;
        }

        public T get() {
            if (this.cachedValue == null) {
                this.cachedValue = this.map(this.original.get());
            }
            return this.cachedValue;
        }
    }

    @Deprecated(forRemoval = true)
    private static class StringCodecConfigValue<T> implements Supplier<T> {

        private final ConfigBuilderImpl.StringJsonConfigValue inner;

        private final Codec<T> codec;

        private T cache;

        public static <T> ConfigBuilderImpl.StringCodecConfigValue<T> define(ConfigBuilderImpl cfg, String name, Supplier<T> defaultValueSupplier, Codec<T> codec) {
            Supplier<JsonElement> jsonSupplier = () -> {
                DataResult<JsonElement> e = codec.encodeStart(JsonOps.INSTANCE, defaultValueSupplier.get());
                Optional<JsonElement> json = e.resultOrPartial(s -> {
                    throw new RuntimeException("Invalid default value for config " + name + ": " + s);
                });
                if (json.isEmpty()) {
                    throw new RuntimeException("Invalid default value for config " + name);
                } else {
                    return (JsonElement) json.get();
                }
            };
            ConfigBuilderImpl.StringJsonConfigValue jsonConfig = cfg.defineJson(name, jsonSupplier);
            return new ConfigBuilderImpl.StringCodecConfigValue<>(jsonConfig, codec);
        }

        public StringCodecConfigValue(ConfigBuilderImpl.StringJsonConfigValue jsonConfig, Codec<T> codec) {
            this.inner = jsonConfig;
            this.codec = codec;
        }

        public T get() {
            if (this.inner.hasBeenReset()) {
                this.cache = null;
            }
            if (this.cache == null) {
                JsonElement j = this.inner.get();
                DataResult<Pair<T, JsonElement>> d = this.codec.decode(JsonOps.INSTANCE, j);
                Optional<Pair<T, JsonElement>> o = d.resultOrPartial(s -> {
                    throw new RuntimeException("Failed to decode config: " + s);
                });
                if (o.isEmpty()) {
                    throw new RuntimeException("Failed to parse decode with value" + j);
                } else {
                    return (T) ((Pair) o.get()).getFirst();
                }
            } else {
                return null;
            }
        }
    }

    private static class StringJsonConfigValue implements Supplier<JsonElement> {

        private static final Field cachedValue = ObfuscationReflectionHelper.findField(ForgeConfigSpec.ConfigValue.class, "cachedValue");

        private final ForgeConfigSpec.ConfigValue<String> inner;

        private JsonElement cache = null;

        public static ConfigBuilderImpl.StringJsonConfigValue define(ConfigBuilderImpl cfg, String path, Supplier<JsonElement> defaultValueSupplier) {
            com.google.common.base.Supplier<JsonElement> lazyDefaultValue = Suppliers.memoize(defaultValueSupplier::get);
            return new ConfigBuilderImpl.StringJsonConfigValue(cfg.define(path, (Supplier<String>) (() -> ((JsonElement) lazyDefaultValue.get()).toString().replace(" ", "").replace("\"", "'")), o -> o != null && ((JsonElement) lazyDefaultValue.get()).getClass().isAssignableFrom(o.getClass())));
        }

        public static ConfigBuilderImpl.StringJsonConfigValue define(ConfigBuilderImpl cfg, String path, JsonElement defaultValue) {
            return new ConfigBuilderImpl.StringJsonConfigValue(cfg.define(path, defaultValue.toString().replace(" ", "").replace("\"", "'")));
        }

        StringJsonConfigValue(Supplier<String> innerConfig) {
            this.inner = (ForgeConfigSpec.ConfigValue<String>) innerConfig;
        }

        public JsonElement get() {
            if (this.hasBeenReset()) {
                this.cache = null;
            }
            if (this.cache == null) {
                String s = this.inner.get().replace("'", "\"");
                try {
                    this.cache = JsonParser.parseString(s);
                } catch (Exception var3) {
                    throw new RuntimeException("Failed to parse json config: ", var3);
                }
            }
            return this.cache;
        }

        public boolean hasBeenReset() {
            try {
                return cachedValue.get(this.inner) == null;
            } catch (IllegalAccessException var2) {
                throw new RuntimeException(var2);
            }
        }

        static {
            cachedValue.setAccessible(true);
        }
    }
}