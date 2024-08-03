package io.github.lightman314.lightmanscurrency.network.message.trader;

import io.github.lightman314.lightmanscurrency.common.menus.TraderMenu;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketExecuteTrade extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketExecuteTrade> HANDLER = new CPacketExecuteTrade.H();

    private final int trader;

    private final int tradeIndex;

    public CPacketExecuteTrade(int trader, int tradeIndex) {
        this.trader = trader;
        this.tradeIndex = tradeIndex;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeInt(this.trader);
        buffer.writeInt(this.tradeIndex);
    }

    private static class H extends CustomPacket.Handler<CPacketExecuteTrade> {

        @Nonnull
        public CPacketExecuteTrade decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketExecuteTrade(buffer.readInt(), buffer.readInt());
        }

        protected void handle(@Nonnull CPacketExecuteTrade message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.f_36096_ instanceof TraderMenu menu) {
                menu.ExecuteTrade(message.trader, message.tradeIndex);
            }
        }
    }
}