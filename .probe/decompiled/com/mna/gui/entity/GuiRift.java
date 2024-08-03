package com.mna.gui.entity;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.entity.ContainerRift;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiRift extends AbstractContainerScreen<ContainerRift> {

    public GuiRift(ContainerRift book, Inventory inv, Component comp) {
        super(book, inv, Component.literal(""));
        this.f_97726_ = 176;
        this.f_97727_ = 212;
    }

    public ResourceLocation texture() {
        return GuiTextures.Entities.RIFT;
    }

    public int rows() {
        return 8;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
    }
}