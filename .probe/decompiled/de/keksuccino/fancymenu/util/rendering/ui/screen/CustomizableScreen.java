package de.keksuccino.fancymenu.util.rendering.ui.screen;

import java.util.List;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.NotNull;

public interface CustomizableScreen {

    @NotNull
    List<GuiEventListener> removeOnInitChildrenFancyMenu();
}