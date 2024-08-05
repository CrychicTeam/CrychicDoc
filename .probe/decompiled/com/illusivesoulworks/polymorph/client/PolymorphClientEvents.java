package com.illusivesoulworks.polymorph.client;

import com.illusivesoulworks.polymorph.api.client.base.ITickingRecipesWidget;
import com.illusivesoulworks.polymorph.client.recipe.RecipesWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public class PolymorphClientEvents {

    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        RecipesWidget.get().ifPresent(widget -> {
            if (mc.player == null || mc.screen == null) {
                RecipesWidget.clear();
            } else if (widget instanceof ITickingRecipesWidget) {
                ((ITickingRecipesWidget) widget).tick();
            }
        });
    }

    public static void initScreen(Screen screen) {
        if (screen instanceof AbstractContainerScreen) {
            RecipesWidget.create((AbstractContainerScreen<?>) screen);
        }
    }

    public static void render(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (screen instanceof AbstractContainerScreen) {
            RecipesWidget.get().ifPresent(recipeController -> recipeController.render(guiGraphics, mouseX, mouseY, partialTicks));
        }
    }

    public static boolean mouseClick(Screen screen, double mouseX, double mouseY, int button) {
        return screen instanceof AbstractContainerScreen ? (Boolean) RecipesWidget.get().map(recipeController -> recipeController.mouseClicked(mouseX, mouseY, button)).orElse(false) : false;
    }
}