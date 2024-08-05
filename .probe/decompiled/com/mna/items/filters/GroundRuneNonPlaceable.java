package com.mna.items.filters;

import com.mna.items.ItemInit;
import com.mna.items.ritual.ItemPractitionersPouch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GroundRuneNonPlaceable extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        Item item = stack.getItem();
        return item == ItemInit.WIZARD_CHALK.get() || item == ItemInit.PURIFIED_VINTEUM_DUST.get() || item instanceof ItemPractitionersPouch;
    }
}