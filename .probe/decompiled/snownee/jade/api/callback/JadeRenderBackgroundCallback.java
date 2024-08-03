package snownee.jade.api.callback;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import snownee.jade.api.Accessor;
import snownee.jade.api.ui.ITooltipRenderer;

@FunctionalInterface
public interface JadeRenderBackgroundCallback {

    boolean onRender(ITooltipRenderer var1, Rect2i var2, GuiGraphics var3, Accessor<?> var4, JadeBeforeRenderCallback.ColorSetting var5);
}