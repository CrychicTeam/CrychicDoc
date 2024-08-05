package dev.xkmc.l2library.serial.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import java.util.Map.Entry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record RecordRecipeFinished<T extends Record>(ResourceLocation id, RecipeSerializer<?> serializer, T recipe) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject json) {
        JsonObject obj = JsonCodec.toJson(this.recipe).getAsJsonObject();
        for (Entry<String, JsonElement> e : obj.entrySet()) {
            json.add((String) e.getKey(), (JsonElement) e.getValue());
        }
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return this.serializer;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}