package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.Movable;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import net.minecraft.network.FriendlyByteBuf;

public class MoveMovableMessage extends BaseC2SMessage {

    private final long id;

    private final long chapterID;

    private final double x;

    private final double y;

    MoveMovableMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.chapterID = buffer.readLong();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
    }

    public MoveMovableMessage(Movable obj, long c, double _x, double _y) {
        this.id = obj.getMovableID();
        this.chapterID = c;
        this.x = _x;
        this.y = _y;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.MOVE_QUEST;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeLong(this.chapterID);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (ServerQuestFile.INSTANCE.get(this.id) instanceof Movable movable) {
            movable.onMoved(this.x, this.y, this.chapterID);
            ServerQuestFile.INSTANCE.markDirty();
            new MoveMovableResponseMessage(movable, this.chapterID, this.x, this.y).sendToAll(context.getPlayer().m_20194_());
        }
    }
}