package noppes.npcs.client.gui.questtypes;

import java.util.TreeMap;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.quests.QuestManual;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcQuestTypeManual extends GuiNPCInterface implements ITextfieldListener {

    private Screen parent;

    private QuestManual quest;

    private GuiTextFieldNop lastSelected;

    public GuiNpcQuestTypeManual(EntityNPCInterface npc, Quest q, Screen parent) {
        this.npc = npc;
        this.parent = parent;
        this.title = "Quest Manual Setup";
        this.quest = (QuestManual) q.questInterface;
        this.setBackground("menubg.png");
        this.imageWidth = 356;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.m_7856_();
        int i = 0;
        this.addLabel(new GuiLabel(0, "You can fill in npc or player names too", this.guiLeft + 4, this.guiTop + 50));
        for (String name : this.quest.manuals.keySet()) {
            this.addTextField(new GuiTextFieldNop(i, this, this.guiLeft + 4, this.guiTop + 70 + i * 22, 180, 20, name));
            this.addTextField(new GuiTextFieldNop(i + 3, this, this.guiLeft + 186, this.guiTop + 70 + i * 22, 24, 20, this.quest.manuals.get(name) + ""));
            this.getTextField(i + 3).numbersOnly = true;
            this.getTextField(i + 3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
            i++;
        }
        while (i < 3) {
            this.addTextField(new GuiTextFieldNop(i, this, this.guiLeft + 4, this.guiTop + 70 + i * 22, 180, 20, ""));
            this.addTextField(new GuiTextFieldNop(i + 3, this, this.guiLeft + 186, this.guiTop + 70 + i * 22, 24, 20, "1"));
            this.getTextField(i + 3).numbersOnly = true;
            this.getTextField(i + 3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
            i++;
        }
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 4, this.guiTop + 140, 98, 20, "gui.back"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.close();
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void unFocused(GuiTextFieldNop guiNpcTextField) {
        if (guiNpcTextField.id < 3) {
            this.lastSelected = guiNpcTextField;
        }
        this.saveTargets();
    }

    private void saveTargets() {
        TreeMap<String, Integer> map = new TreeMap();
        for (int i = 0; i < 3; i++) {
            String name = this.getTextField(i).m_94155_();
            if (!name.isEmpty()) {
                map.put(name, this.getTextField(i + 3).getInteger());
            }
        }
        this.quest.manuals = map;
    }
}