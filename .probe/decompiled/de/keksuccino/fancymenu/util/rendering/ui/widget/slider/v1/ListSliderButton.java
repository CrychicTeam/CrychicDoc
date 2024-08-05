package de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v1;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ListSliderButton extends ExtendedSliderButton {

    private static final Logger LOGGER = LogManager.getLogger("fancymenu/ListSliderButton");

    public List<String> values;

    public ListSliderButton(int x, int y, int width, int height, boolean handleClick, @NotNull List<String> values, double selectedIndex, Consumer<ExtendedSliderButton> applyValueCallback) {
        super(x, y, width, height, handleClick, 0.0, applyValueCallback);
        this.values = values;
        this.setSelectedIndex(selectedIndex);
        this.m_5695_();
    }

    @Override
    public String getSliderMessageWithoutPrefixSuffix() {
        return this.getSelectedListValue();
    }

    public String getSelectedListValue() {
        return (String) this.values.get(this.getSelectedIndex());
    }

    public int getSelectedIndex() {
        if (!this.values.isEmpty()) {
            double minValue = 0.0;
            double maxValue = (double) (this.values.size() - 1);
            return (int) Mth.lerp(Mth.clamp(this.f_93577_, 0.0, 1.0), minValue, maxValue);
        } else {
            return 0;
        }
    }

    public void setSelectedIndex(double index) {
        if (this.values != null && !this.values.isEmpty()) {
            double minValue = 0.0;
            double maxValue = (double) (this.values.size() - 1);
            this.m_93611_((Mth.clamp(index, minValue, maxValue) - minValue) / (maxValue - minValue));
        }
    }
}