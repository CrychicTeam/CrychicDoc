package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.SubGuiMailmanSendSetup;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.SubGuiNpcCommand;
import noppes.npcs.client.gui.SubGuiNpcFactionOptions;
import noppes.npcs.client.gui.select.GuiQuestSelection;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketDialogSave;
import noppes.npcs.shared.client.gui.GuiTextAreaScreen;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiDialogEdit extends GuiBasic implements ITextfieldListener {

    private Dialog dialog;

    public GuiDialogEdit(Dialog dialog) {
        this.dialog = dialog;
        this.setBackground("menubg.png");
        this.imageWidth = 386;
        this.imageHeight = 226;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "gui.title", this.guiLeft + 4, this.guiTop + 8));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 46, this.guiTop + 3, 220, 20, this.dialog.title));
        this.addLabel(new GuiLabel(0, "ID", this.guiLeft + 268, this.guiTop + 4));
        this.addLabel(new GuiLabel(2, this.dialog.id + "", this.guiLeft + 268, this.guiTop + 14));
        this.addLabel(new GuiLabel(3, "dialog.dialogtext", this.guiLeft + 4, this.guiTop + 30));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 120, this.guiTop + 25, 50, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(4, "availability.options", this.guiLeft + 4, this.guiTop + 51));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 120, this.guiTop + 46, 50, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(5, "faction.options", this.guiLeft + 4, this.guiTop + 72));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 120, this.guiTop + 67, 50, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(6, "dialog.options", this.guiLeft + 4, this.guiTop + 93));
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 120, this.guiTop + 89, 50, 20, "selectServer.edit"));
        this.addButton(new GuiButtonNop(this, 7, this.guiLeft + 4, this.guiTop + 114, 144, 20, "availability.selectquest"));
        this.addButton(new GuiButtonNop(this, 8, this.guiLeft + 150, this.guiTop + 114, 20, 20, "X"));
        if (this.dialog.hasQuest()) {
            this.getButton(7).setDisplayText(this.dialog.getQuest().title);
        }
        this.addLabel(new GuiLabel(9, "gui.selectSound", this.guiLeft + 4, this.guiTop + 138));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 4, this.guiTop + 148, 264, 20, this.dialog.sound));
        this.addButton(new GuiButtonNop(this, 9, this.guiLeft + 270, this.guiTop + 148, 60, 20, "mco.template.button.select"));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 4, this.guiTop + 172, 164, 20, "mailbox.setup"));
        this.addButton(new GuiButtonNop(this, 14, this.guiLeft + 170, this.guiTop + 172, 20, 20, "X"));
        if (!this.dialog.mail.subject.isEmpty()) {
            this.getButton(13).setDisplayText(this.dialog.mail.subject);
        }
        int y = this.guiTop + 4;
        int var10005 = this.guiLeft + 330;
        y += 22;
        this.addButton(new GuiButtonNop(this, 10, var10005, y, 50, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(10, "advMode.command", this.guiLeft + 214, y + 5));
        var10005 = this.guiLeft + 330;
        y += 22;
        this.addButton(new GuiButtonYesNo(this, 11, var10005, y, this.dialog.hideNPC));
        this.addLabel(new GuiLabel(11, "dialog.hideNPC", this.guiLeft + 214, y + 5));
        var10005 = this.guiLeft + 330;
        y += 22;
        this.addButton(new GuiButtonYesNo(this, 12, var10005, y, this.dialog.showWheel));
        this.addLabel(new GuiLabel(12, "dialog.showWheel", this.guiLeft + 214, y + 5));
        var10005 = this.guiLeft + 330;
        y += 22;
        this.addButton(new GuiButtonYesNo(this, 15, var10005, y, this.dialog.disableEsc));
        this.addLabel(new GuiLabel(15, "dialog.disableEsc", this.guiLeft + 214, y + 5));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 362, this.guiTop + 4, 20, 20, "X"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 3) {
            this.setSubGui(new GuiTextAreaScreen(this.dialog.text));
        }
        if (id == 4) {
            this.setSubGui(new SubGuiNpcAvailability(this.dialog.availability));
        }
        if (id == 5) {
            this.setSubGui(new SubGuiNpcFactionOptions(this.dialog.factionOptions));
        }
        if (id == 6) {
            this.setSubGui(new SubGuiNpcDialogOptions(this.dialog));
        }
        if (id == 7) {
            this.setSubGui(new GuiQuestSelection(this.dialog.quest));
        }
        if (id == 8) {
            this.dialog.quest = -1;
            this.init();
        }
        if (id == 9) {
            this.setSubGui(new GuiSoundSelection(this.getTextField(2).m_94155_()));
        }
        if (id == 10) {
            this.setSubGui(new SubGuiNpcCommand(this.dialog.command));
        }
        if (id == 11) {
            this.dialog.hideNPC = guibutton.getValue() == 1;
        }
        if (id == 12) {
            this.dialog.showWheel = guibutton.getValue() == 1;
        }
        if (id == 15) {
            this.dialog.disableEsc = guibutton.getValue() == 1;
        }
        if (id == 13) {
            this.setSubGui(new SubGuiMailmanSendSetup(this.dialog.mail));
        }
        if (id == 14) {
            this.dialog.mail = new PlayerMail();
            this.init();
        }
        if (id == 66) {
            this.close();
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop guiNpcTextField) {
        if (guiNpcTextField.id == 1) {
            this.dialog.title = guiNpcTextField.m_94155_();
            while (DialogController.instance.containsDialogName(this.dialog.category, this.dialog)) {
                this.dialog.title = this.dialog.title + "_";
            }
        }
        if (guiNpcTextField.id == 2) {
            this.dialog.sound = guiNpcTextField.m_94155_();
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        if (subgui instanceof GuiTextAreaScreen gui) {
            this.dialog.text = gui.text;
        }
        if (subgui instanceof SubGuiNpcDialogOption) {
            this.setSubGui(new SubGuiNpcDialogOptions(this.dialog));
        }
        if (subgui instanceof SubGuiNpcCommand) {
            this.dialog.command = ((SubGuiNpcCommand) subgui).command;
        }
        if (subgui instanceof GuiQuestSelection gqs && gqs.selectedQuest != null) {
            this.dialog.quest = gqs.selectedQuest.id;
            this.init();
        }
        if (subgui instanceof GuiSoundSelection gss && gss.selectedResource != null) {
            this.getTextField(2).m_94144_(gss.selectedResource.toString());
            this.unFocused(this.getTextField(2));
        }
    }

    @Override
    public void save() {
        GuiTextFieldNop.unfocus();
        Packets.sendServer(new SPacketDialogSave(this.dialog.category.id, this.dialog.save(new CompoundTag())));
    }
}