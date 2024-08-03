package journeymap.client.ui.component;

import java.math.BigDecimal;
import journeymap.client.Constants;
import journeymap.client.render.RenderWrapper;
import journeymap.client.ui.GuiUtils;
import journeymap.common.properties.config.FloatField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;

public class FloatSliderButton extends Button implements IConfigFieldHolder<FloatField>, SliderButton {

    public String prefix = "";

    public boolean dragging = false;

    public float minValue = 0.0F;

    public float maxValue = 0.0F;

    public String suffix = "";

    public boolean drawString = true;

    private float incrementValue = 0.1F;

    private int precision = 2;

    FloatField field;

    public FloatSliderButton(FloatField field, String prefix, String suf) {
        this(field, prefix, suf, field.getMinValue(), field.getMaxValue());
    }

    public FloatSliderButton(FloatField field, String prefix, String suf, float minVal, float maxVal) {
        this(field, prefix, suf, minVal, maxVal, field.getIncrementValue(), field.getPrecision());
    }

    public FloatSliderButton(FloatField field, String prefix, String suf, float minVal, float maxVal, float incrementValue, int precision) {
        super(prefix);
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.prefix = prefix;
        this.suffix = suf;
        this.field = field;
        this.setValue(field.get());
        super.disabledLabelColor = 4210752;
        this.incrementValue = incrementValue;
        this.precision = precision;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, Minecraft mc, int mouseX, int mouseY) {
        double sliderValue = this.getSliderValue();
        RenderWrapper.setShader(GameRenderer::m_172817_);
        GuiUtils.drawContinuousTexturedBox(graphics.pose(), f_93617_, super.m_252754_() + 1 + (int) (sliderValue * (double) ((float) (this.f_93618_ - 10))), super.m_252907_() + 1, 0, 66, 8, this.f_93619_ - 2, 200, 20, 2, 3, 2, 2, 0.0F);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        if (this.f_93624_ && this.isEnabled() && this.m_5953_(mouseX, mouseY) && this.dragging) {
            this.setValueFromMouse(mouseX);
            if (this.clickListeners != null) {
                this.checkClickListeners();
            }
            return true;
        } else {
            return false;
        }
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (double) (super.m_252754_() + 4)) / (double) ((float) (this.f_93618_ - 8)));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.mouseOver(mouseX, mouseY)) {
            this.dragging = true;
            this.setValueFromMouse(mouseX);
            this.checkClickListeners();
            return true;
        } else {
            return false;
        }
    }

    public double getSliderValue() {
        return (double) ((this.field.get() - this.minValue) / (this.maxValue - this.minValue));
    }

    public void setSliderValue(double sliderValue) {
        float val = (float) sliderValue * (this.maxValue - this.minValue) + this.minValue;
        this.setValue(val);
    }

    @Override
    public void updateLabel() {
        if (this.drawString) {
            this.m_93666_(Constants.getStringTextComponent(this.prefix + this.field.get() + this.suffix));
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.dragging = false;
        this.field.save();
        this.checkClickListeners();
        return super.m_6348_(mouseX, mouseY, mouseButton);
    }

    @Override
    public int getFitWidth(Font fr) {
        int max = fr.width(this.prefix + this.minValue + this.suffix);
        max = Math.max(max, fr.width(this.prefix + this.maxValue + this.suffix));
        return max + this.WIDTH_PAD;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        if (this.isEnabled()) {
            if (key == 263 || key == 264 || key == 45) {
                this.setValue(Math.max(this.minValue, this.getValue() - this.incrementValue));
                return true;
            }
            if (key == 262 || key == 265 || key == 61) {
                this.setValue(Math.min(this.maxValue, this.getValue() + this.incrementValue));
                return true;
            }
        }
        return false;
    }

    public float getValue() {
        return this.field.get();
    }

    public void setValue(float value) {
        float roundedValue = new BigDecimal((double) value).setScale(this.precision, 4).floatValue();
        roundedValue = Math.min(roundedValue, this.maxValue);
        roundedValue = Math.max(roundedValue, this.minValue);
        if (this.field.get() != roundedValue) {
            this.field.set(Float.valueOf(roundedValue));
            if (!this.dragging) {
                this.field.save();
            }
        }
        this.updateLabel();
    }

    @Override
    public void refresh() {
        this.setValue(this.field.get());
    }

    public FloatField getConfigField() {
        return this.field;
    }
}