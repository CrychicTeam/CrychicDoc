package com.illusivesoulworks.polymorph.client.recipe.widget;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

public class FurnaceRecipesWidget extends PersistentRecipesWidget {

    private final Slot outputSlot;

    public FurnaceRecipesWidget(AbstractContainerScreen<?> containerScreen) {
        super(containerScreen);
        List<Slot> slots = containerScreen.getMenu().slots;
        this.outputSlot = (Slot) slots.get(2);
    }

    @Override
    public Slot getOutputSlot() {
        return this.outputSlot;
    }
}