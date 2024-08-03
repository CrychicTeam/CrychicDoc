package com.github.alexmodguy.alexscaves.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class NuclearFurnaceDrawable implements IDrawable {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/gui/nuclear_furnace.png");

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 60;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        guiGraphics.blit(TEXTURE, xOffset, yOffset, 5.0F, 15.0F, this.getWidth(), this.getHeight(), 256, 256);
        int ticks = Minecraft.getInstance().player.f_19797_;
        int cookPixels = (int) Math.ceil((double) (24.0F * ((float) ((ticks + 40) % 20) / 20.0F)));
        int fillAnimateTime = ticks % 100;
        if (fillAnimateTime < 70) {
            int barrelPixels = (int) Math.ceil((double) (14.0F * ((float) fillAnimateTime / 70.0F)));
            guiGraphics.blit(TEXTURE, xOffset + 33, yOffset + 21 + (14 - barrelPixels), 192, 14 - barrelPixels, 15, barrelPixels);
            int wastePixels = 5;
            guiGraphics.blit(TEXTURE, xOffset + 8, yOffset + 2 + (52 - wastePixels), 176, 32 + (52 - wastePixels), 16, wastePixels);
        }
        guiGraphics.blit(TEXTURE, xOffset + 86, yOffset + 20, 176, 14, cookPixels, 17);
        guiGraphics.blit(TEXTURE, xOffset + 63, yOffset + 21, 176, 0, 14, 14);
    }
}