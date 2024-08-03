package dev.ftb.mods.ftbquests.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

public class CopyQuestMessage extends BaseC2SMessage {

    private final long id;

    private final long chapterId;

    private final double qx;

    private final double qy;

    private final boolean copyDeps;

    public CopyQuestMessage(Quest toCopy, Chapter chapter, double qx, double qy, boolean copyDeps) {
        this.id = toCopy.id;
        this.chapterId = chapter.id;
        this.qx = qx;
        this.qy = qy;
        this.copyDeps = copyDeps;
    }

    public CopyQuestMessage(FriendlyByteBuf buf) {
        this.id = buf.readLong();
        this.chapterId = buf.readLong();
        this.qx = buf.readDouble();
        this.qy = buf.readDouble();
        this.copyDeps = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return FTBQuestsNetHandler.COPY_QUEST;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeLong(this.id);
        buf.writeLong(this.chapterId);
        buf.writeDouble(this.qx);
        buf.writeDouble(this.qy);
        buf.writeBoolean(this.copyDeps);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        BaseQuestFile file = ServerQuestFile.INSTANCE;
        if (file.get(this.id) instanceof Quest toCopy && file.get(this.chapterId) instanceof Chapter chapter) {
            Quest newQuest = (Quest) Objects.requireNonNull(QuestObjectBase.copy(toCopy, () -> new Quest(file.newID(), chapter)));
            if (!this.copyDeps) {
                newQuest.clearDependencies();
            }
            newQuest.setX(this.qx);
            newQuest.setY(this.qy);
            newQuest.onCreated();
            toCopy.getTasks().forEach(task -> {
                Task newTask = QuestObjectBase.copy(task, () -> TaskType.createTask(file.newID(), newQuest, task.getType().getTypeForNBT()));
                if (newTask != null) {
                    newTask.onCreated();
                }
            });
            for (Reward reward : toCopy.getRewards()) {
                Reward newReward = QuestObjectBase.copy(reward, () -> RewardType.createReward(file.newID(), newQuest, reward.getType().getTypeForNBT()));
                if (newReward != null) {
                    newReward.onCreated();
                }
            }
            MinecraftServer server = context.getPlayer().m_20194_();
            new CreateObjectResponseMessage(newQuest, null).sendToAll(server);
            newQuest.getTasks().forEach(task -> {
                CompoundTag extra = new CompoundTag();
                extra.putString("type", task.getType().getTypeForNBT());
                new CreateObjectResponseMessage(task, extra).sendToAll(server);
            });
            newQuest.getRewards().forEach(rewardx -> {
                CompoundTag extra = new CompoundTag();
                extra.putString("type", rewardx.getType().getTypeForNBT());
                new CreateObjectResponseMessage(rewardx, extra).sendToAll(server);
            });
            ServerQuestFile.INSTANCE.refreshIDMap();
            ServerQuestFile.INSTANCE.clearCachedData();
            ServerQuestFile.INSTANCE.markDirty();
        }
    }
}