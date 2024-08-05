package com.mna.recipes.multiblock;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MultiblockRecipeSerializer extends AMRecipeBaseSerializer<MultiblockDefinition> {

    protected MultiblockDefinition readExtra(ResourceLocation recipeId, JsonObject json) {
        MultiblockDefinition recipe = new MultiblockDefinition(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    protected MultiblockDefinition readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            MultiblockDefinition recipe = new MultiblockDefinition(recipeId);
            recipe.setStructurePath(buffer.readResourceLocation());
            int count = buffer.readInt();
            HashMap<ResourceLocation, Integer> matchersByBlock = new HashMap();
            for (int i = 0; i < count; i++) {
                ResourceLocation rLoc = buffer.readResourceLocation();
                int index = buffer.readInt();
                matchersByBlock.put(rLoc, index);
            }
            recipe.setSpecialBlockMatchersByBlock(matchersByBlock);
            count = buffer.readInt();
            HashMap<Long, Integer> matchersByOffset = new HashMap();
            for (int i = 0; i < count; i++) {
                Long offset = buffer.readLong();
                int index = buffer.readInt();
                matchersByOffset.put(offset, index);
            }
            recipe.setSpecialBlockMatchersByOffset(matchersByOffset);
            recipe.deserializeVariations(buffer.readNbt());
            return recipe;
        } catch (Exception var10) {
            ManaAndArtifice.LOGGER.error("Error reading multiblock definition from packet.", var10);
            throw var10;
        }
    }

    protected void writeExtra(FriendlyByteBuf buffer, MultiblockDefinition recipe) {
        try {
            buffer.writeResourceLocation(recipe.getStructurePath());
            HashMap<ResourceLocation, Integer> matchersByBlock = recipe.getSpecialBlockMatchersByBlock();
            buffer.writeInt(matchersByBlock.size());
            matchersByBlock.entrySet().forEach(ex -> {
                buffer.writeResourceLocation((ResourceLocation) ex.getKey());
                buffer.writeInt((Integer) ex.getValue());
            });
            HashMap<Long, Integer> matchersByOffset = recipe.getSpecialBlockMatchersByOffset();
            buffer.writeInt(matchersByOffset.size());
            matchersByOffset.entrySet().forEach(ex -> {
                buffer.writeLong((Long) ex.getKey());
                buffer.writeInt((Integer) ex.getValue());
            });
            buffer.writeNbt(recipe.serializeVariations());
        } catch (Exception var5) {
            ManaAndArtifice.LOGGER.error("Error writing multiblock definition to packet.", var5);
            throw var5;
        }
    }
}