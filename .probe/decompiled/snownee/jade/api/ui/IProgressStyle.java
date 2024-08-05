package snownee.jade.api.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface IProgressStyle {

    default IProgressStyle color(int color) {
        return this.color(color, color);
    }

    IProgressStyle color(int var1, int var2);

    IProgressStyle textColor(int var1);

    IProgressStyle vertical(boolean var1);

    IProgressStyle overlay(IElement var1);

    void render(GuiGraphics var1, float var2, float var3, float var4, float var5, float var6, Component var7);
}