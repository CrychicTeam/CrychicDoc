package io.github.lightman314.lightmanscurrency.network.message.data.trader;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketMessageRemoveClientTrader extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketMessageRemoveClientTrader> HANDLER = new SPacketMessageRemoveClientTrader.H();

    long traderID;

    public SPacketMessageRemoveClientTrader(long traderID) {
        this.traderID = traderID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
    }

    private static class H extends CustomPacket.Handler<SPacketMessageRemoveClientTrader> {

        @Nonnull
        public SPacketMessageRemoveClientTrader decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketMessageRemoveClientTrader(buffer.readLong());
        }

        protected void handle(@Nonnull SPacketMessageRemoveClientTrader message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.removeTrader(message.traderID);
        }
    }
}