package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.SubGuiNpcFactionPoints;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketFactionGet;
import noppes.npcs.packets.server.SPacketFactionRemove;
import noppes.npcs.packets.server.SPacketFactionSave;
import noppes.npcs.packets.server.SPacketFactionsGet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.IScrollData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNPCManageFactions extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData {

    private GuiCustomScrollNop scrollFactions;

    private Map<String, Integer> data = new HashMap();

    private Faction faction = new Faction();

    private String selected = null;

    public GuiNPCManageFactions(EntityNPCInterface npc) {
        super(npc);
        Packets.sendServer(new SPacketFactionsGet());
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 368, this.guiTop + 8, 45, 20, "gui.add"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 368, this.guiTop + 32, 45, 20, "gui.remove"));
        if (this.scrollFactions == null) {
            this.scrollFactions = new GuiCustomScrollNop(this, 0);
            this.scrollFactions.setSize(143, 208);
        }
        this.scrollFactions.guiLeft = this.guiLeft + 220;
        this.scrollFactions.guiTop = this.guiTop + 4;
        this.addScroll(this.scrollFactions);
        if (this.faction.id != -1) {
            this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 40, this.guiTop + 4, 136, 20, this.faction.name));
            this.getTextField(0).m_94199_(20);
            this.addLabel(new GuiLabel(0, "gui.name", this.guiLeft + 8, this.guiTop + 9));
            this.addLabel(new GuiLabel(10, "ID", this.guiLeft + 178, this.guiTop + 4));
            this.addLabel(new GuiLabel(11, this.faction.id + "", this.guiLeft + 178, this.guiTop + 14));
            String color = Integer.toHexString(this.faction.color);
            while (color.length() < 6) {
                color = "0" + color;
            }
            this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 40, this.guiTop + 26, 60, 20, color));
            this.addLabel(new GuiLabel(1, "gui.color", this.guiLeft + 8, this.guiTop + 31));
            this.getButton(10).setFGColor(this.faction.color);
            this.addLabel(new GuiLabel(2, "faction.points", this.guiLeft + 8, this.guiTop + 53));
            this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 100, this.guiTop + 48, 45, 20, "selectServer.edit"));
            this.addLabel(new GuiLabel(3, "faction.hidden", this.guiLeft + 8, this.guiTop + 75));
            this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 100, this.guiTop + 70, 45, 20, new String[] { "gui.no", "gui.yes" }, this.faction.hideFaction ? 1 : 0));
            this.addLabel(new GuiLabel(4, "faction.attacked", this.guiLeft + 8, this.guiTop + 97));
            this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 100, this.guiTop + 92, 45, 20, new String[] { "gui.no", "gui.yes" }, this.faction.getsAttacked ? 1 : 0));
            this.addLabel(new GuiLabel(6, "faction.hostiles", this.guiLeft + 8, this.guiTop + 145));
            ArrayList<String> hostileList = new ArrayList(this.scrollFactions.getList());
            hostileList.remove(this.faction.name);
            HashSet<String> set = new HashSet();
            for (String s : this.data.keySet()) {
                if (!s.equals(this.faction.name) && this.faction.attackFactions.contains(this.data.get(s))) {
                    set.add(s);
                }
            }
            GuiCustomScrollNop scrollHostileFactions = new GuiCustomScrollNop(this, 1, true);
            scrollHostileFactions.setSize(163, 58);
            scrollHostileFactions.guiLeft = this.guiLeft + 4;
            scrollHostileFactions.guiTop = this.guiTop + 154;
            scrollHostileFactions.setList(hostileList);
            scrollHostileFactions.setSelectedList(set);
            this.addScroll(scrollHostileFactions);
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.save();
            String name = I18n.get("gui.new");
            while (this.data.containsKey(name)) {
                name = name + "_";
            }
            Faction faction = new Faction(-1, name, 65280, 1000);
            CompoundTag compound = new CompoundTag();
            faction.writeNBT(compound);
            Packets.sendServer(new SPacketFactionSave(compound));
        }
        if (guibutton.id == 1 && this.data.containsKey(this.scrollFactions.getSelected())) {
            Packets.sendServer(new SPacketFactionRemove((Integer) this.data.get(this.selected)));
            this.scrollFactions.clear();
            this.faction = new Faction();
            this.init();
        }
        if (guibutton.id == 2) {
            this.setSubGui(new SubGuiNpcFactionPoints(this.faction));
        }
        if (guibutton.id == 3) {
            this.faction.hideFaction = guibutton.getValue() == 1;
        }
        if (guibutton.id == 4) {
            this.faction.getsAttacked = guibutton.getValue() == 1;
        }
        if (guibutton.id == 10) {
            this.setSubGui(new SubGuiColorSelector(this.faction.color));
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.faction = new Faction();
        this.faction.readNBT(compound);
        this.setSelected(this.faction.name);
        this.init();
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        String name = this.scrollFactions.getSelected();
        this.data = data;
        this.scrollFactions.setList(list);
        if (name != null) {
            this.scrollFactions.setSelected(name);
        }
    }

    @Override
    public void setSelected(String selected) {
        this.selected = selected;
        this.scrollFactions.setSelected(selected);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        if (guiCustomScroll.id == 0) {
            this.save();
            this.selected = this.scrollFactions.getSelected();
            Packets.sendServer(new SPacketFactionGet((Integer) this.data.get(this.selected)));
        } else if (guiCustomScroll.id == 1) {
            HashSet<Integer> set = new HashSet();
            for (String s : guiCustomScroll.getSelectedList()) {
                if (this.data.containsKey(s)) {
                    set.add((Integer) this.data.get(s));
                }
            }
            this.faction.attackFactions = set;
            this.save();
        }
    }

    @Override
    public void save() {
        if (this.selected != null && this.data.containsKey(this.selected) && this.faction != null) {
            CompoundTag compound = new CompoundTag();
            this.faction.writeNBT(compound);
            Packets.sendServer(new SPacketFactionSave(compound));
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop guiNpcTextField) {
        if (this.faction.id != -1) {
            if (guiNpcTextField.id == 0) {
                String name = guiNpcTextField.m_94155_();
                if (!name.isEmpty() && !this.data.containsKey(name)) {
                    String old = this.faction.name;
                    this.data.remove(this.faction.name);
                    this.faction.name = name;
                    this.data.put(this.faction.name, this.faction.id);
                    this.selected = name;
                    this.scrollFactions.replace(old, this.faction.name);
                }
            } else if (guiNpcTextField.id == 1) {
                int color = 0;
                try {
                    color = Integer.parseInt(guiNpcTextField.m_94155_(), 16);
                } catch (NumberFormatException var4) {
                    color = 0;
                }
                this.faction.color = color;
                guiNpcTextField.m_94202_(this.faction.color);
            }
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        if (subgui instanceof SubGuiColorSelector) {
            this.faction.color = ((SubGuiColorSelector) subgui).color;
            this.init();
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}