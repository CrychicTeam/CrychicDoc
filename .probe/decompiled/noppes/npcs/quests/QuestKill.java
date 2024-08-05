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

public class QuestKill extends QuestInterface {

    public TreeMap<String, Integer> targets = new TreeMap();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.targets = new TreeMap(NBTTags.getStringIntegerMap(compound.getList("QuestDialogs", 10)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("QuestDialogs", NBTTags.nbtStringIntegerMap(this.targets));
    }

    @Override
    public boolean isCompleted(Player player) {
        PlayerQuestData playerdata = PlayerData.get(player).questData;
        QuestData data = (QuestData) playerdata.activeQuests.get(this.questId);
        if (data == null) {
            return false;
        } else {
            HashMap<String, Integer> killed = this.getKilled(data);
            if (killed.size() != this.targets.size()) {
                return false;
            } else {
                for (String entity : killed.keySet()) {
                    if (!this.targets.containsKey(entity) || (Integer) this.targets.get(entity) > (Integer) killed.get(entity)) {
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

    public HashMap<String, Integer> getKilled(QuestData data) {
        return NBTTags.getStringIntegerMap(data.extraData.getList("Killed", 10));
    }

    public void setKilled(QuestData data, HashMap<String, Integer> killed) {
        data.extraData.put("Killed", NBTTags.nbtStringIntegerMap(killed));
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList();
        for (Entry<String, Integer> entry : this.targets.entrySet()) {
            list.add(new QuestKill.QuestKillObjective(player, (String) entry.getKey(), (Integer) entry.getValue()));
        }
        return (IQuestObjective[]) list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestKillObjective implements IQuestObjective {

        private final Player player;

        private final String entity;

        private final int amount;

        public QuestKillObjective(Player player, String entity, int amount) {
            this.player = player;
            this.entity = entity;
            this.amount = amount;
        }

        @Override
        public int getProgress() {
            PlayerData data = PlayerData.get(this.player);
            PlayerQuestData playerdata = data.questData;
            QuestData questdata = (QuestData) playerdata.activeQuests.get(QuestKill.this.questId);
            HashMap<String, Integer> killed = QuestKill.this.getKilled(questdata);
            return !killed.containsKey(this.entity) ? 0 : (Integer) killed.get(this.entity);
        }

        @Override
        public void setProgress(int progress) {
            if (progress >= 0 && progress <= this.amount) {
                PlayerData data = PlayerData.get(this.player);
                PlayerQuestData playerdata = data.questData;
                QuestData questdata = (QuestData) playerdata.activeQuests.get(QuestKill.this.questId);
                HashMap<String, Integer> killed = QuestKill.this.getKilled(questdata);
                if (!killed.containsKey(this.entity) || (Integer) killed.get(this.entity) != progress) {
                    killed.put(this.entity, progress);
                    QuestKill.this.setKilled(questdata, killed);
                    data.questData.checkQuestCompletion(this.player, 2);
                    data.questData.checkQuestCompletion(this.player, 4);
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