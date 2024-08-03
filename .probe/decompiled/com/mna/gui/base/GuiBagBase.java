package com.mna.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class GuiBagBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public static final int bagXSize = 176;

    public static final int bagYSize = 207;

    public GuiBagBase(Inventory inv, T inventorySlotsIn) {
        super(inventorySlotsIn, inv, Component.literal(""));
        this.f_97726_ = 176;
        this.f_97727_ = 207;
    }

    public GuiBagBase(Inventory inv, T inventorySlotsIn, int sizex, int sizey) {
        super(inventorySlotsIn, inv, Component.literal(""));
        this.f_97726_ = sizex;
        this.f_97727_ = sizey;
    }

    public abstract ResourceLocation texture();

    public abstract String name();

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(this.texture(), i, j, 0, 0, this.f_97726_, this.f_97727_);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int x, int y) {
    }
}