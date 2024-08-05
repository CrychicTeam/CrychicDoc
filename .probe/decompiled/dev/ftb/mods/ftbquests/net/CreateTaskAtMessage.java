package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.util.NetUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class CreateTaskAtMessage extends BaseC2SMessage {

    private final long chapterId;

    private final double x;

    private final double y;

    private final TaskType type;

    private final CompoundTag nbt;

    public CreateTaskAtMessage(Chapter chapter, double x, double y, Task task) {
        this.chapterId = chapter.id;
        this.x = x;
        this.y = y;
        this.type = task.getType();
        this.nbt = new CompoundTag();
        task.writeData(this.nbt);
    }

    CreateTaskAtMessage(FriendlyByteBuf buffer) {
        this.chapterId = buffer.readLong();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.type = ServerQuestFile.INSTANCE.getTaskType(buffer.readVarInt());
        this.nbt = buffer.readNbt();
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.CREATE_TASK_AT;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeLong(this.chapterId);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeVarInt(this.type.internalId);
        buffer.writeNbt(this.nbt);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (NetUtils.canEdit(context) && context.getPlayer() instanceof ServerPlayer sp) {
            ServerQuestFile file = ServerQuestFile.INSTANCE;
            Chapter ch = file.getChapter(this.chapterId);
            if (ch != null) {
                Quest quest = new Quest(file.newID(), ch);
                quest.setX(this.x);
                quest.setY(this.y);
                quest.onCreated();
                new CreateObjectResponseMessage(quest, null).sendToAll(sp.m_20194_());
                Task task = this.type.createTask(file.newID(), quest);
                task.readData(this.nbt);
                task.onCreated();
                CompoundTag extra = new CompoundTag();
                extra.putString("type", this.type.getTypeForNBT());
                new CreateObjectResponseMessage(task, extra, sp.m_20148_()).sendToAll(sp.m_20194_());
                file.refreshIDMap();
                file.clearCachedData();
                file.markDirty();
            }
        }
    }
}