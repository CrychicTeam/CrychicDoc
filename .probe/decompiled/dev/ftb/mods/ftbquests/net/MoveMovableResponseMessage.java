package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.quest.Movable;
import net.minecraft.network.FriendlyByteBuf;

public class MoveMovableResponseMessage extends BaseS2CMessage {

    private final long id;

    private final long chapter;

    private final double x;

    private final double y;

    MoveMovableResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.chapter = buffer.readLong();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
    }

    public MoveMovableResponseMessage(Movable movable, long c, double _x, double _y) {
        this.id = movable.getMovableID();
        this.chapter = c;
        this.x = _x;
        this.y = _y;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.MOVE_QUEST_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeLong(this.chapter);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.moveQuest(this.id, this.chapter, this.x, this.y);
    }
}