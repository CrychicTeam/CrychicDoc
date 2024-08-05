package com.mna.items.filters;

import com.mna.items.ItemInit;
import net.minecraft.world.item.ItemStack;

public class SeerStoneItems extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() == ItemInit.PLAYER_CHARM.get() || stack.getItem() == ItemInit.CRYSTAL_PHYLACTERY.get();
    }
}