package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class CreateOtherTeamDataMessage extends BaseS2CMessage {

    private final TeamDataUpdate dataUpdate;

    CreateOtherTeamDataMessage(FriendlyByteBuf buffer) {
        this.dataUpdate = new TeamDataUpdate(buffer);
    }

    public CreateOtherTeamDataMessage(TeamDataUpdate update) {
        this.dataUpdate = update;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CREATE_OTHER_TEAM_DATA;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        this.dataUpdate.write(buffer);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.createOtherTeamData(this.dataUpdate);
    }
}