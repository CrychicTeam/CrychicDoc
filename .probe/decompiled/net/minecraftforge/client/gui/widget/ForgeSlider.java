package net.minecraftforge.client.gui.widget;

import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ForgeSlider extends AbstractSliderButton {

    protected Component prefix;

    protected Component suffix;

    protected double minValue;

    protected double maxValue;

    protected double stepSize;

    protected boolean drawString;

    private final DecimalFormat format;

    public ForgeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString) {
        super(x, y, width, height, Component.empty(), 0.0);
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = Math.abs(stepSize);
        this.f_93577_ = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.drawString = drawString;
        if (stepSize == 0.0) {
            precision = Math.min(precision, 4);
            StringBuilder builder = new StringBuilder("0");
            if (precision > 0) {
                builder.append('.');
            }
            while (precision-- > 0) {
                builder.append('0');
            }
            this.format = new DecimalFormat(builder.toString());
        } else if (Mth.equal(this.stepSize, Math.floor(this.stepSize))) {
            this.format = new DecimalFormat("0");
        } else {
            this.format = new DecimalFormat(Double.toString(this.stepSize).replaceAll("\\d", "0"));
        }
        this.updateMessage();
    }

    public ForgeSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString) {
        this(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, 1.0, 0, drawString);
    }

    public double getValue() {
        return this.f_93577_ * (this.maxValue - this.minValue) + this.minValue;
    }

    public long getValueLong() {
        return Math.round(this.getValue());
    }

    public int getValueInt() {
        return (int) this.getValueLong();
    }

    @Override
    public void setValue(double value) {
        this.f_93577_ = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        this.updateMessage();
    }

    public String getValueString() {
        return this.format.format(this.getValue());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);
        this.setValueFromMouse(mouseX);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag = keyCode == 263;
        if (flag || keyCode == 262) {
            if (this.minValue > this.maxValue) {
                flag = !flag;
            }
            float f = flag ? -1.0F : 1.0F;
            if (this.stepSize <= 0.0) {
                this.setSliderValue(this.f_93577_ + (double) (f / (float) (this.f_93618_ - 8)));
            } else {
                this.setValue(this.getValue() + (double) f * this.stepSize);
            }
        }
        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (double) (this.m_252754_() + 4)) / (double) (this.f_93618_ - 8));
    }

    private void setSliderValue(double value) {
        double oldValue = this.f_93577_;
        this.f_93577_ = this.snapToNearest(value);
        if (!Mth.equal(oldValue, this.f_93577_)) {
            this.applyValue();
        }
        this.updateMessage();
    }

    private double snapToNearest(double value) {
        if (this.stepSize <= 0.0) {
            return Mth.clamp(value, 0.0, 1.0);
        } else {
            value = Mth.lerp(Mth.clamp(value, 0.0, 1.0), this.minValue, this.maxValue);
            value = this.stepSize * (double) Math.round(value / this.stepSize);
            if (this.minValue > this.maxValue) {
                value = Mth.clamp(value, this.maxValue, this.minValue);
            } else {
                value = Mth.clamp(value, this.minValue, this.maxValue);
            }
            return Mth.map(value, this.minValue, this.maxValue, 0.0, 1.0);
        }
    }

    @Override
    protected void updateMessage() {
        if (this.drawString) {
            this.m_93666_(Component.literal("").append(this.prefix).append(this.getValueString()).append(this.suffix));
        } else {
            this.m_93666_(Component.empty());
        }
    }

    @Override
    protected void applyValue() {
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.blitWithBorder(f_263683_, this.m_252754_(), this.m_252907_(), 0, this.m_264355_(), this.f_93618_, this.f_93619_, 200, 20, 2, 3, 2, 2);
        guiGraphics.blitWithBorder(f_263683_, this.m_252754_() + (int) (this.f_93577_ * (double) (this.f_93618_ - 8)), this.m_252907_(), 0, this.m_264270_(), 8, this.f_93619_, 200, 20, 2, 3, 2, 2);
        this.m_280372_(guiGraphics, mc.font, 2, this.getFGColor() | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }
}