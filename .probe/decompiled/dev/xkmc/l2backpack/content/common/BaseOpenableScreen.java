package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BaseOpenableScreen<T extends BaseContainerMenu<T>> extends BaseContainerScreen<T> {

    public BaseOpenableScreen(T cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void init() {
        super.m_7856_();
    }

    @Override
    protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
        MenuLayoutConfig sm = ((BaseContainerMenu) this.f_97732_).sprite.get();
        MenuLayoutConfig.ScreenRenderer sr = sm.getRenderer(this);
        sr.start(g);
    }
}