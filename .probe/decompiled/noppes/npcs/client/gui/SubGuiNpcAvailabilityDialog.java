package noppes.npcs.client.gui;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.select.GuiDialogSelection;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;

public class SubGuiNpcAvailabilityDialog extends GuiBasic {

    private Availability availabitily;

    private int slot = 0;

    public SubGuiNpcAvailabilityDialog(Availability availabitily) {
        this.availabitily = availabitily;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "availability.available", this.guiLeft, this.guiTop + 4, this.imageWidth, 0));
        int y = this.guiTop + 12;
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, y, 50, 20, new String[] { "availability.always", "availability.after", "availability.before" }, this.availabitily.dialogAvailable.ordinal()));
        this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 56, y, 172, 20, "availability.selectdialog"));
        this.getButton(10).setEnabled(this.availabitily.dialogAvailable != EnumAvailabilityDialog.Always);
        this.addButton(new GuiButtonNop(this, 20, this.guiLeft + 230, y, 20, 20, "X"));
        y += 23;
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 4, y, 50, 20, new String[] { "availability.always", "availability.after", "availability.before" }, this.availabitily.dialog2Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 56, y, 172, 20, "availability.selectdialog"));
        this.getButton(11).setEnabled(this.availabitily.dialog2Available != EnumAvailabilityDialog.Always);
        this.addButton(new GuiButtonNop(this, 21, this.guiLeft + 230, y, 20, 20, "X"));
        y += 23;
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 4, y, 50, 20, new String[] { "availability.always", "availability.after", "availability.before" }, this.availabitily.dialog3Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 56, y, 172, 20, "availability.selectdialog"));
        this.getButton(12).setEnabled(this.availabitily.dialog3Available != EnumAvailabilityDialog.Always);
        this.addButton(new GuiButtonNop(this, 22, this.guiLeft + 230, y, 20, 20, "X"));
        y += 23;
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 4, y, 50, 20, new String[] { "availability.always", "availability.after", "availability.before" }, this.availabitily.dialog4Available.ordinal()));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 56, y, 172, 20, "availability.selectdialog"));
        this.getButton(13).setEnabled(this.availabitily.dialog4Available != EnumAvailabilityDialog.Always);
        this.addButton(new GuiButtonNop(this, 23, this.guiLeft + 230, y, 20, 20, "X"));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 82, this.guiTop + 192, 98, 20, "gui.done"));
        this.updateGuiButtons();
    }

    private void updateGuiButtons() {
        this.getButton(10).setDisplayText("availability.selectdialog");
        this.getButton(11).setDisplayText("availability.selectdialog");
        this.getButton(12).setDisplayText("availability.selectdialog");
        this.getButton(13).setDisplayText("availability.selectdialog");
        if (this.availabitily.dialogId >= 0) {
            Dialog dialog = (Dialog) DialogController.instance.dialogs.get(this.availabitily.dialogId);
            if (dialog != null) {
                this.getButton(10).setDisplayText(dialog.title);
            }
        }
        if (this.availabitily.dialog2Id >= 0) {
            Dialog dialog = (Dialog) DialogController.instance.dialogs.get(this.availabitily.dialog2Id);
            if (dialog != null) {
                this.getButton(11).setDisplayText(dialog.title);
            }
        }
        if (this.availabitily.dialog3Id >= 0) {
            Dialog dialog = (Dialog) DialogController.instance.dialogs.get(this.availabitily.dialog3Id);
            if (dialog != null) {
                this.getButton(12).setDisplayText(dialog.title);
            }
        }
        if (this.availabitily.dialog4Id >= 0) {
            Dialog dialog = (Dialog) DialogController.instance.dialogs.get(this.availabitily.dialog4Id);
            if (dialog != null) {
                this.getButton(13).setDisplayText(dialog.title);
            }
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.availabitily.dialogAvailable = EnumAvailabilityDialog.values()[guibutton.getValue()];
            if (this.availabitily.dialogAvailable == EnumAvailabilityDialog.Always) {
                this.availabitily.dialogId = -1;
            }
            this.init();
        }
        if (guibutton.id == 1) {
            this.availabitily.dialog2Available = EnumAvailabilityDialog.values()[guibutton.getValue()];
            if (this.availabitily.dialog2Available == EnumAvailabilityDialog.Always) {
                this.availabitily.dialog2Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 2) {
            this.availabitily.dialog3Available = EnumAvailabilityDialog.values()[guibutton.getValue()];
            if (this.availabitily.dialog3Available == EnumAvailabilityDialog.Always) {
                this.availabitily.dialog3Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 3) {
            this.availabitily.dialog4Available = EnumAvailabilityDialog.values()[guibutton.getValue()];
            if (this.availabitily.dialog4Available == EnumAvailabilityDialog.Always) {
                this.availabitily.dialog4Id = -1;
            }
            this.init();
        }
        if (guibutton.id == 10) {
            this.slot = 1;
            this.setSubGui(new GuiDialogSelection(this.availabitily.dialogId));
        }
        if (guibutton.id == 11) {
            this.slot = 2;
            this.setSubGui(new GuiDialogSelection(this.availabitily.dialog2Id));
        }
        if (guibutton.id == 12) {
            this.slot = 3;
            this.setSubGui(new GuiDialogSelection(this.availabitily.dialog3Id));
        }
        if (guibutton.id == 13) {
            this.slot = 4;
            this.setSubGui(new GuiDialogSelection(this.availabitily.dialog4Id));
        }
        if (guibutton.id == 20) {
            this.availabitily.dialogId = -1;
            this.getButton(10).setDisplayText("availability.selectdialog");
        }
        if (guibutton.id == 21) {
            this.availabitily.dialog2Id = -1;
            this.getButton(11).setDisplayText("availability.selectdialog");
        }
        if (guibutton.id == 22) {
            this.availabitily.dialog3Id = -1;
            this.getButton(12).setDisplayText("availability.selectdialog");
        }
        if (guibutton.id == 23) {
            this.availabitily.dialog4Id = -1;
            this.getButton(13).setDisplayText("availability.selectdialog");
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        GuiDialogSelection selector = (GuiDialogSelection) subgui;
        if (selector.selectedDialog != null) {
            if (this.slot == 1) {
                this.availabitily.dialogId = selector.selectedDialog.id;
            }
            if (this.slot == 2) {
                this.availabitily.dialog2Id = selector.selectedDialog.id;
            }
            if (this.slot == 3) {
                this.availabitily.dialog3Id = selector.selectedDialog.id;
            }
            if (this.slot == 4) {
                this.availabitily.dialog4Id = selector.selectedDialog.id;
            }
            this.init();
        }
    }
}