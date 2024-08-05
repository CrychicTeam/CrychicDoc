package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.network.FriendlyByteBuf;

public class MoveChapterGroupMessage extends BaseC2SMessage {

    private final long id;

    private final boolean movingUp;

    public MoveChapterGroupMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.movingUp = buffer.readBoolean();
    }

    public MoveChapterGroupMessage(long id, boolean movingUp) {
        this.id = id;
        this.movingUp = movingUp;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.MOVE_CHAPTER_GROUP;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeBoolean(this.movingUp);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context)) {
            ServerQuestFile.INSTANCE.moveChapterGroup(this.id, this.movingUp);
        }
    }
}