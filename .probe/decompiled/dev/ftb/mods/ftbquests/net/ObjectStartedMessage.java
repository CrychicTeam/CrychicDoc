package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import java.util.Date;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;

public class ObjectStartedMessage extends BaseS2CMessage {

    private final UUID team;

    private final long id;

    public ObjectStartedMessage(FriendlyByteBuf buffer) {
        this.team = buffer.readUUID();
        this.id = buffer.readLong();
    }

    public ObjectStartedMessage(UUID t, long i) {
        this.team = t;
        this.id = i;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.OBJECT_STARTED;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.team);
        buffer.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.objectStarted(this.team, this.id, new Date());
    }
}