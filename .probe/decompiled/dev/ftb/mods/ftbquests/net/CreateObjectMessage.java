package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CreateObjectMessage extends BaseC2SMessage {

    private final long parent;

    private final QuestObjectType type;

    private final boolean openScreen;

    private final CompoundTag nbt;

    private final CompoundTag extra;

    CreateObjectMessage(FriendlyByteBuf buffer) {
        this.parent = buffer.readLong();
        this.type = QuestObjectType.NAME_MAP.read(buffer);
        this.openScreen = buffer.readBoolean();
        this.nbt = buffer.readNbt();
        this.extra = buffer.readNbt();
    }

    public CreateObjectMessage(QuestObjectBase o, @Nullable CompoundTag e, boolean openScreen) {
        this.parent = o.getParentID();
        this.type = o.getObjectType();
        this.openScreen = openScreen;
        this.nbt = new CompoundTag();
        o.writeData(this.nbt);
        this.extra = e;
    }

    public CreateObjectMessage(QuestObjectBase o, @Nullable CompoundTag e) {
        this(o, e, true);
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CREATE_OBJECT;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.parent);
        QuestObjectType.NAME_MAP.write(buffer, this.type);
        buffer.writeBoolean(this.openScreen);
        buffer.writeNbt(this.nbt);
        buffer.writeNbt(this.extra);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context) && context.getPlayer() instanceof ServerPlayer sp) {
            QuestObjectBase object = ServerQuestFile.INSTANCE.create(ServerQuestFile.INSTANCE.newID(), this.type, this.parent, this.extra == null ? new CompoundTag() : this.extra);
            object.readData(this.nbt);
            object.onCreated();
            object.getQuestFile().refreshIDMap();
            object.getQuestFile().clearCachedData();
            object.getQuestFile().markDirty();
            new CreateObjectResponseMessage(object, this.extra, this.openScreen ? sp.m_20148_() : Util.NIL_UUID).sendToAll(sp.m_20194_());
        }
    }
}