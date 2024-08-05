package de.keksuccino.fancymenu.util.rendering.ui.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.Nullable;

public interface UniqueWidget {

    AbstractWidget setWidgetIdentifierFancyMenu(@Nullable String var1);

    String getWidgetIdentifierFancyMenu();
}