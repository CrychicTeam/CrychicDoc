package noppes.npcs.client.gui.advanced;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNPCSoundsMenu extends GuiNPCInterface2 implements ITextfieldListener {

    private GuiSoundSelection gui;

    private GuiTextFieldNop selectedField;

    public GuiNPCSoundsMenu(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "advanced.idlesound", this.guiLeft + 5, this.guiTop + 20));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 80, this.guiTop + 15, 200, 20, this.npc.advanced.getSound(0)));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 290, this.guiTop + 15, 80, 20, "gui.selectSound"));
        this.addLabel(new GuiLabel(2, "advanced.angersound", this.guiLeft + 5, this.guiTop + 45));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 80, this.guiTop + 40, 200, 20, this.npc.advanced.getSound(1)));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 290, this.guiTop + 40, 80, 20, "gui.selectSound"));
        this.addLabel(new GuiLabel(3, "advanced.hurtsound", this.guiLeft + 5, this.guiTop + 70));
        this.addTextField(new GuiTextFieldNop(3, this, this.guiLeft + 80, this.guiTop + 65, 200, 20, this.npc.advanced.getSound(2)));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 290, this.guiTop + 65, 80, 20, "gui.selectSound"));
        this.addLabel(new GuiLabel(4, "advanced.deathsound", this.guiLeft + 5, this.guiTop + 95));
        this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 80, this.guiTop + 90, 200, 20, this.npc.advanced.getSound(3)));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 290, this.guiTop + 90, 80, 20, "gui.selectSound"));
        this.addLabel(new GuiLabel(5, "advanced.stepsound", this.guiLeft + 5, this.guiTop + 120));
        this.addTextField(new GuiTextFieldNop(5, this, this.guiLeft + 80, this.guiTop + 115, 200, 20, this.npc.advanced.getSound(4)));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 290, this.guiTop + 115, 80, 20, "gui.selectSound"));
        this.addLabel(new GuiLabel(6, "advanced.haspitch", this.guiLeft + 5, this.guiTop + 150));
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 120, this.guiTop + 145, 80, 20, new String[] { "gui.no", "gui.yes" }, this.npc.advanced.disablePitch ? 0 : 1));
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if (button.id == 6) {
            this.npc.advanced.disablePitch = button.getValue() == 0;
        } else {
            this.selectedField = this.getTextField(button.id);
            this.setSubGui(new GuiSoundSelection(this.selectedField.m_94155_()));
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            this.npc.advanced.setSound(0, textfield.m_94155_());
        }
        if (textfield.id == 2) {
            this.npc.advanced.setSound(1, textfield.m_94155_());
        }
        if (textfield.id == 3) {
            this.npc.advanced.setSound(2, textfield.m_94155_());
        }
        if (textfield.id == 4) {
            this.npc.advanced.setSound(3, textfield.m_94155_());
        }
        if (textfield.id == 5) {
            this.npc.advanced.setSound(4, textfield.m_94155_());
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        GuiSoundSelection gss = (GuiSoundSelection) subgui;
        if (gss.selectedResource != null) {
            this.selectedField.m_94144_(gss.selectedResource.toString());
            this.unFocused(this.selectedField);
        }
    }
}