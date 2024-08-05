package noppes.npcs.controllers.data;

import net.minecraft.nbt.CompoundTag;

public class QuestData {

    public Quest quest;

    public boolean isCompleted;

    public CompoundTag extraData = new CompoundTag();

    public QuestData(Quest quest) {
        this.quest = quest;
    }

    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("QuestCompleted", this.isCompleted);
        nbttagcompound.put("ExtraData", this.extraData);
    }

    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        this.isCompleted = nbttagcompound.getBoolean("QuestCompleted");
        this.extraData = nbttagcompound.getCompound("ExtraData");
    }
}