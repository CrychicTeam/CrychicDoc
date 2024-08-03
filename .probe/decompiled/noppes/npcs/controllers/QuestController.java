package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import noppes.npcs.CustomNpcs;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketSyncRemove;
import noppes.npcs.packets.client.PacketSyncUpdate;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.shared.common.util.LogWriter;
import noppes.npcs.util.NBTJsonUtil;

public class QuestController implements IQuestHandler {

    public HashMap<Integer, QuestCategory> categoriesSync = new HashMap();

    public HashMap<Integer, QuestCategory> categories = new HashMap();

    public HashMap<Integer, Quest> quests = new HashMap();

    public static QuestController instance = new QuestController();

    private int lastUsedCatID = 0;

    private int lastUsedQuestID = 0;

    public QuestController() {
        instance = this;
    }

    public void load() {
        this.categories.clear();
        this.quests.clear();
        this.lastUsedCatID = 0;
        this.lastUsedQuestID = 0;
        try {
            File file = new File(CustomNpcs.getLevelSaveDirectory(), "quests.dat");
            if (file.exists()) {
                this.loadCategoriesOld(file);
                file.delete();
                file = new File(CustomNpcs.getLevelSaveDirectory(), "quests.dat_old");
                if (file.exists()) {
                    file.delete();
                }
                return;
            }
        } catch (Exception var10) {
            LogWriter.except(var10);
        }
        File dir = this.getDir();
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    QuestCategory category = this.loadCategoryDir(file);
                    Iterator<Integer> ite = category.quests.keySet().iterator();
                    while (ite.hasNext()) {
                        int id = (Integer) ite.next();
                        if (id > this.lastUsedQuestID) {
                            this.lastUsedQuestID = id;
                        }
                        Quest quest = (Quest) category.quests.get(id);
                        if (this.quests.containsKey(id)) {
                            LogWriter.error("Duplicate id " + quest.id + " from category " + category.title);
                            ite.remove();
                        } else {
                            this.quests.put(id, quest);
                        }
                    }
                    this.lastUsedCatID++;
                    category.id = this.lastUsedCatID;
                    this.categories.put(category.id, category);
                }
            }
        }
    }

    private QuestCategory loadCategoryDir(File dir) {
        QuestCategory category = new QuestCategory();
        category.title = dir.getName();
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    Quest quest = new Quest(category);
                    quest.id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 5));
                    quest.readNBTPartial(NBTJsonUtil.LoadFile(file));
                    category.quests.put(quest.id, quest);
                } catch (Exception var8) {
                    LogWriter.error("Error loading: " + file.getAbsolutePath(), var8);
                }
            }
        }
        return category;
    }

    private void loadCategoriesOld(File file) throws Exception {
        CompoundTag nbttagcompound1 = NbtIo.readCompressed(new FileInputStream(file));
        this.lastUsedCatID = nbttagcompound1.getInt("lastID");
        this.lastUsedQuestID = nbttagcompound1.getInt("lastQuestID");
        ListTag list = nbttagcompound1.getList("Data", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                QuestCategory category = new QuestCategory();
                category.readNBT(list.getCompound(i));
                this.categories.put(category.id, category);
                this.saveCategory(category);
                Iterator<Entry<Integer, Quest>> ita = category.quests.entrySet().iterator();
                while (ita.hasNext()) {
                    Entry<Integer, Quest> entry = (Entry<Integer, Quest>) ita.next();
                    Quest quest = (Quest) entry.getValue();
                    quest.id = (Integer) entry.getKey();
                    if (this.quests.containsKey(quest.id)) {
                        ita.remove();
                    } else {
                        this.saveQuest(category, quest);
                    }
                }
            }
        }
    }

    public void removeCategory(int category) {
        QuestCategory cat = (QuestCategory) this.categories.get(category);
        if (cat != null) {
            File dir = new File(this.getDir(), cat.title);
            if (dir.delete()) {
                for (int dia : cat.quests.keySet()) {
                    this.quests.remove(dia);
                }
                this.categories.remove(category);
                Packets.sendAll(new PacketSyncRemove(category, 3));
            }
        }
    }

    public void saveCategory(QuestCategory category) {
        category.title = NoppesStringUtils.cleanFileName(category.title);
        if (this.categories.containsKey(category.id)) {
            QuestCategory currentCategory = (QuestCategory) this.categories.get(category.id);
            if (!currentCategory.title.equals(category.title)) {
                while (this.containsCategoryName(category)) {
                    category.title = category.title + "_";
                }
                File newdir = new File(this.getDir(), category.title);
                File olddir = new File(this.getDir(), currentCategory.title);
                if (newdir.exists()) {
                    return;
                }
                if (!olddir.renameTo(newdir)) {
                    return;
                }
            }
            category.quests = currentCategory.quests;
        } else {
            if (category.id < 0) {
                this.lastUsedCatID++;
                category.id = this.lastUsedCatID;
            }
            while (this.containsCategoryName(category)) {
                category.title = category.title + "_";
            }
            File dir = new File(this.getDir(), category.title);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        this.categories.put(category.id, category);
        Packets.sendAll(new PacketSyncUpdate(category.id, 3, category.writeNBT(new CompoundTag())));
    }

    public boolean containsCategoryName(QuestCategory category) {
        for (QuestCategory cat : this.categories.values()) {
            if (cat.id != category.id && cat.title.equalsIgnoreCase(category.title)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsQuestName(QuestCategory category, Quest quest) {
        for (Quest q : category.quests.values()) {
            if (q.id != quest.id && q.title.equalsIgnoreCase(quest.title)) {
                return true;
            }
        }
        return false;
    }

    public void saveQuest(QuestCategory category, Quest quest) {
        if (category != null) {
            while (this.containsQuestName(quest.category, quest)) {
                quest.title = quest.title + "_";
            }
            if (quest.id < 0) {
                this.lastUsedQuestID++;
                quest.id = this.lastUsedQuestID;
            }
            this.quests.put(quest.id, quest);
            category.quests.put(quest.id, quest);
            File dir = new File(this.getDir(), category.title);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, quest.id + ".json_new");
            File file2 = new File(dir, quest.id + ".json");
            try {
                NBTJsonUtil.SaveFile(file, quest.writeToNBTPartial(new CompoundTag()));
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
                Packets.sendAll(new PacketSyncUpdate(category.id, 2, quest.save(new CompoundTag())));
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }
    }

    public void removeQuest(Quest quest) {
        File file = new File(new File(this.getDir(), quest.category.title), quest.id + ".json");
        if (file.delete()) {
            this.quests.remove(quest.id);
            quest.category.quests.remove(quest.id);
            Packets.sendAll(new PacketSyncRemove(quest.id, 2));
        }
    }

    private File getDir() {
        return new File(CustomNpcs.getLevelSaveDirectory(), "quests");
    }

    @Override
    public List<IQuestCategory> categories() {
        return new ArrayList(this.categories.values());
    }

    public Quest get(int id) {
        return (Quest) this.quests.get(id);
    }
}