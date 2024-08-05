package noppes.npcs.client.gui;

import noppes.npcs.client.gui.select.GuiQuestSelection;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;

public class SubGuiNpcAvailabilityQuest extends GuiBasic implements GuiSelectionListener {

    private Availability availabitily;

    private boolean selectFaction = false;

    private int slot = 0;

    public SubGuiNpcAvailabilityQuest(Availability availabitily) {
        this.availabitily = availabitily;
        this.setBackground("menubg.png");
        this.imageWidth = 316;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "availability.available", this.guiLeft, this.guiTop + 4, this.imageWidth, 0));
        int y = this.guiTop + 12;
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, y, 90, 20, new String[] { "availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart" }, this.availabitily.questAvailable.ordinal()));
        this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 96, y, 192, 20, "availability.selectquest"));
        this.getButton(10).setEnabled(this.availabitily.questAvailable != EnumAvailabilityQuest.Always);
        this.addButton(new GuiButtonNop(this, 20, this.guiLeft + 290, y, 20, 20, "X"));
        y += 23;
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 4, y, 90, 20, new String[] { "availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart" }, this.availabitily.quest2Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 96, y, 192, 20, "availability.selectquest"));
        this.getButton(11).setEnabled(this.availabitily.quest2Available != EnumAvailabilityQuest.Always);
        this.addButton(new GuiButtonNop(this, 21, this.guiLeft + 290, y, 20, 20, "X"));
        y += 23;
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 4, y, 90, 20, new String[] { "availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart" }, this.availabitily.quest3Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 96, y, 192, 20, "availability.selectquest"));
        this.getButton(12).setEnabled(this.availabitily.quest3Available != EnumAvailabilityQuest.Always);
        this.addButton(new GuiButtonNop(this, 22, this.guiLeft + 290, y, 20, 20, "X"));
        y += 23;
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 4, y, 90, 20, new String[] { "availability.always", "availability.after", "availability.before", "availability.whenactive", "availability.whennotactive", "availability.completed", "availability.canStart" }, this.availabitily.quest4Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 96, y, 192, 20, "availability.selectquest"));
        this.getButton(13).setEnabled(this.availabitily.quest4Available != EnumAvailabilityQuest.Always);
        this.addButton(new GuiButtonNop(this, 23, this.guiLeft + 290, y, 20, 20, "X"));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 82, this.guiTop + 192, 98, 20, "gui.done"));
        this.updateGuiButtons();
    }

    private void updateGuiButtons() {
        this.getButton(10).setDisplayText("availability.selectquest");
        this.getButton(11).setDisplayText("availability.selectquest");
        this.getButton(12).setDisplayText("availability.selectquest");
        this.getButton(13).setDisplayText("availability.selectquest");
        if (this.availabitily.questId >= 0) {
            Quest quest = (Quest) QuestController.instance.quests.get(this.availabitily.questId);
            if (quest != null) {
                this.getButton(10).setDisplayText(quest.title);
            }
        }
        if (this.availabitily.quest2Id >= 0) {
            Quest quest = (Quest) QuestController.instance.quests.get(this.availabitily.quest2Id);
            if (quest != null) {
                this.getButton(11).setDisplayText(quest.title);
            }
        }
        if (this.availabitily.quest3Id >= 0) {
            Quest quest = (Quest) QuestController.instance.quests.get(this.availabitily.quest3Id);
            if (quest != null) {
                this.getButton(12).setDisplayText(quest.title);
            }
        }
        if (this.availabitily.quest4Id >= 0) {
            Quest quest = (Quest) QuestController.instance.quests.get(this.availabitily.quest4Id);
            if (quest != null) {
                this.getButton(13).setDisplayText(quest.title);
            }
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.availabitily.questAvailable = EnumAvailabilityQuest.values()[guibutton.getValue()];
            if (this.availabitily.questAvailable == EnumAvailabilityQuest.Always) {
                this.availabitily.questId = -1;
            }
            this.init();
        }
        if (guibutton.id == 1) {
            this.availabitily.quest2Available = EnumAvailabilityQuest.values()[guibutton.getValue()];
            if (this.availabitily.quest2Available == EnumAvailabilityQuest.Always) {
                this.availabitily.quest2Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 2) {
            this.availabitily.quest3Available = EnumAvailabilityQuest.values()[guibutton.getValue()];
            if (this.availabitily.quest3Available == EnumAvailabilityQuest.Always) {
                this.availabitily.quest3Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 3) {
            this.availabitily.quest4Available = EnumAvailabilityQuest.values()[guibutton.getValue()];
            if (this.availabitily.quest4Available == EnumAvailabilityQuest.Always) {
                this.availabitily.quest4Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 10) {
            this.slot = 1;
            this.setSubGui(new GuiQuestSelection(this.availabitily.questId));
        }
        if (guibutton.id == 11) {
            this.slot = 2;
            this.setSubGui(new GuiQuestSelection(this.availabitily.quest2Id));
        }
        if (guibutton.id == 12) {
            this.slot = 3;
            this.setSubGui(new GuiQuestSelection(this.availabitily.quest3Id));
        }
        if (guibutton.id == 13) {
            this.slot = 4;
            this.setSubGui(new GuiQuestSelection(this.availabitily.quest4Id));
        }
        if (guibutton.id == 20) {
            this.availabitily.questId = -1;
            this.getButton(10).setDisplayText("availability.selectquest");
        }
        if (guibutton.id == 21) {
            this.availabitily.quest2Id = -1;
            this.getButton(11).setDisplayText("availability.selectquest");
        }
        if (guibutton.id == 22) {
            this.availabitily.quest3Id = -1;
            this.getButton(12).setDisplayText("availability.selectquest");
        }
        if (guibutton.id == 23) {
            this.availabitily.quest4Id = -1;
            this.getButton(13).setDisplayText("availability.selectquest");
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void selected(int id, String name) {
        if (this.slot == 1) {
            this.availabitily.questId = id;
        }
        if (this.slot == 2) {
            this.availabitily.quest2Id = id;
        }
        if (this.slot == 3) {
            this.availabitily.quest3Id = id;
        }
        if (this.slot == 4) {
            this.availabitily.quest4Id = id;
        }
        this.updateGuiButtons();
    }
}