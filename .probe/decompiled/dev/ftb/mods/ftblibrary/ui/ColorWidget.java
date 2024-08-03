package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

public class ColorWidget extends Widget {

    public final Color4I color;

    public Color4I mouseOverColor;

    public ColorWidget(Panel panel, Color4I c, @Nullable Color4I m) {
        super(panel);
        this.color = c;
        this.mouseOverColor = m;
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        (this.mouseOverColor != null && this.isMouseOver() ? this.mouseOverColor : this.color).draw(graphics, x, y, w, h);
    }
}