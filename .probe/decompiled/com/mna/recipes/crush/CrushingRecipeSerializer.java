package com.mna.recipes.crush;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class CrushingRecipeSerializer extends AMRecipeBaseSerializer<CrushingRecipe> {

    public CrushingRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        CrushingRecipe recipe = new CrushingRecipe(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    public CrushingRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            CrushingRecipe recipe = new CrushingRecipe(recipeId);
            recipe.setInputResource(buffer.readResourceLocation());
            recipe.setOutputResource(buffer.readResourceLocation());
            recipe.setOutputQuantity(buffer.readInt());
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading crushing recipe from packet.", var4);
            throw var4;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, CrushingRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getInputResource());
            buffer.writeResourceLocation(recipe.getOutputResource());
            buffer.writeInt(recipe.getOutputQuantity());
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing crushing recipe to packet.", var4);
            throw var4;
        }
    }
}