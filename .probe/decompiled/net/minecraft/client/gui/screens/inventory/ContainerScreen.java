package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class ContainerScreen extends AbstractContainerScreen<ChestMenu> implements MenuAccess<ChestMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");

    private final int containerRows;

    public ContainerScreen(ChestMenu chestMenu0, Inventory inventory1, Component component2) {
        super(chestMenu0, inventory1, component2);
        int $$3 = 222;
        int $$4 = 114;
        this.containerRows = chestMenu0.getRowCount();
        this.f_97727_ = 114 + this.containerRows * 18;
        this.f_97731_ = this.f_97727_ - 94;
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
        guiGraphics0.blit(CONTAINER_BACKGROUND, $$4, $$5, 0, 0, this.f_97726_, this.containerRows * 18 + 17);
        guiGraphics0.blit(CONTAINER_BACKGROUND, $$4, $$5 + this.containerRows * 18 + 17, 0, 126, this.f_97726_, 96);
    }
}