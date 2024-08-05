package com.mna.recipes.spells;

import com.mna.recipes.ItemAndPatternRecipeSerializer;
import net.minecraft.resources.ResourceLocation;

public class ComponentRecipeSerializer extends ItemAndPatternRecipeSerializer<ComponentRecipe> {

    protected ComponentRecipe instantiate(ResourceLocation recipeId) {
        return new ComponentRecipe(recipeId);
    }
}