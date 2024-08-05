package com.mna.gui.radial.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface IDrawingHelper {

    void renderTooltip(GuiGraphics var1, ItemStack var2, int var3, int var4);

    void renderTooltip(GuiGraphics var1, Component var2, int var3, int var4);
}