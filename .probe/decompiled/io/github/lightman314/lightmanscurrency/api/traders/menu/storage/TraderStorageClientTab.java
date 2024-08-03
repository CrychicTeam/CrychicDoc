package io.github.lightman314.lightmanscurrency.api.traders.menu.storage;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyTab;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IEasyScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TraderStorageClientTab<T extends TraderStorageTab> extends EasyTab {

    public final ITraderStorageScreen screen;

    public final ITraderStorageMenu menu;

    public final T commonTab;

    protected TraderStorageClientTab(Object screen, T commonTab) {
        super((IEasyScreen) screen);
        this.screen = (ITraderStorageScreen) screen;
        this.menu = this.screen.getMenu();
        this.commonTab = commonTab;
    }

    @Override
    public int getColor() {
        return 16777215;
    }

    public boolean tabButtonVisible() {
        return true;
    }

    public int getTradeRuleTradeIndex() {
        return -1;
    }

    public void receiveSelfMessage(LazyPacketData message) {
    }

    public void receiveServerMessage(LazyPacketData message) {
    }

    public boolean shouldRenderInventoryText() {
        return true;
    }
}