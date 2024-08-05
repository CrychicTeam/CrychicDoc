package com.mna.recipes.spells;

import com.mna.recipes.ItemAndPatternRecipeSerializer;
import net.minecraft.resources.ResourceLocation;

public class ShapeRecipeSerializer extends ItemAndPatternRecipeSerializer<ShapeRecipe> {

    protected ShapeRecipe instantiate(ResourceLocation recipeId) {
        return new ShapeRecipe(recipeId);
    }
}