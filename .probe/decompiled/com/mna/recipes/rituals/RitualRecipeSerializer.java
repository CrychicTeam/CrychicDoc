package com.mna.recipes.rituals;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RitualRecipeSerializer extends AMRecipeBaseSerializer<RitualRecipe> {

    public RitualRecipe readExtra(ResourceLocation recipeId, JsonObject json) {
        RitualRecipe recipe = new RitualRecipe(recipeId);
        recipe.parseJSON(json);
        return recipe;
    }

    public RitualRecipe readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            RitualRecipe recipe = new RitualRecipe(recipeId);
            recipe.setConnectBeam(buffer.readBoolean());
            recipe.setDisplayIndexes(buffer.readBoolean());
            recipe.setInnerColor(buffer.readLong());
            recipe.setOuterColor(buffer.readLong());
            recipe.setBeamColor(buffer.readLong());
            int[][] pattern = new int[buffer.readVarInt()][];
            for (int i = 0; i < pattern.length; i++) {
                pattern[i] = buffer.readVarIntArray();
            }
            recipe.setPattern(pattern);
            int[][] displayPattern = new int[buffer.readVarInt()][];
            for (int i = 0; i < displayPattern.length; i++) {
                displayPattern[i] = buffer.readVarIntArray();
            }
            recipe.setDisplayPattern(displayPattern);
            recipe.reagentsFromNBT(buffer.readNbt());
            int pattern_count = buffer.readInt();
            String[] manaweave_patterns = new String[pattern_count];
            for (int i = 0; i < pattern_count; i++) {
                manaweave_patterns[i] = buffer.readUtf();
            }
            recipe.setManaweavePatterns(manaweave_patterns);
            return recipe;
        } catch (Exception var9) {
            ManaAndArtifice.LOGGER.error("Error reading ritual recipe from packet.", var9);
            throw var9;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, RitualRecipe recipe) {
        try {
            buffer.writeBoolean(recipe.getConnectBeam());
            buffer.writeBoolean(recipe.getDisplayIndexes());
            buffer.writeLong(recipe.getInnerColor());
            buffer.writeLong(recipe.getOuterColor());
            buffer.writeLong(recipe.getBeamColor());
            buffer.writeVarInt(recipe.getPattern().length);
            for (int i = 0; i < recipe.getPattern().length; i++) {
                buffer.writeVarIntArray(recipe.getPattern()[i]);
            }
            buffer.writeVarInt(recipe.getDisplayPattern().length);
            for (int i = 0; i < recipe.getDisplayPattern().length; i++) {
                buffer.writeVarIntArray(recipe.getDisplayPattern()[i]);
            }
            buffer.writeNbt(recipe.reagentsToNBT());
            buffer.writeInt(recipe.getManaweavePatterns().length);
            for (int i = 0; i < recipe.getManaweavePatterns().length; i++) {
                buffer.writeUtf(recipe.getManaweavePatterns()[i]);
            }
        } catch (Exception var4) {
            ManaAndArtifice.LOGGER.error("Error writing ritual recipe to packet.", var4);
            throw var4;
        }
    }
}