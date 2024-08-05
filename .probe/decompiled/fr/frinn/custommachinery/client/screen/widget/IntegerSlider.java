package fr.frinn.custommachinery.client.screen.widget;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class IntegerSlider extends AbstractSliderButton {

    private final Component baseMessage;

    private final int min;

    private final int max;

    private final boolean onlyValue;

    private final Consumer<Integer> responder;

    public static IntegerSlider.Builder builder() {
        return new IntegerSlider.Builder();
    }

    private IntegerSlider(int x, int y, int width, int height, Component message, double value, int min, int max, boolean onlyValue, Consumer<Integer> responder) {
        super(x, y, width, height, onlyValue ? Component.literal((int) Mth.map(value, 0.0, 1.0, (double) min, (double) max) + "") : Component.empty().append(message).append(": " + (int) Mth.map(value, 0.0, 1.0, (double) min, (double) max)), value);
        this.baseMessage = message;
        this.min = min;
        this.max = max;
        this.onlyValue = onlyValue;
        this.responder = responder;
    }

    public int intValue() {
        return (int) Mth.map(this.f_93577_, 0.0, 1.0, (double) this.min, (double) this.max);
    }

    public void setValue(int value) {
        this.f_93577_ = Mth.map((double) value, (double) this.min, (double) this.max, 0.0, 1.0);
        this.applyValue();
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (this.onlyValue) {
            this.m_93666_(Component.literal(this.intValue() + ""));
        } else {
            this.m_93666_(Component.empty().append(this.baseMessage).append(": " + this.intValue()));
        }
    }

    @Override
    protected void applyValue() {
        this.responder.accept(this.intValue());
    }

    public static class Builder {

        private int defaultValue = 0;

        private int min = 0;

        private int max = 1000;

        private boolean onlyValue = false;

        private Consumer<Integer> responder = value -> {
        };

        public IntegerSlider.Builder bounds(int min, int max) {
            this.min = min;
            this.max = max;
            this.defaultValue = Mth.clamp(this.defaultValue, this.min, this.max);
            return this;
        }

        public IntegerSlider.Builder defaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public IntegerSlider.Builder displayOnlyValue() {
            this.onlyValue = true;
            return this;
        }

        public IntegerSlider.Builder setResponder(Consumer<Integer> responder) {
            this.responder = responder;
            return this;
        }

        public IntegerSlider create(int x, int y, int width, int height, Component message) {
            return new IntegerSlider(x, y, width, height, message, Mth.map((double) Mth.clamp(this.defaultValue, this.min, this.max), (double) this.min, (double) this.max, 0.0, 1.0), this.min, this.max, this.onlyValue, this.responder);
        }
    }
}