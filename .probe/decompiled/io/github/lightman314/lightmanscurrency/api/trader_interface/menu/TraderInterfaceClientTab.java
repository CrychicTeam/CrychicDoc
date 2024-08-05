package io.github.lightman314.lightmanscurrency.api.trader_interface.menu;

import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyTab;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import net.minecraft.nbt.CompoundTag;

public abstract class TraderInterfaceClientTab<T extends TraderInterfaceTab> extends EasyTab {

    protected final TraderInterfaceScreen screen;

    protected final TraderInterfaceMenu menu;

    public final T commonTab;

    protected TraderInterfaceClientTab(TraderInterfaceScreen screen, T commonTab) {
        super(screen);
        this.screen = screen;
        this.menu = (TraderInterfaceMenu) this.screen.m_6262_();
        this.commonTab = commonTab;
    }

    @Override
    public int getColor() {
        return 16777215;
    }

    public boolean tabButtonVisible() {
        return this.commonTab.canOpen(this.menu.player);
    }

    public void receiveSelfMessage(CompoundTag message) {
    }
}