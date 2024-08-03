package de.keksuccino.fancymenu.util.rendering.ui.widget;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public interface UniqueLabeledSwitchCycleButton {

    void setLabeledSwitchComponentLabel_FancyMenu(@Nullable Component var1);

    @Nullable
    Component getLabeledSwitchComponentLabel_FancyMenu();
}