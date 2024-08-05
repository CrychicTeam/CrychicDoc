package snownee.jade.gui.config.value;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class SliderOptionValue extends OptionValue<Float> {

    private final SliderOptionValue.Slider slider;

    private float min;

    private float max;

    private FloatUnaryOperator aligner;

    public SliderOptionValue(String optionName, float value, Consumer<Float> save, float min, float max, FloatUnaryOperator aligner) {
        super(optionName, save);
        this.value = value;
        this.min = min;
        this.max = max;
        this.aligner = aligner;
        this.slider = new SliderOptionValue.Slider(this, 0, 0, 100, 20, this.getTitle());
        this.addWidget(this.slider, 0);
    }

    public void setValue(Float value) {
        this.slider.setValue(value);
    }

    public static class Slider extends AbstractSliderButton {

        private static DecimalFormat fmt = new DecimalFormat("##.##");

        private final SliderOptionValue parent;

        public Slider(SliderOptionValue parent, int x, int y, int width, int height, Component message) {
            super(x, y, width, height, message, fromScaled(parent.value, parent.min, parent.max));
            this.parent = parent;
            this.applyValue();
        }

        public static double fromScaled(float f, float min, float max) {
            return (double) Mth.clamp((f - min) / (max - min), 0.0F, 1.0F);
        }

        public float toScaled() {
            float f = this.parent.aligner.apply(this.parent.min + (this.parent.max - this.parent.min) * (float) this.f_93577_);
            String s = fmt.format((double) f);
            try {
                return fmt.parse(s).floatValue();
            } catch (ParseException var4) {
                return f;
            }
        }

        @Override
        protected void updateMessage() {
            this.parent.value = this.toScaled();
            this.parent.save();
        }

        @Override
        protected void applyValue() {
            this.m_93666_(Component.literal(fmt.format((double) this.toScaled())));
        }

        private void setValue(float value) {
            this.f_93577_ = fromScaled(value, this.parent.min, this.parent.max);
            this.applyValue();
            this.updateMessage();
        }
    }
}