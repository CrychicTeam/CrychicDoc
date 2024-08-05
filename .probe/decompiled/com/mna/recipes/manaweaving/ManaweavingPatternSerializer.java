package com.mna.recipes.manaweaving;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.AMRecipeBaseSerializer;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ManaweavingPatternSerializer extends AMRecipeBaseSerializer<ManaweavingPattern> {

    public static HashMap<ResourceLocation, ManaweavingPattern> ALL_RECIPES = new HashMap();

    public ManaweavingPattern readExtra(ResourceLocation recipeId, JsonObject json) {
        ManaweavingPattern pattern = new ManaweavingPattern(recipeId);
        pattern.parseJSON(json);
        ALL_RECIPES.put(recipeId, pattern);
        return pattern;
    }

    public ManaweavingPattern readExtra(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        try {
            int len = buffer.readInt();
            byte[][] pattern = new byte[len][];
            for (int i = 0; i < len; i++) {
                pattern[i] = buffer.readByteArray();
            }
            ManaweavingPattern mwpattern = new ManaweavingPattern(recipeId);
            mwpattern.setPatternBytes(pattern);
            ALL_RECIPES.put(recipeId, mwpattern);
            return mwpattern;
        } catch (Exception var6) {
            ManaAndArtifice.LOGGER.error("Error reading manaweaving pattern recipe from packet.", var6);
            throw var6;
        }
    }

    public void writeExtra(FriendlyByteBuf buffer, ManaweavingPattern recipe) {
        try {
            byte[][] pattern = recipe.get();
            buffer.writeInt(pattern.length);
            for (int i = 0; i < pattern.length; i++) {
                buffer.writeByteArray(pattern[i]);
            }
        } catch (Exception var5) {
            ManaAndArtifice.LOGGER.error("Error writing manaweaving pattern recipe to packet.", var5);
            throw var5;
        }
    }
}