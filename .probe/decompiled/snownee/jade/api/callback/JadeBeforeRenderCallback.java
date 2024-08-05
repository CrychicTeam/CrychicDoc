package snownee.jade.api.callback;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import org.apache.commons.lang3.mutable.MutableObject;
import snownee.jade.api.Accessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.theme.Theme;

@FunctionalInterface
public interface JadeBeforeRenderCallback {

    boolean beforeRender(ITooltip var1, Rect2i var2, GuiGraphics var3, Accessor<?> var4, JadeBeforeRenderCallback.ColorSetting var5);

    public static class ColorSetting {

        public float alpha;

        public MutableObject<Theme> theme;
    }
}