package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DispenserMenu;

public class DispenserScreen extends AbstractContainerScreen<DispenserMenu> {

    private static final ResourceLocation CONTAINER_LOCATION = new ResourceLocation("textures/gui/container/dispenser.png");

    public DispenserScreen(DispenserMenu dispenserMenu0, Inventory inventory1, Component component2) {
        super(dispenserMenu0, inventory1, component2);
    }

    @Override
    protected void init() {
        super.init();
        this.f_97728_ = (this.f_97726_ - this.f_96547_.width(this.f_96539_)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(CONTAINER_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
    }
}