package noppes.npcs.client.gui.questtypes;

import java.util.HashMap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.select.GuiDialogSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketQuestDialogTitles;
import noppes.npcs.quests.QuestDialog;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcQuestTypeDialog extends GuiNPCInterface implements GuiSelectionListener, IGuiData {

    private Screen parent;

    private QuestDialog quest;

    private HashMap<Integer, String> data = new HashMap();

    private int selectedSlot;

    public GuiNpcQuestTypeDialog(EntityNPCInterface npc, Quest q, Screen parent) {
        this.npc = npc;
        this.parent = parent;
        this.title = "Quest Dialog Setup";
        this.quest = (QuestDialog) q.questInterface;
        this.setBackground("menubg.png");
        this.imageWidth = 256;
        this.imageHeight = 216;
        Packets.sendServer(new SPacketQuestDialogTitles(this.quest.dialogs.containsKey(0) ? (Integer) this.quest.dialogs.get(0) : -1, this.quest.dialogs.containsKey(1) ? (Integer) this.quest.dialogs.get(1) : -1, this.quest.dialogs.containsKey(2) ? (Integer) this.quest.dialogs.get(2) : -1));
    }

    @Override
    public void init() {
        super.m_7856_();
        for (int i = 0; i < 3; i++) {
            String title = "dialog.selectoption";
            if (this.data.containsKey(i)) {
                title = (String) this.data.get(i);
            }
            this.addButton(new GuiButtonNop(this, i + 9, this.guiLeft + 10, 55 + i * 22, 20, 20, "X"));
            this.addButton(new GuiButtonNop(this, i + 3, this.guiLeft + 34, 55 + i * 22, 210, 20, title));
        }
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 150, this.guiTop + 190, 98, 20, "gui.back"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.close();
        }
        if (guibutton.id >= 3 && guibutton.id < 9) {
            this.selectedSlot = guibutton.id - 3;
            int id = -1;
            if (this.quest.dialogs.containsKey(this.selectedSlot)) {
                id = (Integer) this.quest.dialogs.get(this.selectedSlot);
            }
            this.setSubGui(new GuiDialogSelection(id));
        }
        if (guibutton.id >= 9 && guibutton.id < 15) {
            int slot = guibutton.id - 9;
            this.quest.dialogs.remove(slot);
            this.data.remove(slot);
            this.save();
            this.init();
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void selected(int id, String name) {
        this.quest.dialogs.put(this.selectedSlot, id);
        this.data.put(this.selectedSlot, name);
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.data.clear();
        if (compound.contains("1")) {
            this.data.put(0, compound.getString("1"));
        }
        if (compound.contains("2")) {
            this.data.put(1, compound.getString("2"));
        }
        if (compound.contains("3")) {
            this.data.put(2, compound.getString("3"));
        }
        this.init();
    }
}