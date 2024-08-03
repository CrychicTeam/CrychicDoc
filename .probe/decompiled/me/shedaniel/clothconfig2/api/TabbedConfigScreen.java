package me.shedaniel.clothconfig2.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface TabbedConfigScreen extends ConfigScreen {

    void registerCategoryBackground(String var1, ResourceLocation var2);

    Component getSelectedCategory();
}