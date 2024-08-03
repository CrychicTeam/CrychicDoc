package io.github.lightman314.lightmanscurrency.network.message.teams;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketCreateTeamResponse extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketCreateTeamResponse> HANDLER = new SPacketCreateTeamResponse.H();

    long teamID;

    public SPacketCreateTeamResponse(long teamID) {
        this.teamID = teamID;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.teamID);
    }

    private static class H extends CustomPacket.Handler<SPacketCreateTeamResponse> {

        @Nonnull
        public SPacketCreateTeamResponse decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketCreateTeamResponse(buffer.readLong());
        }

        protected void handle(@Nonnull SPacketCreateTeamResponse message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.createTeamResponse(message.teamID);
        }
    }
}