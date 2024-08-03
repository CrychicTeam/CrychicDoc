package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.handler.data.IQuestCategory;

public class QuestCategory implements IQuestCategory {

    public HashMap<Integer, Quest> quests;

    public int id = -1;

    public String title = "";

    public QuestCategory() {
        this.quests = new HashMap();
    }

    public void readNBT(CompoundTag nbttagcompound) {
        this.id = nbttagcompound.getInt("Slot");
        this.title = nbttagcompound.getString("Title");
        ListTag dialogsList = nbttagcompound.getList("Dialogs", 10);
        if (dialogsList != null) {
            for (int ii = 0; ii < dialogsList.size(); ii++) {
                CompoundTag nbttagcompound2 = dialogsList.getCompound(ii);
                Quest quest = new Quest(this);
                quest.readNBT(nbttagcompound2);
                this.quests.put(quest.id, quest);
            }
        }
    }

    public CompoundTag writeNBT(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("Slot", this.id);
        nbttagcompound.putString("Title", this.title);
        ListTag dialogs = new ListTag();
        for (int dialogId : this.quests.keySet()) {
            Quest quest = (Quest) this.quests.get(dialogId);
            dialogs.add(quest.save(new CompoundTag()));
        }
        nbttagcompound.put("Dialogs", dialogs);
        return nbttagcompound;
    }

    @Override
    public List<IQuest> quests() {
        return new ArrayList(this.quests.values());
    }

    @Override
    public String getName() {
        return this.title;
    }

    @Override
    public IQuest create() {
        return new Quest(this);
    }
}