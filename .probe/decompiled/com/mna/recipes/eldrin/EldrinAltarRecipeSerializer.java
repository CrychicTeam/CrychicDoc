package com.mna.recipes.eldrin;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class EldrinAltarRecipeSerializer extends AMRecipeBaseSerializer<EldrinAltarRecipe> {

    public void writeExtra(FriendlyByteBuf buffer, EldrinAltarRecipe recipe) {
        try {
            buffer.writeResourceLocation(recipe.getOutput());
            buffer.writeInt(recipe.getOutputQuantity());
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
            buffer.writeInt(recipe.getPowerRequirements().size());
            recipe.getPowerRequirements().entrySet().forEach(r -> {
                buffer.writeInt(((Affinity) r.getKey()).ordinal());
                buffer.writeFloat((Float) r.getValue());
            });
        } catch (Exception var6) {
            ManaAndArtifice.LOGGER.error("Error writing eldrin altar recipe to packet.", var6);
            throw var6;
        }
    }

    public EldrinAltarRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            EldrinAltarRecipe recipe = new EldrinAltarRecipe(recipeId);
            recipe.setOutput(buffer.readResourceLocation());
            recipe.setOutputQuantity(buffer.readInt());
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
            int num_power_requirements = buffer.readInt();
            for (int i = 0; i < num_power_requirements; i++) {
                int ordinal = buffer.readInt();
                float power = buffer.readFloat();
                recipe.getPowerRequirements().put(Affinity.values()[ordinal], power);
            }
            return recipe;
        } catch (Exception var12) {
            ManaAndArtifice.LOGGER.error("Error reading eldrin altar recipe from packet.", var12);
            throw var12;
        }
    }

    protected EldrinAltarRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        EldrinAltarRecipe recipe = new EldrinAltarRecipe(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }
}