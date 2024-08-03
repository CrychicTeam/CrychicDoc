package dev.ftb.mods.ftbteams.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.client.FTBTeamsClient;
import dev.ftb.mods.ftbteams.client.KnownClientPlayerNet;
import net.minecraft.network.FriendlyByteBuf;

public class UpdatePresenceMessage extends BaseS2CMessage {

    private final KnownClientPlayer update;

    UpdatePresenceMessage(FriendlyByteBuf buffer) {
        this.update = KnownClientPlayerNet.fromNetwork(buffer);
    }

    public UpdatePresenceMessage(KnownClientPlayer p) {
        this.update = p;
    }

    @Override
    public MessageType getType() {
        return FTBTeamsNet.UPDATE_PRESENCE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        KnownClientPlayerNet.write(this.update, buffer);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBTeamsClient.updatePresence(this.update);
    }
}