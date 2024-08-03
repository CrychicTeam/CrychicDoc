package io.github.lightman314.lightmanscurrency.api.trader_interface.menu;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.TraderInterfaceScreen;
import io.github.lightman314.lightmanscurrency.common.menus.TraderInterfaceMenu;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class TraderInterfaceTab {

    public static final int TAB_INFO = 0;

    public static final int TAB_STORAGE = 1;

    public static final int TAB_TRADER_SELECT = 2;

    public static final int TAB_TRADE_SELECT = 3;

    public static final int TAB_OWNERSHIP = 100;

    public final TraderInterfaceMenu menu;

    protected TraderInterfaceTab(TraderInterfaceMenu menu) {
        this.menu = menu;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract TraderInterfaceClientTab<?> createClientTab(TraderInterfaceScreen var1);

    public abstract boolean canOpen(Player var1);

    public abstract void onTabOpen();

    public abstract void onTabClose();

    public void onMenuClose() {
    }

    public abstract void addStorageMenuSlots(Function<Slot, Slot> var1);

    public boolean quickMoveStack(ItemStack stack) {
        return false;
    }

    @Deprecated(since = "2.2.1.4")
    public void receiveMessage(CompoundTag ignored) {
    }

    public abstract void handleMessage(@Nonnull LazyPacketData var1);
}