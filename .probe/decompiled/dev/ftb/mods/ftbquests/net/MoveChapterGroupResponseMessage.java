package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class MoveChapterGroupResponseMessage extends BaseS2CMessage {

    private final long id;

    private final boolean up;

    MoveChapterGroupResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.up = buffer.readBoolean();
    }

    public MoveChapterGroupResponseMessage(long i, boolean u) {
        this.id = i;
        this.up = u;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.MOVE_CHAPTER_GROUP_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeBoolean(this.up);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.moveChapterGroup(this.id, this.up);
    }
}