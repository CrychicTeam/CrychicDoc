package noppes.npcs.client.gui;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketTransportCategoriesGet;
import noppes.npcs.packets.server.SPacketTransportCategorySave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class GuiNPCTransportCategoryEdit extends GuiNPCInterface {

    private Screen parent;

    private String name;

    private int id;

    public GuiNPCTransportCategoryEdit(EntityNPCInterface npc, Screen parent, String name, int id) {
        super(npc);
        this.parent = parent;
        this.name = name;
        this.id = id;
        this.title = "Npc Transport Category";
    }

    @Override
    public void init() {
        super.m_7856_();
        this.addTextField(new GuiTextFieldNop(1, this, this.f_96543_ / 2 - 40, 100, 140, 20, this.name));
        this.addLabel(new GuiLabel(1, "Title:", this.f_96543_ / 2 - 100 + 4, 105, 16777215));
        this.addButton(new GuiButtonNop(this, 2, this.f_96543_ / 2 - 100, 210, 98, 20, "gui.back"));
        this.addButton(new GuiButtonNop(this, 3, this.f_96543_ / 2 + 2, 210, 98, 20, "Save"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            NoppesUtil.openGUI(this.player, this.parent);
            Packets.sendServer(new SPacketTransportCategoriesGet());
        }
        if (id == 3) {
            this.save();
            NoppesUtil.openGUI(this.player, this.parent);
            Packets.sendServer(new SPacketTransportCategoriesGet());
        }
    }

    @Override
    public void save() {
        String name = this.getTextField(1).m_94155_();
        if (!name.trim().isEmpty()) {
            Packets.sendServer(new SPacketTransportCategorySave(this.id, name));
        }
    }
}