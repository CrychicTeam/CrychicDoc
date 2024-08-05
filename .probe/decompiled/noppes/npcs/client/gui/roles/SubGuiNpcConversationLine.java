package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcConversationLine extends GuiBasic implements ITextfieldListener {

    public String line;

    public String sound;

    private GuiSoundSelection gui;

    public SubGuiNpcConversationLine(String line, String sound) {
        this.line = line;
        this.sound = sound;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "Line", this.guiLeft + 4, this.guiTop + 10));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 4, this.guiTop + 22, 200, 20, this.line));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 4, this.guiTop + 55, 90, 20, "Select Sound"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 96, this.guiTop + 55, 20, 20, "X"));
        this.addLabel(new GuiLabel(1, this.sound, this.guiLeft + 4, this.guiTop + 81));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 162, this.guiTop + 192, 90, 20, "gui.done"));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        this.line = textfield.m_94155_();
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 1) {
            this.setSubGui(new GuiSoundSelection(this.sound));
        }
        if (id == 2) {
            this.sound = "";
            this.init();
        }
        if (id == 66) {
            this.close();
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        GuiSoundSelection gss = (GuiSoundSelection) subgui;
        if (gss.selectedResource != null) {
            this.sound = gss.selectedResource.toString();
        }
    }
}