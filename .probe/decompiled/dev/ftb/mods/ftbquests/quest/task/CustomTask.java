package dev.ftb.mods.ftbquests.quest.task;

import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftbquests.net.SubmitTaskMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.TeamData;
import java.util.function.Predicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CustomTask extends Task {

    public static final Predicate<QuestObjectBase> PREDICATE = object -> object instanceof CustomTask;

    private CustomTask.Check check = null;

    private int checkTimer = 1;

    private long maxProgress = 1L;

    private boolean enableButton = false;

    public CustomTask(long id, Quest quest) {
        super(id, quest);
    }

    public void setCheck(CustomTask.Check check) {
        this.check = check;
    }

    @Override
    public TaskType getType() {
        return TaskTypes.CUSTOM;
    }

    @Override
    public long getMaxProgress() {
        return this.maxProgress;
    }

    public void setCheckTimer(int checkTimer) {
        this.checkTimer = checkTimer;
    }

    public void setMaxProgress(long maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setEnableButton(boolean enableButton) {
        this.enableButton = enableButton;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onButtonClicked(Button button, boolean canClick) {
        if (this.enableButton && canClick) {
            button.playClickSound();
            new SubmitTaskMessage(this.id).sendToServer();
        }
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return this.check == null ? 0 : this.checkTimer;
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeVarInt(this.checkTimer);
        buffer.writeVarLong(this.maxProgress);
        buffer.writeBoolean(this.enableButton);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.checkTimer = buffer.readVarInt();
        this.maxProgress = buffer.readVarLong();
        this.enableButton = buffer.readBoolean();
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (this.check != null && this.checkTaskSequence(teamData) && !teamData.isCompleted(this)) {
            this.check.check(new CustomTask.Data(this, teamData), player);
        }
    }

    @Override
    public boolean checkOnLogin() {
        return false;
    }

    @FunctionalInterface
    public interface Check {

        void check(CustomTask.Data var1, ServerPlayer var2);
    }

    public static record Data(CustomTask task, TeamData teamData) {

        public long getProgress() {
            return this.teamData.getProgress(this.task);
        }

        public void setProgress(long l) {
            this.teamData.setProgress(this.task, l);
        }

        public void addProgress(long l) {
            this.teamData.addProgress(this.task, l);
        }
    }
}