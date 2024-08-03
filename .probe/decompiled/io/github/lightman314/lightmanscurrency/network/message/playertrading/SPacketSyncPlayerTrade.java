package io.github.lightman314.lightmanscurrency.network.message.playertrading;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.playertrading.ClientPlayerTrade;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncPlayerTrade extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncPlayerTrade> HANDLER = new SPacketSyncPlayerTrade.H();

    private final ClientPlayerTrade data;

    public SPacketSyncPlayerTrade(ClientPlayerTrade data) {
        this.data = data;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        this.data.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<SPacketSyncPlayerTrade> {

        @Nonnull
        public SPacketSyncPlayerTrade decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncPlayerTrade(ClientPlayerTrade.decode(buffer));
        }

        protected void handle(@Nonnull SPacketSyncPlayerTrade message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.loadPlayerTrade(message.data);
        }
    }
}