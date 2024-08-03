package noppes.npcs.api.gui;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.gui.GuiComponentsScrollableWrapper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.data.DataStats;

public class MovementMenu extends MainMenuGui {

    public MovementMenu(EntityCustomNpc npc, IPlayer player) {
        super(6, npc, player);
        DataStats stats = npc.stats;
        GuiComponentsScrollableWrapper panel = this.gui.getScrollingPanel().init(6, 26, this.gui.getWidth() - 12, this.gui.getHeight() - 32);
        panel.addLabel(0, "stats.health", 0, 4, 100, 8);
        panel.addTextField(1, 104, 4, 320, 20).setCharacterType(1).setText(stats.maxHealth + "").setOnChange((gui, textfield) -> stats.setMaxHealth(textfield.getInteger()));
    }
}