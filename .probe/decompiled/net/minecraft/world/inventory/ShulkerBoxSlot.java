package net.minecraft.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ShulkerBoxSlot extends Slot {

    public ShulkerBoxSlot(Container container0, int int1, int int2, int int3) {
        super(container0, int1, int2, int3);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack0) {
        return itemStack0.getItem().canFitInsideContainerItems();
    }
}