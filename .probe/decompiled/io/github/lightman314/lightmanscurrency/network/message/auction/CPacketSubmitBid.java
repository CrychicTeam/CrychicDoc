package io.github.lightman314.lightmanscurrency.network.message.auction;

import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.AuctionHouseTrader;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketSubmitBid extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketSubmitBid> HANDLER = new CPacketSubmitBid.H();

    final long auctionHouseID;

    final int tradeIndex;

    final MoneyValue bidAmount;

    public CPacketSubmitBid(long auctionHouseID, int tradeIndex, MoneyValue bidAmount) {
        this.auctionHouseID = auctionHouseID;
        this.tradeIndex = tradeIndex;
        this.bidAmount = bidAmount;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.auctionHouseID);
        buffer.writeInt(this.tradeIndex);
        this.bidAmount.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<CPacketSubmitBid> {

        @Nonnull
        public CPacketSubmitBid decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketSubmitBid(buffer.readLong(), buffer.readInt(), MoneyValue.decode(buffer));
        }

        protected void handle(@Nonnull CPacketSubmitBid message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof TraderMenu menu && TraderSaveData.GetTrader(false, message.auctionHouseID) instanceof AuctionHouseTrader ah) {
                ah.makeBid(sender, menu, message.tradeIndex, message.bidAmount);
            }
        }
    }
}