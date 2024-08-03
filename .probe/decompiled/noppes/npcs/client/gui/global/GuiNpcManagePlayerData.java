package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketPlayerDataGet;
import noppes.npcs.packets.server.SPacketPlayerDataRemove;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IScrollData;

public class GuiNpcManagePlayerData extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener {

    private GuiCustomScrollNop scroll;

    private String selectedPlayer = null;

    private String selected = null;

    private Map<String, Integer> data = new HashMap();

    private EnumPlayerData selection = EnumPlayerData.Players;

    private String search = "";

    public GuiNpcManagePlayerData(EntityNPCInterface npc, GuiNPCInterface2 parent) {
        super(npc);
        Packets.sendServer(new SPacketPlayerDataGet(this.selection, ""));
    }

    @Override
    public void init() {
        super.init();
        this.scroll = new GuiCustomScrollNop(this, 0);
        this.scroll.setSize(190, 175);
        this.scroll.guiLeft = this.guiLeft + 4;
        this.scroll.guiTop = this.guiTop + 16;
        this.addScroll(this.scroll);
        this.selected = null;
        this.addLabel(new GuiLabel(0, "playerdata.allPlayers", this.guiLeft + 10, this.guiTop + 6));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 200, this.guiTop + 10, 98, 20, "selectServer.delete"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 200, this.guiTop + 32, 98, 20, "playerdata.players"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 200, this.guiTop + 54, 98, 20, "quest.quest"));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 200, this.guiTop + 76, 98, 20, "dialog.dialog"));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 200, this.guiTop + 98, 98, 20, "global.transport"));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 200, this.guiTop + 120, 98, 20, "role.bank"));
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 200, this.guiTop + 142, 98, 20, "menu.factions"));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 4, this.guiTop + 193, 190, 20, this.search));
        this.getTextField(0).enabled = this.selection == EnumPlayerData.Players;
        this.initButtons();
    }

    public void initButtons() {
        this.getButton(1).setEnabled(this.selection != EnumPlayerData.Players);
        this.getButton(2).setEnabled(this.selection != EnumPlayerData.Quest);
        this.getButton(3).setEnabled(this.selection != EnumPlayerData.Dialog);
        this.getButton(4).setEnabled(this.selection != EnumPlayerData.Transport);
        this.getButton(5).setEnabled(this.selection != EnumPlayerData.Bank);
        this.getButton(6).setEnabled(this.selection != EnumPlayerData.Factions);
        if (this.selection == EnumPlayerData.Players) {
            this.getLabel(0).m_93666_(Component.translatable("playerdata.allPlayers"));
        } else {
            this.getLabel(0).m_93666_(Component.translatable("playerdata.selectedPlayer", this.selectedPlayer));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.scroll.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if (k == 0 && this.scroll != null) {
            this.scroll.mouseClicked(i, j, k);
        }
        return super.mouseClicked(i, j, k);
    }

    @Override
    public boolean charTyped(char c, int i) {
        super.m_5534_(c, i);
        if (this.selection != EnumPlayerData.Players) {
            return false;
        } else if (this.search.equals(this.getTextField(0).m_94155_())) {
            return false;
        } else {
            this.search = this.getTextField(0).m_94155_().toLowerCase();
            this.scroll.setList(this.getSearchList());
            return true;
        }
    }

    private List<String> getSearchList() {
        if (!this.search.isEmpty() && this.selection == EnumPlayerData.Players) {
            List<String> list = new ArrayList();
            for (String name : this.data.keySet()) {
                if (name.toLowerCase().contains(this.search)) {
                    list.add(name);
                }
            }
            return list;
        } else {
            return new ArrayList(this.data.keySet());
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            if (this.selected != null) {
                Packets.sendServer(new SPacketPlayerDataRemove(this.selection, this.selectedPlayer, (Integer) this.data.get(this.selected)));
                this.data.clear();
            }
            this.selected = null;
        }
        if (id >= 1 && id <= 6) {
            if (this.selectedPlayer == null && id != 1) {
                return;
            }
            this.selection = EnumPlayerData.values()[id - 1];
            this.initButtons();
            this.scroll.clear();
            this.data.clear();
            Packets.sendServer(new SPacketPlayerDataGet(this.selection, this.selectedPlayer));
            this.selected = null;
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        this.data.putAll(data);
        this.scroll.setList(this.getSearchList());
        if (this.selection == EnumPlayerData.Players && this.selectedPlayer != null) {
            this.scroll.setSelected(this.selectedPlayer);
            this.selected = this.selectedPlayer;
        }
    }

    @Override
    public void setSelected(String selected) {
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        this.selected = guiCustomScroll.getSelected();
        if (this.selection == EnumPlayerData.Players) {
            this.selectedPlayer = this.selected;
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}