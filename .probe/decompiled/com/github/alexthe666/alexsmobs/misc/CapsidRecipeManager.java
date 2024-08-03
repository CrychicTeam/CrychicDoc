package com.github.alexthe666.alexsmobs.misc;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;

public class CapsidRecipeManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(CapsidRecipe.class, new CapsidRecipe.Deserializer()).create();

    private static final RandomSource RANDOM = RandomSource.create();

    private final List<CapsidRecipe> capsidRecipes = Lists.newArrayList();

    public CapsidRecipeManager() {
        super(GSON, "capsid_recipes");
    }

    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profile) {
        this.capsidRecipes.clear();
        Builder<ResourceLocation, CapsidRecipe> builder = ImmutableMap.builder();
        AlexsMobs.LOGGER.log(Level.ALL, "Loading in capsid_recipes jsons...");
        jsonMap.forEach((resourceLocation, jsonElement) -> {
            try {
                CapsidRecipe capsidRecipe = (CapsidRecipe) GSON.fromJson(jsonElement, CapsidRecipe.class);
                builder.put(resourceLocation, capsidRecipe);
            } catch (Exception var4x) {
                AlexsMobs.LOGGER.error("Couldn't parse capsid recipe {}", resourceLocation, var4x);
            }
        });
        ImmutableMap<ResourceLocation, CapsidRecipe> immutablemap = builder.build();
        immutablemap.forEach((resourceLocation, capsidRecipe) -> this.capsidRecipes.add(capsidRecipe));
    }

    public CapsidRecipe getRecipeFor(ItemStack stack) {
        for (CapsidRecipe recipe : this.capsidRecipes) {
            if (recipe.matches(stack)) {
                return recipe;
            }
        }
        return null;
    }

    public List<CapsidRecipe> getCapsidRecipes() {
        return this.capsidRecipes;
    }

    @Override
    public String getName() {
        return "CapsidRecipeManager";
    }
}