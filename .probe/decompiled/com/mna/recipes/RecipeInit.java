package com.mna.recipes;

import com.mna.api.tools.RLoc;
import com.mna.recipes.arcanefurnace.ArcaneFurnaceRecipe;
import com.mna.recipes.arcanefurnace.ArcaneFurnaceRecipeSerializer;
import com.mna.recipes.crush.CrushingRecipe;
import com.mna.recipes.crush.CrushingRecipeSerializer;
import com.mna.recipes.eldrin.EldrinAltarRecipe;
import com.mna.recipes.eldrin.EldrinAltarRecipeSerializer;
import com.mna.recipes.eldrin.FumeFilterRecipe;
import com.mna.recipes.eldrin.FumeFilterRecipeSerializer;
import com.mna.recipes.manaweaving.ManaweaveCacheEffect;
import com.mna.recipes.manaweaving.ManaweaveCacheEffectSerializer;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternSerializer;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
import com.mna.recipes.manaweaving.ManaweavingRecipeSerializer;
import com.mna.recipes.manaweaving.TransmutationRecipe;
import com.mna.recipes.manaweaving.TransmutationRecipeSerializer;
import com.mna.recipes.multiblock.MultiblockDefinition;
import com.mna.recipes.multiblock.MultiblockRecipeSerializer;
import com.mna.recipes.progression.ProgressionCondition;
import com.mna.recipes.progression.ProgressionConditionSerializer;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.recipes.rituals.RitualRecipeSerializer;
import com.mna.recipes.runeforging.RuneforgingRecipe;
import com.mna.recipes.runeforging.RuneforgingRecipeSerializer;
import com.mna.recipes.runeforging.RunescribingRecipe;
import com.mna.recipes.runeforging.RunescribingRecipeSerializer;
import com.mna.recipes.spells.ComponentRecipe;
import com.mna.recipes.spells.ComponentRecipeSerializer;
import com.mna.recipes.spells.ModifierRecipe;
import com.mna.recipes.spells.ModifierRecipeSerializer;
import com.mna.recipes.spells.ShapeRecipe;
import com.mna.recipes.spells.ShapeRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class RecipeInit {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "mna");

    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "mna");

    public static RegistryObject<RecipeType<RitualRecipe>> RITUAL_TYPE = TYPES.register("ritual-type", () -> RecipeType.simple(RLoc.create("ritual-type")));

    public static RegistryObject<RecipeType<ManaweavingPattern>> MANAWEAVING_PATTERN_TYPE = TYPES.register("manaweaving-pattern-type", () -> RecipeType.simple(RLoc.create("manaweaving-pattern-type")));

    public static RegistryObject<RecipeType<ManaweavingRecipe>> MANAWEAVING_RECIPE_TYPE = TYPES.register("manaweaving-recipe-type", () -> RecipeType.simple(RLoc.create("manaweaving-recipe-type")));

    public static RegistryObject<RecipeType<RunescribingRecipe>> RUNESCRIBING_TYPE = TYPES.register("runescribing-type", () -> RecipeType.simple(RLoc.create("runescribing-type")));

    public static RegistryObject<RecipeType<RuneforgingRecipe>> RUNEFORGING_TYPE = TYPES.register("runeforging-type", () -> RecipeType.simple(RLoc.create("runeforging-type")));

    public static RegistryObject<RecipeType<CrushingRecipe>> CRUSHING_TYPE = TYPES.register("crushing-type", () -> RecipeType.simple(RLoc.create("crushing-type")));

    public static RegistryObject<RecipeType<ArcaneFurnaceRecipe>> ARCANE_FURNACE_TYPE = TYPES.register("arcane-furnace-type", () -> RecipeType.simple(RLoc.create("arcane-furnace-type")));

    public static RegistryObject<RecipeType<MultiblockDefinition>> MULTIBLOCK_TYPE = TYPES.register("multiblock-recipe-type", () -> RecipeType.simple(RLoc.create("multiblock-recipe-type")));

    public static RegistryObject<RecipeType<EldrinAltarRecipe>> ELDRIN_ALTAR_TYPE = TYPES.register("eldrin-altar-recipe-type", () -> RecipeType.simple(RLoc.create("eldrin-altar-recipe-type")));

    public static RegistryObject<RecipeType<ManaweaveCacheEffect>> MANAWEAVE_CACHE_EFFECT_TYPE = TYPES.register("manaweave-cache-effect-recipe-type", () -> RecipeType.simple(RLoc.create("manaweave-cache-effect-recipe-type")));

    public static RegistryObject<RecipeType<TransmutationRecipe>> TRANSMUTATION_TYPE = TYPES.register("transmutation-recipe-type", () -> RecipeType.simple(RLoc.create("transmutation-recipe-type")));

    public static RegistryObject<RecipeType<ProgressionCondition>> PROGRESSION_TYPE = TYPES.register("progression-condition-type", () -> RecipeType.simple(RLoc.create("progression-condition-type")));

    public static RegistryObject<RecipeType<FumeFilterRecipe>> FUME_FILTER_TYPE = TYPES.register("fume-filter-recipe-type", () -> RecipeType.simple(RLoc.create("fume-filter-recipe-type")));

    public static RegistryObject<RecipeType<ShapeRecipe>> SHAPE_TYPE = TYPES.register("shape-recipe-type", () -> RecipeType.simple(RLoc.create("shape-recipe-type")));

    public static RegistryObject<RecipeType<ComponentRecipe>> COMPONENT_TYPE = TYPES.register("component-recipe-type", () -> RecipeType.simple(RLoc.create("component-recipe-type")));

    public static RegistryObject<RecipeType<ModifierRecipe>> MODIFIER_TYPE = TYPES.register("modifier-recipe-type", () -> RecipeType.simple(RLoc.create("modifier-recipe-type")));

    public static final RegistryObject<RecipeSerializer<RitualRecipe>> RITUAL_SERIALIZER = SERIALIZERS.register("ritual", () -> new RitualRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ManaweavingPattern>> MANAWEAVING_SERIALIZER = SERIALIZERS.register("manaweaving-pattern", () -> new ManaweavingPatternSerializer());

    public static final RegistryObject<RecipeSerializer<RunescribingRecipe>> RUNESCRIBING_SERIALIZER = SERIALIZERS.register("runescribing", () -> new RunescribingRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<RuneforgingRecipe>> RUNEFORGING_SERIALIZER = SERIALIZERS.register("runeforging", () -> new RuneforgingRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING_SERIALIZER = SERIALIZERS.register("crushing", () -> new CrushingRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ManaweavingRecipe>> MANAWEAVING_RECIPE_SERIALIZER = SERIALIZERS.register("manaweaving-recipe", () -> new ManaweavingRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ShapeRecipe>> SHAPE_RECIPE_SERIALIZER = SERIALIZERS.register("shape", () -> new ShapeRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ComponentRecipe>> COMPONENT_RECIPE_SERIALIZER = SERIALIZERS.register("component", () -> new ComponentRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ModifierRecipe>> MODIFIER_RECIPE_SERIALIZER = SERIALIZERS.register("modifier", () -> new ModifierRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ArcaneFurnaceRecipe>> ARCANE_FURNACE_RECIPE_SERIALIZER = SERIALIZERS.register("arcane-furnace", () -> new ArcaneFurnaceRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<MultiblockDefinition>> MULTIBLOCK_RECIPE_SERIALIZER = SERIALIZERS.register("multiblock", () -> new MultiblockRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<EldrinAltarRecipe>> ELDRIN_ALTAR_RECIPE_SERIALIZER = SERIALIZERS.register("eldrin-altar", () -> new EldrinAltarRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ManaweaveCacheEffect>> MANAWEAVE_CACHE_EFFECT_SERIALIZER = SERIALIZERS.register("manaweave-cache-effect", () -> new ManaweaveCacheEffectSerializer());

    public static final RegistryObject<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_SERIALIZER = SERIALIZERS.register("transmutation", () -> new TransmutationRecipeSerializer());

    public static final RegistryObject<RecipeSerializer<ProgressionCondition>> PROGRESSION_SERIALIZER = SERIALIZERS.register("progression-condition", () -> new ProgressionConditionSerializer());

    public static final RegistryObject<RecipeSerializer<FumeFilterRecipe>> ELDRIN_FUME_RECIPE_SERIALIZER = SERIALIZERS.register("eldrin-fume", () -> new FumeFilterRecipeSerializer());
}