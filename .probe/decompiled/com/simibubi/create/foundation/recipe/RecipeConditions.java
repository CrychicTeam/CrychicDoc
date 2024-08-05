package com.simibubi.create.foundation.recipe;

import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeConditions {

    public static Predicate<Recipe<?>> isOfType(RecipeType<?>... otherTypes) {
        return recipe -> {
            RecipeType<?> recipeType = recipe.getType();
            for (RecipeType<?> other : otherTypes) {
                if (recipeType == other) {
                    return true;
                }
            }
            return false;
        };
    }

    public static Predicate<Recipe<?>> firstIngredientMatches(ItemStack stack) {
        return r -> !r.getIngredients().isEmpty() && r.getIngredients().get(0).test(stack);
    }

    public static Predicate<Recipe<?>> outputMatchesFilter(FilteringBehaviour filtering) {
        return r -> filtering.test(r.getResultItem(filtering.getWorld().registryAccess()));
    }
}