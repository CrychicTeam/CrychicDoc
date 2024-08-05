package com.github.alexmodguy.alexscaves.server.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ACRecipeRegistry {

    public static final DeferredRegister<RecipeType<?>> TYPE_DEF_REG = DeferredRegister.create(Registries.RECIPE_TYPE, "alexscaves");

    public static final DeferredRegister<RecipeSerializer<?>> DEF_REG = DeferredRegister.create(Registries.RECIPE_SERIALIZER, "alexscaves");

    public static final RegistryObject<RecipeType<NuclearFurnaceRecipe>> NUCLEAR_FURNACE_TYPE = TYPE_DEF_REG.register("nuclear_furnace", () -> new RecipeType<NuclearFurnaceRecipe>() {
    });

    public static final RegistryObject<RecipeSerializer<?>> CAVE_MAP = DEF_REG.register("cave_map", () -> new SimpleCraftingRecipeSerializer(RecipeCaveMap::new));

    public static final RegistryObject<RecipeSerializer<?>> NUCLEAR_FURNACE = DEF_REG.register("nuclear_furnace", () -> new SimpleCookingSerializer(NuclearFurnaceRecipe::new, 100));
}