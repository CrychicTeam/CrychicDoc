package noppes.npcs.api.gui;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.gui.GuiComponentsScrollableWrapper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.data.DataStats;

public class DeathMenu extends MainMenuGui {

    public DeathMenu(EntityCustomNpc npc, IPlayer player) {
        super(5, npc, player);
        DataStats stats = npc.stats;
        GuiComponentsScrollableWrapper panel = this.gui.getScrollingPanel().init(6, 26, this.gui.getWidth() - 12, this.gui.getHeight() - 32);
        int y = 0;
        panel.addLabel(0, "stats.respawn", 0, y, 100, 8);
        panel.addButtonList(1, 0, y + 9, 80, 20).setValues("gui.yes", "gui.day", "gui.night", "gui.no", "stats.naturally").setSelected(stats.spawnCycle).setOnPress((gui2, bb) -> {
            stats.spawnCycle = ((IButtonList) bb).getSelected();
            ILabel timeLabel = (ILabel) panel.getComponent(2);
            ITextField time = (ITextField) panel.getComponent(3);
            if (stats.spawnCycle != 3 && stats.spawnCycle != 4) {
                stats.respawnTime = 20;
                time.setMinMax(1, Integer.MAX_VALUE).setInteger(stats.respawnTime).setEnabled(true);
                timeLabel.setEnabled(true);
            } else {
                stats.respawnTime = 0;
                time.setEnabled(false);
                timeLabel.setEnabled(false);
            }
            this.gui.update(time);
        });
        panel.addLabel(2, "gui.time", 100, y, 100, 8);
        panel.addTextField(3, 100, y + 9, 80, 20).setCharacterType(1).setMinMax(1, Integer.MAX_VALUE).setInteger(stats.respawnTime).setOnChange((gui, textfield) -> stats.setRespawnTime(textfield.getInteger()));
        panel.addLabel(4, "stats.deadbody", 200, y, 100, 8);
        panel.addButtonList(5, 200, y + 9, 80, 20).setValues("gui.no", "gui.yes").setSelected(stats.hideKilledBody ? 1 : 0).setOnPress((gui2, bb) -> stats.setHideDeadBody(((IButtonList) bb).getSelected() == 1));
        y += 36;
        panel.addLabel(6, "stats.expdropped", 0, y, 100, 8);
        panel.addLabel(7, "gui.min", 0, y + 9, 100, 8);
        panel.addTextField(8, 0, y + 18, 80, 20).setCharacterType(1).setInteger(npc.inventory.getExpMin()).setOnChange((gui, textfield) -> npc.inventory.setExp(textfield.getInteger(), npc.inventory.getExpMax()));
        panel.addLabel(9, "gui.max", 100, y + 9, 100, 8);
        panel.addTextField(10, 100, y + 18, 80, 20).setCharacterType(1).setInteger(npc.inventory.getExpMax()).setOnChange((gui, textfield) -> npc.inventory.setExp(npc.inventory.getExpMin(), textfield.getInteger()));
        y += 46;
        panel.addLabel(11, "inv.lootpickup", 0, y, 100, 8);
        panel.addButtonList(12, 0, y + 9, 80, 20).setValues("stats.normal", "inv.auto").setSelected(npc.inventory.lootMode).setOnPress((gui2, bb) -> npc.inventory.lootMode = ((IButtonList) bb).getSelected());
    }
}