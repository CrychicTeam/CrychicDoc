package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcJobSave;
import noppes.npcs.roles.JobHealer;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;

public class GuiNpcHealer extends GuiNPCInterface2 {

    private JobHealer job;

    private GuiCustomScrollNop scroll1;

    private GuiCustomScrollNop scroll2;

    private HashMap<String, Integer> potions;

    private HashMap<String, String> displays;

    private int potency = 0;

    public GuiNpcHealer(EntityNPCInterface npc) {
        super(npc);
        this.job = (JobHealer) npc.job;
        this.potions = new HashMap();
        this.displays = new HashMap();
        Registry<MobEffect> r = BuiltInRegistries.MOB_EFFECT;
        for (ResourceLocation rl : r.keySet()) {
            this.potions.put(rl.toString(), r.getId(r.get(rl)));
        }
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(1, "beacon.range", this.guiLeft + 10, this.guiTop + 9));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 80, this.guiTop + 4, 40, 20, this.job.range + ""));
        this.getTextField(1).numbersOnly = true;
        this.getTextField(1).setMinMaxDefault(1, 64, 16);
        this.addLabel(new GuiLabel(4, "stats.speed", this.guiLeft + 140, this.guiTop + 9));
        this.addTextField(new GuiTextFieldNop(4, this, this.guiLeft + 220, this.guiTop + 4, 40, 20, this.job.speed + ""));
        this.getTextField(4).numbersOnly = true;
        this.getTextField(4).setMinMaxDefault(10, Integer.MAX_VALUE, 20);
        this.addLabel(new GuiLabel(3, "beacon.affect", this.guiLeft + 10, this.guiTop + 31));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 56, this.guiTop + 26, 80, 20, new String[] { "faction.friendly", "faction.unfriendly", "spawner.all" }, this.job.type));
        this.addLabel(new GuiLabel(2, "beacon.potency", this.guiLeft + 140, this.guiTop + 31));
        this.addTextField(new GuiTextFieldNop(2, this, this.guiLeft + 220, this.guiTop + 26, 40, 20, this.potency + ""));
        this.getTextField(2).numbersOnly = true;
        this.getTextField(2).setMinMaxDefault(0, 3, 0);
        if (this.scroll1 == null) {
            this.scroll1 = new GuiCustomScrollNop(this, 0);
            this.scroll1.setSize(175, 154);
        }
        this.scroll1.guiLeft = this.guiLeft + 4;
        this.scroll1.guiTop = this.guiTop + 58;
        this.addScroll(this.scroll1);
        this.addLabel(new GuiLabel(11, "beacon.availableEffects", this.guiLeft + 4, this.guiTop + 48));
        if (this.scroll2 == null) {
            this.scroll2 = new GuiCustomScrollNop(this, 1);
            this.scroll2.setSize(175, 154);
        }
        this.scroll2.guiLeft = this.guiLeft + 235;
        this.scroll2.guiTop = this.guiTop + 58;
        this.addScroll(this.scroll2);
        this.addLabel(new GuiLabel(12, "beacon.currentEffects", this.guiLeft + 235, this.guiTop + 48));
        List<String> all = new ArrayList();
        for (String names : this.potions.keySet()) {
            if (!this.job.effects.containsKey(this.potions.get(names))) {
                all.add(names);
            } else {
                this.displays.put(I18n.get(names) + " " + I18n.get("enchantment.level." + ((Integer) this.job.effects.get(this.potions.get(names)) + 1)), names);
            }
        }
        this.scroll1.setList(all);
        List<String> applied = new ArrayList(this.displays.keySet());
        this.scroll2.setList(applied);
        this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 180, this.guiTop + 80, 55, 20, ">"));
        this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 180, this.guiTop + 102, 55, 20, "<"));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 180, this.guiTop + 130, 55, 20, ">>"));
        this.addButton(new GuiButtonNop(this, 14, this.guiLeft + 180, this.guiTop + 152, 55, 20, "<<"));
    }

    @Override
    public void elementClicked() {
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 3) {
            this.job.type = (byte) guibutton.getValue();
        }
        if (guibutton.id == 11 && this.scroll1.hasSelected()) {
            this.job.effects.put((Integer) this.potions.get(this.scroll1.getSelected()), this.getTextField(2).getInteger());
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 12 && this.scroll2.hasSelected()) {
            this.job.effects.remove(this.potions.get(this.displays.remove(this.scroll2.getSelected())));
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 13) {
            this.job.effects.clear();
            new ArrayList();
            Registry<MobEffect> r = BuiltInRegistries.MOB_EFFECT;
            for (ResourceLocation rl : r.keySet()) {
                this.job.effects.put(r.getId(r.get(rl)), this.potency);
            }
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 14) {
            this.job.effects.clear();
            this.displays.clear();
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
    }

    @Override
    public void save() {
        this.job.range = this.getTextField(1).getInteger();
        this.potency = this.getTextField(2).getInteger();
        this.job.speed = this.getTextField(4).getInteger();
        Packets.sendServer(new SPacketNpcJobSave(this.job.save(new CompoundTag())));
    }
}