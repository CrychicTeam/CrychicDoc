package de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v2;

import de.keksuccino.fancymenu.util.ConsumingSupplier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ListSlider<T> extends AbstractExtendedSlider {

    @NotNull
    protected List<T> listValues;

    @NotNull
    protected ConsumingSupplier<T, String> listValueStringSupplier = consumes -> consumes != null ? consumes.toString() : "";

    public ListSlider(int x, int y, int width, int height, Component label, @NotNull List<T> listValues, int preSelectedIndex) {
        super(x, y, width, height, label, 0.0);
        this.listValues = new ArrayList(listValues);
        if (this.listValues.size() < 2) {
            throw new RuntimeException("Not enough list values! At least 2 list values needed!");
        } else {
            this.setSelectedIndex((double) preSelectedIndex);
        }
    }

    @NotNull
    @Override
    public String getValueDisplayText() {
        return this.listValueStringSupplier.get(this.getSelectedListValue());
    }

    @NotNull
    public T getSelectedListValue() {
        return (T) this.listValues.get(Math.min(this.listValues.size() - 1, Math.max(0, this.getSelectedIndex())));
    }

    public int getSelectedIndex() {
        if (!this.listValues.isEmpty()) {
            double minValue = 0.0;
            double maxValue = (double) (this.listValues.size() - 1);
            return (int) Mth.lerp(Mth.clamp(this.f_93577_, 0.0, 1.0), minValue, maxValue);
        } else {
            return 0;
        }
    }

    public ListSlider<T> setSelectedIndex(double index) {
        if (!this.listValues.isEmpty()) {
            double minValue = 0.0;
            double maxValue = (double) (this.listValues.size() - 1);
            this.m_93611_((Mth.clamp(index, minValue, maxValue) - minValue) / (maxValue - minValue));
        }
        return this;
    }

    public void setListValueStringSupplier(@NotNull ConsumingSupplier<T, String> supplier) {
        this.listValueStringSupplier = supplier;
    }
}