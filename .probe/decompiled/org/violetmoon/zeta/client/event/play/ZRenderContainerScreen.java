package org.violetmoon.zeta.client.event.play;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderContainerScreen extends IZetaPlayEvent {

    AbstractContainerScreen<?> getContainerScreen();

    GuiGraphics getGuiGraphics();

    int getMouseX();

    int getMouseY();

    public interface Background extends ZRenderContainerScreen {
    }

    public interface Foreground extends ZRenderContainerScreen {
    }
}