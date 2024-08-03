package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;

public class QuestLocation extends QuestInterface {

    public String location = "";

    public String location2 = "";

    public String location3 = "";

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.location = compound.getString("QuestLocation");
        this.location2 = compound.getString("QuestLocation2");
        this.location3 = compound.getString("QuestLocation3");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("QuestLocation", this.location);
        compound.putString("QuestLocation2", this.location2);
        compound.putString("QuestLocation3", this.location3);
    }

    @Override
    public boolean isCompleted(Player player) {
        PlayerQuestData playerdata = PlayerData.get(player).questData;
        QuestData data = (QuestData) playerdata.activeQuests.get(this.questId);
        return data == null ? false : this.getFound(data, 0);
    }

    @Override
    public void handleComplete(Player player) {
    }

    public boolean getFound(QuestData data, int i) {
        if (i == 1) {
            return data.extraData.getBoolean("LocationFound");
        } else if (i == 2) {
            return data.extraData.getBoolean("Location2Found");
        } else if (i == 3) {
            return data.extraData.getBoolean("Location3Found");
        } else if (!this.location.isEmpty() && !data.extraData.getBoolean("LocationFound")) {
            return false;
        } else {
            return !this.location2.isEmpty() && !data.extraData.getBoolean("Location2Found") ? false : this.location3.isEmpty() || data.extraData.getBoolean("Location3Found");
        }
    }

    public boolean setFound(QuestData data, String location) {
        if (location.equalsIgnoreCase(this.location) && !data.extraData.getBoolean("LocationFound")) {
            data.extraData.putBoolean("LocationFound", true);
            return true;
        } else if (location.equalsIgnoreCase(this.location2) && !data.extraData.getBoolean("LocationFound2")) {
            data.extraData.putBoolean("Location2Found", true);
            return true;
        } else if (location.equalsIgnoreCase(this.location3) && !data.extraData.getBoolean("LocationFound3")) {
            data.extraData.putBoolean("Location3Found", true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList();
        if (!this.location.isEmpty()) {
            list.add(new QuestLocation.QuestLocationObjective(player, this.location, "LocationFound"));
        }
        if (!this.location2.isEmpty()) {
            list.add(new QuestLocation.QuestLocationObjective(player, this.location2, "Location2Found"));
        }
        if (!this.location3.isEmpty()) {
            list.add(new QuestLocation.QuestLocationObjective(player, this.location3, "Location3Found"));
        }
        return (IQuestObjective[]) list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestLocationObjective implements IQuestObjective {

        private final Player player;

        private final String location;

        private final String nbtName;

        public QuestLocationObjective(Player player, String location, String nbtName) {
            this.player = player;
            this.location = location;
            this.nbtName = nbtName;
        }

        @Override
        public int getProgress() {
            return this.isCompleted() ? 1 : 0;
        }

        @Override
        public void setProgress(int progress) {
            if (progress >= 0 && progress <= 1) {
                PlayerData data = PlayerData.get(this.player);
                QuestData questData = (QuestData) data.questData.activeQuests.get(QuestLocation.this.questId);
                boolean completed = questData.extraData.getBoolean(this.nbtName);
                if ((!completed || progress != 1) && (completed || progress != 0)) {
                    questData.extraData.putBoolean(this.nbtName, progress == 1);
                    data.questData.checkQuestCompletion(this.player, 3);
                    data.updateClient = true;
                }
            } else {
                throw new CustomNPCsException("Progress has to be 0 or 1");
            }
        }

        @Override
        public int getMaxProgress() {
            return 1;
        }

        @Override
        public boolean isCompleted() {
            PlayerData data = PlayerData.get(this.player);
            QuestData questData = (QuestData) data.questData.activeQuests.get(QuestLocation.this.questId);
            return questData.extraData.getBoolean(this.nbtName);
        }

        @Override
        public String getText() {
            return this.getMCText().getString();
        }

        @Override
        public Component getMCText() {
            return Component.translatable(this.location).append(Component.translatable(this.isCompleted() ? "quest.found" : "quest.notfound"));
        }
    }
}