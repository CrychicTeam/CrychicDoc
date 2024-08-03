package io.github.lightman314.lightmanscurrency.common.playertrading;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public interface IPlayerTrade {

    int ITEM_COUNT = 12;

    long PENDING_DURATION = 300000L;

    boolean isCompleted();

    default boolean isHost(@Nonnull Player player) {
        return player.m_20148_().equals(this.getHostID());
    }

    default boolean isGuest(@Nonnull Player player) {
        return player.m_20148_().equals(this.getGuestID());
    }

    @Nonnull
    UUID getHostID();

    @Nonnull
    UUID getGuestID();

    @Nonnull
    Component getHostName();

    @Nonnull
    Component getGuestName();

    @Nonnull
    MoneyValue getHostMoney();

    @Nonnull
    MoneyValue getGuestMoney();

    @Nonnull
    Container getHostItems();

    @Nonnull
    Container getGuestItems();

    int getHostState();

    int getGuestState();
}