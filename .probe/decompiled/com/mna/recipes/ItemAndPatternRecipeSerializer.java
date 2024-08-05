package com.mna.recipes;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class ItemAndPatternRecipeSerializer<T extends ItemAndPatternRecipe> extends AMRecipeBaseSerializer<T> {

    public T readExtra(ResourceLocation recipeId, JsonObject json) {
        T recipe = this.instantiate(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    public T readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            T recipe = this.instantiate(recipeId);
            recipe.setOutput(buffer.readResourceLocation());
            recipe.outputQuantity = Math.max(buffer.readInt(), 1);
            int numItems = buffer.readInt();
            ResourceLocation[] requiredItems = new ResourceLocation[numItems];
            for (int i = 0; i < numItems; i++) {
                requiredItems[i] = buffer.readResourceLocation();
            }
            recipe.setRequiredItems(requiredItems);
            int numPatterns = buffer.readInt();
            ResourceLocation[] requiredPatterns = new ResourceLocation[numPatterns];
            for (int i = 0; i < numPatterns; i++) {
                requiredPatterns[i] = buffer.readResourceLocation();
            }
            recipe.setRequiredPatterns(requiredPatterns);
            return recipe;
        } catch (Exception var9) {
            ManaAndArtifice.LOGGER.error("Error reading item and pattern recipe from packet.", var9);
            throw var9;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, ItemAndPatternRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getOutput());
            buffer.writeInt(recipe.outputQuantity);
            ResourceLocation[] items = recipe.getRequiredItems();
            ResourceLocation[] patterns = recipe.getRequiredPatterns();
            buffer.writeInt(items.length);
            for (int i = 0; i < items.length; i++) {
                buffer.writeResourceLocation(items[i]);
            }
            buffer.writeInt(patterns.length);
            for (int i = 0; i < patterns.length; i++) {
                buffer.writeResourceLocation(patterns[i]);
            }
        } catch (Exception var6) {
            ManaAndArtifice.LOGGER.error("Error writing item and pattern recipe from packet.", var6);
            throw var6;
        }
    }

    protected abstract T instantiate(ResourceLocation var1);
}