package me.shedaniel.clothconfig2.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import me.shedaniel.clothconfig2.api.TabbedConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractTabbedConfigScreen extends AbstractConfigScreen implements TabbedConfigScreen {

    private final Map<String, ResourceLocation> categoryBackgroundLocation = Maps.newHashMap();

    protected AbstractTabbedConfigScreen(Screen parent, Component title, ResourceLocation backgroundLocation) {
        super(parent, title, backgroundLocation);
    }

    @Override
    public final void registerCategoryBackground(String text, ResourceLocation identifier) {
        this.categoryBackgroundLocation.put(text, identifier);
    }

    @Override
    public ResourceLocation getBackgroundLocation() {
        Component selectedCategory = this.getSelectedCategory();
        return this.categoryBackgroundLocation.containsKey(selectedCategory.getString()) ? (ResourceLocation) this.categoryBackgroundLocation.get(selectedCategory.getString()) : super.getBackgroundLocation();
    }
}