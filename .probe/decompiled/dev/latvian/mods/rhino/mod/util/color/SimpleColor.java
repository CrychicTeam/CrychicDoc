package dev.latvian.mods.rhino.mod.util.color;

import net.minecraft.network.chat.TextColor;

public class SimpleColor implements Color {

    public static final SimpleColor BLACK = new SimpleColor(-16777216);

    public static final SimpleColor WHITE = new SimpleColor(-1);

    private final int value;

    private TextColor textColor;

    public SimpleColor(int v) {
        this.value = 0xFF000000 | v;
    }

    @Override
    public int getArgbJS() {
        return this.value;
    }

    @Override
    public String getHexJS() {
        return String.format("#%06X", this.getRgbJS());
    }

    public String toString() {
        return this.getHexJS();
    }

    @Override
    public TextColor createTextColorJS() {
        if (this.textColor == null) {
            this.textColor = TextColor.fromRgb(this.getRgbJS());
        }
        return this.textColor;
    }
}