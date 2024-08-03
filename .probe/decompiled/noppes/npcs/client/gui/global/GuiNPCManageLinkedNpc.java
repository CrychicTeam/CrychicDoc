package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.SubGuiEditText;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketLinkedAdd;
import noppes.npcs.packets.server.SPacketLinkedGet;
import noppes.npcs.packets.server.SPacketLinkedRemove;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.IScrollData;

public class GuiNPCManageLinkedNpc extends GuiNPCInterface2 implements IScrollData {

    private GuiCustomScrollNop scroll;

    private List<String> data = new ArrayList();

    public static Screen Instance;

    public GuiNPCManageLinkedNpc(EntityNPCInterface npc) {
        super(npc);
        Instance = this;
        Packets.sendServer(new SPacketLinkedGet());
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 358, this.guiTop + 38, 58, 20, "gui.add"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 358, this.guiTop + 61, 58, 20, "gui.remove"));
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setSize(143, 208);
        }
        this.scroll.guiLeft = this.guiLeft + 214;
        this.scroll.guiTop = this.guiTop + 4;
        this.scroll.setList(this.data);
        this.addScroll(this.scroll);
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if (button.id == 1) {
            this.save();
            this.setSubGui(new SubGuiEditText("New"));
        }
        if (button.id == 2 && this.scroll.hasSelected()) {
            Packets.sendServer(new SPacketLinkedRemove(this.scroll.getSelected()));
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        if (!((SubGuiEditText) subgui).cancelled) {
            Packets.sendServer(new SPacketLinkedAdd(((SubGuiEditText) subgui).text));
        }
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        this.data = new ArrayList(list);
        this.init();
    }

    @Override
    public void setSelected(String selected) {
    }

    @Override
    public void save() {
    }
}