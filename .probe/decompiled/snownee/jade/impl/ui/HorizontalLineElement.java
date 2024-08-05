package snownee.jade.impl.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.util.Color;

public class HorizontalLineElement extends Element {

    public int color = IThemeHelper.get().getNormalColor();

    @Override
    public Vec2 getSize() {
        return new Vec2(10.0F, 4.0F);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        y = (y + maxY - 0.5F) / 2.0F;
        DisplayHelper.fill(guiGraphics, x + 2.0F, y, maxX - 2.0F, y + 0.5F, this.color);
        if (IThemeHelper.get().theme().textShadow) {
            x += 0.5F;
            y += 0.5F;
            maxX += 0.5F;
            Color shadow = Color.rgb(this.color);
            shadow = Color.rgb(shadow.getRed() / 4, shadow.getGreen() / 4, shadow.getBlue() / 4, shadow.getOpacity());
            DisplayHelper.fill(guiGraphics, x + 2.0F, y, maxX - 2.0F, y + 0.5F, shadow.toInt());
        }
    }
}