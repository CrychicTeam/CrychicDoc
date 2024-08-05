package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.GrindstoneMenu;

public class GrindstoneScreen extends AbstractContainerScreen<GrindstoneMenu> {

    private static final ResourceLocation GRINDSTONE_LOCATION = new ResourceLocation("textures/gui/container/grindstone.png");

    public GrindstoneScreen(GrindstoneMenu grindstoneMenu0, Inventory inventory1, Component component2) {
        super(grindstoneMenu0, inventory1, component2);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.renderBg(guiGraphics0, float3, int1, int2);
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(GRINDSTONE_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        if ((((GrindstoneMenu) this.f_97732_).m_38853_(0).hasItem() || ((GrindstoneMenu) this.f_97732_).m_38853_(1).hasItem()) && !((GrindstoneMenu) this.f_97732_).m_38853_(2).hasItem()) {
            guiGraphics0.blit(GRINDSTONE_LOCATION, $$4 + 92, $$5 + 31, this.f_97726_, 0, 28, 21);
        }
    }
}