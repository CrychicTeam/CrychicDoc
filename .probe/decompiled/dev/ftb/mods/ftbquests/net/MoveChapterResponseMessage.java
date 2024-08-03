package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class MoveChapterResponseMessage extends BaseS2CMessage {

    private final long id;

    private final boolean up;

    MoveChapterResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.up = buffer.readBoolean();
    }

    public MoveChapterResponseMessage(long i, boolean u) {
        this.id = i;
        this.up = u;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.MOVE_CHAPTER_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeBoolean(this.up);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.moveChapter(this.id, this.up);
    }
}