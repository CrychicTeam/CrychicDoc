package com.mna.recipes.runeforging;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RunescribingRecipeSerializer extends AMRecipeBaseSerializer<RunescribingRecipe> {

    public static HashMap<ResourceLocation, RunescribingRecipe> ALL_RECIPES = new HashMap();

    public RunescribingRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        RunescribingRecipe recipe = new RunescribingRecipe(recipeId);
        recipe.parseJSON(json);
        ALL_RECIPES.put(recipeId, recipe);
        return recipe;
    }

    public RunescribingRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            RunescribingRecipe recipe = new RunescribingRecipe(recipeId);
            recipe.setHMutex(buffer.readLong());
            recipe.setVMutex(buffer.readLong());
            recipe.setOutputResource(buffer.readUtf());
            ALL_RECIPES.put(recipeId, recipe);
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading runescribing recipe from packet.", var4);
            throw var4;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, RunescribingRecipe recipe) {
        try {
            buffer.writeLong(recipe.getHMutex());
            buffer.writeLong(recipe.getVMutex());
            buffer.writeUtf(recipe.getOutputResource().toString());
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing runescribing recipe from packet.", var4);
            throw var4;
        }
    }
}