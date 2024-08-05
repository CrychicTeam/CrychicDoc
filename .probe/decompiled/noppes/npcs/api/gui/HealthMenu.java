package noppes.npcs.api.gui;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.gui.GuiComponentsScrollableWrapper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.data.DataStats;

public class HealthMenu extends MainMenuGui {

    public HealthMenu(EntityCustomNpc npc, IPlayer player) {
        super(4, npc, player);
        DataStats stats = npc.stats;
        GuiComponentsScrollableWrapper panel = this.gui.getScrollingPanel().init(6, 26, this.gui.getWidth() - 12, this.gui.getHeight() - 32);
        int y = 0;
        panel.addLabel(0, "stats.health", 0, y, 100, 8);
        panel.addTextField(1, 0, y + 9, 80, 20).setCharacterType(1).setText(stats.maxHealth + "").setOnChange((gui, textfield) -> {
            stats.setMaxHealth(textfield.getInteger());
            npc.m_5634_((float) stats.maxHealth);
        });
        panel.addLabel(4, "stats.regenhealth", 100, y, 100, 8);
        panel.addTextField(5, 100, y + 9, 80, 20).setCharacterType(1).setText(stats.healthRegen + "").setOnChange((gui, textfield) -> stats.setHealthRegen(textfield.getInteger()));
        panel.addLabel(6, "stats.combatregen", 200, y, 100, 8);
        panel.addTextField(7, 200, y + 9, 80, 20).setCharacterType(1).setText(stats.combatRegen + "").setOnChange((gui, textfield) -> stats.setCombatRegen(textfield.getInteger()));
        y += 34;
        panel.addLabel(2, "stats.creaturetype", 0, y, 100, 8);
        panel.addButtonList(3, 0, y + 9, 80, 20).setValues("stats.normal", "stats.undead", "stats.arthropod").setSelected(stats.getCreatureType()).setOnPress((gui2, bb) -> stats.setCreatureType(((IButtonList) bb).getSelected()));
        y += 34;
        panel.addLabel(8, "stats.fireimmune", 0, y, 100, 8);
        panel.addButtonList(9, 0, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(npc.stats.immuneToFire ? 1 : 0).setOnPress((gui2, bb) -> stats.immuneToFire = ((IButtonList) bb).getSelected() == 1);
        panel.addLabel(10, "stats.candrown", 100, y, 100, 8);
        panel.addButtonList(11, 100, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(npc.stats.canDrown ? 1 : 0).setOnPress((gui2, bb) -> stats.canDrown = ((IButtonList) bb).getSelected() == 1);
        panel.addLabel(12, "stats.burninsun", 200, y, 100, 8);
        panel.addButtonList(13, 200, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(npc.stats.burnInSun ? 1 : 0).setOnPress((gui2, bb) -> stats.burnInSun = ((IButtonList) bb).getSelected() == 1);
        y += 34;
        panel.addLabel(14, "stats.nofalldamage", 0, y, 100, 8);
        panel.addButtonList(15, 0, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(npc.stats.noFallDamage ? 1 : 0).setOnPress((gui2, bb) -> stats.noFallDamage = ((IButtonList) bb).getSelected() == 1);
        panel.addLabel(16, "stats.potionImmune", 100, y, 100, 8);
        panel.addButtonList(17, 100, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(npc.stats.potionImmune ? 1 : 0).setOnPress((gui2, bb) -> stats.potionImmune = ((IButtonList) bb).getSelected() == 1);
        panel.addLabel(18, "ai.cobwebAffected", 200, y, 100, 8);
        panel.addButtonList(19, 200, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(npc.stats.ignoreCobweb ? 0 : 1).setOnPress((gui2, bb) -> stats.ignoreCobweb = ((IButtonList) bb).getSelected() == 0);
        y += 40;
        panel.addLabel(20, "stats.resistances", 0, y, 100, 8);
        y += 9;
        panel.addLabel(21, "enchantment.minecraft.knockback", 0, y, 100, 8);
        panel.addSlider(22, 0, y + 9, 150, 18, "%s %%").setMin(-100.0F).setValue((npc.stats.resistances.knockback - 1.0F) * 100.0F).setOnChange((gui, slider) -> npc.stats.resistances.knockback = slider.getValue() / 100.0F + 1.0F);
        panel.addLabel(23, "stats.explosion", 160, y, 100, 8);
        panel.addSlider(24, 160, y + 9, 150, 18, "%s %%").setMin(-100.0F).setValue((npc.stats.resistances.explosion - 1.0F) * 100.0F).setOnChange((gui, slider) -> npc.stats.resistances.explosion = slider.getValue() / 100.0F + 1.0F);
        y += 34;
        panel.addLabel(25, "stats.melee", 0, y, 100, 8);
        panel.addSlider(26, 0, y + 9, 150, 18, "%s %%").setMin(-100.0F).setValue((npc.stats.resistances.melee - 1.0F) * 100.0F).setOnChange((gui, slider) -> npc.stats.resistances.melee = slider.getValue() / 100.0F + 1.0F);
        panel.addLabel(27, "item.minecraft.arrow", 160, y, 100, 8);
        panel.addSlider(28, 160, y + 9, 150, 18, "%s %%").setMin(-100.0F).setValue((npc.stats.resistances.arrow - 1.0F) * 100.0F).setOnChange((gui, slider) -> npc.stats.resistances.arrow = slider.getValue() / 100.0F + 1.0F);
    }
}