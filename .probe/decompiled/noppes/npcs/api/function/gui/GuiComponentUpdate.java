package noppes.npcs.api.function.gui;

import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.ICustomGuiComponent;

@FunctionalInterface
public interface GuiComponentUpdate<T extends ICustomGuiComponent> {

    void onChange(ICustomGui var1, T var2);
}