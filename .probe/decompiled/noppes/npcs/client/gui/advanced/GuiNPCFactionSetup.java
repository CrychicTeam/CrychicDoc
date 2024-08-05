package noppes.npcs.client.gui.advanced;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.SubGuiNpcFactionOptions;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketFactionsGet;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.packets.server.SPacketNpcFactionSet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IScrollData;

public class GuiNPCFactionSetup extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {

    private GuiCustomScrollNop scrollFactions;

    private Map<String, Integer> data = new HashMap();

    public GuiNPCFactionSetup(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "faction.attackHostile", this.guiLeft + 4, this.guiTop + 25));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 144, this.guiTop + 20, 40, 20, new String[] { "gui.no", "gui.yes" }, this.npc.advanced.attackOtherFactions ? 1 : 0));
        this.addLabel(new GuiLabel(1, "faction.defend", this.guiLeft + 4, this.guiTop + 47));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 144, this.guiTop + 42, 40, 20, new String[] { "gui.no", "gui.yes" }, this.npc.advanced.defendFaction ? 1 : 0));
        this.addLabel(new GuiLabel(12, "faction.ondeath", this.guiLeft + 4, this.guiTop + 69));
        this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 90, this.guiTop + 64, 80, 20, "faction.points"));
        if (this.scrollFactions == null) {
            this.scrollFactions = new GuiCustomScrollNop(this, 0);
            this.scrollFactions.setSize(180, 200);
        }
        this.scrollFactions.guiLeft = this.guiLeft + 200;
        this.scrollFactions.guiTop = this.guiTop + 4;
        this.addScroll(this.scrollFactions);
        Packets.sendServer(new SPacketFactionsGet());
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.npc.advanced.attackOtherFactions = guibutton.getValue() == 1;
        }
        if (guibutton.id == 1) {
            this.npc.advanced.defendFaction = guibutton.getValue() == 1;
        }
        if (guibutton.id == 12) {
            this.setSubGui(new SubGuiNpcFactionOptions(this.npc.advanced.factions));
        }
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        String name = this.npc.getFaction().name;
        this.data = data;
        this.scrollFactions.setList(list);
        if (name != null) {
            this.setSelected(name);
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if (k == 0 && this.scrollFactions != null) {
            this.scrollFactions.mouseClicked(i, j, k);
        }
        return super.mouseClicked(i, j, k);
    }

    @Override
    public void setSelected(String selected) {
        this.scrollFactions.setSelected(selected);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        if (guiCustomScroll.id == 0) {
            Packets.sendServer(new SPacketNpcFactionSet((Integer) this.data.get(this.scrollFactions.getSelected())));
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}