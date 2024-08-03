package com.mna.gui.widgets;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class MASliderNotifiable extends ForgeSlider {

    final Consumer<MASliderNotifiable> onChange;

    public MASliderNotifiable(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString, Consumer<MASliderNotifiable> onChange) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
        this.onChange = onChange;
    }

    public MASliderNotifiable(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString, Consumer<MASliderNotifiable> onChange) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
        this.onChange = onChange;
    }

    @Override
    protected void applyValue() {
        if (this.onChange != null) {
            this.onChange.accept(this);
        }
    }
}