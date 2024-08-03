package com.simibubi.create.compat.jei;

import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenResourceWrapper implements IDrawable {

    private AllGuiTextures resource;

    public ScreenResourceWrapper(AllGuiTextures resource) {
        this.resource = resource;
    }

    @Override
    public int getWidth() {
        return this.resource.width;
    }

    @Override
    public int getHeight() {
        return this.resource.height;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        graphics.blit(this.resource.location, xOffset, yOffset, 0, (float) this.resource.startX, (float) this.resource.startY, this.resource.width, this.resource.height, 256, 256);
    }
}