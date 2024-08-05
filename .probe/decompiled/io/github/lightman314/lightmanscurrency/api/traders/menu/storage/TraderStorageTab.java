package io.github.lightman314.lightmanscurrency.api.traders.menu.storage;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class TraderStorageTab {

    public static final int TAB_TRADE_BASIC = 0;

    public static final int TAB_TRADE_STORAGE = 1;

    public static final int TAB_TRADE_ADVANCED = 2;

    public static final int TAB_TRADE_MISC = 3;

    public static final int TAB_TRADER_LOGS = 10;

    public static final int TAB_TRADER_SETTINGS = 11;

    public static final int TAB_TRADER_STATS = 12;

    public static final int TAB_TAX_INFO = 50;

    public static final int TAB_RULES_TRADER = 100;

    public static final int TAB_RULES_TRADE = 101;

    public final ITraderStorageMenu menu;

    protected TraderStorageTab(@Nonnull ITraderStorageMenu menu) {
        this.menu = menu;
    }

    public abstract Object createClientTab(Object var1);

    public abstract boolean canOpen(Player var1);

    public abstract void onTabOpen();

    public abstract void onTabClose();

    public void onMenuClose() {
    }

    public abstract void addStorageMenuSlots(Function<Slot, Slot> var1);

    public boolean quickMoveStack(ItemStack stack) {
        return false;
    }

    public abstract void receiveMessage(LazyPacketData var1);
}