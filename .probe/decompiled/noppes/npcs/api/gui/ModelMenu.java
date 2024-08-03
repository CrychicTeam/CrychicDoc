package noppes.npcs.api.gui;

import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.entity.EntityCustomNpc;

public class ModelMenu extends MainMenuGui {

    public ModelMenu(EntityCustomNpc npc, IPlayer player) {
        super(1, npc, player);
        this.gui.getScrollingPanel().init(180, 26, 230, this.gui.getHeight() - 32);
    }
}