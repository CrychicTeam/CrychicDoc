package com.mna.recipes.progression;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ProgressionConditionSerializer extends AMRecipeBaseSerializer<ProgressionCondition> {

    protected ProgressionCondition readExtra(ResourceLocation recipeId, JsonObject json) {
        ProgressionCondition recipe = new ProgressionCondition(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    protected ProgressionCondition readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            ProgressionCondition recipe = new ProgressionCondition(recipeId);
            recipe.setAdvancementID(buffer.readResourceLocation());
            recipe.setDescriptionID(buffer.readUtf());
            return recipe;
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error reading progression condition from packet.", var4);
            throw var4;
        }
    }

    protected void writeExtra(FriendlyByteBuf buffer, ProgressionCondition recipe) {
        try {
            buffer.writeResourceLocation(recipe.getAdvancementID());
            buffer.writeUtf(recipe.getDescriptionID());
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing progression condition to packet.", var4);
            throw var4;
        }
    }
}