package com.almostreliable.summoningrituals.compat.kubejs;

import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import com.almostreliable.summoningrituals.recipe.component.RecipeSacrifices;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import java.util.Map;

public interface SummoningComponents {

    RecipeComponent<RecipeOutputs> OUTPUTS = new RecipeComponent<RecipeOutputs>() {

        @Override
        public Class<?> componentClass() {
            return RecipeOutputs.class;
        }

        public JsonElement write(RecipeJS recipe, RecipeOutputs value) {
            return value.toJson();
        }

        public RecipeOutputs read(RecipeJS recipe, Object from) {
            if (from instanceof RecipeOutputs) {
                return (RecipeOutputs) from;
            } else {
                JsonArray asList = ListJS.json(from);
                if (asList != null) {
                    return RecipeOutputs.fromJson(asList);
                } else {
                    throw new RecipeExceptionJS("Invalid outputs: " + from);
                }
            }
        }
    };

    RecipeComponent<RecipeSacrifices> SACRIFICES = new RecipeComponent<RecipeSacrifices>() {

        @Override
        public Class<?> componentClass() {
            return RecipeSacrifices.class;
        }

        public JsonElement write(RecipeJS recipe, RecipeSacrifices value) {
            return value.toJson();
        }

        public RecipeSacrifices read(RecipeJS recipe, Object from) {
            if (from instanceof RecipeSacrifices) {
                return (RecipeSacrifices) from;
            } else {
                if (from instanceof Map || from instanceof JsonObject) {
                    JsonObject json = MapJS.json(from);
                    if (json != null) {
                        return RecipeSacrifices.fromJson(json);
                    }
                }
                return new RecipeSacrifices();
            }
        }
    };

    RecipeComponent<BlockReference> BLOCK_REFERENCE = new RecipeComponent<BlockReference>() {

        @Override
        public Class<?> componentClass() {
            return BlockReference.class;
        }

        public JsonElement write(RecipeJS recipe, BlockReference value) {
            return value.toJson();
        }

        public BlockReference read(RecipeJS recipe, Object from) {
            if (from instanceof BlockReference) {
                return (BlockReference) from;
            } else {
                if (from instanceof Map || from instanceof JsonObject) {
                    JsonObject json = MapJS.json(from);
                    if (json != null) {
                        return BlockReference.fromJson(json);
                    }
                }
                throw new RecipeExceptionJS("Invalid block reference: " + from);
            }
        }
    };
}