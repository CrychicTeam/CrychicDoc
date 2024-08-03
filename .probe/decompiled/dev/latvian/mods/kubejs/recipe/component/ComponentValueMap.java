package dev.latvian.mods.kubejs.recipe.component;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import java.util.IdentityHashMap;

public class ComponentValueMap extends IdentityHashMap<RecipeKey<?>, Object> {

    public ComponentValueMap(int init) {
        super(init);
    }

    public <T> T getValue(RecipeJS recipe, RecipeKey<T> key) {
        Object o = this.get(key);
        if (o == null) {
            if (key.optional()) {
                return null;
            } else {
                throw new RecipeExceptionJS("Value for '" + key + "' is missing!");
            }
        } else {
            try {
                return key.component.read(recipe, o);
            } catch (Throwable var5) {
                throw new RecipeExceptionJS("Unable to cast '" + key + "' value '" + o + "' to '" + key.component.componentType() + "'!", var5);
            }
        }
    }
}