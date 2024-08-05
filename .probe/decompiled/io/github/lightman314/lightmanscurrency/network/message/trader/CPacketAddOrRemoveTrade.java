package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketAddOrRemoveTrade extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketAddOrRemoveTrade> HANDLER = new CPacketAddOrRemoveTrade.H();

    long traderID;

    boolean isTradeAdd;

    public CPacketAddOrRemoveTrade(long traderID, boolean isTradeAdd) {
        this.traderID = traderID;
        this.isTradeAdd = isTradeAdd;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
        buffer.writeBoolean(this.isTradeAdd);
    }

    private static class H extends CustomPacket.Handler<CPacketAddOrRemoveTrade> {

        @Nonnull
        public CPacketAddOrRemoveTrade decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketAddOrRemoveTrade(buffer.readLong(), buffer.readBoolean());
        }

        protected void handle(@Nonnull CPacketAddOrRemoveTrade message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                TraderData trader = TraderSaveData.GetTrader(false, message.traderID);
                if (trader != null) {
                    if (message.isTradeAdd) {
                        trader.addTrade(sender);
                    } else {
                        trader.removeTrade(sender);
                    }
                }
            }
        }
    }
}