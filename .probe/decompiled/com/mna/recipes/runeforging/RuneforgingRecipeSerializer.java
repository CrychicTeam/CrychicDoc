package com.mna.recipes.runeforging;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RuneforgingRecipeSerializer extends AMRecipeBaseSerializer<RuneforgingRecipe> {

    public RuneforgingRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        RuneforgingRecipe recipe = new RuneforgingRecipe(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    public RuneforgingRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            RuneforgingRecipe recipe = new RuneforgingRecipe(recipeId);
            recipe.setPatternResource(buffer.readResourceLocation());
            recipe.setOutputResource(buffer.readResourceLocation());
            recipe.setMaterial(buffer.readResourceLocation());
            recipe.setHits(buffer.readInt());
            recipe.setOutputQuantity(buffer.readInt());
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading runeforging recipe from packet.", var4);
            throw var4;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, RuneforgingRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getPatternResource());
            buffer.writeResourceLocation(recipe.getOutputResource());
            buffer.writeResourceLocation(recipe.getMaterial());
            buffer.writeInt(recipe.getHits());
            buffer.writeInt(recipe.getOutputQuantity());
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing runeforging recipe from packet.", var4);
            throw var4;
        }
    }
}