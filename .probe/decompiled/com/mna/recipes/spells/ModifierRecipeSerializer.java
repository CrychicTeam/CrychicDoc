package com.mna.recipes.spells;

import com.mna.recipes.ItemAndPatternRecipeSerializer;
import net.minecraft.resources.ResourceLocation;

public class ModifierRecipeSerializer extends ItemAndPatternRecipeSerializer<ModifierRecipe> {

    protected ModifierRecipe instantiate(ResourceLocation recipeId) {
        return new ModifierRecipe(recipeId);
    }
}