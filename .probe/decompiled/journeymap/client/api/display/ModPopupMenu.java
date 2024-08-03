package journeymap.client.api.display;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;

public interface ModPopupMenu {

    ModPopupMenu addMenuItem(String var1, ModPopupMenu.Action var2);

    ModPopupMenu addMenuItemScreen(String var1, Screen var2);

    ModPopupMenu createSubItemList(String var1);

    public interface Action {

        void doAction(BlockPos var1);
    }
}