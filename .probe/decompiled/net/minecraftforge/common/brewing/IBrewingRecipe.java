package net.minecraftforge.common.brewing;

import net.minecraft.world.item.ItemStack;

public interface IBrewingRecipe {

    boolean isInput(ItemStack var1);

    boolean isIngredient(ItemStack var1);

    ItemStack getOutput(ItemStack var1, ItemStack var2);
}