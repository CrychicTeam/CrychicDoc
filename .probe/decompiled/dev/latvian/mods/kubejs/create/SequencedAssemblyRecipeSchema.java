package dev.latvian.mods.kubejs.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NestedRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeOptional;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public interface SequencedAssemblyRecipeSchema {

    RecipeKey<OutputItem[]> RESULTS = ItemComponents.OUTPUT_ARRAY.key("results");

    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");

    RecipeKey<RecipeJS[]> SEQUENCE = NestedRecipeComponent.RECIPE_ARRAY.key("sequence");

    RecipeKey<OutputItem> TRANSITIONAL_ITEM = ItemComponents.OUTPUT.key("transitionalItem").optional((RecipeOptional<OutputItem>) (type -> OutputItem.of(((SequencedAssemblyItem) AllItems.INCOMPLETE_PRECISION_MECHANISM.get()).m_7968_(), 1.0)));

    RecipeKey<Integer> LOOPS = NumberComponent.INT.key("loops").optional(4);

    RecipeSchema SCHEMA = new RecipeSchema(SequencedAssemblyRecipeSchema.SequencedAssemblyRecipeJS.class, SequencedAssemblyRecipeSchema.SequencedAssemblyRecipeJS::new, RESULTS, INGREDIENT, SEQUENCE, TRANSITIONAL_ITEM, LOOPS);

    public static class SequencedAssemblyRecipeJS extends RecipeJS {

        @Override
        public void afterLoaded() {
            super.afterLoaded();
        }

        @Nullable
        @Override
        public Recipe<?> createRecipe() {
            return super.createRecipe();
        }
    }
}