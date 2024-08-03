package noppes.npcs.client.gui.advanced;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketLinkedGet;
import noppes.npcs.packets.server.SPacketLinkedSet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IScrollData;

public class GuiNPCAdvancedLinkedNpc extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {

    private GuiCustomScrollNop scroll;

    private List<String> data = new ArrayList();

    public static Screen Instance;

    public GuiNPCAdvancedLinkedNpc(EntityNPCInterface npc) {
        super(npc);
        Instance = this;
        Packets.sendServer(new SPacketLinkedGet());
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 358, this.guiTop + 38, 58, 20, "gui.clear"));
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setSize(143, 208);
        }
        this.scroll.guiLeft = this.guiLeft + 137;
        this.scroll.guiTop = this.guiTop + 4;
        this.scroll.setSelected(this.npc.linkedName);
        this.scroll.setList(this.data);
        this.addScroll(this.scroll);
    }

    @Override
    public void buttonEvent(GuiButtonNop button) {
        if (button.id == 1) {
            Packets.sendServer(new SPacketLinkedSet(""));
            this.scroll.setSelected("");
        }
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        this.data = new ArrayList(list);
        this.init();
    }

    @Override
    public void setSelected(String selected) {
        this.scroll.setSelected(selected);
    }

    @Override
    public void save() {
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        Packets.sendServer(new SPacketLinkedSet(guiCustomScroll.getSelected()));
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}