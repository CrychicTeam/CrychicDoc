package de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v1;

import java.util.function.Consumer;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RangeSliderButton extends ExtendedSliderButton {

    private static final Logger LOGGER = LogManager.getLogger();

    public double minValue;

    public double maxValue;

    public RangeSliderButton(int x, int y, int width, int height, boolean handleClick, double minRangeValue, double maxRangeValue, double selectedRangeValue, Consumer<ExtendedSliderButton> applyValueCallback) {
        super(x, y, width, height, handleClick, 0.0, applyValueCallback);
        this.minValue = minRangeValue;
        this.maxValue = maxRangeValue;
        this.setSelectedRangeValue(selectedRangeValue);
        this.m_5695_();
    }

    @Override
    public String getSliderMessageWithoutPrefixSuffix() {
        return this.getSelectedRangeValue() + "";
    }

    public int getSelectedRangeValue() {
        return (int) Mth.lerp(Mth.clamp(this.f_93577_, 0.0, 1.0), this.minValue, this.maxValue);
    }

    public double getSelectedRangeDoubleValue() {
        return Mth.lerp(Mth.clamp(this.f_93577_, 0.0, 1.0), this.minValue, this.maxValue);
    }

    public void setSelectedRangeValue(double rangeValue) {
        this.m_93611_((Mth.clamp(rangeValue, this.minValue, this.maxValue) - this.minValue) / (this.maxValue - this.minValue));
    }
}