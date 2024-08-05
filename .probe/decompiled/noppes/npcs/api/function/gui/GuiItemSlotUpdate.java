package noppes.npcs.api.function.gui;

import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.IItemSlot;

@FunctionalInterface
public interface GuiItemSlotUpdate {

    void onUpdate(ICustomGui var1, IItemSlot var2);
}