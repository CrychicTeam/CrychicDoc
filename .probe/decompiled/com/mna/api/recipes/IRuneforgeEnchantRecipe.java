package com.mna.api.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public interface IRuneforgeEnchantRecipe extends IMARecipe {

    ItemStack getRune();

    Enchantment getEnchant();

    int getEnchantMagnitude();
}