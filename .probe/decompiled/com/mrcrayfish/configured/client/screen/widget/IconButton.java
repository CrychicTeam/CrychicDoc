package com.mrcrayfish.configured.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IconButton extends ConfiguredButton {

    public static final ResourceLocation ICONS = new ResourceLocation("configured:textures/gui/icons.png");

    private final Component label;

    private final int u;

    private final int v;

    public IconButton(int x, int y, int u, int v, Button.OnPress onPress) {
        this(x, y, u, v, 20, CommonComponents.EMPTY, onPress);
    }

    public IconButton(int x, int y, int u, int v, int width, Component label, Button.OnPress onPress) {
        super(x, y, width, 20, CommonComponents.EMPTY, onPress, f_252438_);
        this.label = label;
        this.u = u;
        this.v = v;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(graphics, mouseX, mouseY, partialTicks);
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int contentWidth = 10 + mc.font.width(this.label) + (!this.label.getString().isEmpty() ? 4 : 0);
        boolean renderIcon = contentWidth <= this.f_93618_;
        if (!renderIcon) {
            contentWidth = mc.font.width(this.label);
        }
        int iconX = this.m_252754_() + (this.f_93618_ - contentWidth) / 2;
        int iconY = this.m_252907_() + 5;
        float brightness = this.f_93623_ ? 1.0F : 0.5F;
        if (renderIcon) {
            RenderSystem.setShaderColor(brightness, brightness, brightness, this.f_93625_);
            graphics.blit(ICONS, iconX, iconY, 0, (float) this.u, (float) this.v, 11, 11, 64, 64);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        int textColor = (this.f_93623_ ? 16777215 : 10526880) | Mth.ceil(this.f_93625_ * 255.0F) << 24;
        graphics.drawString(mc.font, this.label, iconX + 14, iconY + 1, textColor);
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return m_168799_(this.label);
    }
}