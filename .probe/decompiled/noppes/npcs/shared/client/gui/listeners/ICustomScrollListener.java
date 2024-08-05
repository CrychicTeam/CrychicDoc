package noppes.npcs.shared.client.gui.listeners;

import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;

public interface ICustomScrollListener {

    void scrollClicked(double var1, double var3, int var5, GuiCustomScrollNop var6);

    void scrollDoubleClicked(String var1, GuiCustomScrollNop var2);
}