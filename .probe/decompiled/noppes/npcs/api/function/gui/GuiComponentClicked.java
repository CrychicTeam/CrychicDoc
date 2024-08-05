package noppes.npcs.api.function.gui;

import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.ICustomGuiComponent;

@FunctionalInterface
public interface GuiComponentClicked<T extends ICustomGuiComponent> {

    void onClick(ICustomGui var1, T var2);
}