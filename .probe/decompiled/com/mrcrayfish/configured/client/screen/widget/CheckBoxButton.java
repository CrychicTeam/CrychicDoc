package com.mrcrayfish.configured.client.screen.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class CheckBoxButton extends Checkbox {

    public static final ResourceLocation ICONS = new ResourceLocation("configured:textures/gui/icons.png");

    private final CheckBoxButton.OnPress onPress;

    public CheckBoxButton(int x, int y, CheckBoxButton.OnPress onPress) {
        super(x, y, 14, 14, CommonComponents.EMPTY, false);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.onPress.onPress(this);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::m_172814_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        graphics.blit(ICONS, this.m_252754_(), this.m_252907_(), this.m_198029_() ? 50.0F : 36.0F, this.m_93840_() ? 49.0F : 35.0F, 14, 14, 64, 64);
    }

    public interface OnPress {

        void onPress(Checkbox var1);
    }
}