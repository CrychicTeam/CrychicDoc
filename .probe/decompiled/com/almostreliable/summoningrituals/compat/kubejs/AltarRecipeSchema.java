package com.almostreliable.summoningrituals.compat.kubejs;

import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import com.almostreliable.summoningrituals.recipe.component.RecipeSacrifices;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeOptional;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.UtilsJS;

public interface AltarRecipeSchema {

    RecipeKey<InputItem> CATALYST = ItemComponents.INPUT.key("catalyst").noBuilders();

    RecipeKey<RecipeOutputs> OUTPUTS = SummoningComponents.OUTPUTS.key("outputs").noBuilders();

    RecipeKey<InputItem[]> INPUTS = ItemComponents.INPUT_ARRAY.key("inputs").defaultOptional();

    RecipeKey<RecipeSacrifices> SACRIFICES = SummoningComponents.SACRIFICES.key("sacrifices").optional((RecipeOptional<RecipeSacrifices>) (type -> new RecipeSacrifices())).noBuilders();

    RecipeKey<Integer> RECIPE_TIME = NumberComponent.ANY_INT.key("recipe_time").preferred(UtilsJS.snakeCaseToCamelCase("recipe_time")).optional(100);

    RecipeKey<BlockReference> BLOCK_BELOW = SummoningComponents.BLOCK_REFERENCE.key("block_below").defaultOptional();

    RecipeKey<AltarRecipe.DAY_TIME> DAY_TIME = new EnumComponent(AltarRecipe.DAY_TIME.class).key("day_time").preferred(UtilsJS.snakeCaseToCamelCase("day_time")).optional(AltarRecipe.DAY_TIME.ANY);

    RecipeKey<AltarRecipe.WEATHER> WEATHER = new EnumComponent(AltarRecipe.WEATHER.class).key("weather").preferred(UtilsJS.snakeCaseToCamelCase("weather")).optional(AltarRecipe.WEATHER.ANY);

    RecipeSchema SCHEMA = new RecipeSchema(AltarRecipeJS.class, AltarRecipeJS::new, CATALYST, OUTPUTS, INPUTS, SACRIFICES, BLOCK_BELOW, DAY_TIME, WEATHER, RECIPE_TIME).constructor((recipe, schemaType, keys, from) -> {
        recipe.setValue(CATALYST, from.getValue(recipe, CATALYST));
        recipe.setValue(OUTPUTS, new RecipeOutputs());
        recipe.setValue(SACRIFICES, new RecipeSacrifices());
    }, CATALYST);
}