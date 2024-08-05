package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.client.FTBTeamsClient;
import dev.ftb.mods.ftbteams.data.TeamPropertyCollectionImpl;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class UpdatePropertiesResponseMessage extends BaseS2CMessage {

    private final UUID teamId;

    private final TeamPropertyCollection properties;

    UpdatePropertiesResponseMessage(FriendlyByteBuf buffer) {
        this.teamId = buffer.readUUID();
        this.properties = new TeamPropertyCollectionImpl();
        this.properties.read(buffer);
    }

    public UpdatePropertiesResponseMessage(UUID id, TeamPropertyCollection p) {
        this.teamId = id;
        this.properties = p;
    }

    @Override
    public MessageType getType() {
        return FTBTeamsNet.UPDATE_SETTINGS_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.teamId);
        this.properties.write(buffer);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBTeamsClient.updateSettings(this.teamId, this.properties);
    }
}