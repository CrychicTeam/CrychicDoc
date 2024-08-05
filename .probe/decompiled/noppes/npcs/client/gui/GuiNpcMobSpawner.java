package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.controllers.ClientCloneController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCloneList;
import noppes.npcs.packets.server.SPacketCloneRemove;
import noppes.npcs.packets.server.SPacketToolMobSpawner;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiMenuSideButton;
import noppes.npcs.shared.client.gui.components.GuiMenuTopButton;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNpcMobSpawner extends GuiNPCInterface implements IGuiData {

    private GuiCustomScrollNop scroll;

    private BlockPos pos;

    private List<String> list;

    private static int showingClones = 0;

    private int activeTab = 1;

    public GuiNpcMobSpawner(BlockPos pos) {
        this.imageWidth = 256;
        this.pos = pos;
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
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 170, this.guiTop + 6, 82, 20, "gui.spawn"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 170, this.guiTop + 100, 82, 20, "spawner.mobspawner"));
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
            this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 170, this.guiTop + 30, 82, 20, "gui.remove"));
            this.getSideButton(20 + this.activeTab).active = true;
            this.showClones();
        }
    }

    private void showEntities() {
        this.list = new ArrayList(EntityUtil.getAllEntities(Minecraft.getInstance().level, false).keySet());
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

    private CompoundTag getCompound() {
        String sel = this.scroll.getSelected();
        if (sel == null) {
            return null;
        } else if (showingClones == 0) {
            return ClientCloneController.Instance.getCloneData(this.player.m_20203_(), sel, this.activeTab);
        } else {
            ResourceLocation loc = (ResourceLocation) EntityUtil.getAllEntities(Minecraft.getInstance().level, false).get(sel);
            EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(loc);
            Entity entity = type.create(Minecraft.getInstance().level);
            if (entity == null) {
                return null;
            } else {
                CompoundTag compound = new CompoundTag();
                entity.saveAsPassenger(compound);
                return compound;
            }
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            this.close();
        }
        if (id == 1) {
            if (showingClones == 2) {
                String sel = this.scroll.getSelected();
                if (sel == null) {
                    return;
                }
                Packets.sendServer(new SPacketToolMobSpawner(false, this.pos, sel, this.activeTab));
                this.close();
            } else {
                CompoundTag compound = this.getCompound();
                if (compound == null) {
                    return;
                }
                Packets.sendServer(new SPacketToolMobSpawner(false, this.pos, compound));
                this.close();
            }
        }
        if (id == 2) {
            if (showingClones == 2) {
                String sel = this.scroll.getSelected();
                if (sel == null) {
                    return;
                }
                Packets.sendServer(new SPacketToolMobSpawner(true, this.pos, sel, this.activeTab));
                this.close();
            } else {
                CompoundTag compound = this.getCompound();
                if (compound == null) {
                    return;
                }
                Packets.sendServer(new SPacketToolMobSpawner(true, this.pos, compound));
                this.close();
            }
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
        if (id == 6 && this.scroll.getSelected() != null) {
            if (showingClones == 2) {
                Packets.sendServer(new SPacketCloneRemove(this.scroll.getSelected(), this.activeTab));
                return;
            }
            ClientCloneController.Instance.removeClone(this.scroll.getSelected(), this.activeTab);
            this.scroll.clearSelection();
            this.init();
        }
        if (id > 20) {
            this.activeTab = id - 20;
            this.init();
        }
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
        this.scroll.setList(this.list);
    }
}