package com.almostreliable.summoningrituals.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public record IngredientStack(Ingredient ingredient, int count) {

    public static IngredientStack fromJson(JsonElement json) {
        IngredientStack ingredientStack;
        label21: {
            if (json instanceof JsonObject jsonObject && jsonObject.has("count")) {
                Ingredient ingredient = Ingredient.fromJson((JsonElement) (jsonObject.has("ingredient") ? jsonObject.get("ingredient") : jsonObject));
                int count = GsonHelper.getAsInt(jsonObject, "count");
                ingredientStack = new IngredientStack(ingredient, count);
                break label21;
            }
            ingredientStack = new IngredientStack(Ingredient.fromJson(json), 1);
        }
        if (ingredientStack.ingredient.getItems().length == 0) {
            throw new IllegalArgumentException("Ingredient is empty, maybe wrong tag");
        } else {
            return ingredientStack;
        }
    }

    public static IngredientStack fromNetwork(FriendlyByteBuf buffer) {
        Ingredient ingred = Ingredient.fromNetwork(buffer);
        int count = buffer.readVarInt();
        return new IngredientStack(ingred, count);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        this.ingredient.toNetwork(buffer);
        buffer.writeVarInt(this.count);
    }
}