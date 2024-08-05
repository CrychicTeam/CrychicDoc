package dev.latvian.mods.kubejs.recipe.schema.minecraft;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface ShapelessRecipeSchema {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");

    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.UNWRAPPED_INPUT_ARRAY.key("ingredients");

    RecipeSchema SCHEMA = new RecipeSchema(ShapelessRecipeSchema.ShapelessRecipeJS.class, ShapelessRecipeSchema.ShapelessRecipeJS::new, RESULT, INGREDIENTS).uniqueOutputId(RESULT);

    public static class ShapelessRecipeJS extends RecipeJS {

        @Override
        public RecipeTypeFunction getSerializationTypeFunction() {
            return this.type == this.type.event.shapeless && this.type.event.shapeless != this.type.event.vanillaShapeless && !this.json.has("kubejs:actions") && !this.json.has("kubejs:modify_result") && !this.json.has("kubejs:stage") ? this.type.event.vanillaShapeless : super.getSerializationTypeFunction();
        }
    }
}