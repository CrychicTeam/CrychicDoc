package noppes.npcs.client.gui.mainmenu;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.SubGuiNpcMeleeProperties;
import noppes.npcs.client.gui.SubGuiNpcProjectiles;
import noppes.npcs.client.gui.SubGuiNpcRangeProperties;
import noppes.npcs.client.gui.SubGuiNpcResistanceProperties;
import noppes.npcs.client.gui.SubGuiNpcRespawn;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataStats;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuGet;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcStats extends GuiNPCInterface2 implements ITextfieldListener, IGuiData {

    private DataStats stats;

    public GuiNpcStats(EntityNPCInterface npc) {
        super(npc, 2);
        this.stats = npc.stats;
        Packets.sendServer(new SPacketMenuGet(EnumMenuType.STATS));
    }

    @Override
    public void init() {
        super.init();
        int y = this.guiTop + 10;
        this.addLabel(new GuiLabel(0, "stats.health", this.guiLeft + 5, y + 5));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 85, y, 50, 18, this.stats.maxHealth + ""));
        this.getTextField(0).numbersOnly = true;
        this.getTextField(0).setMinMaxDefault(0, Integer.MAX_VALUE, 20);
        this.addLabel(new GuiLabel(1, "stats.aggro", this.guiLeft + 140, y + 5));
        this.addTextField(new GuiTextFieldNop(1, this, this.guiLeft + 220, y, 50, 18, this.stats.aggroRange + ""));
        this.getTextField(1).numbersOnly = true;
        this.getTextField(1).setMinMaxDefault(1, 64, 2);
        this.addLabel(new GuiLabel(34, "stats.creaturetype", this.guiLeft + 275, y + 5));
        this.addButton(new GuiButtonNop(this, 8, this.guiLeft + 355, y, 56, 20, new String[] { "stats.normal", "stats.undead", "stats.arthropod" }, this.stats.getCreatureType()));
        int var10005 = this.guiLeft + 82;
        y += 22;
        this.addButton(new GuiButtonNop(this, 0, var10005, y, 56, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(2, "stats.respawn", this.guiLeft + 5, y + 5));
        var10005 = this.guiLeft + 82;
        y += 22;
        this.addButton(new GuiButtonNop(this, 2, var10005, y, 56, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(5, "stats.meleeproperties", this.guiLeft + 5, y + 5));
        var10005 = this.guiLeft + 82;
        y += 22;
        this.addButton(new GuiButtonNop(this, 3, var10005, y, 56, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(6, "stats.rangedproperties", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 9, this.guiLeft + 217, y, 56, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(7, "stats.projectileproperties", this.guiLeft + 140, y + 5));
        var10005 = this.guiLeft + 82;
        y += 34;
        this.addButton(new GuiButtonNop(this, 15, var10005, y, 56, 20, "selectServer.edit"));
        this.addLabel(new GuiLabel(15, "effect.minecraft.resistance", this.guiLeft + 5, y + 5));
        var10005 = this.guiLeft + 82;
        y += 34;
        this.addButton(new GuiButtonNop(this, 4, var10005, y, 56, 20, new String[] { "gui.no", "gui.yes" }, this.npc.fireImmune() ? 1 : 0));
        this.addLabel(new GuiLabel(10, "stats.fireimmune", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 217, y, 56, 20, new String[] { "gui.no", "gui.yes" }, this.stats.canDrown ? 1 : 0));
        this.addLabel(new GuiLabel(11, "stats.candrown", this.guiLeft + 140, y + 5));
        this.addTextField(new GuiTextFieldNop(14, this, this.guiLeft + 355, y, 56, 20, this.stats.healthRegen + "").setNumbersOnly());
        this.addLabel(new GuiLabel(14, "stats.regenhealth", this.guiLeft + 275, y + 5));
        var10005 = this.guiLeft + 355;
        y += 22;
        this.addTextField(new GuiTextFieldNop(16, this, var10005, y, 56, 20, this.stats.combatRegen + "").setNumbersOnly());
        this.addLabel(new GuiLabel(16, "stats.combatregen", this.guiLeft + 275, y + 5));
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 82, y, 56, 20, new String[] { "gui.no", "gui.yes" }, this.stats.burnInSun ? 1 : 0));
        this.addLabel(new GuiLabel(12, "stats.burninsun", this.guiLeft + 5, y + 5));
        this.addButton(new GuiButtonNop(this, 7, this.guiLeft + 217, y, 56, 20, new String[] { "gui.no", "gui.yes" }, this.stats.noFallDamage ? 1 : 0));
        this.addLabel(new GuiLabel(13, "stats.nofalldamage", this.guiLeft + 140, y + 5));
        var10005 = this.guiLeft + 82;
        y += 22;
        this.addButton(new GuiButtonYesNo(this, 17, var10005, y, 56, 20, this.stats.potionImmune));
        this.addLabel(new GuiLabel(17, "stats.potionImmune", this.guiLeft + 5, y + 5));
        this.addLabel(new GuiLabel(22, "ai.cobwebAffected", this.guiLeft + 140, y + 5));
        this.addButton(new GuiButtonNop(this, 22, this.guiLeft + 217, y, 56, 20, new String[] { "gui.no", "gui.yes" }, this.stats.ignoreCobweb ? 0 : 1));
    }

    @Override
    public void unFocused(GuiTextFieldNop textfield) {
        if (textfield.id == 0) {
            this.stats.maxHealth = textfield.getInteger();
            this.npc.m_5634_((float) this.stats.maxHealth);
        } else if (textfield.id == 1) {
            this.stats.aggroRange = textfield.getInteger();
        } else if (textfield.id == 14) {
            this.stats.healthRegen = textfield.getInteger();
        } else if (textfield.id == 16) {
            this.stats.combatRegen = textfield.getInteger();
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.setSubGui(new SubGuiNpcRespawn(this.stats));
        } else if (guibutton.id == 2) {
            this.setSubGui(new SubGuiNpcMeleeProperties(this.stats.melee));
        } else if (guibutton.id == 3) {
            this.setSubGui(new SubGuiNpcRangeProperties(this.stats));
        } else if (guibutton.id == 4) {
            this.npc.setImmuneToFire(guibutton.getValue() == 1);
        } else if (guibutton.id == 5) {
            this.stats.canDrown = guibutton.getValue() == 1;
        } else if (guibutton.id == 6) {
            this.stats.burnInSun = guibutton.getValue() == 1;
        } else if (guibutton.id == 7) {
            this.stats.noFallDamage = guibutton.getValue() == 1;
        } else if (guibutton.id == 8) {
            this.stats.setCreatureType(guibutton.getValue());
        } else if (guibutton.id == 9) {
            this.setSubGui(new SubGuiNpcProjectiles(this.stats.ranged));
        } else if (guibutton.id == 15) {
            this.setSubGui(new SubGuiNpcResistanceProperties(this.stats.resistances));
        } else if (guibutton.id == 17) {
            this.stats.potionImmune = ((GuiButtonYesNo) guibutton).getBoolean();
        } else if (guibutton.id == 22) {
            this.stats.ignoreCobweb = guibutton.getValue() == 0;
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.STATS, this.stats.save(new CompoundTag())));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.stats.readToNBT(compound);
        this.init();
    }
}