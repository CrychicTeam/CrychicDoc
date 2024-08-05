package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class EditObjectResponseMessage extends BaseS2CMessage {

    private final long id;

    private final CompoundTag nbt;

    EditObjectResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.nbt = buffer.readNbt();
    }

    public EditObjectResponseMessage(QuestObjectBase o) {
        this.id = o.id;
        this.nbt = new CompoundTag();
        o.writeData(this.nbt);
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.EDIT_OBJECT_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeNbt(this.nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.editObject(this.id, this.nbt);
    }
}