package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class DeleteObjectResponseMessage extends BaseS2CMessage {

    private final long id;

    DeleteObjectResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
    }

    public DeleteObjectResponseMessage(long i) {
        this.id = i;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.DELETE_OBJECT_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.deleteObject(this.id);
    }
}