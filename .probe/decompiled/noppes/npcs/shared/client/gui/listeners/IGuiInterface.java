package noppes.npcs.shared.client.gui.listeners;

import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiWrapper;

public interface IGuiInterface {

    void buttonEvent(GuiButtonNop var1);

    void save();

    boolean hasSubGui();

    Screen getSubGui();

    int getWidth();

    int getHeight();

    Screen getParent();

    void elementClicked();

    void subGuiClosed(Screen var1);

    GuiWrapper getWrapper();

    void initGui();
}