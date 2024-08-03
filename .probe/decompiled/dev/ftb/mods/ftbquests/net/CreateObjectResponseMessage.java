package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import java.util.UUID;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class CreateObjectResponseMessage extends BaseS2CMessage {

    private final long id;

    private final long parent;

    private final QuestObjectType type;

    private final CompoundTag nbt;

    private final CompoundTag extra;

    private final UUID creator;

    public CreateObjectResponseMessage(FriendlyByteBuf buffer) {
        this.id = buffer.readLong();
        this.parent = buffer.readLong();
        this.type = QuestObjectType.NAME_MAP.read(buffer);
        this.nbt = buffer.readNbt();
        this.extra = buffer.readNbt();
        this.creator = buffer.readBoolean() ? buffer.readUUID() : Util.NIL_UUID;
    }

    public CreateObjectResponseMessage(QuestObjectBase o, @Nullable CompoundTag e) {
        this(o, e, Util.NIL_UUID);
    }

    public CreateObjectResponseMessage(QuestObjectBase o, @Nullable CompoundTag e, UUID creator) {
        this.id = o.id;
        this.parent = o.getParentID();
        this.type = o.getObjectType();
        this.nbt = new CompoundTag();
        o.writeData(this.nbt);
        this.extra = e;
        this.creator = creator;
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CREATE_OBJECT_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
        buffer.writeLong(this.parent);
        QuestObjectType.NAME_MAP.write(buffer, this.type);
        buffer.writeNbt(this.nbt);
        buffer.writeNbt(this.extra);
        if (this.creator.equals(Util.NIL_UUID)) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeUUID(this.creator);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        FTBQuestsNetClient.createObject(this.id, this.parent, this.type, this.nbt, this.extra, this.creator);
    }
}