package com.mna.recipes.manaweaving;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TransmutationRecipeSerializer extends AMRecipeBaseSerializer<TransmutationRecipe> {

    public static HashMap<ResourceLocation, TransmutationRecipe> ALL_RECIPES = new HashMap();

    public TransmutationRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        TransmutationRecipe recipe = new TransmutationRecipe(recipeId);
        recipe.parseJSON(json);
        ALL_RECIPES.put(recipeId, recipe);
        return recipe;
    }

    public TransmutationRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            TransmutationRecipe recipe = new TransmutationRecipe(recipeId);
            recipe.setTargetBlock(buffer.readResourceLocation());
            if (buffer.readBoolean()) {
                recipe.setReplaceBlock(buffer.readResourceLocation());
            }
            if (buffer.readBoolean()) {
                recipe.setLootTable(buffer.readResourceLocation());
            }
            if (buffer.readBoolean()) {
                recipe.setRepresentationItem(buffer.readResourceLocation());
            }
            ALL_RECIPES.put(recipeId, recipe);
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading transmutation recipe from packet.", var4);
            throw var4;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, TransmutationRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getTargetBlock());
            if (recipe.hasReplaceBlock()) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.getReplaceBlock());
            } else {
                buffer.writeBoolean(false);
            }
            if (recipe.hasLootTable()) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.getLootTable());
            } else {
                buffer.writeBoolean(false);
            }
            if (recipe.hasRepresentationItem()) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.getRepresentationItem());
            } else {
                buffer.writeBoolean(false);
            }
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing transmutation recipe to packet.", var4);
            throw var4;
        }
    }
}