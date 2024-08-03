package io.github.lightman314.lightmanscurrency.network.message.teams;

import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketCreateTeam extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketCreateTeam> HANDLER = new CPacketCreateTeam.H();

    String teamName;

    public CPacketCreateTeam(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUtf(this.teamName, 32);
    }

    private static class H extends CustomPacket.Handler<CPacketCreateTeam> {

        @Nonnull
        public CPacketCreateTeam decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketCreateTeam(buffer.readUtf(32));
        }

        protected void handle(@Nonnull CPacketCreateTeam message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                Team newTeam = TeamSaveData.RegisterTeam(sender, message.teamName);
                if (newTeam != null) {
                    new SPacketCreateTeamResponse(newTeam.getID()).sendTo(sender);
                }
            }
        }
    }
}