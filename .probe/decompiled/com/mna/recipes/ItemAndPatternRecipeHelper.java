package com.mna.recipes;

import com.mna.tools.ContainerTools;
import java.util.Collection;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ItemAndPatternRecipeHelper {

    public static <T extends ItemAndPatternRecipe> T GetRecipe(Level world, ResourceLocation rLoc, RecipeType<T> type) {
        List<T> iapList = world.getRecipeManager().getRecipesFor(type, ContainerTools.createTemporaryContainer(ItemStack.EMPTY), world);
        if (iapList != null && iapList.size() != 0) {
            for (T recipe : iapList) {
                if (recipe.m_6423_().compareTo(rLoc) == 0) {
                    return recipe;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public static <T extends ItemAndPatternRecipe> Collection<T> getAllRecipes(Level world, RecipeType<T> type) {
        return world.getRecipeManager().<CraftingContainer, T>getRecipesFor(type, ContainerTools.createTemporaryContainer(ItemStack.EMPTY), world);
    }
}