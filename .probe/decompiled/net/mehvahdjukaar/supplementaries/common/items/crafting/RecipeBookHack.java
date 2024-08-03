package net.mehvahdjukaar.supplementaries.common.items.crafting;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public class RecipeBookHack {

    private static final Map<ResourceLocation, Recipe<?>> SERVER_SPECIAL_RECIPE_VIEWS = new HashMap();

    public static void reloadServer() {
        SERVER_SPECIAL_RECIPE_VIEWS.clear();
        Map<RecipeBookCategories, List<List<Recipe<?>>>> map = createClientRecipes();
        map.values().forEach(h -> h.forEach(j -> j.forEach(k -> SERVER_SPECIAL_RECIPE_VIEWS.put(k.getId(), k))));
    }

    public static Map<RecipeBookCategories, List<List<Recipe<?>>>> createClientRecipes() {
        Map<RecipeBookCategories, List<List<Recipe<?>>>> map = new EnumMap(RecipeBookCategories.class);
        for (RecipeBookCategories c : RecipeBookCategories.values()) {
            SpecialRecipeDisplays.registerRecipes(c, v -> {
                List<List<Recipe<?>>> list = (List<List<Recipe<?>>>) map.computeIfAbsent(c, k -> new ArrayList());
                list.add(new ArrayList(v));
            });
        }
        return map;
    }

    @Nullable
    public static Recipe<?> getSpecialRecipe(ResourceLocation recipe) {
        return (Recipe<?>) SERVER_SPECIAL_RECIPE_VIEWS.get(recipe);
    }
}