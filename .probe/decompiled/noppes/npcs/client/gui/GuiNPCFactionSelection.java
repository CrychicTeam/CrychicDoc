package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiStringSlotNop;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;

public class GuiNPCFactionSelection extends GuiNPCInterface {

    private GuiStringSlotNop slot;

    private Screen parent;

    private int factionId;

    public GuiSelectionListener listener;

    public GuiNPCFactionSelection(EntityNPCInterface npc, Screen parent, int dialog) {
        super(npc);
        this.drawDefaultBackground = false;
        this.title = "Select Dialog Category";
        this.parent = parent;
        this.factionId = dialog;
        if (parent instanceof GuiSelectionListener) {
            this.listener = (GuiSelectionListener) parent;
        }
    }

    @Override
    public void init() {
        super.m_7856_();
        Map<String, Integer> coloredMap = new HashMap();
        String selected = null;
        for (IFaction f : FactionController.instance.list()) {
            coloredMap.put(f.getName(), f.getColor());
            if (this.factionId == f.getId()) {
                selected = f.getName();
            }
        }
        this.slot = new GuiStringSlotNop(null, this, false);
        this.slot.setColoredList(coloredMap);
        this.slot.setSelected(selected);
        this.m_7787_(this.slot);
        this.addButton(new GuiButtonNop(this, 2, this.f_96543_ / 2 - 100, this.f_96544_ - 41, 98, 20, "gui.back"));
        this.addButton(new GuiButtonNop(this, 4, this.f_96543_ / 2 + 2, this.f_96544_ - 41, 98, 20, "mco.template.button.select"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.slot.m_88315_(graphics, mouseX, mouseY, partialTicks);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            this.close();
            NoppesUtil.openGUI(this.player, this.parent);
        }
        if (id == 4) {
            this.doubleClicked();
        }
    }

    @Override
    public void doubleClicked() {
        if (this.slot.getSelectedString() != null && !this.slot.getSelectedString().isEmpty()) {
            this.factionId = FactionController.instance.getFactionFromName(this.slot.getSelectedString()).id;
            this.close();
            NoppesUtil.openGUI(this.player, this.parent);
        }
    }

    @Override
    public void save() {
        if (this.factionId >= 0 && this.listener != null) {
            this.listener.selected(this.factionId, this.slot.getSelectedString());
        }
    }
}