package com.illusivesoulworks.polymorph.client.recipe;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.client.recipe.widget.PersistentRecipesWidget;
import com.illusivesoulworks.polymorph.client.recipe.widget.PlayerRecipesWidget;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public class RecipesWidget {

    private static IRecipesWidget widget = null;

    private static Screen lastScreen = null;

    private static Pair<SortedSet<IRecipePair>, ResourceLocation> pendingData = null;

    public static Optional<IRecipesWidget> get() {
        return Optional.ofNullable(widget);
    }

    public static void enqueueRecipesList(SortedSet<IRecipePair> recipesList, ResourceLocation resourceLocation) {
        pendingData = new Pair(recipesList, resourceLocation);
    }

    public static void create(AbstractContainerScreen<?> containerScreen) {
        if (containerScreen != lastScreen || widget == null) {
            widget = null;
            Optional<IRecipesWidget> maybeWidget = PolymorphApi.client().getWidget(containerScreen);
            maybeWidget.ifPresent(newWidget -> widget = newWidget);
            if (widget == null) {
                PolymorphApi.client().findCraftingResultSlot(containerScreen).ifPresent(slot -> widget = new PlayerRecipesWidget(containerScreen, slot));
            }
            if (widget != null) {
                if (widget instanceof PersistentRecipesWidget && Minecraft.getInstance().getConnection() != null) {
                    PolymorphApi.common().getPacketDistributor().sendBlockEntityListenerC2S(true);
                }
                widget.initChildWidgets();
                lastScreen = containerScreen;
                if (pendingData != null) {
                    widget.setRecipesList((Set<IRecipePair>) pendingData.getFirst(), (ResourceLocation) pendingData.getSecond());
                }
            } else {
                lastScreen = null;
            }
            pendingData = null;
        }
    }

    public static void clear() {
        if (widget instanceof PersistentRecipesWidget && Minecraft.getInstance().getConnection() != null) {
            PolymorphApi.common().getPacketDistributor().sendBlockEntityListenerC2S(false);
        }
        widget = null;
        lastScreen = null;
    }
}