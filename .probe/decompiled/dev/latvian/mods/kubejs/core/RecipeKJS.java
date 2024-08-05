package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaType;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

@RemapPrefixForJS("kjs$")
public interface RecipeKJS {

    default String kjs$getGroup() {
        return ((Recipe) this).getGroup();
    }

    default void kjs$setGroup(String group) {
    }

    default ResourceLocation kjs$getOrCreateId() {
        return ((Recipe) this).getId();
    }

    default RecipeSchema kjs$getSchema() {
        ResourceLocation s = RegistryInfo.RECIPE_SERIALIZER.getId(((Recipe) this).getSerializer());
        return ((RecipeSchemaType) ((RecipeNamespace) RecipeNamespace.getAll().get(s.getNamespace())).get(s.getPath())).schema;
    }

    default String kjs$getMod() {
        return this.kjs$getOrCreateId().getNamespace();
    }

    default ResourceLocation kjs$getType() {
        return RegistryInfo.RECIPE_SERIALIZER.getId(((Recipe) this).getSerializer());
    }

    default boolean hasInput(ReplacementMatch match) {
        if (match instanceof ItemMatch m) {
            for (Ingredient in : ((Recipe) this).getIngredients()) {
                if (m.contains(in)) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean replaceInput(ReplacementMatch match, InputReplacement with) {
        return false;
    }

    default boolean hasOutput(ReplacementMatch match) {
        if (!(match instanceof ItemMatch m)) {
            return false;
        } else {
            ItemStack result = ((Recipe) this).getResultItem(UtilsJS.staticRegistryAccess);
            return result != null && result != ItemStack.EMPTY && !result.isEmpty() && m.contains(result);
        }
    }

    default boolean replaceOutput(ReplacementMatch match, OutputReplacement with) {
        return false;
    }
}