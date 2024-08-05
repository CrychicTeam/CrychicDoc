package net.minecraft.client.gui.components;

import net.minecraft.client.Options;
import net.minecraft.network.chat.CommonComponents;

public abstract class AbstractOptionSliderButton extends AbstractSliderButton {

    protected final Options options;

    protected AbstractOptionSliderButton(Options options0, int int1, int int2, int int3, int int4, double double5) {
        super(int1, int2, int3, int4, CommonComponents.EMPTY, double5);
        this.options = options0;
    }
}