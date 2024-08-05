package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class RecipeBlank extends RecipeRendererBase {

    public RecipeBlank(int xIn, int yIn) {
        super(xIn, yIn, 213, 256);
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return null;
    }

    @Override
    protected void init_internal(ResourceLocation recipeLocation) {
    }

    @Override
    public int getTier() {
        return 0;
    }
}