package org.violetmoon.quark.base.client.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SocialButton extends Button {

    public static final ResourceLocation SOCIAL_ICONS = new ResourceLocation("quark", "textures/gui/social_icons.png");

    private final int textColor;

    private final int socialId;

    public SocialButton(int x, int y, Component text, int textColor, int socialId, Button.OnPress onClick) {
        super(new Button.Builder(Component.literal(""), onClick).size(20, 20).pos(x, y));
        this.textColor = textColor;
        this.socialId = socialId;
        this.m_257544_(Tooltip.create(text));
    }

    public SocialButton(int x, int y, Component text, int textColor, int socialId, String url) {
        this(x, y, text, textColor, socialId, (Button.OnPress) (b -> Util.getPlatform().openUri(url)));
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int u = this.socialId * 20;
        int v = this.f_93622_ ? 20 : 0;
        guiGraphics.blit(SOCIAL_ICONS, this.m_252754_(), this.m_252907_(), (float) u, (float) v, 20, 20, 128, 64);
    }

    public int getFGColor() {
        return this.textColor;
    }
}