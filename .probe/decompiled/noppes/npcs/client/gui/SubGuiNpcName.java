package noppes.npcs.client.gui;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.entity.data.DataDisplay;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpRandomNameSet;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonBiDirectional;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class SubGuiNpcName extends GuiBasic implements ITextfieldListener, IGuiData {

    private DataDisplay display;

    public SubGuiNpcName(DataDisplay display) {
        this.display = display;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 4;
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + this.imageWidth - 24, y, 20, 20, "X"));
        int var10005 = this.guiLeft + 4;
        y += 50;
        this.addTextField(new GuiTextFieldNop(0, this, var10005, y, 226, 20, this.display.getName()));
        var10005 = this.guiLeft + 4;
        y += 22;
        this.addButton(new GuiButtonBiDirectional(this, 1, var10005, y, 200, 20, new String[] { "markov.roman.name", "markov.japanese.name", "markov.slavic.name", "markov.welsh.name", "markov.sami.name", "markov.oldNorse.name", "markov.ancientGreek.name", "markov.aztec.name", "markov.classicCNPCs.name", "markov.spanish.name" }, this.display.getMarkovGeneratorId()));
        var10005 = this.guiLeft + 64;
        y += 22;
        this.addButton(new GuiButtonBiDirectional(this, 2, var10005, y, 120, 20, new String[] { "markov.gender.either", "markov.gender.male", "markov.gender.female" }, this.display.getMarkovGender()));
        this.addLabel(new GuiLabel(2, "markov.gender.name", this.guiLeft + 5, y + 5));
        var10005 = this.guiLeft + 4;
        y += 42;
        this.addButton(new GuiButtonNop(this, 3, var10005, y, 70, 20, "markov.generate"));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            if (!textfield.isEmpty()) {
                this.display.setName(textfield.m_94155_());
            } else {
                textfield.m_94144_(this.display.getName());
            }
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 1) {
            this.display.setMarkovGeneratorId(guibutton.getValue());
        }
        if (guibutton.id == 2) {
            this.display.setMarkovGender(guibutton.getValue());
        }
        if (guibutton.id == 3) {
            Packets.sendServer(new SPacketNpRandomNameSet(this.display.getMarkovGeneratorId(), this.display.getMarkovGender()));
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.display.readToNBT(compound);
        this.init();
    }
}