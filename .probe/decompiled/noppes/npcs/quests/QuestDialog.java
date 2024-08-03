package noppes.npcs.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.handler.data.IQuestObjective;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.PlayerData;

public class QuestDialog extends QuestInterface {

    public HashMap<Integer, Integer> dialogs = new HashMap();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.dialogs = NBTTags.getIntegerIntegerMap(compound.getList("QuestDialogs", 10));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("QuestDialogs", NBTTags.nbtIntegerIntegerMap(this.dialogs));
    }

    @Override
    public boolean isCompleted(Player player) {
        for (int dialogId : this.dialogs.values()) {
            if (!PlayerData.get(player).dialogData.dialogsRead.contains(dialogId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void handleComplete(Player player) {
    }

    @Override
    public IQuestObjective[] getObjectives(Player player) {
        List<IQuestObjective> list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            if (this.dialogs.containsKey(i)) {
                Dialog dialog = (Dialog) DialogController.instance.dialogs.get(this.dialogs.get(i));
                if (dialog != null) {
                    list.add(new QuestDialog.QuestDialogObjective(player, dialog));
                }
            }
        }
        return (IQuestObjective[]) list.toArray(new IQuestObjective[list.size()]);
    }

    class QuestDialogObjective implements IQuestObjective {

        private final Player player;

        private final Dialog dialog;

        public QuestDialogObjective(Player player, Dialog dialog) {
            this.player = player;
            this.dialog = dialog;
        }

        @Override
        public int getProgress() {
            return this.isCompleted() ? 1 : 0;
        }

        @Override
        public void setProgress(int progress) {
            if (progress >= 0 && progress <= 1) {
                PlayerData data = PlayerData.get(this.player);
                boolean completed = data.dialogData.dialogsRead.contains(this.dialog.id);
                if (progress == 0 && completed) {
                    data.dialogData.dialogsRead.remove(this.dialog.id);
                    data.questData.checkQuestCompletion(this.player, 1);
                    data.updateClient = true;
                }
                if (progress == 1 && !completed) {
                    data.dialogData.dialogsRead.add(this.dialog.id);
                    data.questData.checkQuestCompletion(this.player, 1);
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
            return data.dialogData.dialogsRead.contains(this.dialog.id);
        }

        @Override
        public String getText() {
            return this.getMCText().getString();
        }

        @Override
        public Component getMCText() {
            return Component.translatable(this.dialog.title).append(" (").append(Component.translatable(this.isCompleted() ? "quest.read" : "quest.unread")).append(")");
        }
    }
}