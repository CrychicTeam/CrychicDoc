package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

public class NestedRecipeComponent implements RecipeComponent<RecipeJS> {

    public static final RecipeComponent<RecipeJS> RECIPE = new NestedRecipeComponent();

    public static final RecipeComponent<RecipeJS[]> RECIPE_ARRAY = RECIPE.asArray();

    @Override
    public Class<?> componentClass() {
        return RecipeJS.class;
    }

    public JsonElement write(RecipeJS recipe, RecipeJS value) {
        value.serialize();
        value.json.addProperty("type", value.type.idString);
        return value.json;
    }

    public RecipeJS read(RecipeJS recipe, Object from) {
        if (from instanceof RecipeJS r) {
            r.newRecipe = false;
            return r;
        } else {
            if (from instanceof JsonObject json && json.has("type")) {
                RecipeJS r = recipe.type.event.custom(json);
                r.newRecipe = false;
                return r;
            }
            throw new IllegalArgumentException("Can't parse recipe from " + from);
        }
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        if (from instanceof RecipeJS) {
            return true;
        } else {
            if (from instanceof JsonObject json && json.has("type")) {
                return true;
            }
            return false;
        }
    }
}