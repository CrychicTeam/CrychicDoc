package net.minecraft.data.recipes;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface FinishedRecipe {

    void serializeRecipeData(JsonObject var1);

    default JsonObject serializeRecipe() {
        JsonObject $$0 = new JsonObject();
        $$0.addProperty("type", BuiltInRegistries.RECIPE_SERIALIZER.getKey(this.getType()).toString());
        this.serializeRecipeData($$0);
        return $$0;
    }

    ResourceLocation getId();

    RecipeSerializer<?> getType();

    @Nullable
    JsonObject serializeAdvancement();

    @Nullable
    ResourceLocation getAdvancementId();
}