package com.mna.recipes.eldrin;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FumeFilterRecipeSerializer extends AMRecipeBaseSerializer<FumeFilterRecipe> {

    public void writeExtra(FriendlyByteBuf buffer, FumeFilterRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getItemOrTagID());
            buffer.writeInt(recipe.getAffinity().ordinal());
            buffer.writeFloat(recipe.getTotalGeneration());
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing eldrin fume recipe to packet.", var4);
            throw var4;
        }
    }

    public FumeFilterRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            FumeFilterRecipe recipe = new FumeFilterRecipe(recipeId);
            recipe.setItemOrTagID(buffer.readResourceLocation());
            recipe.setAffinity(Affinity.values()[buffer.readInt()]);
            recipe.setTotalGeneration(buffer.readFloat());
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading eldrin fume recipe from packet.", var4);
            throw var4;
        }
    }

    protected FumeFilterRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        FumeFilterRecipe recipe = new FumeFilterRecipe(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }
}