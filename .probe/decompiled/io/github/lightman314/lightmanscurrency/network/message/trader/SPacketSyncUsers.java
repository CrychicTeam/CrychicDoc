package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncUsers extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncUsers> HANDLER = new SPacketSyncUsers.H();

    long traderID;

    int userCount;

    public SPacketSyncUsers(long traderID, int userCount) {
        this.traderID = traderID;
        this.userCount = userCount;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
        buffer.writeInt(this.userCount);
    }

    private static class H extends CustomPacket.Handler<SPacketSyncUsers> {

        @Nonnull
        public SPacketSyncUsers decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncUsers(buffer.readLong(), buffer.readInt());
        }

        protected void handle(@Nonnull SPacketSyncUsers message, @Nullable ServerPlayer sender) {
            TraderData trader = TraderSaveData.GetTrader(true, message.traderID);
            if (trader != null) {
                trader.updateUserCount(message.userCount);
            }
        }
    }
}