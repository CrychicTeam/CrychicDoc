package net.mehvahdjukaar.moonlight.api.platform.configs;

import com.google.common.base.Supplier;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.events.MoonlightEventsHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.forge.ConfigBuilderImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class ConfigBuilder {

    protected final Map<String, String> comments = new HashMap();

    private String currentComment;

    private String currentKey;

    protected boolean synced;

    protected Runnable changeCallback;

    protected boolean usesDataBuddy = true;

    private final ResourceLocation name;

    protected final ConfigType type;

    public static final Predicate<Object> STRING_CHECK = o -> o instanceof String;

    public static final Predicate<Object> LIST_STRING_CHECK = s -> s instanceof List ? ((Collection) s).stream().allMatch(o -> o instanceof String) : false;

    @ExpectPlatform
    @Transformed
    public static ConfigBuilder create(ResourceLocation name, ConfigType type) {
        return ConfigBuilderImpl.create(name, type);
    }

    public static ConfigBuilder create(String modId, ConfigType type) {
        return create(new ResourceLocation(modId, type.toString().toLowerCase(Locale.ROOT)), type);
    }

    protected ConfigBuilder(ResourceLocation name, ConfigType type) {
        this.name = name;
        this.type = type;
        Consumer<AfterLanguageLoadEvent> consumer = e -> {
            if (e.isDefault()) {
                this.comments.forEach(e::addEntry);
            }
        };
        MoonlightEventsHelper.addListener(consumer, AfterLanguageLoadEvent.class);
    }

    public ConfigSpec buildAndRegister() {
        ConfigSpec spec = this.build();
        spec.register();
        return spec;
    }

    public abstract ConfigSpec build();

    public ResourceLocation getName() {
        return this.name;
    }

    public abstract ConfigBuilder push(String var1);

    public abstract ConfigBuilder pop();

    public <T extends ConfigBuilder> T setWriteJsons() {
        this.usesDataBuddy = false;
        return (T) this;
    }

    public abstract java.util.function.Supplier<Boolean> define(String var1, boolean var2);

    public abstract java.util.function.Supplier<Double> define(String var1, double var2, double var4, double var6);

    public abstract java.util.function.Supplier<Integer> define(String var1, int var2, int var3, int var4);

    public abstract java.util.function.Supplier<Integer> defineColor(String var1, int var2);

    public abstract java.util.function.Supplier<String> define(String var1, String var2, Predicate<Object> var3);

    public java.util.function.Supplier<String> define(String name, String defaultValue) {
        return this.define(name, defaultValue, STRING_CHECK);
    }

    public <T extends String> java.util.function.Supplier<List<String>> define(String name, List<? extends T> defaultValue) {
        return this.define(name, defaultValue, s -> true);
    }

    public abstract String currentCategory();

    public abstract <T extends String> java.util.function.Supplier<List<String>> define(String var1, List<? extends T> var2, Predicate<Object> var3);

    public abstract <V extends Enum<V>> java.util.function.Supplier<V> define(String var1, V var2);

    public abstract <T> java.util.function.Supplier<T> defineObject(String var1, Supplier<T> var2, Codec<T> var3);

    public <T> java.util.function.Supplier<List<T>> defineObjectList(String name, Supplier<List<T>> defaultSupplier, Codec<T> codec) {
        return this.defineObject(name, defaultSupplier, codec.listOf());
    }

    public java.util.function.Supplier<Map<String, String>> defineMap(String name, Map<String, String> def) {
        return this.defineObject(name, () -> def, Codec.unboundedMap(Codec.STRING, Codec.STRING));
    }

    public java.util.function.Supplier<Map<ResourceLocation, ResourceLocation>> defineIDMap(String name, Map<ResourceLocation, ResourceLocation> def) {
        return this.defineObject(name, () -> def, Codec.unboundedMap(ResourceLocation.CODEC, ResourceLocation.CODEC));
    }

    public abstract java.util.function.Supplier<JsonElement> defineJson(String var1, JsonElement var2);

    public abstract java.util.function.Supplier<JsonElement> defineJson(String var1, java.util.function.Supplier<JsonElement> var2);

    public java.util.function.Supplier<ResourceLocation> define(String name, ResourceLocation defaultValue) {
        return new ConfigBuilder.ResourceLocationConfigValue(this, name, defaultValue);
    }

    public Component description(String name) {
        return Component.translatable(this.translationKey(name));
    }

    public Component tooltip(String name) {
        return Component.translatable(this.tooltipKey(name));
    }

    public String tooltipKey(String name) {
        return "config." + this.name.getNamespace() + "." + this.currentCategory() + "." + name + ".description";
    }

    public String translationKey(String name) {
        return "config." + this.name.getNamespace() + "." + this.currentCategory() + "." + name;
    }

    public ConfigBuilder comment(String comment) {
        this.currentComment = comment;
        if (this.currentComment != null && this.currentKey != null) {
            this.comments.put(this.currentKey, this.currentComment);
            this.currentComment = null;
            this.currentKey = null;
        }
        return this;
    }

    public ConfigBuilder setSynced() {
        if (this.type == ConfigType.CLIENT) {
            throw new UnsupportedOperationException("Config syncing cannot be used for client config as its not needed");
        } else {
            this.synced = true;
            return this;
        }
    }

    public ConfigBuilder onChange(Runnable callback) {
        this.changeCallback = callback;
        return this;
    }

    public abstract ConfigBuilder worldReload();

    public abstract ConfigBuilder gameRestart();

    protected void maybeAddTranslationString(String name) {
        this.currentKey = this.tooltipKey(name);
        if (this.currentComment != null && this.currentKey != null) {
            this.comments.put(this.currentKey, this.currentComment);
            this.currentComment = null;
            this.currentKey = null;
        }
        if (this.currentCategory() == null && PlatHelper.isDev()) {
            throw new AssertionError();
        }
    }

    private static class ResourceLocationConfigValue implements java.util.function.Supplier<ResourceLocation> {

        private final java.util.function.Supplier<String> inner;

        private ResourceLocation cache;

        private String oldString;

        public ResourceLocationConfigValue(ConfigBuilder builder, String path, ResourceLocation defaultValue) {
            this.inner = builder.define(path, defaultValue.toString(), s -> s != null && ResourceLocation.isValidResourceLocation((String) s));
        }

        public ResourceLocation get() {
            String s = (String) this.inner.get();
            if (!s.equals(this.oldString)) {
                this.cache = null;
            }
            this.oldString = s;
            if (this.cache == null) {
                this.cache = new ResourceLocation(s);
            }
            return this.cache;
        }
    }
}