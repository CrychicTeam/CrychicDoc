package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValueFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.Scriptable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RecipeFunction extends NativeJavaObject {

    public final RecipeJS recipe;

    public final Map<String, RecipeComponentValueFunction> builderFunctions;

    public RecipeFunction(Context cx, Scriptable scope, Class<?> staticType, RecipeJS recipe) {
        super(scope, recipe, staticType, cx);
        this.recipe = recipe;
        Map<String, RecipeComponentValue<?>> map = recipe.getAllValueMap();
        this.builderFunctions = new HashMap(map.size());
        for (Entry<String, RecipeComponentValue<?>> entry : map.entrySet()) {
            String key = (String) entry.getKey();
            RecipeComponentValue<?> value = (RecipeComponentValue<?>) entry.getValue();
            if (!value.key.noBuilders) {
                this.builderFunctions.put(key, new RecipeComponentValueFunction(recipe, value));
            }
        }
    }

    @Override
    public Object get(Context cx, String name, Scriptable start) {
        if (this.recipe instanceof ErroredRecipeJS errored) {
            return errored.dummyFunction;
        } else {
            Object s = super.get(cx, name, start);
            if (s == Scriptable.NOT_FOUND) {
                RecipeComponentValueFunction r = (RecipeComponentValueFunction) this.builderFunctions.get(name);
                if (r != null) {
                    return r;
                }
            }
            return s;
        }
    }
}