package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class TeamDataChangedMessage extends BaseS2CMessage {

    private final TeamDataUpdate oldDataUpdate;

    private final TeamDataUpdate newDataUpdate;

    TeamDataChangedMessage(FriendlyByteBuf buffer) {
        this.oldDataUpdate = new TeamDataUpdate(buffer);
        this.newDataUpdate = new TeamDataUpdate(buffer);
    }

    public TeamDataChangedMessage(TeamDataUpdate oldData, TeamDataUpdate newData) {
        this.oldDataUpdate = oldData;
        this.newDataUpdate = newData;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.TEAM_DATA_CHANGED;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        this.oldDataUpdate.write(buffer);
        this.newDataUpdate.write(buffer);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.teamDataChanged(this.oldDataUpdate, this.newDataUpdate);
    }
}