package noppes.npcs.quests;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.handler.data.IQuestObjective;

public abstract class QuestInterface {

    public int questId;

    public abstract void addAdditionalSaveData(CompoundTag var1);

    public abstract void readAdditionalSaveData(CompoundTag var1);

    public abstract boolean isCompleted(Player var1);

    public abstract void handleComplete(Player var1);

    public abstract IQuestObjective[] getObjectives(Player var1);
}