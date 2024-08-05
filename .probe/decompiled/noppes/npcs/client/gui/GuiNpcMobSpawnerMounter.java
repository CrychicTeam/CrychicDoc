package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.controllers.ClientCloneController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCloneList;
import noppes.npcs.packets.server.SPacketToolMounter;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiMenuSideButton;
import noppes.npcs.shared.client.gui.components.GuiMenuTopButton;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcMobSpawnerMounter extends GuiNPCInterface implements IGuiData {

    private GuiCustomScrollNop scroll;

    private List<String> list;

    private static int showingClones = 1;

    private int activeTab = 1;

    public GuiNpcMobSpawnerMounter() {
        this.imageWidth = 256;
        this.setBackground("menubg.png");
    }

    @Override
    public void init() {
        super.m_7856_();
        this.guiTop += 10;
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setSize(165, 210);
        } else {
            this.scroll.clear();
        }
        this.scroll.guiLeft = this.guiLeft + 4;
        this.scroll.guiTop = this.guiTop + 4;
        this.addScroll(this.scroll);
        GuiMenuTopButton button;
        this.addTopButton(button = new GuiMenuTopButton(this, 3, this.guiLeft + 4, this.guiTop - 17, "spawner.clones"));
        button.active = showingClones == 0;
        GuiMenuTopButton var2;
        this.addTopButton(var2 = new GuiMenuTopButton(this, 4, button, "spawner.entities"));
        var2.active = showingClones == 1;
        this.addTopButton(button = new GuiMenuTopButton(this, 5, var2, "gui.server"));
        button.active = showingClones == 2;
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 170, this.guiTop + 6, 82, 20, "spawner.mount"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 170, this.guiTop + 50, 82, 20, "spawner.mountplayer"));
        if (showingClones != 0 && showingClones != 2) {
            this.showEntities();
        } else {
            this.addSideButton(new GuiMenuSideButton(this, 21, this.guiLeft - 69, this.guiTop + 2, 70, 22, "Tab 1"));
            this.addSideButton(new GuiMenuSideButton(this, 22, this.guiLeft - 69, this.guiTop + 23, 70, 22, "Tab 2"));
            this.addSideButton(new GuiMenuSideButton(this, 23, this.guiLeft - 69, this.guiTop + 44, 70, 22, "Tab 3"));
            this.addSideButton(new GuiMenuSideButton(this, 24, this.guiLeft - 69, this.guiTop + 65, 70, 22, "Tab 4"));
            this.addSideButton(new GuiMenuSideButton(this, 25, this.guiLeft - 69, this.guiTop + 86, 70, 22, "Tab 5"));
            this.addSideButton(new GuiMenuSideButton(this, 26, this.guiLeft - 69, this.guiTop + 107, 70, 22, "Tab 6"));
            this.addSideButton(new GuiMenuSideButton(this, 27, this.guiLeft - 69, this.guiTop + 128, 70, 22, "Tab 7"));
            this.addSideButton(new GuiMenuSideButton(this, 28, this.guiLeft - 69, this.guiTop + 149, 70, 22, "Tab 8"));
            this.addSideButton(new GuiMenuSideButton(this, 29, this.guiLeft - 69, this.guiTop + 170, 70, 22, "Tab 9"));
            this.getSideButton(20 + this.activeTab).active = true;
            this.showClones();
        }
    }

    private void showEntities() {
        this.list = new ArrayList(EntityUtil.getAllEntities(this.f_96541_.level, false).keySet());
        this.scroll.setList(this.list);
    }

    private void showClones() {
        if (showingClones == 2) {
            Packets.sendServer(new SPacketCloneList(this.activeTab));
        } else {
            new ArrayList();
            this.list = ClientCloneController.Instance.getClones(this.activeTab);
            this.scroll.setList(this.list);
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
        if (id == 1) {
            String sel = this.scroll.getSelected();
            if (sel == null) {
                return;
            }
            if (showingClones == 0) {
                Packets.sendServer(new SPacketToolMounter(0, ClientCloneController.Instance.getCloneData(this.player.m_20203_(), sel, this.activeTab)));
            }
            if (showingClones == 1) {
                Packets.sendServer(new SPacketToolMounter(2, sel, -1));
            }
            if (showingClones == 2) {
                Packets.sendServer(new SPacketToolMounter(1, sel, this.activeTab));
            }
            this.close();
        }
        if (id == 2) {
            Packets.sendServer(new SPacketToolMounter());
            this.close();
        }
        if (id == 3) {
            showingClones = 0;
            this.init();
        }
        if (id == 4) {
            showingClones = 1;
            this.init();
        }
        if (id == 5) {
            showingClones = 2;
            this.init();
        }
        if (id > 20) {
            this.activeTab = id - 20;
            this.init();
        }
    }

    protected ListTag newDoubleTagList(double... par1ArrayOfDouble) {
        ListTag nbttaglist = new ListTag();
        for (double d1 : par1ArrayOfDouble) {
            nbttaglist.add(DoubleTag.valueOf(d1));
        }
        return nbttaglist;
    }

    @Override
    public void save() {
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        ListTag nbtlist = compound.getList("List", 8);
        List<String> list = new ArrayList();
        for (int i = 0; i < nbtlist.size(); i++) {
            list.add(nbtlist.getString(i));
        }
        this.list = list;
        this.scroll.setList(list);
    }
}