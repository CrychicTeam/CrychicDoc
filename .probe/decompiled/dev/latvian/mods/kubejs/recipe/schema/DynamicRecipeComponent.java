package dev.latvian.mods.kubejs.recipe.schema;

import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public record DynamicRecipeComponent(TypeDescJS desc, DynamicRecipeComponent.Factory factory) {

    public interface Factory {

        @Nullable
        RecipeComponent<?> create(Context var1, Scriptable var2, Map<String, Object> var3);
    }
}