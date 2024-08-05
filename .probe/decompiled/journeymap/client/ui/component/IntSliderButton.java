package journeymap.client.ui.component;

import journeymap.client.Constants;
import journeymap.client.render.RenderWrapper;
import journeymap.client.ui.GuiUtils;
import journeymap.common.properties.config.IntegerField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;

public class IntSliderButton extends Button implements IConfigFieldHolder<IntegerField>, SliderButton {

    public String prefix = "";

    public boolean dragging = false;

    public int minValue = 0;

    public int maxValue = 0;

    public String suffix = "";

    public boolean drawString = true;

    IntegerField field;

    public IntSliderButton(IntegerField field, String prefix, String suf) {
        this(field, prefix, suf, true);
    }

    public IntSliderButton(IntegerField field, String prefix, String suf, boolean drawStr) {
        super(prefix);
        this.minValue = field.getMinValue();
        this.maxValue = field.getMaxValue();
        this.prefix = prefix;
        this.suffix = suf;
        this.field = field;
        this.setValue(field.get());
        super.disabledLabelColor = 4210752;
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
            this.setSliderValue((mouseX - (double) (super.m_252754_() + 4)) / (double) ((float) (this.f_93618_ - 8)));
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
        return ((double) this.field.get().intValue() - (double) this.minValue * 1.0) / (double) (this.maxValue - this.minValue);
    }

    public void setSliderValue(double sliderValue) {
        if (sliderValue < 0.0) {
            sliderValue = 0.0;
        }
        if (sliderValue > 1.0) {
            sliderValue = 1.0;
        }
        int intVal = (int) Math.round(sliderValue * (double) (this.maxValue - this.minValue) + (double) this.minValue);
        this.setValue(intVal);
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
                this.setValue(Math.max(this.minValue, this.getValue() - 1));
                return true;
            }
            if (key == 262 || key == 265 || key == 61) {
                this.setValue(Math.min(this.maxValue, this.getValue() + 1));
                return true;
            }
        }
        return false;
    }

    public int getValue() {
        return this.field.get();
    }

    public void setValue(int value) {
        value = Math.min(value, this.maxValue);
        value = Math.max(value, this.minValue);
        if (this.field.get() != value) {
            this.field.set(Integer.valueOf(value));
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

    public IntegerField getConfigField() {
        return this.field;
    }
}