package dev.latvian.mods.kubejs.recipe.schema;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import dev.latvian.mods.kubejs.recipe.component.ComponentValueMap;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public record RecipeConstructor(RecipeSchema schema, RecipeKey<?>[] keys, RecipeConstructor.Factory factory) {

    public String toString() {
        return (String) Arrays.stream(this.keys).map(RecipeKey::toString).collect(Collectors.joining(", ", "(", ")"));
    }

    @FunctionalInterface
    public interface Factory {

        RecipeConstructor.Factory DEFAULT = (recipe, schemaType, keys, from) -> {
            for (RecipeKey<?> key : keys) {
                recipe.setValue(key, UtilsJS.cast(from.getValue(recipe, key)));
            }
        };

        static RecipeConstructor.Factory defaultWith(BiFunction<RecipeJS, RecipeKey<?>, Object> valueSupplier) {
            return (recipe, schemaType, keys, from) -> {
                DEFAULT.setValues(recipe, schemaType, keys, from);
                for (RecipeKey<?> key : schemaType.schema.keys) {
                    Object v = valueSupplier.apply(recipe, key);
                    if (v != null) {
                        recipe.setValue(key, UtilsJS.cast(v));
                    }
                }
            };
        }

        default RecipeJS create(RecipeTypeFunction type, RecipeSchemaType schemaType, RecipeKey<?>[] keys, ComponentValueMap from) {
            RecipeJS r = (RecipeJS) schemaType.schema.factory.get();
            r.type = type;
            r.json = new JsonObject();
            r.json.addProperty("type", "unknown");
            r.newRecipe = true;
            r.initValues(true);
            this.setValues(r, schemaType, keys, from);
            return r;
        }

        void setValues(RecipeJS var1, RecipeSchemaType var2, RecipeKey<?>[] var3, ComponentValueMap var4);
    }
}