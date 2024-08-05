package com.mna.gui.block;

import com.mna.gui.GuiTextures;
import com.mna.gui.containers.block.ContainerBookshelf;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiBookshelf extends AbstractContainerScreen<ContainerBookshelf> {

    public GuiBookshelf(ContainerBookshelf pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public ResourceLocation texture() {
        return GuiTextures.Blocks.BOOKSHELF;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.m_280273_(pGuiGraphics);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }
}