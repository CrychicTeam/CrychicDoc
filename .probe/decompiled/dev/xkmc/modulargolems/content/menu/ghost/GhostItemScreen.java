package dev.xkmc.modulargolems.content.menu.ghost;

import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.init.ModularGolems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public abstract class GhostItemScreen<T extends GhostItemMenu> extends AbstractContainerScreen<T> implements ITabScreen {

    public GhostItemScreen(T cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
        this.f_97727_ = ((GhostItemMenu) this.f_97732_).sprite.getHeight();
        this.f_97731_ = ((GhostItemMenu) this.f_97732_).sprite.getPlInvY() - 11;
    }

    public void addGhost(int ind, ItemStack stack) {
        ((GhostItemMenu) this.f_97732_).setSlotContent(ind, stack);
        ModularGolems.HANDLER.toServer(new SetItemFilterToServer(ind, stack));
    }

    @Override
    public void render(GuiGraphics stack, int mx, int my, float partial) {
        super.render(stack, mx, my, partial);
        this.m_280072_(stack, mx, my);
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }
}