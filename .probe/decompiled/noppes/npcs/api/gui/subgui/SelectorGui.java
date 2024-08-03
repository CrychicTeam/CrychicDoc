package noppes.npcs.api.gui.subgui;

import java.util.HashMap;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.gui.IButton;
import noppes.npcs.api.gui.IScroll;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IDialogCategory;
import noppes.npcs.api.handler.data.IQuestCategory;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;

public class SelectorGui {

    public static CustomGuiWrapper openFaction(int id, IPlayer player, SelectorGui.SelectionCallback callback) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(280, 214);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        gui.addLabel(0, "menu.factions", 0, 0, 280, 8).setCentered(true);
        IButton b = gui.addTexturedButton(666, "X", 266, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
        b.getTextureRect().setRepeatingTexture(64, 22, 3);
        b.setTextureHoverOffset(22).setHoverText("gui.close");
        b.setOnPress((guii, bb) -> guii.close());
        Faction faction = FactionController.instance.getFaction(id);
        IScroll scroll = gui.addScroll(1, 4, 14, 272, 200, (String[]) FactionController.instance.list().stream().map(t -> t.getName()).toArray(String[]::new)).setOnClick((gui2, scroll2) -> callback.call(FactionController.instance.getFactionFromName(scroll2.getSelectionList()[0]).id)).setOnDoubleClick((gui2, scroll2) -> gui2.close());
        if (faction != null) {
            scroll.setSelectionList(faction.getName());
        }
        return gui;
    }

    public static CustomGuiWrapper openDialog(int id, IPlayer player, SelectorGui.SelectionCallback callback) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        new SelectorGui.DialogSelectorGui(gui, id, callback);
        return gui;
    }

    public static CustomGuiWrapper openQuest(int id, IPlayer player, SelectorGui.SelectionCallback callback) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        new SelectorGui.QuestSelectorGui(gui, id, callback);
        return gui;
    }

    static class DialogSelectorGui {

        private Dialog dialog;

        private DialogCategory category;

        private HashMap<String, Dialog> data = new HashMap();

        private IScroll dialogScroll;

        public DialogSelectorGui(CustomGuiWrapper gui, int id, SelectorGui.SelectionCallback callback) {
            this.dialog = DialogController.instance.get(id);
            if (this.dialog != null) {
                this.category = this.dialog.category;
            }
            HashMap<String, DialogCategory> categoryData = new HashMap();
            for (DialogCategory category : DialogController.instance.categories.values()) {
                categoryData.put(category.title, category);
            }
            gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
            gui.setSize(366, 226);
            gui.getBackgroundRect().setTextureOffset(0, 0);
            gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
            IButton b = gui.addTexturedButton(666, "X", 352, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
            b.getTextureRect().setRepeatingTexture(64, 22, 3);
            b.setTextureHoverOffset(22).setHoverText("gui.close");
            b.setOnPress((guii, bb) -> guii.close());
            gui.addLabel(0, "gui.categories", 8, 4, 10, 8);
            gui.addLabel(1, "dialog.dialogs", 175, 4, 10, 8);
            IScroll scrollCategories = gui.addScroll(2, 4, 14, 170, 208, (String[]) DialogController.instance.categories().stream().map(IDialogCategory::getName).toArray(String[]::new)).setOnClick((gui2, scroll2) -> {
                this.category = (DialogCategory) categoryData.get(scroll2.getSelectionList()[0]);
                this.fill();
                gui2.update();
            });
            if (this.dialog != null) {
                scrollCategories.setSelectionList(this.dialog.getCategory().getName());
            }
            this.dialogScroll = gui.addScroll(3, 175, 14, 170, 208, new String[0]).setOnClick((gui2, scroll2) -> {
                this.dialog = (Dialog) this.data.get(scroll2.getSelectionList()[0]);
                callback.call(this.dialog.id);
            }).setOnDoubleClick((gui2, scroll2) -> gui2.close());
            this.fill();
        }

        private void fill() {
            HashMap<String, Dialog> data = new HashMap();
            if (this.category != null) {
                for (Dialog dialog : this.category.dialogs.values()) {
                    data.put(dialog.title, dialog);
                }
            }
            this.data = data;
            this.dialogScroll.setList((String[]) data.values().stream().map(IDialog::getName).toArray(String[]::new));
            if (this.dialog != null && this.category != null && this.dialog.category.id == this.category.id) {
                this.dialogScroll.setSelectionList(this.dialog.getName());
            }
        }
    }

    static class QuestSelectorGui {

        private Quest quest;

        private QuestCategory category;

        private HashMap<String, Quest> data = new HashMap();

        private IScroll questScroll;

        public QuestSelectorGui(CustomGuiWrapper gui, int id, SelectorGui.SelectionCallback callback) {
            this.quest = QuestController.instance.get(id);
            if (this.quest != null) {
                this.category = this.quest.category;
            }
            HashMap<String, QuestCategory> categoryData = new HashMap();
            for (QuestCategory category : QuestController.instance.categories.values()) {
                categoryData.put(category.title, category);
            }
            gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
            gui.setSize(366, 226);
            gui.getBackgroundRect().setTextureOffset(0, 0);
            gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
            IButton b = gui.addTexturedButton(666, "X", 352, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
            b.getTextureRect().setRepeatingTexture(64, 22, 3);
            b.setTextureHoverOffset(22).setHoverText("gui.close");
            b.setOnPress((guii, bb) -> guii.close());
            gui.addLabel(0, "gui.categories", 8, 4, 10, 8);
            gui.addLabel(1, "quest.quests", 175, 4, 10, 8);
            IScroll scrollCategories = gui.addScroll(2, 4, 14, 170, 208, (String[]) QuestController.instance.categories().stream().map(IQuestCategory::getName).toArray(String[]::new)).setOnClick((gui2, scroll2) -> {
                this.category = (QuestCategory) categoryData.get(scroll2.getSelectionList()[0]);
                this.fill();
                gui2.update();
            });
            if (this.quest != null) {
                scrollCategories.setSelectionList(this.quest.getCategory().getName());
            }
            this.questScroll = gui.addScroll(3, 175, 14, 170, 208, new String[0]).setOnClick((gui2, scroll2) -> {
                this.quest = (Quest) this.data.get(scroll2.getSelectionList()[0]);
                callback.call(this.quest.id);
            }).setOnDoubleClick((gui2, scroll2) -> gui2.close());
            this.fill();
        }

        private void fill() {
            HashMap<String, Quest> data = new HashMap();
            if (this.category != null) {
                for (Quest dialog : this.category.quests.values()) {
                    data.put(dialog.title, dialog);
                }
            }
            this.data = data;
            this.questScroll.setList((String[]) data.values().stream().map(Quest::getName).toArray(String[]::new));
            if (this.quest != null && this.category != null && this.quest.category.id == this.category.id) {
                this.questScroll.setSelectionList(this.quest.getName());
            }
        }
    }

    @FunctionalInterface
    public interface SelectionCallback {

        void call(int var1);
    }
}