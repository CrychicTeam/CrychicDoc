package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class AbstractStringWidget extends AbstractWidget {

    private final Font font;

    private int color = 16777215;

    public AbstractStringWidget(int int0, int int1, int int2, int int3, Component component4, Font font5) {
        super(int0, int1, int2, int3, component4);
        this.font = font5;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    public AbstractStringWidget setColor(int int0) {
        this.color = int0;
        return this;
    }

    protected final Font getFont() {
        return this.font;
    }

    protected final int getColor() {
        return this.color;
    }
}