package org.violetmoon.zeta.client.config.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;

public class PencilButton extends Button {

    private final ResourceLocation iconsTexture;

    public PencilButton(ResourceLocation iconsTexture, int x, int y, Button.OnPress pressable) {
        super(new Button.Builder(Component.literal(""), pressable).size(20, 20).pos(x, y));
        this.iconsTexture = iconsTexture;
    }

    public PencilButton(ZetaClient zc, int x, int y, Button.OnPress pressable) {
        this(zc.generalIcons, x, y, pressable);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int u = 32;
        int v = 0;
        guiGraphics.blit(this.iconsTexture, this.m_252754_() + 2, this.m_252907_() + 1, u, v, 16, 16);
    }
}