package dev.latvian.mods.rhino.mod.util.color;

import net.minecraft.network.chat.TextColor;

public class SimpleColorWithAlpha implements Color {

    private final int value;

    private TextColor textColor;

    public SimpleColorWithAlpha(int v) {
        this.value = v;
    }

    @Override
    public int getArgbJS() {
        return this.value;
    }

    @Override
    public TextColor createTextColorJS() {
        if (this.textColor == null) {
            this.textColor = TextColor.fromRgb(this.getRgbJS());
        }
        return this.textColor;
    }

    public String toString() {
        return this.getHexJS();
    }
}