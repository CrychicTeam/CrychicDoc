package com.mna.gui.item;

import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiBagBase;
import com.mna.gui.containers.item.ContainerPhylacteryStaff;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiPhylacteryStaff extends GuiBagBase<ContainerPhylacteryStaff> {

    public GuiPhylacteryStaff(ContainerPhylacteryStaff book, Inventory inv, Component comp) {
        super(inv, book);
        this.f_97726_ = 256;
        this.f_97727_ = 256;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public ResourceLocation texture() {
        return GuiTextures.Items.STAFF_OF_CALLING;
    }

    @Override
    public String name() {
        return "Staff of Calling";
    }
}