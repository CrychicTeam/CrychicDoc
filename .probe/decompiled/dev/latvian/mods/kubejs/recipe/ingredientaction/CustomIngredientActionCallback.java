package dev.latvian.mods.kubejs.recipe.ingredientaction;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface CustomIngredientActionCallback {

    ItemStack transform(ItemStack var1, int var2, InventoryKJS var3);
}