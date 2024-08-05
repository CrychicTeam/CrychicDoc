package com.illusivesoulworks.polymorph.api.client.base;

import java.util.Optional;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

public interface IPolymorphClient {

    Optional<IRecipesWidget> getWidget(AbstractContainerScreen<?> var1);

    void registerWidget(IPolymorphClient.IRecipesWidgetFactory var1);

    Optional<Slot> findCraftingResultSlot(AbstractContainerScreen<?> var1);

    public interface IRecipesWidgetFactory {

        IRecipesWidget createWidget(AbstractContainerScreen<?> var1);
    }
}