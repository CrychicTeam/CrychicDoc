package io.redspace.ironsspellbooks.gui.arcane_anvil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArcaneAnvilScreen extends ItemCombinerScreen<ArcaneAnvilMenu> {

    private static final ResourceLocation ANVIL_LOCATION = new ResourceLocation("irons_spellbooks", "textures/gui/arcane_anvil.png");

    public ArcaneAnvilScreen(ArcaneAnvilMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, ANVIL_LOCATION);
        this.f_97728_ = 48;
        this.f_97729_ = 24;
    }

    @Override
    protected void renderBg(GuiGraphics guiHelper, float pPartialTick, int pX, int pY) {
        int leftPos = (this.f_96543_ - this.f_97726_) / 2;
        int topPos = (this.f_96544_ - this.f_97727_) / 2;
        guiHelper.blit(ANVIL_LOCATION, leftPos, topPos, 0, 0, this.f_97726_, this.f_97727_);
        if (((ArcaneAnvilMenu) this.f_97732_).m_38853_(0).hasItem() && ((ArcaneAnvilMenu) this.f_97732_).m_38853_(1).hasItem() && !((ArcaneAnvilMenu) this.f_97732_).m_38853_(2).hasItem()) {
            guiHelper.blit(ANVIL_LOCATION, leftPos + 99, topPos + 45, this.f_97726_, 0, 28, 21);
        }
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics0, int int1, int int2) {
    }
}