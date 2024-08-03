package com.mna.gui.widgets.lodestar;

import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FastColor;

public class IntegerParameterInput extends LodestarParameter<Integer> {

    public static final int PIP_U = 250;

    public static final int PIP_V = 244;

    public static final int PIP_HEIGHT = 6;

    public static final int BAR_U = 216;

    public static final int BAR_V = 250;

    public static final int BAR_HEIGHT = 6;

    public static final int BAR_OFFSET = 16;

    public static final int BAR_WIDTH = 40;

    public static final int V = 66;

    private boolean isDragging = false;

    private int min = 0;

    private int max = 100;

    public IntegerParameterInput(boolean lowTier, int x, int y, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 66, 0, pressHandler, tooltip);
    }

    public IntegerParameterInput(boolean lowTier, int x, int y, int defaultValue, int min, int max, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 66, defaultValue, pressHandler, tooltip);
        this.min = min;
        this.max = max;
        if (this.max < this.min + 1) {
            this.max = this.min + 1;
        }
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        boolean wasHovered = this.f_93622_;
        this.f_93622_ = false;
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        float pct = (float) (this.value - this.min) / (float) (this.max - this.min);
        pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 16, this.m_252907_() + this.f_93619_ - 6, 216.0F, 250.0F, 40, 6, 256, 256);
        pGuiGraphics.blit(this.f_94223_, this.m_252754_() + 16 + (int) (34.0F * pct), this.m_252907_() + this.f_93619_ - 6, 250.0F, 244.0F, 6, 6, 256, 256);
        MutableComponent tc = Component.literal(this.value.toString());
        int valWidth = this.mc.font.width(tc);
        pGuiGraphics.drawString(this.mc.font, tc, this.m_252754_() + 36 - valWidth / 2, this.m_252907_() + this.f_93619_ / 2 - 9 / 2, FastColor.ARGB32.color(255, 205, 223, 225));
        if (wasHovered) {
            this.f_93622_ = true;
            pGuiGraphics.renderTooltip(this.mc.font, tc, pMouseX, pMouseY);
        }
    }

    @Override
    public void onClick(double mouse_x, double mouse_y) {
        super.m_5716_(mouse_x, mouse_y);
        if (mouse_y >= (double) (this.m_252907_() + this.f_93619_ - 6) && mouse_y <= (double) (this.m_252907_() + this.f_93619_)) {
            this.isDragging = true;
            this.setValueFromMouse(mouse_x);
        }
        this.m_93692_(true);
    }

    @Override
    public boolean mouseReleased(double mouse_x, double mouse_y, int button) {
        this.isDragging = false;
        this.m_93692_(false);
        return super.m_6348_(mouse_x, mouse_y, button);
    }

    @Override
    protected void onDrag(double mouse_x, double mouse_y, double delta_x, double delta_y) {
        if (this.isDragging) {
            this.setValueFromMouse(mouse_x);
        }
        super.m_7212_(mouse_x, mouse_y, delta_x, delta_y);
    }

    private void setValueFromMouse(double mouse_x) {
        double pct = (mouse_x - (double) (this.m_252754_() + 16)) / 40.0;
        int value = this.min + (int) ((double) (this.max - this.min) * pct);
        value = MathUtils.clamp(value, this.min, this.max);
        this.setValue(Integer.valueOf(value));
    }

    @Override
    public void saveTo(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskIntegerParameter) {
            ((ConstructTaskIntegerParameter) param).setValue(this.value);
        }
    }

    @Override
    public void loadFrom(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskIntegerParameter) {
            this.value = ((ConstructTaskIntegerParameter) param).getValue();
        }
    }

    @Override
    public List<Component> getTooltipItems() {
        List<Component> baseTT = super.getTooltipItems();
        baseTT.add(Component.literal(" "));
        baseTT.add(Component.literal(this.value.toString()));
        return baseTT;
    }
}