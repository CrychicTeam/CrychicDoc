package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.client.gui.MyTeamScreen;
import dev.ftb.mods.ftbteams.data.ClientTeam;
import dev.ftb.mods.ftbteams.data.ClientTeamManagerImpl;
import dev.ftb.mods.ftbteams.data.TeamMessageImpl;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;

public class SyncMessageHistoryMessage extends BaseS2CMessage {

    private final List<TeamMessage> messages;

    public SyncMessageHistoryMessage(FriendlyByteBuf buf) {
        long now = System.currentTimeMillis();
        int nMessages = buf.readVarInt();
        this.messages = new ArrayList(nMessages);
        for (int i = 0; i < nMessages; i++) {
            this.messages.add(TeamMessageImpl.fromNetwork(now, buf));
        }
    }

    public SyncMessageHistoryMessage(Team team) {
        this.messages = team.getMessageHistory();
    }

    @Override
    public MessageType getType() {
        return FTBTeamsNet.SYNC_MESSAGE_HISTORY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        long now = System.currentTimeMillis();
        buf.writeVarInt(this.messages.size());
        for (TeamMessage msg : this.messages) {
            TeamMessageImpl.toNetwork(msg, now, buf);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ClientTeam team = ClientTeamManagerImpl.getInstance().selfTeam();
        if (team != null) {
            team.setMessageHistory(this.messages);
            MyTeamScreen.refreshIfOpen();
        }
    }
}