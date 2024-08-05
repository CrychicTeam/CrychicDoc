package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotRecipes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "crockpot");

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "crockpot");

    public static final String CROCK_POT_COOKING = "crock_pot_cooking";

    public static final String EXPLOSION_CRAFTING = "explosion_crafting";

    public static final String FOOD_VALUES = "food_values";

    public static final String PARROT_FEEDING = "parrot_feeding";

    public static final String PIGLIN_BARTERING = "piglin_bartering";

    public static final RegistryObject<RecipeType<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_TYPE = RECIPE_TYPES.register("crock_pot_cooking", () -> new CrockPotRecipeType("crock_pot_cooking"));

    public static final RegistryObject<RecipeType<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_TYPE = RECIPE_TYPES.register("explosion_crafting", () -> new CrockPotRecipeType("explosion_crafting"));

    public static final RegistryObject<RecipeType<FoodValuesDefinition>> FOOD_VALUES_RECIPE_TYPE = RECIPE_TYPES.register("food_values", () -> new CrockPotRecipeType("food_values"));

    public static final RegistryObject<RecipeType<ParrotFeedingRecipe>> PARROT_FEEDING_RECIPE_TYPE = RECIPE_TYPES.register("parrot_feeding", () -> new CrockPotRecipeType("parrot_feeding"));

    public static final RegistryObject<RecipeType<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_TYPE = RECIPE_TYPES.register("piglin_bartering", () -> new CrockPotRecipeType("piglin_bartering"));

    public static final RegistryObject<RecipeSerializer<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crock_pot_cooking", CrockPotCookingRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("explosion_crafting", ExplosionCraftingRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<FoodValuesDefinition>> FOOD_VALUES_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("food_values", FoodValuesDefinition.Serializer::new);

    public static final RegistryObject<RecipeSerializer<ParrotFeedingRecipe>> PARROT_FEEDING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("parrot_feeding", ParrotFeedingRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("piglin_bartering", PiglinBarteringRecipe.Serializer::new);

    private CrockPotRecipes() {
    }
}