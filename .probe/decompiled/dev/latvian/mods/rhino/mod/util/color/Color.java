package dev.latvian.mods.rhino.mod.util.color;

import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import dev.latvian.mods.rhino.util.SpecialEquality;
import net.minecraft.network.chat.TextColor;

public interface Color extends SpecialEquality {

    int getArgbJS();

    default int getRgbJS() {
        return this.getArgbJS() & 16777215;
    }

    default int getFireworkColorJS() {
        return this.getRgbJS();
    }

    default String getHexJS() {
        return String.format("#%08X", this.getArgbJS());
    }

    default String getSerializeJS() {
        return this.getHexJS();
    }

    default TextColor createTextColorJS() {
        return TextColor.fromRgb(this.getRgbJS());
    }

    @Override
    default boolean specialEquals(Object o, boolean shallow) {
        Color c = ColorWrapper.of(o);
        return shallow ? this.getArgbJS() == c.getArgbJS() : this.getRgbJS() == c.getRgbJS();
    }
}