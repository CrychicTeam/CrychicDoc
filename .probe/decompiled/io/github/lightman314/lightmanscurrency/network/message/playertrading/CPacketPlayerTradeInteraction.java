package io.github.lightman314.lightmanscurrency.network.message.playertrading;

import io.github.lightman314.lightmanscurrency.common.playertrading.PlayerTrade;
import io.github.lightman314.lightmanscurrency.common.playertrading.PlayerTradeManager;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketPlayerTradeInteraction extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketPlayerTradeInteraction> HANDLER = new CPacketPlayerTradeInteraction.H();

    private final int tradeID;

    private final CompoundTag message;

    public CPacketPlayerTradeInteraction(int tradeID, CompoundTag message) {
        this.tradeID = tradeID;
        this.message = message;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.tradeID);
        buffer.writeNbt(this.message);
    }

    private static class H extends CustomPacket.Handler<CPacketPlayerTradeInteraction> {

        @Nonnull
        public CPacketPlayerTradeInteraction decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketPlayerTradeInteraction(buffer.readInt(), buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull CPacketPlayerTradeInteraction message, @Nullable ServerPlayer sender) {
            PlayerTrade trade = PlayerTradeManager.GetTrade(message.tradeID);
            if (trade != null && sender != null) {
                trade.handleInteraction(sender, message.message);
            }
        }
    }
}