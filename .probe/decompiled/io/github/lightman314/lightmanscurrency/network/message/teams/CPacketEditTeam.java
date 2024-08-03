package io.github.lightman314.lightmanscurrency.network.message.teams;

import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketEditTeam extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketEditTeam> HANDLER = new CPacketEditTeam.H();

    long teamID;

    LazyPacketData request;

    public CPacketEditTeam(long teamID, LazyPacketData request) {
        this.teamID = teamID;
        this.request = request;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.teamID);
        this.request.encode(buffer);
    }

    private static class H extends CustomPacket.Handler<CPacketEditTeam> {

        @Nonnull
        public CPacketEditTeam decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketEditTeam(buffer.readLong(), LazyPacketData.decode(buffer));
        }

        protected void handle(@Nonnull CPacketEditTeam message, @Nullable ServerPlayer sender) {
            Team team = TeamSaveData.GetTeam(false, message.teamID);
            if (sender != null && team != null) {
                team.HandleEditRequest(sender, message.request);
            }
        }
    }
}