package com.simibubi.create.content.kinetics.mixer;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

public class MixingRecipe extends BasinRecipe {

    public MixingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.MIXING, params);
    }
}