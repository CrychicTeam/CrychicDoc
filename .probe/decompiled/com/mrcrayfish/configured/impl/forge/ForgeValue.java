package com.mrcrayfish.configured.impl.forge;

import com.mrcrayfish.configured.api.IConfigValue;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;

public class ForgeValue<T> implements IConfigValue<T> {

    public final ForgeConfigSpec.ConfigValue<T> configValue;

    public final ForgeConfigSpec.ValueSpec valueSpec;

    protected final T initialValue;

    protected T value;

    protected Pair<T, T> range;

    protected Component validationHint;

    public ForgeValue(ForgeConfigSpec.ConfigValue<T> configValue, ForgeConfigSpec.ValueSpec valueSpec) {
        this.configValue = configValue;
        this.valueSpec = valueSpec;
        this.initialValue = configValue.get();
        this.set(configValue.get());
    }

    @Override
    public T get() {
        return this.value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public boolean isDefault() {
        return Objects.equals(this.get(), this.valueSpec.getDefault());
    }

    @Override
    public boolean isChanged() {
        return !Objects.equals(this.get(), this.initialValue);
    }

    @Override
    public void restore() {
        this.set(this.getDefault());
    }

    @Override
    public T getDefault() {
        return (T) this.valueSpec.getDefault();
    }

    @Override
    public boolean isValid(T value) {
        return this.valueSpec.test(value);
    }

    @Nullable
    @Override
    public Component getComment() {
        String rawComment = this.valueSpec.getComment();
        String key = this.getTranslationKey() + ".tooltip";
        if (!I18n.exists(key)) {
            return rawComment != null ? Component.literal(rawComment) : null;
        } else {
            MutableComponent comment = Component.translatable(key);
            if (rawComment != null) {
                int rangeIndex = rawComment.indexOf("Range: ");
                int allowedValIndex = rawComment.indexOf("Allowed Values: ");
                if (rangeIndex >= 0 || allowedValIndex >= 0) {
                    comment.append(Component.literal(rawComment.substring(Math.max(rangeIndex, allowedValIndex) - 1)));
                }
            }
            return comment;
        }
    }

    @Override
    public String getTranslationKey() {
        return this.valueSpec.getTranslationKey();
    }

    @Nullable
    @Override
    public Component getValidationHint() {
        if (this.validationHint == null) {
            this.loadRange();
            if (this.range != null && this.range.getLeft() != null && this.range.getRight() != null) {
                this.validationHint = Component.translatable("configured.validator.range_hint", this.range.getLeft().toString(), this.range.getRight().toString());
            }
        }
        return this.validationHint;
    }

    @Override
    public String getName() {
        return lastValue(this.configValue.getPath(), "");
    }

    @Override
    public void cleanCache() {
        this.configValue.clearCache();
    }

    @Override
    public boolean requiresWorldRestart() {
        return this.valueSpec.needsWorldRestart();
    }

    @Override
    public boolean requiresGameRestart() {
        return false;
    }

    public static <V> V lastValue(List<V> list, V defaultValue) {
        return (V) (list.size() > 0 ? list.get(list.size() - 1) : defaultValue);
    }

    public void loadRange() {
        if (this.range == null) {
            try {
                Object range = ObfuscationReflectionHelper.getPrivateValue(ForgeConfigSpec.ValueSpec.class, this.valueSpec, "range");
                if (range != null) {
                    Class rangeClass = Class.forName("net.minecraftforge.common.ForgeConfigSpec$Range");
                    Object min = ObfuscationReflectionHelper.getPrivateValue(rangeClass, range, "min");
                    Object max = ObfuscationReflectionHelper.getPrivateValue(rangeClass, range, "max");
                    this.range = Pair.of(min, max);
                    return;
                }
            } catch (ClassNotFoundException var5) {
            }
            this.range = Pair.of(null, null);
        }
    }
}