package noppes.npcs.client.gui;

import noppes.npcs.constants.EnumAvailabilityScoreboard;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class SubGuiNpcAvailabilityScoreboard extends GuiBasic implements ITextfieldListener {

    private Availability availabitily;

    private boolean selectFaction = false;

    private int slot = 0;

    public SubGuiNpcAvailabilityScoreboard(Availability availabitily) {
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
        this.addTextField(new GuiTextFieldNop(10, this, this.guiLeft + 4, y, 140, 20, this.availabitily.scoreboardObjective));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 148, y, 90, 20, new String[] { "availability.smaller", "availability.equals", "availability.bigger" }, this.availabitily.scoreboardType.ordinal()));
        this.addTextField(new GuiTextFieldNop(20, this, this.guiLeft + 244, y, 60, 20, this.availabitily.scoreboardValue + ""));
        this.getTextField(20).numbersOnly = true;
        y += 23;
        this.addTextField(new GuiTextFieldNop(11, this, this.guiLeft + 4, y, 140, 20, this.availabitily.scoreboard2Objective));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 148, y, 90, 20, new String[] { "availability.smaller", "availability.equals", "availability.bigger" }, this.availabitily.scoreboard2Type.ordinal()));
        this.addTextField(new GuiTextFieldNop(21, this, this.guiLeft + 244, y, 60, 20, this.availabitily.scoreboard2Value + ""));
        this.getTextField(21).numbersOnly = true;
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 82, this.guiTop + 192, 98, 20, "gui.done"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.availabitily.scoreboardType = EnumAvailabilityScoreboard.values()[guibutton.getValue()];
        }
        if (guibutton.id == 1) {
            this.availabitily.scoreboard2Type = EnumAvailabilityScoreboard.values()[guibutton.getValue()];
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 10) {
            this.availabitily.scoreboardObjective = textfield.m_94155_();
        }
        if (textfield.id == 11) {
            this.availabitily.scoreboard2Objective = textfield.m_94155_();
        }
        if (textfield.id == 20) {
            this.availabitily.scoreboardValue = NoppesStringUtils.parseInt(textfield.m_94155_(), 0);
        }
        if (textfield.id == 21) {
            this.availabitily.scoreboard2Value = NoppesStringUtils.parseInt(textfield.m_94155_(), 0);
        }
    }
}