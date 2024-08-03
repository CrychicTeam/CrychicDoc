package dev.xkmc.l2library.base.menu.base;

import dev.xkmc.l2library.util.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class BaseContainerScreen<T extends BaseContainerMenu<T>> extends AbstractContainerScreen<T> {

    public BaseContainerScreen(T cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
        this.f_97727_ = ((BaseContainerMenu) this.f_97732_).sprite.get().getHeight();
        this.f_97731_ = ((BaseContainerMenu) this.f_97732_).sprite.get().getPlInvY() - 11;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float partial) {
        super.render(g, mx, my, partial);
        this.m_280072_(g, mx, my);
    }

    protected boolean click(int btn) {
        if (((BaseContainerMenu) this.f_97732_).m_6366_(Proxy.getClientPlayer(), btn) && Minecraft.getInstance().gameMode != null) {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(((BaseContainerMenu) this.f_97732_).f_38840_, btn);
            return true;
        } else {
            return false;
        }
    }
}