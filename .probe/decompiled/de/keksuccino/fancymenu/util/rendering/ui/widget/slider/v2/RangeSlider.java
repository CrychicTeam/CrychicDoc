package de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v2;

import de.keksuccino.fancymenu.util.MathUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RangeSlider extends AbstractExtendedSlider {

    protected double minRangeValue;

    protected double maxRangeValue;

    protected boolean showAsInteger = false;

    protected int roundingDecimalPlace = 2;

    public RangeSlider(int x, int y, int width, int height, Component label, double minRangeValue, double maxRangeValue, double preSelectedRangeValue) {
        super(x, y, width, height, label, 0.0);
        this.minRangeValue = minRangeValue;
        this.maxRangeValue = maxRangeValue;
        this.setRangeValue(preSelectedRangeValue);
    }

    @NotNull
    @Override
    public String getValueDisplayText() {
        return this.showAsInteger() ? this.getIntegerRangeValue() + "" : this.getRangeValue() + "";
    }

    public int getIntegerRangeValue() {
        return (int) this.getRangeValue();
    }

    public double getRangeValue() {
        double d = Mth.lerp(Mth.clamp(this.f_93577_, 0.0, 1.0), this.minRangeValue, this.maxRangeValue);
        return this.roundingDecimalPlace < 0 ? d : MathUtils.round(d, this.roundingDecimalPlace);
    }

    public RangeSlider setRangeValue(double rangeValue) {
        rangeValue = Math.min(this.maxRangeValue, Math.max(this.minRangeValue, rangeValue));
        if (rangeValue == this.maxRangeValue) {
            this.m_93611_(1.0);
        } else if (rangeValue == this.minRangeValue) {
            this.m_93611_(0.0);
        } else {
            this.m_93611_((Mth.clamp(rangeValue, this.minRangeValue, this.maxRangeValue) - this.minRangeValue) / (this.maxRangeValue - this.minRangeValue));
        }
        return this;
    }

    public double getMinRangeValue() {
        return this.minRangeValue;
    }

    public RangeSlider setMinRangeValue(double minRangeValue) {
        this.minRangeValue = minRangeValue;
        return this;
    }

    public double getMaxRangeValue() {
        return this.maxRangeValue;
    }

    public RangeSlider setMaxRangeValue(double maxRangeValue) {
        this.maxRangeValue = maxRangeValue;
        return this;
    }

    public boolean showAsInteger() {
        return this.showAsInteger;
    }

    public RangeSlider setShowAsInteger(boolean showAsInteger) {
        this.showAsInteger = showAsInteger;
        return this;
    }

    public int getRoundingDecimalPlace() {
        return this.roundingDecimalPlace;
    }

    public RangeSlider setRoundingDecimalPlace(int decimalPlace) {
        this.roundingDecimalPlace = decimalPlace;
        return this;
    }
}