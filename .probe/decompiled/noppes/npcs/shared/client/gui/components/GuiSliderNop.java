package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import noppes.npcs.shared.client.gui.listeners.ISliderListener;

public class GuiSliderNop extends AbstractWidget {

    private ISliderListener listener;

    public int id;

    public float sliderValue = 1.0F;

    public float startValue = 1.0F;

    public GuiSliderNop(Screen parent, int id, int xPos, int yPos, String displayString, float sliderValue) {
        super(xPos, yPos, 150, 20, Component.translatable(displayString));
        this.id = id;
        this.sliderValue = sliderValue;
        this.startValue = sliderValue;
        this.listener = (ISliderListener) parent;
    }

    public GuiSliderNop(Screen parent, int id, int xPos, int yPos, float sliderValue) {
        this(parent, id, xPos, yPos, "", sliderValue);
        this.listener.mouseDragged(this);
    }

    public GuiSliderNop(Screen parent, int id, int xPos, int yPos, int width, int height, float sliderValue) {
        this(parent, id, xPos, yPos, "", sliderValue);
        this.f_93618_ = width;
        this.f_93619_ = height;
        this.listener.mouseDragged(this);
    }

    public void setString(String str) {
        this.m_93666_(Component.translatable(str));
    }

    private void setSliderValue(float value) {
        value = Mth.clamp(value, 0.0F, 1.0F);
        if (value != this.sliderValue) {
            this.sliderValue = value;
            this.listener.mouseDragged(this);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, f_93617_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        int i = !this.f_93623_ ? 0 : (this.m_198029_() ? 2 : 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        guiGraphics0.blit(f_93617_, this.m_252754_(), this.m_252907_(), 0, 46 + i * 20, this.f_93618_ / 2, this.f_93619_);
        guiGraphics0.blit(f_93617_, this.m_252754_() + this.f_93618_ / 2, this.m_252907_(), 200 - this.f_93618_ / 2, 46 + i * 20, this.f_93618_ / 2, this.f_93619_);
        this.renderBg(guiGraphics0, minecraft, int1, int2);
        int j = this.getFGColor();
        guiGraphics0.drawCenteredString(font, this.m_6035_(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, j | Mth.ceil(this.f_93625_ * 255.0F) << 24);
    }

    @Override
    public void onClick(double x, double y) {
        if (this.f_93624_ && this.f_93623_) {
            this.setSliderValue((float) (x - (double) (this.m_252754_() + 4)) / (float) (this.f_93618_ - 8));
            super.onClick(x, y);
        }
    }

    @Override
    protected void onDrag(double x, double y, double p_onDrag_5_, double p_onDrag_7_) {
        this.setSliderValue((float) (x - (double) (this.m_252754_() + 4)) / (float) (this.f_93618_ - 8));
        super.onDrag(x, y, p_onDrag_5_, p_onDrag_7_);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    @Override
    public void onRelease(double x, double y) {
        if (this.sliderValue != this.startValue) {
            super.playDownSound(Minecraft.getInstance().getSoundManager());
            this.listener.mouseReleased(this);
            this.startValue = this.sliderValue;
        }
    }

    public void renderBg(GuiGraphics graphics, Minecraft mc, int p_146119_2_, int p_146119_3_) {
        if (this.f_93624_) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, f_93617_);
            int lvt_4_1_ = (this.f_93622_ ? 2 : 1) * 20;
            graphics.blit(f_93617_, this.m_252754_() + (int) ((double) this.sliderValue * (double) (this.f_93618_ - 8)), this.m_252907_(), 0, 46 + lvt_4_1_, 4, 20);
            graphics.blit(f_93617_, this.m_252754_() + (int) ((double) this.sliderValue * (double) (this.f_93618_ - 8)) + 4, this.m_252907_(), 196, 46 + lvt_4_1_, 4, 20);
        }
    }
}