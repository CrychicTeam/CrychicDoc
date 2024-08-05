package dev.ftb.mods.ftblibrary.icon;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface Drawable {

    @OnlyIn(Dist.CLIENT)
    void draw(GuiGraphics var1, int var2, int var3, int var4, int var5);

    @OnlyIn(Dist.CLIENT)
    default void drawStatic(GuiGraphics graphics, int x, int y, int w, int h) {
        this.draw(graphics, x, y, w, h);
    }

    @OnlyIn(Dist.CLIENT)
    default void draw3D(GuiGraphics graphics) {
        graphics.pose().pushPose();
        graphics.pose().scale(0.0625F, 0.0625F, 1.0F);
        this.draw(graphics, -8, -8, 16, 16);
        graphics.pose().popPose();
    }
}