package fr.frinn.custommachinery.client.screen.widget;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class DoubleSlider extends AbstractSliderButton {

    private final Component baseMessage;

    private final double min;

    private final double max;

    private final boolean onlyValue;

    private final Consumer<Double> responder;

    public static DoubleSlider.Builder builder() {
        return new DoubleSlider.Builder();
    }

    private DoubleSlider(int x, int y, int width, int height, Component message, double value, double min, double max, boolean onlyValue, Consumer<Double> responder) {
        super(x, y, width, height, onlyValue ? Component.literal(String.format("%.2f", Mth.map(value, 0.0, 1.0, min, max))) : Component.empty().append(message).append(": " + (int) Mth.map(value, 0.0, 1.0, min, max)), value);
        this.baseMessage = message;
        this.min = min;
        this.max = max;
        this.onlyValue = onlyValue;
        this.responder = responder;
    }

    public double doubleValue() {
        return Mth.map(this.f_93577_, 0.0, 1.0, this.min, this.max);
    }

    @Override
    public void setValue(double value) {
        this.f_93577_ = Mth.map(value, this.min, this.max, 0.0, 1.0);
        this.applyValue();
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (this.onlyValue) {
            this.m_93666_(Component.literal(String.format("%.2f", this.doubleValue())));
        } else {
            this.m_93666_(Component.empty().append(this.baseMessage).append(String.format(": %.2f", this.doubleValue())));
        }
    }

    @Override
    protected void applyValue() {
        this.responder.accept(this.doubleValue());
    }

    public static class Builder {

        private double defaultValue = 0.0;

        private double min = 0.0;

        private double max = 1000.0;

        private boolean onlyValue = false;

        private Consumer<Double> responder = value -> {
        };

        public DoubleSlider.Builder bounds(double min, double max) {
            this.min = min;
            this.max = max;
            this.defaultValue = Mth.clamp(this.defaultValue, this.min, this.max);
            return this;
        }

        public DoubleSlider.Builder defaultValue(double defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public DoubleSlider.Builder displayOnlyValue() {
            this.onlyValue = true;
            return this;
        }

        public DoubleSlider.Builder setResponder(Consumer<Double> responder) {
            this.responder = responder;
            return this;
        }

        public DoubleSlider create(int x, int y, int width, int height, Component message) {
            return new DoubleSlider(x, y, width, height, message, Mth.map(Mth.clamp(this.defaultValue, this.min, this.max), this.min, this.max, 0.0, 1.0), this.min, this.max, this.onlyValue, this.responder);
        }
    }
}