package noppes.npcs.api.gui.subgui;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.gui.IButton;
import noppes.npcs.api.gui.IButtonList;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.constants.EnumAvailabilityScoreboard;
import noppes.npcs.constants.EnumDayTime;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Quest;

public class AvailabilityGui {

    public static CustomGuiWrapper open(Availability availability, IPlayer player) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(280, 214);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        IButton b = gui.addTexturedButton(666, "X", 266, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
        b.getTextureRect().setRepeatingTexture(64, 22, 3);
        b.setTextureHoverOffset(22).setHoverText("gui.close");
        b.setOnPress((guii, bb) -> guii.close());
        gui.addLabel(0, "availability.available", 0, 4, 280, 8).setCentered(true);
        gui.addButton(1, "availability.selectdialog", 20, 12, 240, 20).setOnPress((gui2, bb) -> gui2.openSubGui(openDialog(availability, player)));
        gui.addButton(2, "availability.selectquest", 20, 35, 240, 20).setOnPress((gui2, bb) -> gui2.openSubGui(openQuest(availability, player)));
        gui.addButton(3, "availability.selectscoreboard", 20, 58, 240, 20).setOnPress((gui2, bb) -> gui2.openSubGui(openScoreboard(availability, player)));
        gui.addButtonList(4, 4, 90, 60, 20).setValues("availability.always", "availability.is", "availability.isnot").setSelected(availability.factionAvailable.ordinal()).setOnPress((gui2, bb) -> {
            availability.setFactionAvailability(((IButtonList) bb).getSelected());
            if (availability.factionAvailable == EnumAvailabilityFactionType.Always) {
                availability.factionId = -1;
            }
            enableFactionButtons(gui, availability.factionAvailable, availability.factionId, 5, 6);
        });
        gui.addButtonList(5, 66, 90, 66, 20).setValues("faction.friendly", "faction.neutral", "faction.unfriendly").setSelected(availability.factionStance.ordinal()).setOnPress((gui2, bb) -> availability.setFactionAvailabilityStance(((IButtonList) bb).getSelected()));
        gui.addButton(6, "availability.selectfaction", 134, 90, 120, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openFaction(availability.factionId, player, id -> {
            availability.factionId = id;
            enableFactionButtons(gui, availability.factionAvailable, availability.factionId, 5, 6);
        })));
        gui.addButton(7, "X", 256, 90, 20, 20).setOnPress((gui2, bb) -> {
            availability.factionId = -1;
            enableFactionButtons(gui, availability.factionAvailable, availability.factionId, 5, 6);
        });
        enableFactionButtons(gui, availability.factionAvailable, availability.factionId, 5, 6);
        gui.addButtonList(8, 4, 112, 60, 20).setValues("availability.always", "availability.is", "availability.isnot").setSelected(availability.faction2Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.setFaction2Availability(((IButtonList) bb).getSelected());
            if (availability.faction2Available == EnumAvailabilityFactionType.Always) {
                availability.faction2Id = -1;
            }
            enableFactionButtons(gui, availability.faction2Available, availability.faction2Id, 5, 6);
        });
        gui.addButtonList(9, 66, 112, 66, 20).setValues("faction.friendly", "faction.neutral", "faction.unfriendly").setSelected(availability.faction2Stance.ordinal()).setOnPress((gui2, bb) -> availability.setFaction2AvailabilityStance(((IButtonList) bb).getSelected()));
        gui.addButton(10, "availability.selectfaction", 134, 112, 120, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openFaction(availability.faction2Id, player, id -> {
            availability.faction2Id = id;
            enableFactionButtons(gui, availability.faction2Available, availability.faction2Id, 9, 10);
        })));
        gui.addButton(11, "X", 256, 112, 20, 20).setOnPress((gui2, bb) -> {
            availability.faction2Id = -1;
            enableFactionButtons(gui, availability.faction2Available, availability.faction2Id, 5, 6);
        });
        enableFactionButtons(gui, availability.faction2Available, availability.faction2Id, 9, 10);
        gui.addLabel(12, "availability.daytime", 4, 142, 90, 8);
        gui.addButtonList(13, 66, 137, 70, 20).setValues("availability.wholeday", "availability.night", "availability.day").setSelected(availability.daytime.ordinal()).setOnPress((gui2, bb) -> availability.daytime = EnumDayTime.values()[((IButtonList) bb).getSelected()]);
        gui.addLabel(14, "availability.minlevel", 4, 164, 90, 8);
        gui.addTextField(15, 100, 159, 60, 20).setCharacterType(1).setMinMax(0, Integer.MAX_VALUE).setInteger(availability.minPlayerLevel).setOnFocusLost((gui2, textfield) -> availability.minPlayerLevel = textfield.getInteger());
        gui.addButton(16, "gui.done", 90, 190, 100, 20).setOnPress((gui2, bb) -> gui2.close());
        return gui;
    }

    private static void enableFactionButtons(CustomGuiWrapper gui, EnumAvailabilityFactionType type, int factionId, int typeId, int selectId) {
        String s = "availability.selectfaction";
        Faction f = FactionController.instance.getFaction(factionId);
        if (f != null) {
            s = f.getName();
        }
        ((IButton) gui.getComponent(typeId)).setEnabled(type != EnumAvailabilityFactionType.Always);
        ((IButton) gui.getComponent(selectId)).setLabel(s).setEnabled(type != EnumAvailabilityFactionType.Always);
        gui.update();
    }

    public static CustomGuiWrapper openDialog(Availability availability, IPlayer player) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(320, 134);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        IButton b = gui.addTexturedButton(666, "X", 308, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
        b.getTextureRect().setRepeatingTexture(64, 22, 3);
        b.setTextureHoverOffset(22).setHoverText("gui.close");
        b.setOnPress((guii, bb) -> guii.close());
        gui.addButtonList(1, 4, 14, 60, 20).setValues("availability.always", "availability.after", "availability.before").setSelected(availability.dialogAvailable.ordinal()).setOnPress((gui2, bb) -> {
            availability.dialogAvailable = EnumAvailabilityDialog.values()[((IButtonList) bb).getSelected()];
            if (availability.dialogAvailable == EnumAvailabilityDialog.Always) {
                availability.dialogId = -1;
            }
            enableDialogButtons(gui, availability.dialogAvailable, availability.dialogId, 2);
        });
        gui.addButton(2, "availability.selectdialog", 66, 14, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openDialog(availability.dialogId, player, id -> {
            availability.dialogId = id;
            enableDialogButtons(gui, availability.dialogAvailable, availability.dialogId, 2);
        })));
        gui.addButton(3, "X", 296, 14, 20, 20).setOnPress((gui2, bb) -> enableDialogButtons(gui, availability.dialogAvailable, availability.dialogId = -1, 2));
        enableDialogButtons(gui, availability.dialogAvailable, availability.dialogId, 2);
        gui.addButtonList(11, 4, 37, 60, 20).setValues("availability.always", "availability.after", "availability.before").setSelected(availability.dialog2Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.dialog2Available = EnumAvailabilityDialog.values()[((IButtonList) bb).getSelected()];
            if (availability.dialog2Available == EnumAvailabilityDialog.Always) {
                availability.dialog2Id = -1;
            }
            enableDialogButtons(gui, availability.dialog2Available, availability.dialog2Id, 12);
        });
        gui.addButton(12, "availability.selectdialog", 66, 37, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openDialog(availability.dialog2Id, player, id -> {
            availability.dialog2Id = id;
            enableDialogButtons(gui, availability.dialog2Available, availability.dialog2Id, 12);
        })));
        gui.addButton(13, "X", 296, 37, 20, 20).setOnPress((gui2, bb) -> enableDialogButtons(gui, availability.dialog2Available, availability.dialog2Id = -1, 12));
        enableDialogButtons(gui, availability.dialog2Available, availability.dialog2Id, 12);
        gui.addButtonList(21, 4, 60, 60, 20).setValues("availability.always", "availability.after", "availability.before").setSelected(availability.dialog3Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.dialog3Available = EnumAvailabilityDialog.values()[((IButtonList) bb).getSelected()];
            if (availability.dialog3Available == EnumAvailabilityDialog.Always) {
                availability.dialog3Id = -1;
            }
            enableDialogButtons(gui, availability.dialog3Available, availability.dialog3Id, 22);
        });
        gui.addButton(22, "availability.selectdialog", 66, 60, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openDialog(availability.dialog3Id, player, id -> {
            availability.dialog3Id = id;
            enableDialogButtons(gui, availability.dialog3Available, availability.dialog3Id, 22);
        })));
        gui.addButton(23, "X", 296, 60, 20, 20).setOnPress((gui2, bb) -> enableDialogButtons(gui, availability.dialog3Available, availability.dialog3Id = -1, 22));
        enableDialogButtons(gui, availability.dialog3Available, availability.dialog3Id, 22);
        gui.addButtonList(31, 4, 83, 60, 20).setValues("availability.always", "availability.after", "availability.before").setSelected(availability.dialog4Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.dialog4Available = EnumAvailabilityDialog.values()[((IButtonList) bb).getSelected()];
            if (availability.dialog4Available == EnumAvailabilityDialog.Always) {
                availability.dialog4Id = -1;
            }
            enableDialogButtons(gui, availability.dialog4Available, availability.dialog4Id, 32);
        });
        gui.addButton(32, "availability.selectdialog", 66, 83, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openDialog(availability.dialog4Id, player, id -> {
            availability.dialog4Id = id;
            enableDialogButtons(gui, availability.dialog4Available, availability.dialog4Id, 32);
        })));
        gui.addButton(33, "X", 296, 83, 20, 20).setOnPress((gui2, bb) -> enableDialogButtons(gui, availability.dialog4Available, availability.dialog4Id = -1, 32));
        enableDialogButtons(gui, availability.dialog4Available, availability.dialog4Id, 32);
        gui.addButton(16, "gui.done", 110, 110, 100, 20).setOnPress((gui2, bb) -> gui2.close());
        return gui;
    }

    private static void enableDialogButtons(CustomGuiWrapper gui, EnumAvailabilityDialog type, int dialogId, int selectId) {
        String s = "availability.selectdialog";
        Dialog dialog = (Dialog) DialogController.instance.dialogs.get(dialogId);
        if (dialog != null) {
            s = dialog.getName();
        }
        ((IButton) gui.getComponent(selectId)).setLabel(s).setEnabled(type != EnumAvailabilityDialog.Always);
        gui.update();
    }

    public static CustomGuiWrapper openQuest(Availability availability, IPlayer player) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(320, 134);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        IButton b = gui.addTexturedButton(666, "X", 308, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
        b.getTextureRect().setRepeatingTexture(64, 22, 3);
        b.setTextureHoverOffset(22).setHoverText("gui.close");
        b.setOnPress((guii, bb) -> guii.close());
        gui.addButtonList(1, 4, 14, 60, 20).setValues("availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart").setSelected(availability.questAvailable.ordinal()).setOnPress((gui2, bb) -> {
            availability.questAvailable = EnumAvailabilityQuest.values()[((IButtonList) bb).getSelected()];
            if (availability.questAvailable == EnumAvailabilityQuest.Always) {
                availability.questId = -1;
            }
            enableQuestButtons(gui, availability.questAvailable, availability.questId, 2);
        });
        gui.addButton(2, "availability.selectquest", 66, 14, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openQuest(availability.questId, player, id -> {
            availability.questId = id;
            enableQuestButtons(gui, availability.questAvailable, availability.questId, 2);
        })));
        gui.addButton(3, "X", 296, 14, 20, 20).setOnPress((gui2, bb) -> enableQuestButtons(gui, availability.questAvailable, availability.questId = -1, 2));
        enableQuestButtons(gui, availability.questAvailable, availability.questId, 2);
        gui.addButtonList(11, 4, 37, 60, 20).setValues("availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart").setSelected(availability.quest2Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.quest2Available = EnumAvailabilityQuest.values()[((IButtonList) bb).getSelected()];
            if (availability.quest2Available == EnumAvailabilityQuest.Always) {
                availability.quest2Id = -1;
            }
            enableQuestButtons(gui, availability.quest2Available, availability.quest2Id, 12);
        });
        gui.addButton(12, "availability.selectquest", 66, 37, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openQuest(availability.quest2Id, player, id -> {
            availability.quest2Id = id;
            enableQuestButtons(gui, availability.quest2Available, availability.quest2Id, 12);
        })));
        gui.addButton(13, "X", 296, 37, 20, 20).setOnPress((gui2, bb) -> enableQuestButtons(gui, availability.quest2Available, availability.quest2Id = -1, 12));
        enableQuestButtons(gui, availability.quest2Available, availability.quest2Id, 12);
        gui.addButtonList(21, 4, 60, 60, 20).setValues("availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart").setSelected(availability.quest3Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.quest3Available = EnumAvailabilityQuest.values()[((IButtonList) bb).getSelected()];
            if (availability.quest3Available == EnumAvailabilityQuest.Always) {
                availability.quest3Id = -1;
            }
            enableQuestButtons(gui, availability.quest3Available, availability.quest3Id, 22);
        });
        gui.addButton(22, "availability.selectquest", 66, 60, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openQuest(availability.quest3Id, player, id -> {
            availability.quest3Id = id;
            enableQuestButtons(gui, availability.quest3Available, availability.quest3Id, 22);
        })));
        gui.addButton(23, "X", 296, 60, 20, 20).setOnPress((gui2, bb) -> enableQuestButtons(gui, availability.quest3Available, availability.quest3Id = -1, 22));
        enableQuestButtons(gui, availability.quest3Available, availability.quest3Id, 22);
        gui.addButtonList(31, 4, 83, 60, 20).setValues("availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart").setSelected(availability.quest4Available.ordinal()).setOnPress((gui2, bb) -> {
            availability.quest4Available = EnumAvailabilityQuest.values()[((IButtonList) bb).getSelected()];
            if (availability.quest4Available == EnumAvailabilityQuest.Always) {
                availability.quest4Id = -1;
            }
            enableQuestButtons(gui, availability.quest4Available, availability.quest4Id, 32);
        });
        gui.addButton(32, "availability.selectquest", 66, 83, 228, 20).setOnPress((gui2, bb) -> gui2.openSubGui(SelectorGui.openQuest(availability.quest4Id, player, id -> {
            availability.quest4Id = id;
            enableQuestButtons(gui, availability.quest4Available, availability.quest4Id, 32);
        })));
        gui.addButton(33, "X", 296, 83, 20, 20).setOnPress((gui2, bb) -> enableQuestButtons(gui, availability.quest4Available, availability.quest4Id = -1, 32));
        enableQuestButtons(gui, availability.quest4Available, availability.quest4Id, 32);
        gui.addButton(16, "gui.done", 110, 110, 100, 20).setOnPress((gui2, bb) -> gui2.close());
        return gui;
    }

    private static void enableQuestButtons(CustomGuiWrapper gui, EnumAvailabilityQuest type, int questId, int selectId) {
        String s = "availability.selectquest";
        Quest quest = (Quest) QuestController.instance.quests.get(questId);
        if (quest != null) {
            s = quest.getName();
        }
        ((IButton) gui.getComponent(selectId)).setLabel(s).setEnabled(type != EnumAvailabilityQuest.Always);
        gui.update();
    }

    public static CustomGuiWrapper openScoreboard(Availability availability, IPlayer player) {
        CustomGuiWrapper gui = new CustomGuiWrapper(player);
        gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        gui.setSize(316, 134);
        gui.getBackgroundRect().setTextureOffset(0, 0);
        gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        IButton b = gui.addTexturedButton(666, "X", 308, -4, 14, 14, "customnpcs:textures/gui/components.png", 0, 64);
        b.getTextureRect().setRepeatingTexture(64, 22, 3);
        b.setTextureHoverOffset(22).setHoverText("gui.close");
        b.setOnPress((guii, bb) -> guii.close());
        gui.addTextField(1, 4, 14, 140, 20).setText(availability.scoreboardObjective).setOnFocusLost((gui2, textfield) -> availability.scoreboardObjective = textfield.getText());
        gui.addButtonList(2, 148, 14, 90, 20).setValues("availability.smaller", "availability.equals", "availability.bigger").setSelected(availability.scoreboardType.ordinal()).setOnPress((gui2, bb) -> availability.scoreboardType = EnumAvailabilityScoreboard.values()[((IButtonList) bb).getSelected()]);
        gui.addTextField(3, 244, 14, 60, 20).setCharacterType(1).setInteger(availability.scoreboardValue).setOnFocusLost((gui2, textfield) -> availability.scoreboardValue = textfield.getInteger());
        gui.addTextField(11, 4, 37, 140, 20).setText(availability.scoreboard2Objective).setOnFocusLost((gui2, textfield) -> availability.scoreboard2Objective = textfield.getText());
        gui.addButtonList(12, 148, 37, 90, 20).setValues("availability.smaller", "availability.equals", "availability.bigger").setSelected(availability.scoreboard2Type.ordinal()).setOnPress((gui2, bb) -> availability.scoreboard2Type = EnumAvailabilityScoreboard.values()[((IButtonList) bb).getSelected()]);
        gui.addTextField(13, 244, 37, 60, 20).setCharacterType(1).setInteger(availability.scoreboard2Value).setOnFocusLost((gui2, textfield) -> availability.scoreboard2Value = textfield.getInteger());
        gui.addButton(16, "gui.done", 108, 110, 100, 20).setOnPress((gui2, bb) -> gui2.close());
        return gui;
    }
}