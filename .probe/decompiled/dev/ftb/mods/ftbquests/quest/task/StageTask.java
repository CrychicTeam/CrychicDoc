package dev.ftb.mods.ftbquests.quest.task;

import dev.architectury.hooks.level.entity.PlayerHooks;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.integration.stages.StageHelper;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StageTask extends AbstractBooleanTask {

    private String stage = "";

    public StageTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TaskTypes.STAGE;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString("stage", this.stage);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.stage = nbt.getString("stage");
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeUtf(this.stage, 32767);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.stage = buffer.readUtf(32767);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.addString("stage", this.stage, v -> this.stage = v, "").setNameKey("ftbquests.task.ftbquests.gamestage");
    }

    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.translatable("ftbquests.task.ftbquests.gamestage").append(": ").append(Component.literal(this.stage).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 20;
    }

    @Override
    public boolean canSubmit(TeamData teamData, ServerPlayer player) {
        return StageHelper.INSTANCE.getProvider().has(player, this.stage);
    }

    public static void checkStages(ServerPlayer player) {
        TeamData data = ServerQuestFile.INSTANCE != null && !PlayerHooks.isFake(player) ? ServerQuestFile.INSTANCE.getOrCreateTeamData(player) : null;
        if (data != null && !data.isLocked()) {
            ServerQuestFile.INSTANCE.withPlayerContext(player, () -> {
                for (Task task : ServerQuestFile.INSTANCE.getAllTasks()) {
                    if (task instanceof StageTask && data.canStartTasks(task.getQuest())) {
                        task.submitTask(data, player);
                    }
                }
            });
        }
    }
}