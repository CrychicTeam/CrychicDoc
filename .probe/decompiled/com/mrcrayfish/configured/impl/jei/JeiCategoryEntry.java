package com.mrcrayfish.configured.impl.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mrcrayfish.configured.api.IConfigEntry;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.ValueEntry;
import java.util.List;
import java.util.Objects;
import mezz.jei.api.runtime.config.IJeiConfigCategory;
import mezz.jei.api.runtime.config.IJeiConfigValue;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class JeiCategoryEntry implements IConfigEntry {

    private final IJeiConfigCategory category;

    @Nullable
    private List<IConfigEntry> entries;

    public JeiCategoryEntry(IJeiConfigCategory category) {
        this.category = category;
    }

    @Override
    public List<IConfigEntry> getChildren() {
        if (this.entries == null) {
            Builder<IConfigEntry> builder = ImmutableList.builder();
            this.category.getConfigValues().forEach(configValue -> {
                Objects.requireNonNull(configValue);
                builder.add(new ValueEntry(this.createJeiValue(configValue)));
            });
            this.entries = builder.build();
        }
        return this.entries;
    }

    private IConfigValue<?> createJeiValue(IJeiConfigValue<?> configValue) {
        return (IConfigValue<?>) (configValue.getDefaultValue() instanceof List ? new JeiListValue(configValue) : new JeiValue<>(configValue));
    }

    @Override
    public boolean isRoot() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Nullable
    @Override
    public IConfigValue<?> getValue() {
        return null;
    }

    @Override
    public String getEntryName() {
        return this.category.getName();
    }

    @Nullable
    @Override
    public Component getTooltip() {
        return null;
    }

    @Nullable
    @Override
    public String getTranslationKey() {
        return null;
    }
}