package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcJobSave;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;

public class GuiNpcGuard extends GuiNPCInterface2 {

    private JobGuard role;

    private GuiCustomScrollNop scroll1;

    private GuiCustomScrollNop scroll2;

    public GuiNpcGuard(EntityNPCInterface npc) {
        super(npc);
        this.role = (JobGuard) npc.job;
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 10, this.guiTop + 4, 100, 20, "guard.animals"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 140, this.guiTop + 4, 100, 20, "guard.mobs"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 275, this.guiTop + 4, 100, 20, "guard.creepers"));
        if (this.scroll1 == null) {
            this.scroll1 = new GuiCustomScrollNop(this, 0);
            this.scroll1.setSize(175, 154);
        }
        this.scroll1.guiLeft = this.guiLeft + 4;
        this.scroll1.guiTop = this.guiTop + 58;
        this.addScroll(this.scroll1);
        this.addLabel(new GuiLabel(11, "guard.availableTargets", this.guiLeft + 4, this.guiTop + 48));
        if (this.scroll2 == null) {
            this.scroll2 = new GuiCustomScrollNop(this, 1);
            this.scroll2.setSize(175, 154);
        }
        this.scroll2.guiLeft = this.guiLeft + 235;
        this.scroll2.guiTop = this.guiTop + 58;
        this.addScroll(this.scroll2);
        this.addLabel(new GuiLabel(12, "guard.currentTargets", this.guiLeft + 235, this.guiTop + 48));
        this.scroll1.setList(new ArrayList(EntityUtil.getAllEntities(this.npc.m_9236_(), false).keySet()));
        this.scroll2.setList(this.role.targets);
        this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 180, this.guiTop + 80, 55, 20, ">"));
        this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 180, this.guiTop + 102, 55, 20, "<"));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 180, this.guiTop + 130, 55, 20, ">>"));
        this.addButton(new GuiButtonNop(this, 14, this.guiLeft + 180, this.guiTop + 152, 55, 20, "<<"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            for (Entry<EntityType<? extends Entity>, Class> entry : EntityUtil.getAllEntitiesClasses(this.f_96541_.level).entrySet()) {
                EntityType<?> ent = (EntityType<?>) entry.getKey();
                Class<? extends Entity> cl = (Class<? extends Entity>) entry.getValue();
                String name = ent.getDescriptionId();
                if (Animal.class.isAssignableFrom(cl) && !this.role.targets.contains(name)) {
                    this.role.targets.add(name);
                }
            }
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 1) {
            for (Entry<EntityType<? extends Entity>, Class> entryx : EntityUtil.getAllEntitiesClasses(this.f_96541_.level).entrySet()) {
                EntityType<?> ent = (EntityType<?>) entryx.getKey();
                Class<? extends Entity> cl = (Class<? extends Entity>) entryx.getValue();
                String name = ent.getDescriptionId();
                if (Monster.class.isAssignableFrom(cl) && !Creeper.class.isAssignableFrom(cl) && !this.role.targets.contains(name)) {
                    this.role.targets.add(name);
                }
            }
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 2) {
            for (Entry<EntityType<? extends Entity>, Class> entryxx : EntityUtil.getAllEntitiesClasses(this.f_96541_.level).entrySet()) {
                EntityType<?> ent = (EntityType<?>) entryxx.getKey();
                Class<? extends Entity> cl = (Class<? extends Entity>) entryxx.getValue();
                String name = ent.getDescriptionId();
                if (Creeper.class.isAssignableFrom(cl) && !this.role.targets.contains(name)) {
                    this.role.targets.add(name);
                }
            }
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 11 && this.scroll1.hasSelected()) {
            this.role.targets.add(this.scroll1.getSelected());
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 12 && this.scroll2.hasSelected()) {
            this.role.targets.remove(this.scroll2.getSelected());
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 13) {
            this.role.targets = new ArrayList(EntityUtil.getAllEntities(this.npc.m_9236_(), false).keySet());
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 14) {
            this.role.targets.clear();
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketNpcJobSave(this.role.save(new CompoundTag())));
    }
}