package net.mehvahdjukaar.modelfix.moonlight_configs.forge;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigBuilder;
import net.mehvahdjukaar.modelfix.moonlight_configs.ConfigType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigBuilderImpl extends ConfigBuilder {

    private final ForgeConfigSpec.Builder builder;

    private String cat = null;

    public static ConfigBuilder create(ResourceLocation name, ConfigType type) {
        return new ConfigBuilderImpl(name, type);
    }

    public ConfigBuilderImpl(ResourceLocation name, ConfigType type) {
        super(name, type);
        this.builder = new ForgeConfigSpec.Builder();
    }

    @Override
    protected String currentCategory() {
        return this.cat;
    }

    public ConfigSpecWrapper build() {
        return new ConfigSpecWrapper(this.getName(), this.builder.build(), this.type, this.synced, this.changeCallback);
    }

    public ConfigBuilderImpl push(String category) {
        assert this.cat == null;
        this.builder.push(category);
        this.cat = category;
        return this;
    }

    public ConfigBuilderImpl pop() {
        assert this.cat != null;
        this.builder.pop();
        this.cat = null;
        return this;
    }

    @Override
    public Supplier<Boolean> define(String name, boolean defaultValue) {
        this.maybeAddTranslationString(name);
        return this.builder.define(name, defaultValue);
    }

    @Override
    public Supplier<Double> define(String name, double defaultValue, double min, double max) {
        this.maybeAddTranslationString(name);
        return this.builder.defineInRange(name, defaultValue, min, max);
    }

    @Override
    public Supplier<Integer> define(String name, int defaultValue, int min, int max) {
        this.maybeAddTranslationString(name);
        return this.builder.defineInRange(name, defaultValue, min, max);
    }

    @Override
    public Supplier<Integer> defineColor(String name, int defaultValue) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.ConfigValue<String> stringConfig = this.builder.define(name, Integer.toHexString(defaultValue), ConfigBuilder.COLOR_CHECK);
        return () -> Integer.parseUnsignedInt(stringConfig.get().replace("0x", ""), 16);
    }

    @Override
    public Supplier<String> define(String name, String defaultValue, Predicate<Object> validator) {
        this.maybeAddTranslationString(name);
        return this.builder.define(name, defaultValue, validator);
    }

    @Override
    public <T extends String> Supplier<List<String>> define(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        this.maybeAddTranslationString(name);
        ForgeConfigSpec.ConfigValue<? extends List<? extends T>> value = this.builder.defineList(name, defaultValue, predicate);
        return () -> value.get();
    }

    @Override
    public <T> Supplier<List<? extends T>> defineForgeList(String name, List<? extends T> defaultValue, Predicate<Object> predicate) {
        this.maybeAddTranslationString(name);
        return this.builder.defineList(name, defaultValue, predicate);
    }

    @Override
    public <V extends Enum<V>> Supplier<V> define(String name, V defaultValue) {
        this.maybeAddTranslationString(name);
        return this.builder.defineEnum(name, defaultValue);
    }

    @Override
    public ConfigBuilder comment(String comment) {
        this.builder.comment(comment);
        return super.comment(comment);
    }
}