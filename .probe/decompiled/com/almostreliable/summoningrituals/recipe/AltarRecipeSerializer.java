package com.almostreliable.summoningrituals.recipe;

import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.recipe.component.IngredientStack;
import com.almostreliable.summoningrituals.recipe.component.RecipeOutputs;
import com.almostreliable.summoningrituals.recipe.component.RecipeSacrifices;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AltarRecipeSerializer implements RecipeSerializer<AltarRecipe> {

    public static int MAX_INPUTS;

    public AltarRecipe fromJson(ResourceLocation id, JsonObject json) {
        Ingredient catalyst = Ingredient.fromJson(json.getAsJsonObject("catalyst"));
        AltarRecipe.CATALYST_CACHE.add(catalyst);
        RecipeOutputs outputs = RecipeOutputs.fromJson(json.getAsJsonArray("outputs"));
        NonNullList<IngredientStack> inputs = NonNullList.create();
        if (json.has("inputs")) {
            JsonElement inputJson = json.get("inputs");
            if (inputJson.isJsonObject()) {
                this.addInput(inputs, IngredientStack.fromJson(inputJson));
            } else {
                for (JsonElement input : inputJson.getAsJsonArray()) {
                    this.addInput(inputs, IngredientStack.fromJson(input.getAsJsonObject()));
                }
            }
        }
        expandMaxInputs(inputs.size());
        RecipeSacrifices sacrifices = new RecipeSacrifices();
        if (json.has("sacrifices")) {
            sacrifices = RecipeSacrifices.fromJson(json.getAsJsonObject("sacrifices"));
        }
        int recipeTime = GsonHelper.getAsInt(json, "recipe_time", 100);
        BlockReference blockBelow = null;
        if (json.has("block_below")) {
            blockBelow = BlockReference.fromJson(json.getAsJsonObject("block_below"));
        }
        AltarRecipe.DAY_TIME dayTime = AltarRecipe.DAY_TIME.valueOf(GsonHelper.getAsString(json, "day_time", AltarRecipe.DAY_TIME.ANY.name()).toUpperCase());
        AltarRecipe.WEATHER weather = AltarRecipe.WEATHER.valueOf(GsonHelper.getAsString(json, "weather", AltarRecipe.WEATHER.ANY.name()).toUpperCase());
        return new AltarRecipe(id, catalyst, outputs, inputs, sacrifices, recipeTime, blockBelow, dayTime, weather);
    }

    public AltarRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient catalyst = Ingredient.fromNetwork(buffer);
        RecipeOutputs outputs = RecipeOutputs.fromNetwork(buffer);
        NonNullList<IngredientStack> inputs = NonNullList.create();
        int inputCount = buffer.readVarInt();
        for (int i = 0; i < inputCount; i++) {
            inputs.add(IngredientStack.fromNetwork(buffer));
        }
        expandMaxInputs(inputs.size());
        RecipeSacrifices sacrifices = new RecipeSacrifices();
        if (buffer.readBoolean()) {
            sacrifices = RecipeSacrifices.fromNetwork(buffer);
        }
        int recipeTime = buffer.readInt();
        BlockReference blockBelow = null;
        if (buffer.readBoolean()) {
            blockBelow = BlockReference.fromNetwork(buffer);
        }
        AltarRecipe.DAY_TIME dayTime = AltarRecipe.DAY_TIME.values()[buffer.readVarInt()];
        AltarRecipe.WEATHER weather = AltarRecipe.WEATHER.values()[buffer.readVarInt()];
        return new AltarRecipe(id, catalyst, outputs, inputs, sacrifices, recipeTime, blockBelow, dayTime, weather);
    }

    public void toNetwork(FriendlyByteBuf buffer, AltarRecipe recipe) {
        recipe.getCatalyst().toNetwork(buffer);
        recipe.getOutputs().toNetwork(buffer);
        buffer.writeVarInt(recipe.getInputs().size());
        for (IngredientStack input : recipe.getInputs()) {
            input.toNetwork(buffer);
        }
        if (recipe.getSacrifices().isEmpty()) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            recipe.getSacrifices().toNetwork(buffer);
        }
        buffer.writeInt(recipe.getRecipeTime());
        if (recipe.getBlockBelow() != null) {
            buffer.writeBoolean(true);
            recipe.getBlockBelow().toNetwork(buffer);
        } else {
            buffer.writeBoolean(false);
        }
        buffer.writeVarInt(recipe.getDayTime().ordinal());
        buffer.writeVarInt(recipe.getWeather().ordinal());
    }

    private void addInput(NonNullList<IngredientStack> inputs, IngredientStack input) {
        if (inputs.size() >= 64) {
            throw new IllegalArgumentException("Too many inputs, max is 64");
        } else {
            inputs.add(input);
        }
    }

    private static void expandMaxInputs(int amount) {
        if (MAX_INPUTS < amount) {
            MAX_INPUTS = amount;
        }
    }
}