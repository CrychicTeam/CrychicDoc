package net.minecraftforge.client.gui.overlay;

import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface IGuiOverlay {

    void render(ForgeGui var1, GuiGraphics var2, float var3, int var4, int var5);
}