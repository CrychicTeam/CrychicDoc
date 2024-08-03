package com.mna.gui.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SlotNoPlace extends BaseSlot {

    public SlotNoPlace(IItemHandler craftResult, int index, int x, int y) {
        super(craftResult, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}