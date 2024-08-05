package com.mna.recipes.manaweaving;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.recipes.AMRecipeBaseSerializer;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class ManaweavingRecipeSerializer extends AMRecipeBaseSerializer<ManaweavingRecipe> {

    public static HashMap<ResourceLocation, ManaweavingRecipe> ALL_RECIPES = new HashMap();

    public ManaweavingRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        ManaweavingRecipe recipe = new ManaweavingRecipe(recipeId);
        recipe.parseJSON(json);
        ALL_RECIPES.put(recipeId, recipe);
        return recipe;
    }

    public ManaweavingRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            ManaweavingRecipe recipe = new ManaweavingRecipe(recipeId);
            recipe.setOutput(buffer.readResourceLocation());
            recipe.setOutputQuantity(buffer.readInt());
            recipe.setEnchantment(buffer.readResourceLocation());
            recipe.setEnchantmentMagnitude(buffer.readInt());
            recipe.setCopyNBT(buffer.readBoolean());
            if (buffer.readBoolean()) {
                recipe.setRequiredFaction(buffer.readResourceLocation());
            }
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
            ALL_RECIPES.put(recipeId, recipe);
            return recipe;
        } catch (Exception var9) {
            ManaAndArtifice.LOGGER.error("Error reading manaweaving recipe from packet.", var9);
            throw var9;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, ManaweavingRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getOutput());
            buffer.writeInt(recipe.getOutputQuantity());
            buffer.writeResourceLocation(recipe.getEnchantment());
            buffer.writeInt(recipe.getEnchantmentMagnitude());
            buffer.writeBoolean(recipe.getCopyNBT());
            if (recipe.getFactionRequirement() != null) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(((IForgeRegistry) Registries.Factions.get()).getKey(recipe.getFactionRequirement()));
            } else {
                buffer.writeBoolean(false);
            }
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
            ManaAndArtifice.LOGGER.error("Error writing manaweaving recipe to packet.", var6);
            throw var6;
        }
    }
}