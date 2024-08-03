package com.mna.recipes.arcanefurnace;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ArcaneFurnaceRecipeSerializer extends AMRecipeBaseSerializer<ArcaneFurnaceRecipe> {

    public ArcaneFurnaceRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        ArcaneFurnaceRecipe recipe = new ArcaneFurnaceRecipe(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    public ArcaneFurnaceRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            ArcaneFurnaceRecipe recipe = new ArcaneFurnaceRecipe(recipeId);
            recipe.setInputItem(buffer.readResourceLocation());
            recipe.setOutputItem(buffer.readResourceLocation());
            recipe.setBurnTime(buffer.readInt());
            recipe.setOutputQuantity(buffer.readInt());
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading arcane furnace recipe from packet.", var4);
            throw var4;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, ArcaneFurnaceRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getInputItem());
            buffer.writeResourceLocation(recipe.getOutputItem());
            buffer.writeInt(recipe.getBurnTime());
            buffer.writeInt(recipe.getOutputQuantity());
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing arcane furnace recipe to packet.", var4);
            throw var4;
        }
    }
}