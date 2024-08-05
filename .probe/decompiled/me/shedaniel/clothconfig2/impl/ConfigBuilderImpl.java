package me.shedaniel.clothconfig2.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.Expandable;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import me.shedaniel.clothconfig2.gui.ClothConfigScreen;
import me.shedaniel.clothconfig2.gui.GlobalizedClothConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
@Internal
public class ConfigBuilderImpl implements ConfigBuilder {

    private Runnable savingRunnable;

    private Screen parent;

    private Component title = Component.translatable("text.cloth-config.config");

    private boolean globalized = false;

    private boolean globalizedExpanded = true;

    private boolean editable = true;

    private boolean tabsSmoothScroll = true;

    private boolean listSmoothScroll = true;

    private boolean doesConfirmSave = true;

    private boolean transparentBackground = false;

    private ResourceLocation defaultBackground = Screen.BACKGROUND_LOCATION;

    private Consumer<Screen> afterInitConsumer = screen -> {
    };

    private final Map<String, ConfigCategory> categoryMap = Maps.newLinkedHashMap();

    private String fallbackCategory = null;

    private boolean alwaysShowTabs = false;

    @Override
    public void setGlobalized(boolean globalized) {
        this.globalized = globalized;
    }

    @Override
    public void setGlobalizedExpanded(boolean globalizedExpanded) {
        this.globalizedExpanded = globalizedExpanded;
    }

    @Override
    public boolean isAlwaysShowTabs() {
        return this.alwaysShowTabs;
    }

    @Override
    public ConfigBuilder setAlwaysShowTabs(boolean alwaysShowTabs) {
        this.alwaysShowTabs = alwaysShowTabs;
        return this;
    }

    @Override
    public ConfigBuilder setTransparentBackground(boolean transparentBackground) {
        this.transparentBackground = transparentBackground;
        return this;
    }

    @Override
    public boolean hasTransparentBackground() {
        return this.transparentBackground;
    }

    @Override
    public ConfigBuilder setAfterInitConsumer(Consumer<Screen> afterInitConsumer) {
        this.afterInitConsumer = afterInitConsumer;
        return this;
    }

    @Override
    public ConfigBuilder setFallbackCategory(ConfigCategory fallbackCategory) {
        this.fallbackCategory = ((ConfigCategory) Objects.requireNonNull(fallbackCategory)).getCategoryKey().getString();
        return this;
    }

    @Override
    public Screen getParentScreen() {
        return this.parent;
    }

    @Override
    public ConfigBuilder setParentScreen(Screen parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public ConfigBuilder setTitle(Component title) {
        this.title = title;
        return this;
    }

    @Override
    public boolean isEditable() {
        return this.editable;
    }

    @Override
    public ConfigBuilder setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    @Override
    public ConfigCategory getOrCreateCategory(Component categoryKey) {
        if (this.categoryMap.containsKey(categoryKey.getString())) {
            return (ConfigCategory) this.categoryMap.get(categoryKey.getString());
        } else {
            if (this.fallbackCategory == null) {
                this.fallbackCategory = categoryKey.getString();
            }
            return (ConfigCategory) this.categoryMap.computeIfAbsent(categoryKey.getString(), key -> new ConfigCategoryImpl(this, categoryKey));
        }
    }

    @Override
    public ConfigBuilder removeCategory(Component category) {
        if (this.categoryMap.containsKey(category.getString()) && Objects.equals(this.fallbackCategory, category.getString())) {
            this.fallbackCategory = null;
        }
        if (!this.categoryMap.containsKey(category.getString())) {
            throw new NullPointerException("Category doesn't exist!");
        } else {
            this.categoryMap.remove(category.getString());
            return this;
        }
    }

    @Override
    public ConfigBuilder removeCategoryIfExists(Component category) {
        if (this.categoryMap.containsKey(category.getString()) && Objects.equals(this.fallbackCategory, category.getString())) {
            this.fallbackCategory = null;
        }
        this.categoryMap.remove(category.getString());
        return this;
    }

    @Override
    public boolean hasCategory(Component category) {
        return this.categoryMap.containsKey(category.getString());
    }

    @Override
    public ConfigBuilder setShouldTabsSmoothScroll(boolean shouldTabsSmoothScroll) {
        this.tabsSmoothScroll = shouldTabsSmoothScroll;
        return this;
    }

    @Override
    public boolean isTabsSmoothScrolling() {
        return this.tabsSmoothScroll;
    }

    @Override
    public ConfigBuilder setShouldListSmoothScroll(boolean shouldListSmoothScroll) {
        this.listSmoothScroll = shouldListSmoothScroll;
        return this;
    }

    @Override
    public boolean isListSmoothScrolling() {
        return this.listSmoothScroll;
    }

    @Override
    public ConfigBuilder setDoesConfirmSave(boolean confirmSave) {
        this.doesConfirmSave = confirmSave;
        return this;
    }

    @Override
    public boolean doesConfirmSave() {
        return this.doesConfirmSave;
    }

    @Override
    public ResourceLocation getDefaultBackgroundTexture() {
        return this.defaultBackground;
    }

    @Override
    public ConfigBuilder setDefaultBackgroundTexture(ResourceLocation texture) {
        this.defaultBackground = texture;
        return this;
    }

    @Override
    public ConfigBuilder setSavingRunnable(Runnable runnable) {
        this.savingRunnable = runnable;
        return this;
    }

    @Override
    public Consumer<Screen> getAfterInitConsumer() {
        return this.afterInitConsumer;
    }

    @Override
    public Screen build() {
        if (!this.categoryMap.isEmpty() && this.fallbackCategory != null) {
            AbstractConfigScreen screen;
            if (this.globalized) {
                screen = new GlobalizedClothConfigScreen(this.parent, this.title, this.categoryMap, this.defaultBackground);
            } else {
                screen = new ClothConfigScreen(this.parent, this.title, this.categoryMap, this.defaultBackground);
            }
            screen.setSavingRunnable(this.savingRunnable);
            screen.setEditable(this.editable);
            screen.setFallbackCategory(this.fallbackCategory == null ? null : Component.literal(this.fallbackCategory));
            screen.setTransparentBackground(this.transparentBackground);
            screen.setAlwaysShowTabs(this.alwaysShowTabs);
            screen.setConfirmSave(this.doesConfirmSave);
            screen.setAfterInitConsumer(this.afterInitConsumer);
            if (screen instanceof Expandable) {
                ((Expandable) screen).setExpanded(this.globalizedExpanded);
            }
            return screen;
        } else {
            throw new NullPointerException("There cannot be no categories or fallback category!");
        }
    }

    @Override
    public Runnable getSavingRunnable() {
        return this.savingRunnable;
    }
}