package com.github.alexthe666.iceandfire.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IafRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "iceandfire");

    public static final RegistryObject<RecipeSerializer<?>> DRAGONFORGE_SERIALIZER = SERIALIZERS.register("dragonforge", DragonForgeRecipe.Serializer::new);
}