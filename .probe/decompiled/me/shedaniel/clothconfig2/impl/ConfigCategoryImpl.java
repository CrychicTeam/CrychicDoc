package me.shedaniel.clothconfig2.impl;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ConfigCategoryImpl implements ConfigCategory {

    private final ConfigBuilder builder;

    private final List<Object> data;

    @Nullable
    private ResourceLocation background;

    private final Component categoryKey;

    @Nullable
    private Supplier<Optional<FormattedText[]>> description = Optional::empty;

    ConfigCategoryImpl(ConfigBuilder builder, Component categoryKey) {
        this.builder = builder;
        this.data = Lists.newArrayList();
        this.categoryKey = categoryKey;
    }

    @Override
    public Component getCategoryKey() {
        return this.categoryKey;
    }

    @Override
    public List<Object> getEntries() {
        return this.data;
    }

    @Override
    public ConfigCategory addEntry(AbstractConfigListEntry entry) {
        this.data.add(entry);
        return this;
    }

    @Override
    public ConfigCategory setCategoryBackground(ResourceLocation identifier) {
        if (this.builder.hasTransparentBackground()) {
            throw new IllegalStateException("Cannot set category background if screen is using transparent background.");
        } else {
            this.background = identifier;
            return this;
        }
    }

    @Override
    public void removeCategory() {
        this.builder.removeCategory(this.categoryKey);
    }

    @Override
    public void setBackground(@Nullable ResourceLocation background) {
        this.background = background;
    }

    @Nullable
    @Override
    public ResourceLocation getBackground() {
        return this.background;
    }

    @Nullable
    @Override
    public Supplier<Optional<FormattedText[]>> getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(@Nullable Supplier<Optional<FormattedText[]>> description) {
        this.description = description;
    }
}