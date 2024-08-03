package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class UpdateTeamDataMessage extends BaseS2CMessage {

    private final UUID team;

    private final String name;

    UpdateTeamDataMessage(FriendlyByteBuf buffer) {
        this.team = buffer.readUUID();
        this.name = buffer.readUtf(32767);
    }

    public UpdateTeamDataMessage(TeamData data) {
        this.team = data.getTeamId();
        this.name = data.getName();
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.UPDATE_TEAM_DATA;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.team);
        buffer.writeUtf(this.name, 32767);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.updateTeamData(this.team, this.name);
    }
}