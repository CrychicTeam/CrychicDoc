package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCloneList;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiMenuSideButton;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcMobSpawnerSelector extends GuiBasic implements IGuiData {

    private GuiCustomScrollNop scroll;

    private List<String> list;

    public int activeTab = 1;

    public GuiNpcMobSpawnerSelector() {
        this.imageWidth = 256;
        this.setBackground("menubg.png");
    }

    @Override
    public void init() {
        super.init();
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setSize(165, 210);
        } else {
            this.scroll.clear();
        }
        this.scroll.guiLeft = this.guiLeft + 4;
        this.scroll.guiTop = this.guiTop + 4;
        this.addScroll(this.scroll);
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 171, this.guiTop + 80, 80, 20, "gui.done"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 171, this.guiTop + 103, 80, 20, "gui.cancel"));
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

    public String getSelected() {
        return this.scroll.getSelected();
    }

    private void showClones() {
        Packets.sendServer(new SPacketCloneList(this.activeTab));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
        if (id == 1) {
            this.scroll.clear();
            this.close();
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