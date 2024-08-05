package com.mna.items.filters;

import com.mna.api.tools.MATags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class EnchantedRuneFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return MATags.isItemIn(stack.getItem(), MATags.Items.RUNES) && EnchantmentHelper.getEnchantments(stack).size() > 0;
    }
}