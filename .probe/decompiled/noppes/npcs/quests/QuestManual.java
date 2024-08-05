package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.QuestData;

public class QuestManual extends QuestInterface {

    public TreeMap<String, Integer> manuals = new TreeMap();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.manuals = new TreeMap(NBTTags.getStringIntegerMap(compound.getList("QuestManual", 10)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("QuestManual", NBTTags.nbtStringIntegerMap(this.manuals));
    }

    @Override
    public boolean isCompleted(Player player) {
        PlayerQuestData playerdata = PlayerData.get(player).questData;
        QuestData data = (QuestData) playerdata.activeQuests.get(this.questId);
        if (data == null) {
            return false;
        } else {
            HashMap<String, Integer> manual = this.getManual(data);
            if (manual.size() != this.manuals.size()) {
                return false;
            } else {
                for (String entity : manual.keySet()) {
                    if (!this.manuals.containsKey(entity) || (Integer) this.manuals.get(entity) > (Integer) manual.get(entity)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void handleComplete(Player player) {
    }

    public HashMap<String, Integer> getManual(QuestData data) {
        return NBTTags.getStringIntegerMap(data.extraData.getList("Manual", 10));
    }

    public void setManual(QuestData data, HashMap<String, Integer> manual) {
        data.extraData.put("Manual", NBTTags.nbtStringIntegerMap(manual));
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList();
        for (Entry<String, Integer> entry : this.manuals.entrySet()) {
            list.add(new QuestManual.QuestManualObjective(player, (String) entry.getKey(), (Integer) entry.getValue()));
        }
        return (IQuestObjective[]) list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestManualObjective implements IQuestObjective {

        private final Player player;

        private final String entity;

        private final int amount;

        public QuestManualObjective(Player player, String entity, int amount) {
            this.player = player;
            this.entity = entity;
            this.amount = amount;
        }

        @Override
        public int getProgress() {
            PlayerData data = PlayerData.get(this.player);
            PlayerQuestData playerdata = data.questData;
            QuestData questdata = (QuestData) playerdata.activeQuests.get(QuestManual.this.questId);
            HashMap<String, Integer> manual = QuestManual.this.getManual(questdata);
            return !manual.containsKey(this.entity) ? 0 : (Integer) manual.get(this.entity);
        }

        @Override
        public void setProgress(int progress) {
            if (progress >= 0 && progress <= this.amount) {
                PlayerData data = PlayerData.get(this.player);
                PlayerQuestData playerdata = data.questData;
                QuestData questdata = (QuestData) playerdata.activeQuests.get(QuestManual.this.questId);
                HashMap<String, Integer> manual = QuestManual.this.getManual(questdata);
                if (!manual.containsKey(this.entity) || (Integer) manual.get(this.entity) != progress) {
                    manual.put(this.entity, progress);
                    QuestManual.this.setManual(questdata, manual);
                    data.questData.checkQuestCompletion(this.player, 5);
                    data.updateClient = true;
                }
            } else {
                throw new CustomNPCsException("Progress has to be between 0 and " + this.amount);
            }
        }

        @Override
        public int getMaxProgress() {
            return this.amount;
        }

        @Override
        public boolean isCompleted() {
            return this.getProgress() >= this.amount;
        }

        @Override
        public String getText() {
            return this.getMCText().getString();
        }

        @Override
        public Component getMCText() {
            return Component.translatable(this.entity).append(": " + this.getProgress() + "/" + this.getMaxProgress());
        }
    }
}