package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.network.FriendlyByteBuf;

public class DeleteObjectMessage extends BaseC2SMessage {

    private final long id;

    DeleteObjectMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
    }

    public DeleteObjectMessage(long i) {
        this.id = i;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.DELETE_OBJECT;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context)) {
            ServerQuestFile.INSTANCE.deleteObject(this.id);
        }
    }
}