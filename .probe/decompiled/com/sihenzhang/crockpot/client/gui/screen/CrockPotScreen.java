package com.sihenzhang.crockpot.client.gui.screen;

import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CrockPotScreen extends AbstractContainerScreen<CrockPotMenu> {

    private static final ResourceLocation TEXTURE = RLUtils.createRL("textures/gui/crock_pot.png");

    public CrockPotScreen(CrockPotMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.f_97727_ = 184;
        this.f_97731_ = this.f_97727_ - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.m_280072_(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        guiGraphics.blit(TEXTURE, this.f_97735_ + 38, this.f_97736_ + 16, 176, 97, 36, 36);
        guiGraphics.blit(TEXTURE, this.f_97735_ + 47, this.f_97736_ + 55, 176, 30, 18, 33);
        int burningProgress = ((CrockPotMenu) this.f_97732_).getBurningProgress();
        guiGraphics.blit(TEXTURE, this.f_97735_ + 48, this.f_97736_ + 54 + 13 - burningProgress, 176, 13 - burningProgress, 14, burningProgress + 1);
        guiGraphics.blit(TEXTURE, this.f_97735_ + 80, this.f_97736_ + 44, 176, 63, 24, 17);
        int cookingProgress = ((CrockPotMenu) this.f_97732_).getCookingProgress();
        guiGraphics.blit(TEXTURE, this.f_97735_ + 80, this.f_97736_ + 43, 176, 80, cookingProgress + 1, 16);
        guiGraphics.blit(TEXTURE, this.f_97735_ + 112, this.f_97736_ + 39, 176, 133, 26, 26);
    }
}