package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class AbstractSliderButton extends AbstractWidget {

    private static final ResourceLocation SLIDER_LOCATION = new ResourceLocation("textures/gui/slider.png");

    protected static final int TEXTURE_WIDTH = 200;

    protected static final int TEXTURE_HEIGHT = 20;

    protected static final int TEXTURE_BORDER_X = 20;

    protected static final int TEXTURE_BORDER_Y = 4;

    protected static final int TEXT_MARGIN = 2;

    private static final int HEIGHT = 20;

    private static final int HANDLE_HALF_WIDTH = 4;

    private static final int HANDLE_WIDTH = 8;

    private static final int BACKGROUND = 0;

    private static final int BACKGROUND_FOCUSED = 1;

    private static final int HANDLE = 2;

    private static final int HANDLE_FOCUSED = 3;

    protected double value;

    private boolean canChangeValue;

    public AbstractSliderButton(int int0, int int1, int int2, int int3, Component component4, double double5) {
        super(int0, int1, int2, int3, component4);
        this.value = double5;
    }

    private int getTextureY() {
        int $$0 = this.m_93696_() && !this.canChangeValue ? 1 : 0;
        return $$0 * 20;
    }

    private int getHandleTextureY() {
        int $$0 = !this.f_93622_ && !this.canChangeValue ? 2 : 3;
        return $$0 * 20;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.slider", this.m_6035_());
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.f_93623_) {
            if (this.m_93696_()) {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.focused"));
            } else {
                narrationElementOutput0.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.hovered"));
            }
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Minecraft $$4 = Minecraft.getInstance();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics0.blitNineSliced(SLIDER_LOCATION, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 20, 4, 200, 20, 0, this.getTextureY());
        guiGraphics0.blitNineSliced(SLIDER_LOCATION, this.m_252754_() + (int) (this.value * (double) (this.f_93618_ - 8)), this.m_252907_(), 8, 20, 20, 4, 200, 20, 0, this.getHandleTextureY());
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int $$5 = this.f_93623_ ? 16777215 : 10526880;
        this.m_280372_(guiGraphics0, $$4.font, 2, $$5 | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    @Override
    public void onClick(double double0, double double1) {
        this.setValueFromMouse(double0);
    }

    @Override
    public void setFocused(boolean boolean0) {
        super.setFocused(boolean0);
        if (!boolean0) {
            this.canChangeValue = false;
        } else {
            InputType $$1 = Minecraft.getInstance().getLastInputType();
            if ($$1 == InputType.MOUSE || $$1 == InputType.KEYBOARD_TAB) {
                this.canChangeValue = true;
            }
        }
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (CommonInputs.selected(int0)) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                boolean $$3 = int0 == 263;
                if ($$3 || int0 == 262) {
                    float $$4 = $$3 ? -1.0F : 1.0F;
                    this.setValue(this.value + (double) ($$4 / (float) (this.f_93618_ - 8)));
                    return true;
                }
            }
            return false;
        }
    }

    private void setValueFromMouse(double double0) {
        this.setValue((double0 - (double) (this.m_252754_() + 4)) / (double) (this.f_93618_ - 8));
    }

    private void setValue(double double0) {
        double $$1 = this.value;
        this.value = Mth.clamp(double0, 0.0, 1.0);
        if ($$1 != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }

    @Override
    protected void onDrag(double double0, double double1, double double2, double double3) {
        this.setValueFromMouse(double0);
        super.onDrag(double0, double1, double2, double3);
    }

    @Override
    public void playDownSound(SoundManager soundManager0) {
    }

    @Override
    public void onRelease(double double0, double double1) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }

    protected abstract void updateMessage();

    protected abstract void applyValue();
}