package io.github.lightman314.lightmanscurrency.network.message.data.team;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketRemoveClientTeam extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketRemoveClientTeam> HANDLER = new SPacketRemoveClientTeam.H();

    long teamID;

    public SPacketRemoveClientTeam(long teamID) {
        this.teamID = teamID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.teamID);
    }

    private static class H extends CustomPacket.Handler<SPacketRemoveClientTeam> {

        @Nonnull
        public SPacketRemoveClientTeam decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketRemoveClientTeam(buffer.readLong());
        }

        protected void handle(@Nonnull SPacketRemoveClientTeam message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.removeTeam(message.teamID);
        }
    }
}