package pie.ilikepiefoo.compat.jei.events;

import dev.latvian.mods.kubejs.event.EventJS;
import java.util.Map;
import java.util.TreeMap;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import pie.ilikepiefoo.compat.jei.impl.CustomJSRecipe;

public class JEIEventJS extends EventJS {

    public static final Map<ResourceLocation, RecipeType<CustomJSRecipe>> customRecipeTypes = new TreeMap();

    public static final Map<ResourceLocation, RecipeType> overriddenRecipeTypes = new TreeMap();

    public static IJeiHelpers JEI_HELPERS;

    public static void clearCustomRecipeTypes() {
        customRecipeTypes.clear();
    }

    public static void clearOverriddenRecipeTypes() {
        overriddenRecipeTypes.clear();
    }

    public static void removeCustomRecipeType(ResourceLocation recipeType) {
        customRecipeTypes.remove(recipeType);
    }

    public static void removeOverriddenRecipeType(ResourceLocation recipeType) {
        overriddenRecipeTypes.remove(recipeType);
    }

    public static RecipeType<CustomJSRecipe> getCustomRecipeType(ResourceLocation recipeType) {
        return (RecipeType<CustomJSRecipe>) customRecipeTypes.get(recipeType);
    }

    public static RecipeType<?> getOverriddenRecipeType(ResourceLocation recipeType) {
        return (RecipeType<?>) overriddenRecipeTypes.get(recipeType);
    }

    public static RecipeType<CustomJSRecipe> getOrCreateCustomRecipeType(ResourceLocation recipeType) {
        customRecipeTypes.computeIfAbsent(recipeType, key -> RecipeType.create(key.getNamespace(), key.getPath(), CustomJSRecipe.class));
        return (RecipeType<CustomJSRecipe>) customRecipeTypes.get(recipeType);
    }

    public static <T> RecipeType<T> getOrCreateCustomOverriddenRecipeType(ResourceLocation recipeType, RecipeType<T> existingRecipeType) {
        overriddenRecipeTypes.computeIfAbsent(recipeType, key -> RecipeType.create(key.getNamespace(), key.getPath(), existingRecipeType.getRecipeClass()));
        return (RecipeType<T>) overriddenRecipeTypes.get(recipeType);
    }
}