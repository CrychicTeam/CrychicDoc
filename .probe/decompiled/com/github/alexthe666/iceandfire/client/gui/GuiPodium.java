package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.ContainerPodium;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiPodium extends AbstractContainerScreen<ContainerPodium> {

    public static final ResourceLocation PODUIM_TEXTURE = new ResourceLocation("iceandfire:textures/gui/podium.png");

    public GuiPodium(ContainerPodium container, Inventory inv, Component name) {
        super(container, inv, name);
        this.f_97727_ = 133;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
        if (this.f_97732_ != null) {
            String s = I18n.get("block.iceandfire.podium");
            pGuiGraphics.drawString(this.f_96547_, s, this.f_97726_ / 2 - this.getMinecraft().font.width(s) / 2, 6, 4210752, false);
        }
        pGuiGraphics.drawString(this.f_96547_, this.f_169604_, 8, this.f_97727_ - 96 + 2, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(PODUIM_TEXTURE, i, j, 0, 0, this.f_97726_, this.f_97727_);
    }
}