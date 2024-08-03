package noppes.npcs.client.gui;

import java.util.HashMap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.Lines;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuGet;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNPCLinesEdit extends GuiNPCInterface2 implements IGuiData {

    private Lines lines;

    private int selectedId = -1;

    private GuiSoundSelection gui;

    public GuiNPCLinesEdit(EntityNPCInterface npc, Lines lines) {
        super(npc);
        this.lines = lines;
        Packets.sendServer(new SPacketMenuGet(EnumMenuType.ADVANCED));
    }

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < 8; i++) {
            String text = "";
            String sound = "";
            if (this.lines.lines.containsKey(i)) {
                Line line = (Line) this.lines.lines.get(i);
                text = line.getText();
                sound = line.getSound();
            }
            this.addTextField(new GuiTextFieldNop(i, this, this.guiLeft + 4, this.guiTop + 4 + i * 24, 200, 20, text));
            this.addTextField(new GuiTextFieldNop(i + 8, this, this.guiLeft + 208, this.guiTop + 4 + i * 24, 146, 20, sound));
            this.addButton(new GuiButtonNop(this, i, this.guiLeft + 358, this.guiTop + 4 + i * 24, 60, 20, "mco.template.button.select"));
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        this.selectedId = guibutton.id + 8;
        this.setSubGui(new GuiSoundSelection(this.getTextField(this.selectedId).m_94155_()));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.npc.advanced.readToNBT(compound);
        this.init();
    }

    private void saveLines() {
        HashMap<Integer, Line> lines = new HashMap();
        for (int i = 0; i < 8; i++) {
            GuiTextFieldNop tf = this.getTextField(i);
            GuiTextFieldNop tf2 = this.getTextField(i + 8);
            if (!tf.isEmpty() || !tf2.isEmpty()) {
                Line line = new Line();
                line.setText(tf.m_94155_());
                line.setSound(tf2.m_94155_());
                lines.put(i, line);
            }
        }
        this.lines.lines = lines;
    }

    @Override
    public void save() {
        this.saveLines();
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        GuiSoundSelection gss = (GuiSoundSelection) subgui;
        if (gss.selectedResource != null) {
            this.getTextField(this.selectedId).m_94144_(gss.selectedResource.toString());
            this.saveLines();
            this.init();
        }
    }
}