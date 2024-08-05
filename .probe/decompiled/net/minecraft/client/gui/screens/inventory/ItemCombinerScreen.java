package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;

public abstract class ItemCombinerScreen<T extends ItemCombinerMenu> extends AbstractContainerScreen<T> implements ContainerListener {

    private final ResourceLocation menuResource;

    public ItemCombinerScreen(T t0, Inventory inventory1, Component component2, ResourceLocation resourceLocation3) {
        super(t0, inventory1, component2);
        this.menuResource = resourceLocation3;
    }

    protected void subInit() {
    }

    @Override
    protected void init() {
        super.init();
        this.subInit();
        ((ItemCombinerMenu) this.f_97732_).m_38893_(this);
    }

    @Override
    public void removed() {
        super.removed();
        ((ItemCombinerMenu) this.f_97732_).m_38943_(this);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
        this.renderFg(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    protected void renderFg(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        guiGraphics0.blit(this.menuResource, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        this.renderErrorIcon(guiGraphics0, this.f_97735_, this.f_97736_);
    }

    protected abstract void renderErrorIcon(GuiGraphics var1, int var2, int var3);

    @Override
    public void dataChanged(AbstractContainerMenu abstractContainerMenu0, int int1, int int2) {
    }

    @Override
    public void slotChanged(AbstractContainerMenu abstractContainerMenu0, int int1, ItemStack itemStack2) {
    }
}