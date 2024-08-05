package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IndexPageButton extends Button {

    public IndexPageButton(int x, int y, Component buttonText, Button.OnPress butn) {
        super(x, y, 160, 32, buttonText, butn, f_252438_);
        this.f_93618_ = 160;
        this.f_93619_ = 32;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partial) {
        if (this.f_93623_) {
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            Font font = IafConfig.useVanillaFont ? Minecraft.getInstance().font : (Font) IceAndFire.PROXY.getFontRenderer();
            boolean flag = this.m_198029_();
            pGuiGraphics.blit(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"), this.m_252754_(), this.m_252907_(), 0, flag ? 32 : 0, this.f_93618_, this.f_93619_);
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = this.getFGColor();
            this.m_280139_(pGuiGraphics, font, i | Mth.ceil(this.f_93625_ * 255.0F) << 24);
        }
    }
}