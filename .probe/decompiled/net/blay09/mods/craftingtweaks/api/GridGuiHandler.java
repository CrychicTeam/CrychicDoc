package net.blay09.mods.craftingtweaks.api;

import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface GridGuiHandler {

    void createButtons(AbstractContainerScreen<?> var1, CraftingGrid var2, Consumer<AbstractWidget> var3);
}