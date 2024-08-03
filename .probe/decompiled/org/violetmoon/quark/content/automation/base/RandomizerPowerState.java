package org.violetmoon.quark.content.automation.base;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum RandomizerPowerState implements StringRepresentable {

    OFF, LEFT, RIGHT;

    @NotNull
    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}