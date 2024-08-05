package dev.xkmc.l2complements.compat.ars;

import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public record ArsFinished(ArsRecipeBuilder builder, ResourceLocation id) implements FinishedRecipe {

    @Override
    public void serializeRecipeData(JsonObject json) {
        JsonObject obj = JsonCodec.toJson(this.builder).getAsJsonObject();
        for (String e : obj.keySet()) {
            json.add(e, obj.get(e));
        }
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getType() {
        return (RecipeSerializer<?>) RecipeRegistry.APPARATUS_SERIALIZER.get();
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