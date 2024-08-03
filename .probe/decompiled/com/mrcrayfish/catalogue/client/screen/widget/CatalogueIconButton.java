package com.mrcrayfish.catalogue.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CatalogueIconButton extends Button {

    private static final ResourceLocation TEXTURE = new ResourceLocation("catalogue", "textures/gui/icons.png");

    private final Component label;

    private final int u;

    private final int v;

    public CatalogueIconButton(int x, int y, int u, int v, Button.OnPress onPress) {
        this(x, y, u, v, 20, CommonComponents.EMPTY, onPress);
    }

    public CatalogueIconButton(int x, int y, int u, int v, int width, Component label, Button.OnPress onPress) {
        super(x, y, width, 20, CommonComponents.EMPTY, onPress, Button.DEFAULT_NARRATION);
        this.label = label;
        this.u = u;
        this.v = v;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(graphics, mouseX, mouseY, partialTicks);
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        int contentWidth = 10 + minecraft.font.width(this.label) + (!this.label.getString().isEmpty() ? 4 : 0);
        int iconX = this.m_252754_() + (this.f_93618_ - contentWidth) / 2;
        int iconY = this.m_252907_() + 5;
        float brightness = this.f_93623_ ? 1.0F : 0.5F;
        RenderSystem.setShaderColor(brightness, brightness, brightness, this.f_93625_);
        graphics.blit(TEXTURE, iconX, iconY, (float) this.u, (float) this.v, 10, 10, 64, 64);
        RenderSystem.setShaderColor(brightness, brightness, brightness, this.f_93625_);
        int textColor = 16777215 | Mth.ceil(this.f_93625_ * 255.0F) << 24;
        graphics.drawString(minecraft.font, this.label, iconX + 14, iconY + 1, textColor);
    }
}