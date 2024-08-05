package io.github.lightman314.lightmanscurrency.network.message.paygate;

import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.paygate.PaygateTraderData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketCollectTicketStubs extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketCollectTicketStubs> HANDLER = new CPacketCollectTicketStubs.H();

    private final long traderID;

    public CPacketCollectTicketStubs(long traderID) {
        this.traderID = traderID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
    }

    private static class H extends CustomPacket.Handler<CPacketCollectTicketStubs> {

        @Nonnull
        public CPacketCollectTicketStubs decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketCollectTicketStubs(buffer.readLong());
        }

        protected void handle(@Nonnull CPacketCollectTicketStubs message, @Nullable ServerPlayer sender) {
            if (TraderSaveData.GetTrader(false, message.traderID) instanceof PaygateTraderData paygate && sender != null) {
                paygate.collectTicketStubs(sender);
            }
        }
    }
}