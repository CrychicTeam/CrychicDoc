package net.mehvahdjukaar.modelfix.moonlight_configs;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mehvahdjukaar.modelfix.moonlight_configs.forge.ConfigBuilderImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class ConfigBuilder {

    protected final Map<String, String> comments = new HashMap();

    private String currentComment;

    private String currentKey;

    protected boolean synced;

    protected Runnable changeCallback;

    private final ResourceLocation name;

    protected final ConfigType type;

    public static final Predicate<Object> STRING_CHECK = o -> o instanceof String;

    public static final Predicate<Object> LIST_STRING_CHECK = s -> s instanceof List ? ((Collection) s).stream().allMatch(o -> o instanceof String) : false;

    public static final Predicate<Object> COLOR_CHECK = s -> {
        try {
            Integer.parseUnsignedInt(((String) s).replace("0x", ""), 16);
            return true;
        } catch (Exception var2) {
            return false;
        }
    };

    @ExpectPlatform
    @Transformed
    public static ConfigBuilder create(ResourceLocation name, ConfigType type) {
        return ConfigBuilderImpl.create(name, type);
    }

    public ConfigBuilder(ResourceLocation name, ConfigType type) {
        this.name = name;
        this.type = type;
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

    public abstract Supplier<Boolean> define(String var1, boolean var2);

    public abstract Supplier<Double> define(String var1, double var2, double var4, double var6);

    public abstract Supplier<Integer> define(String var1, int var2, int var3, int var4);

    public abstract Supplier<Integer> defineColor(String var1, int var2);

    public abstract Supplier<String> define(String var1, String var2, Predicate<Object> var3);

    public Supplier<String> define(String name, String defaultValue) {
        return this.define(name, defaultValue, STRING_CHECK);
    }

    public <T extends String> Supplier<List<String>> define(String name, List<? extends T> defaultValue) {
        return this.define(name, defaultValue, s -> true);
    }

    protected abstract String currentCategory();

    public abstract <T extends String> Supplier<List<String>> define(String var1, List<? extends T> var2, Predicate<Object> var3);

    public abstract <V extends Enum<V>> Supplier<V> define(String var1, V var2);

    @Deprecated(forRemoval = true)
    public abstract <T> Supplier<List<? extends T>> defineForgeList(String var1, List<? extends T> var2, Predicate<Object> var3);

    public Component description(String name) {
        return Component.translatable(this.translationKey(name));
    }

    public Component tooltip(String name) {
        return Component.translatable(this.tooltipKey(name));
    }

    public String tooltipKey(String name) {
        return name;
    }

    public String translationKey(String name) {
        return name;
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

    protected void maybeAddTranslationString(String name) {
        this.currentKey = this.tooltipKey(name);
        if (this.currentComment != null && this.currentKey != null) {
            this.comments.put(this.currentKey, this.currentComment);
            this.currentComment = null;
            this.currentKey = null;
        }
    }
}