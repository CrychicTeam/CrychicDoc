package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import net.minecraft.network.FriendlyByteBuf;

public class ChangeChapterGroupResponseMessage extends BaseS2CMessage {

    private final long id;

    private final long group;

    public ChangeChapterGroupResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.group = buffer.readLong();
    }

    public ChangeChapterGroupResponseMessage(long i, long g) {
        this.id = i;
        this.group = g;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CHANGE_CHAPTER_GROUP_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeLong(this.group);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.changeChapterGroup(this.id, this.group);
    }
}