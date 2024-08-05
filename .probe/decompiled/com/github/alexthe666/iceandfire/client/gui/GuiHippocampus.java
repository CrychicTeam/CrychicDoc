package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.inventory.HippocampusContainerMenu;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class GuiHippocampus extends AbstractContainerScreen<HippocampusContainerMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/hippogryph.png");

    private float mousePosx;

    private float mousePosY;

    public GuiHippocampus(HippocampusContainerMenu dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        int k = 0;
        int l = 0;
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        Font font = this.getMinecraft().font;
        if (entity instanceof EntityHippocampus hippo) {
            pGuiGraphics.drawString(font, hippo.m_5446_().getString(), l + 8, 6, 4210752, false);
        }
        pGuiGraphics.drawString(font, this.f_169604_, k + 8, l + this.f_97727_ - 96 + 2, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(pGuiGraphics);
        this.mousePosx = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.m_280072_(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        int i = (this.f_96543_ - this.f_97726_) / 2;
        int j = (this.f_96544_ - this.f_97727_) / 2;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.f_97726_, this.f_97727_);
        if (IceAndFire.PROXY.getReferencedMob() instanceof EntityHippocampus hippo) {
            if (hippo.isChested()) {
                pGuiGraphics.blit(TEXTURE, i + 79, j + 17, 0, this.f_97727_, 90, 54);
            }
            InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, i + 51, j + 60, 17, (float) (i + 51) - this.mousePosx, (float) (j + 75 - 50) - this.mousePosY, hippo);
        }
    }
}