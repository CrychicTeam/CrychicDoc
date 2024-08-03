package io.github.lightman314.lightmanscurrency.api.traders.menu.storage;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ITraderStorageMenu extends IClientTracker {

    void setTab(int var1, @Nonnull TraderStorageTab var2);

    void clearTab(int var1);

    void changeTab(int var1);

    void changeTab(int var1, @Nullable LazyPacketData.Builder var2);

    @Nonnull
    TradeContext getContext();

    @Nonnull
    LazyPacketData.Builder createTabChangeMessage(int var1);

    @Nonnull
    LazyPacketData.Builder createTabChangeMessage(int var1, @Nullable LazyPacketData.Builder var2);

    @Nullable
    TraderData getTrader();

    @Nonnull
    Player getPlayer();

    @Nonnull
    ItemStack getHeldItem();

    void setHeldItem(@Nonnull ItemStack var1);

    void clearContainer(@Nonnull Container var1);

    void SetCoinSlotsActive(boolean var1);

    void SendMessage(@Nonnull LazyPacketData.Builder var1);

    default boolean hasPermission(@Nonnull String permission) {
        return this.getPermissionLevel(permission) > 0;
    }

    int getPermissionLevel(@Nonnull String var1);
}