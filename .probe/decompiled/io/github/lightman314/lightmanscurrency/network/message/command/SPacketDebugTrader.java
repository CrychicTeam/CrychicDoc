package io.github.lightman314.lightmanscurrency.network.message.command;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketDebugTrader extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketDebugTrader> HANDLER = new SPacketDebugTrader.H();

    final long traderID;

    public SPacketDebugTrader(long traderID) {
        this.traderID = traderID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
    }

    private static class H extends CustomPacket.Handler<SPacketDebugTrader> {

        @Nonnull
        public SPacketDebugTrader decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketDebugTrader(buffer.readLong());
        }

        protected void handle(@Nonnull SPacketDebugTrader message, @Nullable ServerPlayer sender) {
            TraderData trader = TraderSaveData.GetTrader(true, message.traderID);
            if (trader == null) {
                LightmansCurrency.LogInfo("Client is missing trader with id " + message.traderID + "!");
            } else {
                LightmansCurrency.LogInfo("Client Trader NBT for trader " + message.traderID + ":\n" + trader.save());
            }
        }
    }
}