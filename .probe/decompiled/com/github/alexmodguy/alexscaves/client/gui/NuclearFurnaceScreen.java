package com.github.alexmodguy.alexscaves.client.gui;

import com.github.alexmodguy.alexscaves.server.inventory.NuclearFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class NuclearFurnaceScreen extends AbstractContainerScreen<NuclearFurnaceMenu> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/gui/nuclear_furnace.png");

    public NuclearFurnaceScreen(NuclearFurnaceMenu furnaceMenu, Inventory inventory, Component component) {
        super(furnaceMenu, inventory, component);
        this.f_97731_ = this.f_97727_ - 92;
        this.f_97728_ = this.f_97727_ / 2 + 5;
        this.f_97729_ = 5;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.f_96547_, this.f_96539_, this.f_97728_ - this.f_96547_.width(this.f_96539_) / 2, this.f_97729_, 4210752, false);
        guiGraphics.drawString(this.f_96547_, this.f_169604_, this.f_97730_, this.f_97731_, 4210752, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.m_280072_(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int x, int y) {
        this.m_280273_(guiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.f_97726_, this.f_97727_);
        if (!((NuclearFurnaceMenu) this.f_97732_).m_38853_(1).hasItem()) {
            guiGraphics.blit(TEXTURE, i + 67, j + 53, 176, 84, 16, 16);
        }
        if (!((NuclearFurnaceMenu) this.f_97732_).m_38853_(2).hasItem()) {
            guiGraphics.blit(TEXTURE, i + 37, j + 53, 192, 84, 16, 16);
        }
        float wasteAmount = ((NuclearFurnaceMenu) this.f_97732_).getWasteScale();
        if (wasteAmount > 0.0F) {
            int wastePixels = (int) Math.ceil((double) (52.0F * wasteAmount));
            guiGraphics.blit(TEXTURE, i + 13, j + 17 + (52 - wastePixels), 176, 32 + (52 - wastePixels), 16, wastePixels);
        }
        float barrelAmount = ((NuclearFurnaceMenu) this.f_97732_).getBarrelScale();
        if (barrelAmount > 0.0F) {
            int barrelPixels = (int) Math.ceil((double) (14.0F * barrelAmount));
            guiGraphics.blit(TEXTURE, i + 38, j + 36 + (14 - barrelPixels), 192, 14 - barrelPixels, 15, barrelPixels);
        }
        float fissionAmount = ((NuclearFurnaceMenu) this.f_97732_).getFissionScale();
        if (fissionAmount > 0.0F) {
            int fissionPixels = (int) Math.ceil((double) (14.0F * fissionAmount));
            guiGraphics.blit(TEXTURE, i + 68, j + 36 + (14 - fissionPixels), 176, 14 - fissionPixels, 14, fissionPixels);
        }
        float cookAmount = ((NuclearFurnaceMenu) this.f_97732_).getCookScale();
        if (cookAmount > 0.0F) {
            int cookPixels = (int) Math.ceil((double) (24.0F * cookAmount));
            guiGraphics.blit(TEXTURE, i + 90, j + 35, 176, 14, cookPixels, 17);
        }
    }
}