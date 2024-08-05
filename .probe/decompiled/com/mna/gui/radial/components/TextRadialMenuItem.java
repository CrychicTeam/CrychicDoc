package com.mna.gui.radial.components;

import com.mna.gui.radial.GenericRadialMenu;
import net.minecraft.network.chat.Component;

public class TextRadialMenuItem extends RadialMenuItem {

    private final Component text;

    private final int color;

    public Component getText() {
        return this.text;
    }

    public int getColor() {
        return this.color;
    }

    public TextRadialMenuItem(GenericRadialMenu owner, Component text) {
        this(owner, text, -1);
    }

    public TextRadialMenuItem(GenericRadialMenu owner, Component text, int color) {
        super(owner);
        this.text = text;
        this.color = color;
    }

    @Override
    public void draw(DrawingContext context) {
        String textString = this.text.getString();
        float x = context.x - (float) context.fontRenderer.width(textString) / 2.0F;
        float y = context.y - 9.0F / 2.0F;
        context.guiGraphics.drawString(context.fontRenderer, textString, x, y, this.color, true);
    }

    @Override
    public void drawTooltips(DrawingContext context) {
    }
}