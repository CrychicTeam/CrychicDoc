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
import noppes.npcs.api.handler.IDialogHandler;
import noppes.npcs.api.handler.data.IDialogCategory;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketSyncRemove;
import noppes.npcs.packets.client.PacketSyncUpdate;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.shared.common.util.LogWriter;
import noppes.npcs.util.NBTJsonUtil;

public class DialogController implements IDialogHandler {

    public HashMap<Integer, DialogCategory> categoriesSync = new HashMap();

    public HashMap<Integer, DialogCategory> categories = new HashMap();

    public HashMap<Integer, Dialog> dialogs = new HashMap();

    public static DialogController instance = new DialogController();

    private int lastUsedDialogID = 0;

    private int lastUsedCatID = 0;

    public DialogController() {
        instance = this;
    }

    public void load() {
        LogWriter.info("Loading Dialogs");
        this.loadCategories();
        LogWriter.info("Done loading Dialogs");
    }

    private void loadCategories() {
        this.categories.clear();
        this.dialogs.clear();
        this.lastUsedCatID = 0;
        this.lastUsedDialogID = 0;
        try {
            File file = new File(CustomNpcs.getLevelSaveDirectory(), "dialog.dat");
            if (file.exists()) {
                this.loadCategoriesOld(file);
                file.delete();
                file = new File(CustomNpcs.getLevelSaveDirectory(), "dialog.dat_old");
                if (file.exists()) {
                    file.delete();
                }
                return;
            }
        } catch (Exception var11) {
            LogWriter.except(var11);
        }
        File dir = this.getDir();
        if (!dir.exists()) {
            dir.mkdir();
            this.loadDefaultDialogs();
        } else {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    DialogCategory category = this.loadCategoryDir(file);
                    Iterator<Entry<Integer, Dialog>> ite = category.dialogs.entrySet().iterator();
                    while (ite.hasNext()) {
                        Entry<Integer, Dialog> entry = (Entry<Integer, Dialog>) ite.next();
                        int id = (Integer) entry.getKey();
                        if (id > this.lastUsedDialogID) {
                            this.lastUsedDialogID = id;
                        }
                        Dialog dialog = (Dialog) entry.getValue();
                        if (this.dialogs.containsKey(id)) {
                            LogWriter.error("Duplicate id " + dialog.id + " from category " + category.title);
                            ite.remove();
                        } else {
                            this.dialogs.put(id, dialog);
                        }
                    }
                    this.lastUsedCatID++;
                    category.id = this.lastUsedCatID;
                    this.categories.put(category.id, category);
                }
            }
        }
    }

    private DialogCategory loadCategoryDir(File dir) {
        DialogCategory category = new DialogCategory();
        category.title = dir.getName();
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try {
                    Dialog dialog = new Dialog(category);
                    dialog.id = Integer.parseInt(file.getName().substring(0, file.getName().length() - 5));
                    dialog.readNBTPartial(NBTJsonUtil.LoadFile(file));
                    category.dialogs.put(dialog.id, dialog);
                } catch (Exception var8) {
                    LogWriter.error("Error loading: " + file.getAbsolutePath(), var8);
                }
            }
        }
        return category;
    }

    private void loadCategoriesOld(File file) throws Exception {
        CompoundTag nbttagcompound1 = NbtIo.readCompressed(new FileInputStream(file));
        ListTag list = nbttagcompound1.getList("Data", 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                DialogCategory category = new DialogCategory();
                category.readNBT(list.getCompound(i));
                this.saveCategory(category);
                Iterator<Entry<Integer, Dialog>> ita = category.dialogs.entrySet().iterator();
                while (ita.hasNext()) {
                    Entry<Integer, Dialog> entry = (Entry<Integer, Dialog>) ita.next();
                    Dialog dialog = (Dialog) entry.getValue();
                    dialog.id = (Integer) entry.getKey();
                    if (this.dialogs.containsKey(dialog.id)) {
                        ita.remove();
                    } else {
                        this.saveDialog(category, dialog);
                    }
                }
            }
        }
    }

    private void loadDefaultDialogs() {
        DialogCategory cat = new DialogCategory();
        cat.id = this.lastUsedCatID++;
        cat.title = "Villager";
        Dialog dia1 = new Dialog(cat);
        dia1.id = 1;
        dia1.title = "Start";
        dia1.text = "Hello {player}, \n\nWelcome to our village. I hope you enjoy your stay";
        Dialog dia2 = new Dialog(cat);
        dia2.id = 2;
        dia2.title = "Ask about village";
        dia2.text = "This village has been around for ages. Enjoy your stay here.";
        Dialog dia3 = new Dialog(cat);
        dia3.id = 3;
        dia3.title = "Who are you";
        dia3.text = "I'm a villager here. I have lived in this village my whole life.";
        cat.dialogs.put(dia1.id, dia1);
        cat.dialogs.put(dia2.id, dia2);
        cat.dialogs.put(dia3.id, dia3);
        DialogOption option = new DialogOption();
        option.title = "Tell me something about this village";
        option.dialogId = 2;
        option.optionType = 1;
        DialogOption option2 = new DialogOption();
        option2.title = "Who are you?";
        option2.dialogId = 3;
        option2.optionType = 1;
        DialogOption option3 = new DialogOption();
        option3.title = "Goodbye";
        option3.optionType = 0;
        dia1.options.put(0, option2);
        dia1.options.put(1, option);
        dia1.options.put(2, option3);
        DialogOption option4 = new DialogOption();
        option4.title = "Back";
        option4.dialogId = 1;
        dia2.options.put(1, option4);
        dia3.options.put(1, option4);
        this.lastUsedDialogID = 3;
        this.lastUsedCatID = 1;
        this.saveCategory(cat);
        this.saveDialog(cat, dia1);
        this.saveDialog(cat, dia2);
        this.saveDialog(cat, dia3);
    }

    public void saveCategory(DialogCategory category) {
        category.title = NoppesStringUtils.cleanFileName(category.title);
        if (this.categories.containsKey(category.id)) {
            DialogCategory currentCategory = (DialogCategory) this.categories.get(category.id);
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
            category.dialogs = currentCategory.dialogs;
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
        Packets.sendAll(new PacketSyncUpdate(category.id, 5, category.writeNBT(new CompoundTag())));
    }

    public void removeCategory(int category) {
        DialogCategory cat = (DialogCategory) this.categories.get(category);
        if (cat != null) {
            File dir = new File(this.getDir(), cat.title);
            if (dir.delete()) {
                for (int dia : cat.dialogs.keySet()) {
                    this.dialogs.remove(dia);
                }
                this.categories.remove(category);
                Packets.sendAll(new PacketSyncRemove(category, 5));
            }
        }
    }

    public boolean containsCategoryName(DialogCategory category) {
        for (DialogCategory cat : this.categories.values()) {
            if (category.id != cat.id && cat.title.equalsIgnoreCase(category.title)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsDialogName(DialogCategory category, Dialog dialog) {
        for (Dialog dia : category.dialogs.values()) {
            if (dia.id != dialog.id && dia.title.equalsIgnoreCase(dialog.title)) {
                return true;
            }
        }
        return false;
    }

    public Dialog saveDialog(DialogCategory category, Dialog dialog) {
        if (category == null) {
            return dialog;
        } else {
            while (this.containsDialogName(dialog.category, dialog)) {
                dialog.title = dialog.title + "_";
            }
            if (dialog.id < 0) {
                this.lastUsedDialogID++;
                dialog.id = this.lastUsedDialogID;
            }
            this.dialogs.put(dialog.id, dialog);
            category.dialogs.put(dialog.id, dialog);
            File dir = new File(this.getDir(), category.title);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, dialog.id + ".json_new");
            File file2 = new File(dir, dialog.id + ".json");
            try {
                CompoundTag compound = dialog.save(new CompoundTag());
                NBTJsonUtil.SaveFile(file, compound);
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
                Packets.sendAll(new PacketSyncUpdate(category.id, 4, compound));
            } catch (Exception var7) {
                LogWriter.except(var7);
            }
            return dialog;
        }
    }

    public void removeDialog(Dialog dialog) {
        DialogCategory category = dialog.category;
        File file = new File(new File(this.getDir(), category.title), dialog.id + ".json");
        if (file.delete()) {
            category.dialogs.remove(dialog.id);
            this.dialogs.remove(dialog.id);
            Packets.sendAll(new PacketSyncRemove(dialog.id, 4));
        }
    }

    private File getDir() {
        return new File(CustomNpcs.getLevelSaveDirectory(), "dialogs");
    }

    public boolean hasDialog(int dialogId) {
        return this.dialogs.containsKey(dialogId);
    }

    @Override
    public List<IDialogCategory> categories() {
        return new ArrayList(this.categories.values());
    }

    public Dialog get(int id) {
        return (Dialog) this.dialogs.get(id);
    }
}