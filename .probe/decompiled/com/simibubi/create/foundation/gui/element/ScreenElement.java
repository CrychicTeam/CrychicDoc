package com.simibubi.create.foundation.gui.element;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ScreenElement {

    @OnlyIn(Dist.CLIENT)
    void render(GuiGraphics var1, int var2, int var3);
}