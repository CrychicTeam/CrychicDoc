package com.mna.gui.widgets;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class ForgeSliderNotifiable extends ForgeSlider {

    final Consumer<ForgeSliderNotifiable> onChange;

    public ForgeSliderNotifiable(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString, Consumer<ForgeSliderNotifiable> onChange) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
        this.onChange = onChange;
    }

    public ForgeSliderNotifiable(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString, Consumer<ForgeSliderNotifiable> onChange) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
        this.onChange = onChange;
    }
}