package com.mna.gui.containers.slots;

import com.mna.items.filters.ItemFilterGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class RuneBagSlot extends BaseSlot {

    public RuneBagSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return ItemFilterGroup.ANY_STONE_RUNE.anyMatchesFilter(stack);
    }
}