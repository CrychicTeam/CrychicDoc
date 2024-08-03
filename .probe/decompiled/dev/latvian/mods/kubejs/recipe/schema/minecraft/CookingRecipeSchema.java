package dev.latvian.mods.kubejs.recipe.schema.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentWithParent;
import dev.latvian.mods.kubejs.recipe.component.TimeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface CookingRecipeSchema {

    RecipeComponent<OutputItem> PLATFORM_OUTPUT_ITEM = new RecipeComponentWithParent<OutputItem>() {

        @Override
        public RecipeComponent<OutputItem> parentComponent() {
            return ItemComponents.OUTPUT;
        }

        public JsonElement write(RecipeJS recipe, OutputItem value) {
            return (JsonElement) (Platform.isForge() ? ItemComponents.OUTPUT.write(recipe, value) : new JsonPrimitive(value.item.kjs$getId()));
        }

        public String toString() {
            return this.parentComponent().toString();
        }
    };

    RecipeKey<OutputItem> RESULT = PLATFORM_OUTPUT_ITEM.key("result");

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");

    RecipeKey<Float> XP = NumberComponent.FLOAT.key("experience").optional(0.0F).preferred("xp");

    RecipeKey<Long> COOKING_TIME = TimeComponent.TICKS.key("cookingtime").optional(200L).preferred("cookingTime");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT, XP, COOKING_TIME).uniqueOutputId(RESULT);
}