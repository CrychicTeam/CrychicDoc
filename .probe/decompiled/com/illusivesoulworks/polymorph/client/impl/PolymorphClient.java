package com.illusivesoulworks.polymorph.client.impl;

import com.illusivesoulworks.polymorph.api.client.base.IPolymorphClient;
import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.client.recipe.widget.FurnaceRecipesWidget;
import com.illusivesoulworks.polymorph.client.recipe.widget.PlayerRecipesWidget;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmithingMenu;

public class PolymorphClient implements IPolymorphClient {

    private static final IPolymorphClient INSTANCE = new PolymorphClient();

    private final List<IPolymorphClient.IRecipesWidgetFactory> widgetFactories = new LinkedList();

    public static IPolymorphClient get() {
        return INSTANCE;
    }

    public static void setup() {
        get().registerWidget(containerScreen -> {
            AbstractContainerMenu container = containerScreen.getMenu();
            if (container instanceof SmithingMenu) {
                return new PlayerRecipesWidget(containerScreen, container.slots.get(3));
            } else {
                return container instanceof AbstractFurnaceMenu ? new FurnaceRecipesWidget(containerScreen) : null;
            }
        });
    }

    @Override
    public Optional<IRecipesWidget> getWidget(AbstractContainerScreen<?> pContainerScreen) {
        for (IPolymorphClient.IRecipesWidgetFactory factory : this.widgetFactories) {
            IRecipesWidget widget = factory.createWidget(pContainerScreen);
            if (widget != null) {
                return Optional.of(widget);
            }
        }
        return Optional.empty();
    }

    @Override
    public void registerWidget(IPolymorphClient.IRecipesWidgetFactory pFactory) {
        this.widgetFactories.add(pFactory);
    }

    @Override
    public Optional<Slot> findCraftingResultSlot(AbstractContainerScreen<?> pContainerScreen) {
        AbstractContainerMenu container = pContainerScreen.getMenu();
        for (Slot slot : container.slots) {
            if (slot.container instanceof ResultContainer) {
                return Optional.of(slot);
            }
        }
        return Optional.empty();
    }
}