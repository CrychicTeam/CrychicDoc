package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import com.mna.gui.containers.item.ContainerAstroBlade;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiAstroBlade extends GuiBagBase<ContainerAstroBlade> {

    public GuiAstroBlade(ContainerAstroBlade inventorySlotsIn, Inventory inv, Component comp) {
        super(inv, inventorySlotsIn);
        this.f_97726_ = 176;
        this.f_97727_ = 209;
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.Items.RUNIC_MALUS;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_ + this.f_97726_ / 2 - 51;
        int j = this.f_97736_;
        pGuiGraphics.blit(this.texture(), i, j, 154, 0, 102, 114);
        i = this.f_97735_;
        j = this.f_97736_ + 119;
        pGuiGraphics.blit(GuiTextures.Widgets.STANDALONE_INVENTORY_TEXTURE, i, j, 0.0F, 0.0F, 176, 90, 176, 90);
    }

    @Override
    public String name() {
        return "Astro Blade";
    }
}