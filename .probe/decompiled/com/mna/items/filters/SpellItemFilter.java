package com.mna.items.filters;

import com.mna.api.spells.ICanContainSpell;
import com.mna.items.ItemInit;
import net.minecraft.world.item.ItemStack;

public class SpellItemFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        if (stack.getItem() instanceof ICanContainSpell inter && inter.canAcceptSpell(stack) && stack.getItem() != ItemInit.ENCHANTED_VELLUM.get()) {
            return true;
        }
        return false;
    }
}