package journeymap.client.render.draw;

import journeymap.client.render.map.GridRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;

public interface DrawStep {

    void draw(GuiGraphics var1, MultiBufferSource var2, DrawStep.Pass var3, double var4, double var6, GridRenderer var8, double var9, double var11);

    int getDisplayOrder();

    String getModId();

    public static enum Pass {

        Object, Text, Tooltip
    }
}