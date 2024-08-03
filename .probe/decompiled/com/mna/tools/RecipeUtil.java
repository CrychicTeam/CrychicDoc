package com.mna.tools;

import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecipeUtil {

    public static Optional<CraftingRecipe> lookupCraftingRecipe(Level world, ItemStack[] items) {
        NonNullList<ItemStack> invItems = NonNullList.create();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = j + i * 3;
                invItems.add(index >= items.length ? ItemStack.EMPTY : items[index]);
            }
        }
        CraftingContainer inventory = ContainerTools.createTemporaryContainer(3, 3, invItems);
        return world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, inventory, world);
    }
}