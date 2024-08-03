package com.github.alexmodguy.alexscaves.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SpelunkeryTableDrawable implements IDrawable {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/gui/spelunkery_table_jei.png");

    private static final ResourceLocation TEXTURE_WIDGETS = new ResourceLocation("alexscaves", "textures/gui/spelunkery_table_widgets.png");

    @Override
    public int getWidth() {
        return 136;
    }

    @Override
    public int getHeight() {
        return 27;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        int i = xOffset;
        int j = yOffset;
        guiGraphics.blit(TEXTURE, xOffset, yOffset, 0.0F, 0.0F, this.getWidth(), this.getHeight(), 256, 256);
        int bulbs = Minecraft.getInstance().player.f_19797_ % 40 / 10;
        for (int bulb = 0; bulb < bulbs; bulb++) {
            guiGraphics.blit(TEXTURE_WIDGETS, i + 56 + bulb * 15, j + 7, 0, 0, 13, 14);
        }
    }
}