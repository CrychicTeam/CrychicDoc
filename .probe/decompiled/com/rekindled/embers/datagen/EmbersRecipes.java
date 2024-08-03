package com.rekindled.embers.datagen;

import com.google.common.collect.ImmutableList;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.recipe.AlchemyRecipeBuilder;
import com.rekindled.embers.recipe.AnvilAugmentRecipeBuilder;
import com.rekindled.embers.recipe.AnvilAugmentRemoveRecipe;
import com.rekindled.embers.recipe.AnvilBreakdownRecipe;
import com.rekindled.embers.recipe.AnvilRepairMateriaRecipe;
import com.rekindled.embers.recipe.AnvilRepairRecipe;
import com.rekindled.embers.recipe.AugmentIngredient;
import com.rekindled.embers.recipe.BoilingRecipeBuilder;
import com.rekindled.embers.recipe.BoringRecipeBuilder;
import com.rekindled.embers.recipe.CatalysisCombustionRecipeBuilder;
import com.rekindled.embers.recipe.EmberActivationRecipeBuilder;
import com.rekindled.embers.recipe.GaseousFuelRecipeBuilder;
import com.rekindled.embers.recipe.GemSocketRecipeBuilder;
import com.rekindled.embers.recipe.GemUnsocketRecipe;
import com.rekindled.embers.recipe.GenericRecipeBuilder;
import com.rekindled.embers.recipe.HeatIngredient;
import com.rekindled.embers.recipe.MeltingRecipeBuilder;
import com.rekindled.embers.recipe.MetalCoefficientRecipeBuilder;
import com.rekindled.embers.recipe.MixingRecipeBuilder;
import com.rekindled.embers.recipe.StampingRecipeBuilder;
import com.rekindled.embers.util.ConsumerWrapperBuilder;
import com.rekindled.embers.util.MeltingBonus;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import net.minecraftforge.common.crafting.conditions.AndCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

public class EmbersRecipes extends RecipeProvider implements IConditionBuilder {

    public static String boringFolder = "boring";

    public static String activationFolder = "ember_activation";

    public static String meltingFolder = "melting";

    public static String stampingFolder = "stamping";

    public static String mixingFolder = "mixing";

    public static String coefficientFolder = "metal_coefficient";

    public static String alchemyFolder = "alchemy";

    public static String boilingFolder = "boiling";

    public static String gaseousFuelFolder = "gas_fuel";

    public static String catalysisFolder = "catalysis";

    public static String combustionFolder = "combustion";

    public static String anvilFolder = "dawnstone_anvil";

    public EmbersRecipes(PackOutput gen) {
        super(gen);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        BoringRecipeBuilder.create(RegistryManager.EMBER_CRYSTAL.get()).folder(boringFolder).dimension(Level.OVERWORLD.location()).require(EmbersBlockTags.WORLD_BOTTOM, 3).weight(20).maxHeight(-57).save(consumer);
        BoringRecipeBuilder.create(RegistryManager.EMBER_SHARD.get()).folder(boringFolder).dimension(Level.OVERWORLD.location()).require(EmbersBlockTags.WORLD_BOTTOM, 3).weight(60).maxHeight(-57).save(consumer);
        BoringRecipeBuilder.create(RegistryManager.EMBER_GRIT.get()).folder(boringFolder).dimension(Level.OVERWORLD.location()).require(EmbersBlockTags.WORLD_BOTTOM, 3).weight(20).maxHeight(-57).save(consumer);
        BoringRecipeBuilder.create(RegistryManager.EMBER_CRYSTAL.get()).folder(boringFolder + "/nether").dimension(Level.NETHER.location()).require(EmbersBlockTags.WORLD_BOTTOM, 3).weight(20).maxHeight(7).save(consumer);
        BoringRecipeBuilder.create(RegistryManager.EMBER_SHARD.get()).folder(boringFolder + "/nether").dimension(Level.NETHER.location()).require(EmbersBlockTags.WORLD_BOTTOM, 3).weight(60).maxHeight(7).save(consumer);
        BoringRecipeBuilder.create(RegistryManager.EMBER_GRIT.get()).folder(boringFolder + "/nether").dimension(Level.NETHER.location()).require(EmbersBlockTags.WORLD_BOTTOM, 3).weight(20).maxHeight(7).save(consumer);
        BoringRecipeBuilder.create(Items.GRAVEL).folder(boringFolder).require(Tags.Blocks.GRAVEL, 5).chance(0.9).weight(50).type(RegistryManager.EXCAVATION_SERIALIZER.get()).save(consumer);
        BoringRecipeBuilder.create(Items.FLINT).folder(boringFolder).require(Tags.Blocks.GRAVEL, 5).chance(1.0).weight(5).type(RegistryManager.EXCAVATION_SERIALIZER.get()).save(consumer);
        BoringRecipeBuilder.create(Items.SNOWBALL).folder(boringFolder).require(EmbersBlockTags.SNOW, 5).chance(0.9).weight(50).type(RegistryManager.EXCAVATION_SERIALIZER.get()).save(consumer);
        EmberActivationRecipeBuilder.create(RegistryManager.EMBER_CRYSTAL.get()).folder(activationFolder).ember(2400).save(consumer);
        EmberActivationRecipeBuilder.create(RegistryManager.EMBER_SHARD.get()).folder(activationFolder).ember(400).save(consumer);
        EmberActivationRecipeBuilder.create(RegistryManager.EMBER_GRIT.get()).folder(activationFolder).ember(0).save(consumer);
        EmberActivationRecipeBuilder.create(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).folder(activationFolder).ember(4400).save(consumer);
        this.fullOreRecipes("lead", ImmutableList.of(RegistryManager.LEAD_ORE_ITEM.get(), RegistryManager.DEEPSLATE_LEAD_ORE_ITEM.get(), RegistryManager.RAW_LEAD.get()), RegistryManager.MOLTEN_LEAD.FLUID.get(), RegistryManager.RAW_LEAD.get(), RegistryManager.RAW_LEAD_BLOCK_ITEM.get(), RegistryManager.LEAD_BLOCK_ITEM.get(), RegistryManager.LEAD_INGOT.get(), RegistryManager.LEAD_NUGGET.get(), RegistryManager.LEAD_PLATE.get(), consumer, MeltingBonus.SILVER);
        this.fullOreRecipes("silver", ImmutableList.of(RegistryManager.SILVER_ORE_ITEM.get(), RegistryManager.DEEPSLATE_SILVER_ORE_ITEM.get(), RegistryManager.RAW_SILVER.get()), RegistryManager.MOLTEN_SILVER.FLUID.get(), RegistryManager.RAW_SILVER.get(), RegistryManager.RAW_SILVER_BLOCK_ITEM.get(), RegistryManager.SILVER_BLOCK_ITEM.get(), RegistryManager.SILVER_INGOT.get(), RegistryManager.SILVER_NUGGET.get(), RegistryManager.SILVER_PLATE.get(), consumer, MeltingBonus.LEAD);
        this.fullMetalRecipes("dawnstone", RegistryManager.MOLTEN_DAWNSTONE.FLUID.get(), RegistryManager.DAWNSTONE_BLOCK_ITEM.get(), RegistryManager.DAWNSTONE_INGOT.get(), RegistryManager.DAWNSTONE_NUGGET.get(), RegistryManager.DAWNSTONE_PLATE.get(), consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT).pattern("XXX").pattern("XXX").pattern("XXX").define('X', this.itemTag("forge", "nuggets/copper")).unlockedBy("has_nugget", m_206406_(this.itemTag("forge", "nuggets/copper"))).save(consumer, getResource("copper_nugget_to_ingot"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegistryManager.COPPER_NUGGET.get(), 9).requires(this.itemTag("forge", "ingots/copper")).group("").unlockedBy("has_ingot", m_206406_(this.itemTag("forge", "ingots/copper"))).save(consumer, getResource("copper_ingot_to_nugget"));
        this.plateHammerRecipe("iron", RegistryManager.IRON_PLATE.get(), consumer);
        this.plateHammerRecipe("copper", RegistryManager.COPPER_PLATE.get(), consumer);
        this.fullOreMeltingStampingRecipes("iron", RegistryManager.MOLTEN_IRON.FLUID.get(), consumer, MeltingBonus.NICKEL, MeltingBonus.ALUMINUM, MeltingBonus.COPPER);
        this.fullOreMeltingStampingRecipes("gold", RegistryManager.MOLTEN_GOLD.FLUID.get(), consumer, MeltingBonus.SILVER);
        this.fullOreMeltingStampingRecipes("copper", RegistryManager.MOLTEN_COPPER.FLUID.get(), consumer, MeltingBonus.GOLD);
        this.fullOreMeltingStampingRecipes("nickel", RegistryManager.MOLTEN_NICKEL.FLUID.get(), consumer, MeltingBonus.IRON);
        this.fullOreMeltingStampingRecipes("tin", RegistryManager.MOLTEN_TIN.FLUID.get(), consumer, MeltingBonus.LEAD);
        this.fullOreMeltingStampingRecipes("aluminum", RegistryManager.MOLTEN_ALUMINUM.FLUID.get(), consumer, MeltingBonus.IRON);
        this.fullOreMeltingStampingRecipes("zinc", RegistryManager.MOLTEN_ZINC.FLUID.get(), consumer, MeltingBonus.TIN, MeltingBonus.IRON);
        this.fullOreMeltingStampingRecipes("platinum", RegistryManager.MOLTEN_PLATINUM.FLUID.get(), consumer, MeltingBonus.GOLD);
        this.fullOreMeltingStampingRecipes("uranium", RegistryManager.MOLTEN_URANIUM.FLUID.get(), consumer, MeltingBonus.LEAD);
        this.fullMeltingStampingRecipes("bronze", RegistryManager.MOLTEN_BRONZE.FLUID.get(), consumer);
        this.fullMeltingStampingRecipes("electrum", RegistryManager.MOLTEN_ELECTRUM.FLUID.get(), consumer);
        this.fullMeltingStampingRecipes("brass", RegistryManager.MOLTEN_BRASS.FLUID.get(), consumer);
        this.fullMeltingStampingRecipes("constantan", RegistryManager.MOLTEN_CONSTANTAN.FLUID.get(), consumer);
        this.fullMeltingStampingRecipes("invar", RegistryManager.MOLTEN_INVAR.FLUID.get(), consumer);
        MeltingRecipeBuilder.create(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL)).id(new ResourceLocation("embers", meltingFolder + "/soul_crude")).output(RegistryManager.SOUL_CRUDE.FLUID.get(), 100).save(consumer);
        MeltingRecipeBuilder.create(Items.SNOWBALL).id(new ResourceLocation("embers", meltingFolder + "/snow_melting")).output(Fluids.WATER, 250).save(consumer);
        StampingRecipeBuilder.create(RegistryManager.EMBER_GRIT.get()).domain("embers").folder(stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(new ItemStack(RegistryManager.EMBER_SHARD.get(), 6)).domain("embers").folder(stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(RegistryManager.EMBER_CRYSTAL.get()).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(new ItemStack(RegistryManager.ASH.get(), 8)).domain("embers").folder(stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(RegistryManager.ALCHEMICAL_WASTE.get()).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(new ItemStack(Items.BLAZE_POWDER, 4)).domain("embers").folder(stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(Tags.Items.RODS_BLAZE).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(Items.SAND).domain("embers").folder(stampingFolder).stamp(RegistryManager.FLAT_STAMP.get()).input(Items.GRAVEL).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(RegistryManager.IRON_ASPECTUS.get()).domain("embers").folder(stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(this.fluidTag("forge", "molten_iron"), 90).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(RegistryManager.COPPER_ASPECTUS.get()).domain("embers").folder(stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(this.fluidTag("forge", "molten_copper"), 90).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(RegistryManager.LEAD_ASPECTUS.get()).domain("embers").folder(stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(this.fluidTag("forge", "molten_lead"), 90).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(RegistryManager.SILVER_ASPECTUS.get()).domain("embers").folder(stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(this.fluidTag("forge", "molten_silver"), 90).save(ConsumerWrapperBuilder.wrap().build(consumer));
        StampingRecipeBuilder.create(RegistryManager.DAWNSTONE_ASPECTUS.get()).domain("embers").folder(stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).input(RegistryManager.EMBER_SHARD.get()).fluid(this.fluidTag("forge", "molten_dawnstone"), 90).save(ConsumerWrapperBuilder.wrap().build(consumer));
        MixingRecipeBuilder.create(RegistryManager.MOLTEN_DAWNSTONE.FLUID.get(), 4).folder(mixingFolder).input(EmbersFluidTags.MOLTEN_COPPER, 2).input(EmbersFluidTags.MOLTEN_GOLD, 2).save(consumer);
        MixingRecipeBuilder.create(RegistryManager.MOLTEN_BRONZE.FLUID.get(), 4).folder(mixingFolder).input(EmbersFluidTags.MOLTEN_COPPER, 3).input(EmbersFluidTags.MOLTEN_TIN, 1).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "ingots/bronze"))).build(consumer));
        MixingRecipeBuilder.create(RegistryManager.MOLTEN_ELECTRUM.FLUID.get(), 4).folder(mixingFolder).input(EmbersFluidTags.MOLTEN_SILVER, 2).input(EmbersFluidTags.MOLTEN_GOLD, 2).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "ingots/electrum"))).build(consumer));
        MixingRecipeBuilder.create(RegistryManager.MOLTEN_BRASS.FLUID.get(), 4).folder(mixingFolder).input(EmbersFluidTags.MOLTEN_COPPER, 2).input(EmbersFluidTags.MOLTEN_ZINC, 2).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "ingots/brass"))).build(consumer));
        MixingRecipeBuilder.create(RegistryManager.MOLTEN_CONSTANTAN.FLUID.get(), 4).folder(mixingFolder).input(EmbersFluidTags.MOLTEN_COPPER, 2).input(EmbersFluidTags.MOLTEN_NICKEL, 2).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "ingots/constantan"))).build(consumer));
        MixingRecipeBuilder.create(RegistryManager.MOLTEN_INVAR.FLUID.get(), 3).folder(mixingFolder).input(EmbersFluidTags.MOLTEN_IRON, 2).input(EmbersFluidTags.MOLTEN_NICKEL, 1).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "ingots/invar"))).build(consumer));
        MixingRecipeBuilder.create(RegistryManager.DWARVEN_OIL.FLUID.get(), 10).id(new ResourceLocation("embers", mixingFolder + "/dwarven_oil_steam")).input(RegistryManager.SOUL_CRUDE.FLUID.get(), 5).input(RegistryManager.STEAM.FLUID.get(), 20).save(consumer);
        MixingRecipeBuilder.create(RegistryManager.DWARVEN_OIL.FLUID.get(), 30).id(new ResourceLocation("embers", mixingFolder + "/dwarven_oil")).input(RegistryManager.SOUL_CRUDE.FLUID.get(), 10).input(RegistryManager.DWARVEN_GAS.FLUID.get(), 5).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.DAWNSTONE_BLOCK).domain("embers").folder(coefficientFolder).coefficient(1.5).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.LEAD_BLOCK).domain("embers").folder(coefficientFolder).coefficient(2.625).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.BRONZE_BLOCK).domain("embers").folder(coefficientFolder).coefficient(2.625).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(EmbersBlockTags.BRONZE_BLOCK)).build(consumer));
        MetalCoefficientRecipeBuilder.create(Tags.Blocks.STORAGE_BLOCKS_IRON).domain("embers").folder(coefficientFolder).coefficient(2.625).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.NICKEL_BLOCK).domain("embers").folder(coefficientFolder).coefficient(2.85).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(EmbersBlockTags.NICKEL_BLOCK)).build(consumer));
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.TIN_BLOCK).domain("embers").folder(coefficientFolder).coefficient(2.85).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(EmbersBlockTags.TIN_BLOCK)).build(consumer));
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.ALUMINUM_BLOCK).domain("embers").folder(coefficientFolder).coefficient(2.85).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(EmbersBlockTags.ALUMINUM_BLOCK)).build(consumer));
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.SILVER_BLOCK).domain("embers").folder(coefficientFolder).coefficient(3.0).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.ELECTRUM_BLOCK).domain("embers").folder(coefficientFolder).coefficient(3.0).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(EmbersBlockTags.ELECTRUM_BLOCK)).build(consumer));
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.PRISTINE_COPPER).domain("embers").folder(coefficientFolder).coefficient(3.0).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.EXPOSED_COPPER).domain("embers").folder(coefficientFolder).coefficient(2.5).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.WEATHERED_COPPER).domain("embers").folder(coefficientFolder).coefficient(2.0).save(consumer);
        MetalCoefficientRecipeBuilder.create(EmbersBlockTags.OXIDIZED_COPPER).domain("embers").folder(coefficientFolder).coefficient(1.5).save(consumer);
        MetalCoefficientRecipeBuilder.create(Tags.Blocks.STORAGE_BLOCKS_GOLD).domain("embers").folder(coefficientFolder).coefficient(3.0).save(consumer);
        AlchemyRecipeBuilder.create(new ItemStack(Items.NETHERRACK, 4)).tablet(RegistryManager.EMBER_GRIT.get()).domain("embers").folder(alchemyFolder).inputs(Items.COBBLESTONE, Items.COBBLESTONE, Items.COBBLESTONE, Items.COBBLESTONE).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(new ItemStack(Items.SOUL_SAND, 4)).tablet(RegistryManager.ASH.get()).domain("embers").folder(alchemyFolder).inputs(Items.SAND, Items.SAND, Items.SAND, Items.SAND).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(new ItemStack(RegistryManager.ARCHAIC_BRICK.get(), 5)).tablet(RegistryManager.ARCHAIC_BRICK.get()).folder(alchemyFolder).inputs(Items.SOUL_SAND, Items.CLAY_BALL, Items.CLAY_BALL).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.ANCIENT_MOTIVE_CORE.get()).tablet(RegistryManager.EMBER_SHARD.get()).folder(alchemyFolder).inputs(RegistryManager.ARCHAIC_BRICK.get(), RegistryManager.ARCHAIC_BRICK.get(), RegistryManager.ARCHAIC_BRICK.get()).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.CODEBREAKING_SLATE.get()).tablet(RegistryManager.EMBER_GRIT.get()).folder(alchemyFolder).inputs(RegistryManager.CAMINITE_PLATE.get(), RegistryManager.ARCHAIC_BRICK.get(), RegistryManager.ARCHAIC_BRICK.get()).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.ASHEN_FABRIC.get()).tablet(ItemTags.WOOL).folder(alchemyFolder).inputs(EmbersItemTags.ASH_DUST, EmbersItemTags.ASH_DUST, Tags.Items.STRING, Tags.Items.STRING).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).tablet(RegistryManager.EMBER_CRYSTAL.get()).folder(alchemyFolder).inputs(Ingredient.of(Tags.Items.GUNPOWDER), Ingredient.of(RegistryManager.EMBER_SHARD.get()), Ingredient.of(RegistryManager.EMBER_SHARD.get()), Ingredient.of(RegistryManager.EMBER_SHARD.get())).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.WILDFIRE_CORE.get()).tablet(RegistryManager.ANCIENT_MOTIVE_CORE.get()).folder(alchemyFolder).inputs(Ingredient.of(EmbersItemTags.DAWNSTONE_INGOT), Ingredient.of(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()), Ingredient.of(EmbersItemTags.DAWNSTONE_INGOT), Ingredient.of(EmbersItemTags.COPPER_PLATE)).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.TYRFING.get()).tablet(RegistryManager.LEAD_TOOLS.SWORD.get()).folder(alchemyFolder).inputs(Tags.Items.STORAGE_BLOCKS_COAL, Tags.Items.OBSIDIAN, EmbersItemTags.LEAD_INGOT, EmbersItemTags.LEAD_INGOT).aspects(EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(new ItemStack(RegistryManager.ISOLATED_MATERIA.get(), 4)).tablet(Tags.Items.INGOTS_IRON).folder(alchemyFolder).inputs(Ingredient.of(Tags.Items.GEMS_QUARTZ), Ingredient.of(Items.CLAY_BALL), Ingredient.of(Tags.Items.GEMS_LAPIS)).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.CATALYTIC_PLUG.get()).tablet(EmbersItemTags.SILVER_INGOT).folder(alchemyFolder).inputs(Ingredient.of(RegistryManager.FLUID_PIPE.get()), Ingredient.of(Tags.Items.GLASS_SILICA), Ingredient.of(RegistryManager.FLUID_PIPE.get())).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.COPPER_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(Tags.Items.INGOTS_COPPER, Tags.Items.INGOTS_COPPER, Tags.Items.INGOTS_COPPER).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.IRON_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_IRON).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.GOLD_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_GOLD, Tags.Items.INGOTS_GOLD).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.LEAD_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(EmbersItemTags.LEAD_INGOT, EmbersItemTags.LEAD_INGOT, EmbersItemTags.LEAD_INGOT).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.SILVER_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(EmbersItemTags.SILVER_INGOT, EmbersItemTags.SILVER_INGOT, EmbersItemTags.SILVER_INGOT).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.NICKEL_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(EmbersItemTags.NICKEL_INGOT, EmbersItemTags.NICKEL_INGOT, EmbersItemTags.NICKEL_INGOT).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new AndCondition(this.tagReal(EmbersItemTags.NICKEL_INGOT), this.tagReal(EmbersItemTags.NICKEL_NUGGET))).build(consumer));
        AlchemyRecipeBuilder.create(RegistryManager.TIN_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(EmbersItemTags.TIN_INGOT, EmbersItemTags.TIN_INGOT, EmbersItemTags.TIN_INGOT).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new AndCondition(this.tagReal(EmbersItemTags.TIN_INGOT), this.tagReal(EmbersItemTags.TIN_NUGGET))).build(consumer));
        AlchemyRecipeBuilder.create(RegistryManager.ALUMINUM_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(EmbersItemTags.ALUMINUM_INGOT, EmbersItemTags.ALUMINUM_INGOT, EmbersItemTags.ALUMINUM_INGOT).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new AndCondition(this.tagReal(EmbersItemTags.ALUMINUM_INGOT), this.tagReal(EmbersItemTags.ALUMINUM_NUGGET))).build(consumer));
        AlchemyRecipeBuilder.create(RegistryManager.ZINC_CRYSTAL_SEED.ITEM.get()).tablet(EmbersItemTags.CRYSTAL_SEEDS).folder(alchemyFolder).inputs(EmbersItemTags.ZINC_INGOT, EmbersItemTags.ZINC_INGOT, EmbersItemTags.ZINC_INGOT).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new AndCondition(this.tagReal(EmbersItemTags.ZINC_INGOT), this.tagReal(EmbersItemTags.ZINC_NUGGET))).build(consumer));
        AlchemyRecipeBuilder.create(RegistryManager.INFLICTOR_GEM.get()).tablet(Tags.Items.GEMS_DIAMOND).folder(alchemyFolder).inputs(EmbersItemTags.DAWNSTONE_INGOT, ItemTags.COALS, ItemTags.COALS, ItemTags.COALS).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(new ItemStack(RegistryManager.ADHESIVE.get(), 6)).tablet(Items.CLAY_BALL).folder(alchemyFolder).inputs(Items.BONE_MEAL, Items.BONE_MEAL).aspects(EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.GLIMMER_CRYSTAL.get()).tablet(Tags.Items.GEMS_QUARTZ).folder(alchemyFolder).inputs(Ingredient.of(Tags.Items.GUNPOWDER), Ingredient.of(Tags.Items.GUNPOWDER), Ingredient.of(RegistryManager.EMBER_SHARD.get()), Ingredient.of(RegistryManager.EMBER_SHARD.get())).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.ENTROPIC_ENUMERATOR.get()).tablet(RegistryManager.ASHEN_TILE.get()).folder(alchemyFolder).inputs(Tags.Items.INGOTS_COPPER, Tags.Items.INGOTS_IRON, Tags.Items.INGOTS_GOLD, EmbersItemTags.LEAD_INGOT, EmbersItemTags.SILVER_INGOT, EmbersItemTags.DAWNSTONE_INGOT).aspects(EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.BLASTING_CORE.get()).tablet(Tags.Items.GUNPOWDER).folder(alchemyFolder).inputs(EmbersItemTags.IRON_PLATE, EmbersItemTags.IRON_PLATE, EmbersItemTags.IRON_PLATE, Tags.Items.INGOTS_COPPER).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.FLAME_BARRIER.get()).tablet(RegistryManager.EMBER_CRYSTAL.get()).folder(alchemyFolder).inputs(EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.SILVER_INGOT).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.ELDRITCH_INSIGNIA.get()).tablet(RegistryManager.ARCHAIC_CIRCUIT.get()).folder(alchemyFolder).inputs(ItemTags.COALS, EmbersItemTags.ARCHAIC_BRICK, ItemTags.COALS, EmbersItemTags.ARCHAIC_BRICK).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.IRON_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.INTELLIGENT_APPARATUS.get()).tablet(EmbersItemTags.COPPER_PLATE).folder(alchemyFolder).inputs(Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(RegistryManager.ARCHAIC_CIRCUIT.get()), Ingredient.of(Tags.Items.INGOTS_COPPER), Ingredient.of(RegistryManager.ARCHAIC_CIRCUIT.get())).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.FOCAL_LENS.get()).tablet(RegistryManager.EMBER_CRYSTAL.get()).folder(alchemyFolder).inputs(EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.SILVER_PLATE, EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.SILVER_PLATE).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.SHIFTING_SCALES.get()).tablet(RegistryManager.ASHEN_FABRIC.get()).folder(alchemyFolder).inputs(EmbersItemTags.LEAD_PLATE, EmbersItemTags.LEAD_PLATE, EmbersItemTags.LEAD_PLATE, EmbersItemTags.LEAD_PLATE, EmbersItemTags.LEAD_PLATE).aspects(EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.IRON_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(RegistryManager.WINDING_GEARS.get()).tablet(EmbersItemTags.DAWNSTONE_INGOT).folder(alchemyFolder).inputs(EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.DAWNSTONE_PLATE, EmbersItemTags.DAWNSTONE_PLATE).aspects(EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.DAWNSTONE_ASPECTUS).save(consumer);
        AlchemyRecipeBuilder.create(CuriosCompat.NONBELEIVER_AMULET.get()).tablet(CuriosCompat.ASHEN_AMULET.get()).folder(alchemyFolder).inputs(RegistryManager.ELDRITCH_INSIGNIA.get(), RegistryManager.ARCHAIC_CIRCUIT.get(), RegistryManager.ARCHAIC_BRICK.get(), RegistryManager.ARCHAIC_CIRCUIT.get()).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.LEAD_ASPECTUS, EmbersItemTags.IRON_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        AlchemyRecipeBuilder.create(CuriosCompat.EXPLOSION_CHARM.get()).tablet(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).folder(alchemyFolder).inputs(Ingredient.of(RegistryManager.ARCHAIC_BRICK.get()), Ingredient.of(RegistryManager.ARCHAIC_BRICK.get()), Ingredient.of(Tags.Items.LEATHER), Ingredient.of(RegistryManager.ARCHAIC_BRICK.get())).aspects(EmbersItemTags.DAWNSTONE_ASPECTUS, EmbersItemTags.SILVER_ASPECTUS, EmbersItemTags.COPPER_ASPECTUS, EmbersItemTags.IRON_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        AlchemyRecipeBuilder.create(CuriosCompat.EXPLOSION_PEDESTAL_ITEM.get()).tablet(RegistryManager.ALCHEMY_PEDESTAL_ITEM.get()).folder(alchemyFolder).inputs(CuriosCompat.EXPLOSION_CHARM.get()).aspects(EmbersItemTags.SILVER_ASPECTUS).save(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        BoilingRecipeBuilder.create(RegistryManager.STEAM.FLUID.get(), 5).folder(boilingFolder).input(FluidTags.WATER, 1).save(consumer);
        BoilingRecipeBuilder.create(RegistryManager.DWARVEN_GAS.FLUID.get(), 1).folder(boilingFolder).input(RegistryManager.DWARVEN_OIL.FLUID.get(), 1).save(consumer);
        GaseousFuelRecipeBuilder.create(RegistryManager.STEAM.FLUID.get(), 1).folder(gaseousFuelFolder).burnTime(1).powerMultiplier(2.0).save(consumer);
        GaseousFuelRecipeBuilder.create(RegistryManager.DWARVEN_GAS.FLUID.get(), 1).folder(gaseousFuelFolder).burnTime(5).powerMultiplier(2.5).save(consumer);
        CatalysisCombustionRecipeBuilder.create(RegistryManager.EMBER_GRIT.get()).catalysis().folder(catalysisFolder).multiplier(2.0).burnTime(400).save(consumer);
        CatalysisCombustionRecipeBuilder.create(Tags.Items.GUNPOWDER).catalysis().domain("embers").folder(catalysisFolder).multiplier(3.0).burnTime(400).save(consumer);
        CatalysisCombustionRecipeBuilder.create(Tags.Items.DUSTS_GLOWSTONE).catalysis().domain("embers").folder(catalysisFolder).multiplier(4.0).burnTime(400).save(consumer);
        CatalysisCombustionRecipeBuilder.create(ItemTags.COALS).combustion().domain("embers").folder(combustionFolder).multiplier(2.0).burnTime(400).save(consumer);
        CatalysisCombustionRecipeBuilder.create(Tags.Items.INGOTS_NETHER_BRICK).combustion().domain("embers").folder(combustionFolder).multiplier(3.0).burnTime(400).save(consumer);
        CatalysisCombustionRecipeBuilder.create(Items.BLAZE_POWDER).combustion().domain("embers").folder(combustionFolder).multiplier(4.0).burnTime(400).save(consumer);
        GenericRecipeBuilder.create(new AnvilRepairRecipe(new ResourceLocation("embers", anvilFolder + "/tool_repair"))).save(consumer);
        GenericRecipeBuilder.create(new AnvilRepairMateriaRecipe(new ResourceLocation("embers", anvilFolder + "/tool_materia_repair"))).save(consumer);
        GenericRecipeBuilder.create(new AnvilBreakdownRecipe(new ResourceLocation("embers", anvilFolder + "/tool_breakdown"))).save(consumer);
        GenericRecipeBuilder.create(new AnvilAugmentRemoveRecipe(new ResourceLocation("embers", anvilFolder + "/tool_augment_remove"))).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.CORE_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE), true)).input(RegistryManager.ANCIENT_MOTIVE_CORE.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.TINKER_LENS_AUGMENT).folder(anvilFolder).tool(AugmentIngredient.of(DifferenceIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_HELMETS), Ingredient.of(EmbersItemTags.TINKER_LENS_HELMETS)), RegistryManager.TINKER_LENS_AUGMENT, true)).input(RegistryManager.TINKER_LENS.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.SMOKY_LENS_AUGMENT).folder(anvilFolder).tool(AugmentIngredient.of(IntersectionIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_HELMETS), Ingredient.of(EmbersItemTags.TINKER_LENS_HELMETS)), RegistryManager.SMOKY_LENS_AUGMENT, true)).input(RegistryManager.SMOKY_TINKER_LENS.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.SUPERHEATER_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS))).input(RegistryManager.SUPERHEATER.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.CINDER_JET_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_ARMORS))).input(RegistryManager.CINDER_JET.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.BLASTING_CORE_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS_AND_ARMORS))).input(RegistryManager.BLASTING_CORE.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.CASTER_ORB_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS))).input(RegistryManager.CASTER_ORB.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.RESONATING_BELL_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS))).input(RegistryManager.RESONATING_BELL.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.FLAME_BARRIER_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_ARMORS))).input(RegistryManager.FLAME_BARRIER.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.ELDRITCH_INSIGNIA_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_ARMORS))).input(RegistryManager.ELDRITCH_INSIGNIA.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.INTELLIGENT_APPARATUS_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_ARMORS))).input(RegistryManager.INTELLIGENT_APPARATUS.get()).save(consumer);
        Ingredient projectileWeapons = CompoundIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_PROJECTILE_WEAPONS), AugmentIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS), RegistryManager.CASTER_ORB_AUGMENT));
        AnvilAugmentRecipeBuilder.create(RegistryManager.DIFFRACTION_BARREL_AUGMENT).folder(anvilFolder).tool(projectileWeapons).input(RegistryManager.DIFFRACTION_BARREL.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.FOCAL_LENS_AUGMENT).folder(anvilFolder).tool(projectileWeapons).input(RegistryManager.FOCAL_LENS.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.SHIFTING_SCALES_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_ARMORS))).input(RegistryManager.SHIFTING_SCALES.get()).save(consumer);
        AnvilAugmentRecipeBuilder.create(RegistryManager.WINDING_GEARS_AUGMENT).folder(anvilFolder).tool(HeatIngredient.of(CompoundIngredient.of(Ingredient.of(EmbersItemTags.AUGMENTABLE_TOOLS), Ingredient.of(EmbersItemTags.AUGMENTABLE_BOOTS)))).input(RegistryManager.WINDING_GEARS.get()).save(consumer);
        GemSocketRecipeBuilder.create(Tags.Items.STRING).id(new ResourceLocation("embers", "gem_socketing")).save(consumer);
        GenericRecipeBuilder.create(new GemUnsocketRecipe(new ResourceLocation("embers", "gem_unsocketing"))).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_CRYSTAL.get()).pattern("XXX").pattern("XXX").define('X', RegistryManager.EMBER_SHARD.get()).unlockedBy("has_shard", m_125977_(RegistryManager.EMBER_SHARD.get())).save(consumer, getResource("ember_shard_to_crystal"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegistryManager.EMBER_SHARD.get(), 6).requires(RegistryManager.EMBER_CRYSTAL.get()).unlockedBy("has_crystal", m_125977_(RegistryManager.EMBER_CRYSTAL.get())).save(consumer, getResource("ember_crystal_to_shard"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegistryManager.CAMINITE_BLEND.get(), 8).requires(Items.CLAY_BALL).requires(Items.CLAY_BALL).requires(Items.CLAY_BALL).requires(Items.CLAY_BALL).requires(Tags.Items.SAND).unlockedBy("has_clay", m_125977_(Items.CLAY_BALL)).save(consumer, getResource("caminite_blend"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RAW_CAMINITE_PLATE.get()).pattern("XX").pattern("XX").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("raw_caminite_plate"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RAW_FLAT_STAMP.get()).pattern("XXX").pattern("X X").pattern("XXX").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("raw_flat_stamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RAW_INGOT_STAMP.get()).pattern(" X ").pattern("X X").pattern(" X ").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("raw_ingot_stamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RAW_NUGGET_STAMP.get()).pattern("X X").pattern("X X").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("raw_nugget_stamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RAW_PLATE_STAMP.get()).pattern("X X").pattern("   ").pattern("X X").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("raw_plate_stamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RAW_GEAR_STAMP.get()).pattern("X X").pattern(" X ").pattern("X X").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "gears"))).build(consumer), getResource("raw_gear_stamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_BRICKS.get()).pattern("XX").pattern("XX").define('X', RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_brick", m_125977_(RegistryManager.CAMINITE_BRICK.get())).save(consumer, getResource("caminite_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_LARGE_BRICKS.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_brick", m_125977_(RegistryManager.CAMINITE_BRICK.get())).save(consumer, getResource("caminite_large_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_BRICKS.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.CAMINITE_LARGE_BRICKS.get()).unlockedBy("has_brick", m_125977_(RegistryManager.CAMINITE_BRICK.get())).save(consumer, getResource("caminite_bricks_reverse"));
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_LARGE_BRICKS.get(), RegistryManager.CAMINITE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_BRICKS.get(), RegistryManager.CAMINITE_LARGE_BRICKS.get());
        this.decoRecipes(RegistryManager.CAMINITE_BRICKS_DECO, consumer);
        this.decoRecipes(RegistryManager.CAMINITE_LARGE_BRICKS_DECO, consumer);
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.CAMINITE_BLEND.get()), RecipeCategory.MISC, RegistryManager.CAMINITE_BRICK.get(), 0.1F, 200).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("caminite_brick"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_CAMINITE_PLATE.get()), RecipeCategory.MISC, RegistryManager.CAMINITE_PLATE.get(), 0.1F, 200).unlockedBy("has_raw_plate", m_125977_(RegistryManager.RAW_CAMINITE_PLATE.get())).save(consumer, getResource("caminite_plate"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_FLAT_STAMP.get()), RecipeCategory.MISC, RegistryManager.FLAT_STAMP.get(), 0.1F, 200).unlockedBy("has_raw_flat_stamp", m_125977_(RegistryManager.RAW_FLAT_STAMP.get())).save(consumer, getResource("flat_stamp"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_INGOT_STAMP.get()), RecipeCategory.MISC, RegistryManager.INGOT_STAMP.get(), 0.1F, 200).unlockedBy("has_raw_ingot_stamp", m_125977_(RegistryManager.RAW_INGOT_STAMP.get())).save(consumer, getResource("ingot_stamp"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_NUGGET_STAMP.get()), RecipeCategory.MISC, RegistryManager.NUGGET_STAMP.get(), 0.1F, 200).unlockedBy("has_raw_nugget_stamp", m_125977_(RegistryManager.RAW_NUGGET_STAMP.get())).save(consumer, getResource("nugget_stamp"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_PLATE_STAMP.get()), RecipeCategory.MISC, RegistryManager.PLATE_STAMP.get(), 0.1F, 200).unlockedBy("has_raw_plate_stamp", m_125977_(RegistryManager.RAW_PLATE_STAMP.get())).save(consumer, getResource("plate_stamp"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_GEAR_STAMP.get()), RecipeCategory.MISC, RegistryManager.GEAR_STAMP.get(), 0.1F, 200).unlockedBy("has_raw_gear_stamp", m_125977_(RegistryManager.RAW_GEAR_STAMP.get())).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(this.itemTag("forge", "gears"))).build(consumer), getResource("gear_stamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.RAW_CAMINITE_BLOCK.get(), 3).pattern("XXX").pattern("XXX").pattern("XXX").define('X', RegistryManager.CAMINITE_BLEND.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("raw_caminite_block"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RegistryManager.RAW_CAMINITE_BLOCK.get()), RecipeCategory.MISC, RegistryManager.CAMINITE_LARGE_TILE.get(), 0.1F, 200).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BLEND.get())).save(consumer, getResource("caminite_large_tile_smelting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_TILES.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.CAMINITE_LARGE_TILE.get()).unlockedBy("has_tile", m_125977_(RegistryManager.CAMINITE_LARGE_TILE.get())).save(consumer, getResource("caminite_tiles"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_LARGE_TILE.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.CAMINITE_TILES.get()).unlockedBy("has_tile", m_125977_(RegistryManager.CAMINITE_LARGE_TILE.get())).save(consumer, getResource("caminite_large_tile_reverse"));
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_TILES.get(), RegistryManager.CAMINITE_LARGE_TILE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.CAMINITE_LARGE_TILE.get(), RegistryManager.CAMINITE_TILES.get());
        this.decoRecipes(RegistryManager.CAMINITE_LARGE_TILE_DECO, consumer);
        this.decoRecipes(RegistryManager.CAMINITE_TILES_DECO, consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_BRICKS.get()).pattern("XX").pattern("XX").define('X', RegistryManager.ARCHAIC_BRICK.get()).unlockedBy("has_brick", m_125977_(RegistryManager.ARCHAIC_BRICK.get())).save(consumer, getResource("archaic_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_LIGHT.get()).pattern(" X ").pattern("XSX").pattern(" X ").define('X', RegistryManager.ARCHAIC_BRICK.get()).define('S', RegistryManager.EMBER_SHARD.get()).unlockedBy("has_shard", m_125977_(RegistryManager.EMBER_SHARD.get())).save(consumer, getResource("archaic_light"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_EDGE.get(), 2).pattern("XXX").pattern("XSX").pattern("XXX").define('X', RegistryManager.ARCHAIC_BRICK.get()).define('S', RegistryManager.EMBER_SHARD.get()).unlockedBy("has_shard", m_125977_(RegistryManager.EMBER_SHARD.get())).save(consumer, getResource("archaic_edge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_TILE.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.ARCHAIC_BRICKS.get()).unlockedBy("has_bricks", m_125977_(RegistryManager.ARCHAIC_BRICKS.get())).save(consumer, getResource("archaic_tile"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_LARGE_BRICKS.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.ARCHAIC_TILE.get()).unlockedBy("has_bricks", m_125977_(RegistryManager.ARCHAIC_BRICKS.get())).save(consumer, getResource("archaic_large_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_BRICKS.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.ARCHAIC_LARGE_BRICKS.get()).unlockedBy("has_bricks", m_125977_(RegistryManager.ARCHAIC_BRICKS.get())).save(consumer, getResource("archaic_bricks_2"));
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_TILE.get(), RegistryManager.ARCHAIC_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_LARGE_BRICKS.get(), RegistryManager.ARCHAIC_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_BRICKS.get(), RegistryManager.ARCHAIC_TILE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_LARGE_BRICKS.get(), RegistryManager.ARCHAIC_TILE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_BRICKS.get(), RegistryManager.ARCHAIC_LARGE_BRICKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ARCHAIC_TILE.get(), RegistryManager.ARCHAIC_LARGE_BRICKS.get());
        this.decoRecipes(RegistryManager.ARCHAIC_BRICKS_DECO, consumer);
        this.decoRecipes(RegistryManager.ARCHAIC_TILE_DECO, consumer);
        this.decoRecipes(RegistryManager.ARCHAIC_LARGE_BRICKS_DECO, consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_STONE.get(), 4).pattern(" S ").pattern("SAS").pattern(" S ").define('S', Tags.Items.STONE).define('A', EmbersItemTags.ASH_DUST).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).save(consumer, getResource("ashen_stone"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_BRICK.get(), 4).pattern(" S ").pattern("SAS").pattern(" S ").define('S', ItemTags.STONE_BRICKS).define('A', EmbersItemTags.ASH_DUST).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).save(consumer, getResource("ashen_brick"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_BRICK.get(), 4).pattern("SS").pattern("SS").define('S', RegistryManager.ASHEN_STONE.get()).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).save(consumer, getResource("ashen_brick_2"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_TILE.get(), 4).pattern("SS").pattern("SS").define('S', RegistryManager.ASHEN_BRICK.get()).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).save(consumer, getResource("ashen_tile"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_STONE.get(), 4).pattern("SS").pattern("SS").define('S', RegistryManager.ASHEN_TILE.get()).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).save(consumer, getResource("ashen_stone_2"));
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_BRICK.get(), RegistryManager.ASHEN_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_TILE.get(), RegistryManager.ASHEN_STONE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_STONE.get(), RegistryManager.ASHEN_BRICK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_TILE.get(), RegistryManager.ASHEN_BRICK.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_STONE.get(), RegistryManager.ASHEN_TILE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.ASHEN_BRICK.get(), RegistryManager.ASHEN_TILE.get());
        this.decoRecipes(RegistryManager.ASHEN_STONE_DECO, consumer);
        this.decoRecipes(RegistryManager.ASHEN_BRICK_DECO, consumer);
        this.decoRecipes(RegistryManager.ASHEN_TILE_DECO, consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_PLANKS.get(), 8).pattern("PPP").pattern("PSP").pattern("PPP").define('P', ItemTags.PLANKS).define('S', Tags.Items.SLIMEBALLS).unlockedBy("has_slime", m_206406_(Tags.Items.SLIMEBALLS)).save(consumer, getResource("sealed_wood"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_TILE.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.SEALED_PLANKS.get()).unlockedBy("has_planks", m_125977_(RegistryManager.SEALED_PLANKS.get())).save(consumer, getResource("sealed_wood_tile"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_PLANKS.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.SEALED_WOOD_TILE.get()).unlockedBy("has_planks", m_125977_(RegistryManager.SEALED_PLANKS.get())).save(consumer, getResource("sealed_wood_reverse"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_PILLAR.get(), 2).pattern("X").pattern("X").define('X', RegistryManager.SEALED_PLANKS.get()).unlockedBy("has_planks", m_125977_(RegistryManager.SEALED_PLANKS.get())).save(consumer, getResource("sealed_wood_pillar"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, RegistryManager.REINFORCED_SEALED_PLANKS.get()).requires(RegistryManager.SEALED_PLANKS.get()).requires(Items.IRON_BARS).unlockedBy("has_planks", m_125977_(RegistryManager.SEALED_PLANKS.get())).save(consumer, getResource("reinforced_sealed_wood"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_KEG.get(), 2).pattern("X").pattern("X").define('X', RegistryManager.REINFORCED_SEALED_PLANKS.get()).unlockedBy("has_planks", m_125977_(RegistryManager.SEALED_PLANKS.get())).save(consumer, getResource("sealed_wood_keg"));
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_TILE.get(), RegistryManager.SEALED_PLANKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_PILLAR.get(), RegistryManager.SEALED_PLANKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_PLANKS.get(), RegistryManager.SEALED_WOOD_TILE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_PILLAR.get(), RegistryManager.SEALED_WOOD_TILE.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_TILE.get(), RegistryManager.SEALED_WOOD_PILLAR.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_PLANKS.get(), RegistryManager.SEALED_WOOD_PILLAR.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SEALED_WOOD_KEG.get(), RegistryManager.REINFORCED_SEALED_PLANKS.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.REINFORCED_SEALED_PLANKS.get(), RegistryManager.SEALED_WOOD_KEG.get());
        this.decoRecipes(RegistryManager.SEALED_PLANKS_DECO, consumer);
        this.decoRecipes(RegistryManager.SEALED_WOOD_TILE_DECO, consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.METAL_PLATFORM.get(), 4).pattern("XX").pattern("XX").define('X', RegistryManager.SOLIDIFIED_METAL.get()).unlockedBy("has_metal", m_125977_(RegistryManager.SOLIDIFIED_METAL.get())).save(consumer, getResource("metal_platform"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, RegistryManager.METAL_PLATFORM.get(), 1).pattern("X").pattern("X").define('X', RegistryManager.METAL_PLATFORM_DECO.slab.get()).unlockedBy("has_metal", m_125977_(RegistryManager.SOLIDIFIED_METAL.get())).save(consumer, getResource("metal_platform_reverse"));
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.METAL_PLATFORM.get(), RegistryManager.SOLIDIFIED_METAL.get());
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, RegistryManager.SOLIDIFIED_METAL.get(), RegistryManager.METAL_PLATFORM.get());
        this.decoRecipes(RegistryManager.METAL_PLATFORM_DECO, consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, RegistryManager.EMBER_LANTERN.get(), 4).pattern("P").pattern("E").pattern("I").define('E', RegistryManager.EMBER_SHARD.get()).define('P', this.itemTag("forge", "plates/iron")).define('I', this.itemTag("forge", "ingots/iron")).unlockedBy("has_shard", m_125977_(RegistryManager.EMBER_SHARD.get())).save(consumer, getResource("ember_lantern"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ANCIENT_CODEX.get()).pattern(" X ").pattern("XCX").pattern(" X ").define('X', RegistryManager.ARCHAIC_BRICK.get()).define('C', RegistryManager.ANCIENT_MOTIVE_CORE.get()).unlockedBy("has_core", m_125977_(RegistryManager.ANCIENT_MOTIVE_CORE.get())).save(consumer, getResource("ancient_codex"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.TINKER_HAMMER.get()).pattern("IBI").pattern("ISI").pattern(" S ").define('B', this.itemTag("forge", "ingots/lead")).define('I', this.itemTag("forge", "ingots/iron")).define('S', Tags.Items.RODS_WOODEN).unlockedBy("has_lead", m_206406_(this.itemTag("forge", "ingots/lead"))).save(consumer, getResource("tinker_hammer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.TINKER_LENS.get()).pattern("BE ").pattern("IPE").pattern("BE ").define('E', this.itemTag("forge", "nuggets/lead")).define('I', this.itemTag("forge", "plates/lead")).define('B', this.itemTag("forge", "ingots/iron")).define('P', Tags.Items.GLASS_SILICA).unlockedBy("has_lead_plate", m_206406_(this.itemTag("forge", "plates/lead"))).save(consumer, getResource("tinker_lens"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.SMOKY_TINKER_LENS.get()).pattern(" A ").pattern("APA").pattern(" A ").define('A', EmbersItemTags.ASH_DUST).define('P', RegistryManager.TINKER_LENS.get()).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).save(consumer, getResource("smoky_tinker_lens"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.ATMOSPHERIC_GAUGE.get()).pattern(" I ").pattern("CRC").pattern("CIC").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('R', this.itemTag("forge", "dusts/redstone")).unlockedBy("has_redstone", m_206406_(this.itemTag("forge", "dusts/redstone"))).save(consumer, getResource("atmospheric_gauge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.EMBER_JAR.get()).pattern(" C ").pattern("ISI").pattern(" G ").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('S', RegistryManager.EMBER_SHARD.get()).define('G', Tags.Items.GLASS_SILICA).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("ember_jar"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.EMBER_CARTRIDGE.get()).pattern("ICI").pattern("GSG").pattern(" G ").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('S', RegistryManager.EMBER_CRYSTAL.get()).define('G', Tags.Items.GLASS_SILICA).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("ember_cartridge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CuriosCompat.EMBER_BULB.get()).pattern(" CI").pattern("GSG").pattern(" G ").define('I', this.itemTag("forge", "ingots/lead")).define('C', this.itemTag("forge", "plates/dawnstone")).define('S', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).define('G', Tags.Items.GLASS_SILICA).unlockedBy("has_cluster", m_125977_(RegistryManager.EMBER_CRYSTAL_CLUSTER.get())).m_176498_(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.CLOCKWORK_PICKAXE.get()).pattern("ISI").pattern(" C ").pattern(" W ").define('C', this.itemTag("forge", "ingots/copper")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('S', RegistryManager.EMBER_SHARD.get()).define('W', Tags.Items.RODS_WOODEN).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("clockwork_pickaxe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.CLOCKWORK_AXE.get()).pattern("PCP").pattern("ISI").pattern(" W ").define('C', this.itemTag("forge", "plates/copper")).define('P', this.itemTag("forge", "plates/dawnstone")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('S', RegistryManager.EMBER_SHARD.get()).define('W', Tags.Items.RODS_WOODEN).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("clockwork_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, RegistryManager.GRANDHAMMER.get()).pattern("BIB").pattern(" C ").pattern(" W ").define('C', this.itemTag("forge", "ingots/copper")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('B', this.itemTag("forge", "storage_blocks/dawnstone")).define('W', Tags.Items.RODS_WOODEN).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("grandhammer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RegistryManager.BLAZING_RAY.get()).pattern(" DP").pattern("DPI").pattern("SW ").define('I', this.itemTag("forge", "ingots/iron")).define('D', this.itemTag("forge", "ingots/dawnstone")).define('P', this.itemTag("forge", "plates/dawnstone")).define('S', RegistryManager.EMBER_SHARD.get()).define('W', Tags.Items.RODS_WOODEN).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("blazing_ray"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RegistryManager.CINDER_STAFF.get()).pattern("SES").pattern("IWI").pattern(" W ").define('S', this.itemTag("forge", "plates/silver")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('E', RegistryManager.EMBER_SHARD.get()).define('W', Tags.Items.RODS_WOODEN).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("cinder_staff"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RegistryManager.ASHEN_GOGGLES.get()).pattern(" S ").pattern("C C").pattern("DCD").define('S', Tags.Items.STRING).define('D', this.itemTag("forge", "ingots/dawnstone")).define('C', RegistryManager.ASHEN_FABRIC.get()).unlockedBy("has_ashen_fabric", m_125977_(RegistryManager.ASHEN_FABRIC.get())).save(consumer, getResource("ashen_goggles"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RegistryManager.ASHEN_CLOAK.get()).pattern("P P").pattern("CDC").pattern("CDC").define('D', this.itemTag("forge", "ingots/dawnstone")).define('P', this.itemTag("forge", "plates/dawnstone")).define('C', RegistryManager.ASHEN_FABRIC.get()).unlockedBy("has_ashen_fabric", m_125977_(RegistryManager.ASHEN_FABRIC.get())).save(consumer, getResource("ashen_cloak"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RegistryManager.ASHEN_LEGGINGS.get()).pattern("CCC").pattern("D D").pattern("D D").define('D', this.itemTag("forge", "ingots/dawnstone")).define('C', RegistryManager.ASHEN_FABRIC.get()).unlockedBy("has_ashen_fabric", m_125977_(RegistryManager.ASHEN_FABRIC.get())).save(consumer, getResource("ashen_leggings"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, RegistryManager.ASHEN_BOOTS.get()).pattern("C C").pattern("C C").pattern("C C").define('C', RegistryManager.ASHEN_FABRIC.get()).unlockedBy("has_ashen_fabric", m_125977_(RegistryManager.ASHEN_FABRIC.get())).save(consumer, getResource("ashen_boots"));
        this.toolRecipes(RegistryManager.LEAD_TOOLS, EmbersItemTags.LEAD_INGOT, RegistryManager.LEAD_NUGGET.get(), consumer);
        this.toolRecipes(RegistryManager.SILVER_TOOLS, EmbersItemTags.SILVER_INGOT, RegistryManager.SILVER_NUGGET.get(), consumer);
        this.toolRecipes(RegistryManager.DAWNSTONE_TOOLS, EmbersItemTags.DAWNSTONE_INGOT, RegistryManager.DAWNSTONE_NUGGET.get(), consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CuriosCompat.EMBER_RING.get()).pattern("CN ").pattern("N N").pattern(" N ").define('C', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).define('N', this.itemTag("forge", "ingots/dawnstone")).unlockedBy("has_cluster", m_125977_(RegistryManager.EMBER_CRYSTAL_CLUSTER.get())).m_176498_(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CuriosCompat.EMBER_BELT.get()).pattern("LIL").pattern("L L").pattern("PCP").define('C', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).define('I', this.itemTag("forge", "ingots/dawnstone")).define('P', this.itemTag("forge", "plates/dawnstone")).define('L', Tags.Items.LEATHER).unlockedBy("has_cluster", m_125977_(RegistryManager.EMBER_CRYSTAL_CLUSTER.get())).m_176498_(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CuriosCompat.EMBER_AMULET.get()).pattern(" L ").pattern("L L").pattern("NCN").define('C', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).define('N', this.itemTag("forge", "ingots/dawnstone")).define('L', Tags.Items.LEATHER).unlockedBy("has_cluster", m_125977_(RegistryManager.EMBER_CRYSTAL_CLUSTER.get())).m_176498_(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CuriosCompat.DAWNSTONE_MAIL.get()).pattern("P P").pattern("PPP").pattern("PPP").define('P', this.itemTag("forge", "plates/dawnstone")).unlockedBy("has_dawnstone_plate", m_206406_(this.itemTag("forge", "plates/dawnstone"))).m_176498_(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, CuriosCompat.ASHEN_AMULET.get()).pattern(" L ").pattern("L L").pattern("NCN").define('C', EmbersItemTags.ASH_DUST).define('N', RegistryManager.ARCHAIC_BRICK.get()).define('L', Tags.Items.LEATHER).unlockedBy("has_ash", m_206406_(EmbersItemTags.ASH_DUST)).m_176498_(ConsumerWrapperBuilder.wrap().addCondition(new ModLoadedCondition("curios")).build(consumer));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MECHANICAL_CORE.get()).pattern("IBI").pattern(" P ").pattern("I I").define('P', this.itemTag("forge", "plates/lead")).define('I', this.itemTag("forge", "ingots/iron")).define('B', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_lead_plate", m_206406_(this.itemTag("forge", "plates/lead"))).save(consumer, getResource("mechanical_core"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_BORE.get()).pattern("YCY").pattern("YBY").pattern("III").define('B', RegistryManager.MECHANICAL_CORE.get()).define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('Y', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_mech_core", m_125977_(RegistryManager.MECHANICAL_CORE.get())).save(consumer, getResource("ember_bore"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, RegistryManager.CAMINITE_LEVER.get(), 4).pattern("S").pattern("P").define('S', Tags.Items.RODS_WOODEN).define('P', RegistryManager.CAMINITE_PLATE.get()).unlockedBy("has_caminite_plate", m_125977_(RegistryManager.CAMINITE_PLATE.get())).save(consumer, getResource("caminite_lever"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, RegistryManager.CAMINITE_BUTTON.get()).requires(RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_caminite", m_125977_(RegistryManager.CAMINITE_BRICK.get())).save(consumer, getResource("caminite_button"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_EMITTER.get(), 4).pattern(" C ").pattern(" C ").pattern("IPI").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('P', RegistryManager.CAMINITE_PLATE.get()).unlockedBy("has_caminite_plate", m_125977_(RegistryManager.CAMINITE_PLATE.get())).save(consumer, getResource("ember_emitter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_RECEIVER.get(), 4).pattern("I I").pattern("CPC").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('P', RegistryManager.CAMINITE_PLATE.get()).unlockedBy("has_caminite_plate", m_125977_(RegistryManager.CAMINITE_PLATE.get())).save(consumer, getResource("ember_receiver"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_ACTIVATOR.get()).pattern("CCC").pattern("CCC").pattern("IFI").define('I', this.itemTag("forge", "plates/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('F', Items.FURNACE).unlockedBy("has_iron_plate", m_206406_(this.itemTag("forge", "plates/iron"))).save(consumer, getResource("ember_activator"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.PRESSURE_REFINERY.get()).pattern("CCC").pattern("IDI").pattern("IBI").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('B', this.itemTag("forge", "storage_blocks/copper")).define('D', this.itemTag("forge", "ingots/dawnstone")).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("pressure_refinery"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.COPPER_CELL.get()).pattern("BIB").pattern("ICI").pattern("BIB").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "storage_blocks/copper")).define('B', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_caminite_bricks", m_125977_(RegistryManager.CAMINITE_BRICKS.get())).save(consumer, getResource("copper_cell"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MELTER.get()).pattern("BPB").pattern("BCB").pattern("IFI").define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('P', RegistryManager.CAMINITE_PLATE.get()).define('B', RegistryManager.CAMINITE_BRICKS.get()).define('F', Items.FURNACE).unlockedBy("has_caminite_bricks", m_125977_(RegistryManager.CAMINITE_BRICKS.get())).save(consumer, getResource("melter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.FLUID_VESSEL.get()).pattern("B B").pattern("P P").pattern("BIB").define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/iron")).define('B', RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_caminite_brick", m_125977_(RegistryManager.CAMINITE_BRICK.get())).save(consumer, getResource("fluid_vessel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.STAMPER.get()).pattern("XCX").pattern("XBX").pattern("X X").define('B', this.itemTag("forge", "storage_blocks/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('X', RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_iron_block", m_206406_(this.itemTag("forge", "storage_blocks/iron"))).save(consumer, getResource("stamper"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.STAMP_BASE.get()).pattern("I I").pattern("XBX").define('I', this.itemTag("forge", "ingots/iron")).define('B', Items.BUCKET).define('X', RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_bucket", m_125977_(Items.BUCKET)).save(consumer, getResource("stamp_base"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_DIAL.get()).pattern("P").pattern("C").define('P', Items.PAPER).define('C', this.itemTag("forge", "plates/copper")).unlockedBy("has_copper_plate", m_206406_(this.itemTag("forge", "plates/copper"))).save(consumer, getResource("ember_dial"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ITEM_DIAL.get()).pattern("P").pattern("L").define('P', Items.PAPER).define('L', this.itemTag("forge", "plates/lead")).unlockedBy("has_lead_plate", m_206406_(this.itemTag("forge", "plates/lead"))).save(consumer, getResource("item_dial"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.FLUID_DIAL.get()).pattern("P").pattern("I").define('P', Items.PAPER).define('I', this.itemTag("forge", "plates/iron")).unlockedBy("has_iron_plate", m_206406_(this.itemTag("forge", "plates/iron"))).save(consumer, getResource("fluid_dial"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CLOCKWORK_ATTENUATOR.get()).pattern("P").pattern("I").define('P', Items.PAPER).define('I', this.itemTag("forge", "plates/silver")).unlockedBy("has_silver_plate", m_206406_(this.itemTag("forge", "plates/silver"))).save(consumer, getResource("clockwork_attenuator"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.FLUID_PIPE.get(), 8).pattern("IPI").define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/iron")).unlockedBy("has_iron_plate", m_206406_(this.itemTag("forge", "plates/iron"))).save(consumer, getResource("fluid_pipe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ITEM_PIPE.get(), 8).pattern("IPI").define('I', this.itemTag("forge", "ingots/lead")).define('P', this.itemTag("forge", "plates/lead")).unlockedBy("has_lead_plate", m_206406_(this.itemTag("forge", "plates/lead"))).save(consumer, getResource("item_pipe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.FLUID_EXTRACTOR.get()).pattern(" R ").pattern("PBP").pattern(" R ").define('P', RegistryManager.FLUID_PIPE.get()).define('B', RegistryManager.CAMINITE_PLATE.get()).define('R', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_fluid_pipe", m_125977_(RegistryManager.FLUID_PIPE.get())).save(consumer, getResource("fluid_extractor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ITEM_EXTRACTOR.get()).pattern(" R ").pattern("PBP").pattern(" R ").define('P', RegistryManager.ITEM_PIPE.get()).define('B', RegistryManager.CAMINITE_PLATE.get()).define('R', Tags.Items.DUSTS_REDSTONE).unlockedBy("has_item_pipe", m_125977_(RegistryManager.ITEM_PIPE.get())).save(consumer, getResource("item_extractor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ITEM_DROPPER.get()).pattern(" P ").pattern("I I").define('I', this.itemTag("forge", "ingots/lead")).define('P', RegistryManager.ITEM_PIPE.get()).unlockedBy("has_item_pipe", m_125977_(RegistryManager.ITEM_PIPE.get())).save(consumer, getResource("item_dropper"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.BIN.get()).pattern("I I").pattern("I I").pattern("IPI").define('I', this.itemTag("forge", "ingots/lead")).define('P', this.itemTag("forge", "plates/lead")).unlockedBy("has_lead_plate", m_206406_(this.itemTag("forge", "plates/lead"))).save(consumer, getResource("bin"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MIXER_CENTRIFUGE.get()).pattern("PPP").pattern("PCP").pattern("IMI").define('P', this.itemTag("forge", "plates/iron")).define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).define('M', RegistryManager.MECHANICAL_CORE.get()).unlockedBy("has_melter", m_125977_(RegistryManager.MELTER.get())).save(consumer, getResource("mixer_centrifuge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_EJECTOR.get()).pattern("P").pattern("E").pattern("I").define('P', this.itemTag("forge", "plates/dawnstone")).define('I', this.itemTag("forge", "ingots/iron")).define('E', RegistryManager.EMBER_EMITTER.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("ember_ejector"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_FUNNEL.get()).pattern("P P").pattern("CRC").pattern(" P ").define('P', this.itemTag("forge", "plates/dawnstone")).define('C', this.itemTag("forge", "ingots/copper")).define('R', RegistryManager.EMBER_RECEIVER.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("ember_funnel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_RELAY.get(), 4).pattern(" C ").pattern("C C").pattern(" P ").define('P', this.itemTag("forge", "plates/iron")).define('C', this.itemTag("forge", "ingots/copper")).unlockedBy("has_iron_plate", m_206406_(this.itemTag("forge", "plates/iron"))).save(consumer, getResource("ember_relay"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MIRROR_RELAY.get(), 4).pattern(" P ").pattern("S S").define('P', this.itemTag("forge", "plates/lead")).define('S', this.itemTag("forge", "ingots/silver")).unlockedBy("has_silver", m_206406_(this.itemTag("forge", "ingots/silver"))).save(consumer, getResource("mirror_relay"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.BEAM_SPLITTER.get()).pattern(" D ").pattern("CPC").pattern(" I ").define('C', this.itemTag("forge", "ingots/copper")).define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/iron")).define('D', this.itemTag("forge", "ingots/dawnstone")).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("beam_splitter"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ITEM_VACUUM.get()).pattern(" II").pattern("P  ").pattern(" II").define('I', this.itemTag("forge", "ingots/lead")).define('P', RegistryManager.ITEM_PIPE.get()).unlockedBy("has_item_pipe", m_125977_(RegistryManager.ITEM_PIPE.get())).save(consumer, getResource("item_vacuum"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.HEARTH_COIL.get()).pattern("PPP").pattern("ICI").pattern(" B ").define('B', RegistryManager.MECHANICAL_CORE.get()).define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/copper")).define('C', this.itemTag("forge", "storage_blocks/copper")).unlockedBy("has_mech_core", m_125977_(RegistryManager.MECHANICAL_CORE.get())).save(consumer, getResource("hearth_coil"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RESERVOIR.get()).pattern("B B").pattern("I I").pattern("BTB").define('I', this.itemTag("forge", "ingots/iron")).define('B', RegistryManager.CAMINITE_BRICKS.get()).define('T', RegistryManager.FLUID_VESSEL.get()).unlockedBy("has_vessel", m_125977_(RegistryManager.FLUID_VESSEL.get())).save(consumer, getResource("reservoir"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CAMINITE_RING.get()).pattern("BBB").pattern("B B").pattern("BBB").define('B', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_reservoir", m_125977_(RegistryManager.RESERVOIR.get())).save(consumer, getResource("caminite_ring"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CAMINITE_GAUGE.get()).pattern("BBB").pattern("G G").pattern("BBB").define('B', RegistryManager.CAMINITE_BRICKS.get()).define('G', Tags.Items.GLASS_SILICA).unlockedBy("has_reservoir", m_125977_(RegistryManager.RESERVOIR.get())).save(consumer, getResource("caminite_gauge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CAMINITE_VALVE.get()).pattern("BBB").pattern("P P").pattern("BBB").define('B', RegistryManager.CAMINITE_BRICKS.get()).define('P', RegistryManager.FLUID_PIPE.get()).unlockedBy("has_reservoir", m_125977_(RegistryManager.RESERVOIR.get())).save(consumer, getResource("caminite_valve"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CRYSTAL_CELL.get()).pattern(" E ").pattern("DED").pattern("CBC").define('C', this.itemTag("forge", "storage_blocks/copper")).define('B', this.itemTag("forge", "storage_blocks/dawnstone")).define('D', this.itemTag("forge", "plates/dawnstone")).define('E', RegistryManager.EMBER_CRYSTAL.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("crystal_cell"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.GEOLOGIC_SEPARATOR.get()).pattern("  B").pattern("GIG").define('B', this.itemTag("forge", "storage_blocks/silver")).define('G', RegistryManager.CAMINITE_BRICK.get()).define('I', RegistryManager.FLUID_VESSEL.get()).unlockedBy("has_silver", m_206406_(this.itemTag("forge", "ingots/silver"))).save(consumer, getResource("geologic_separator"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.COPPER_CHARGER.get()).pattern(" X ").pattern("DCD").pattern("IPI").define('D', this.itemTag("forge", "ingots/dawnstone")).define('P', this.itemTag("forge", "plates/copper")).define('C', this.itemTag("forge", "ingots/copper")).define('X', this.itemTag("forge", "plates/iron")).define('I', this.itemTag("forge", "ingots/iron")).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("copper_charger"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_SIPHON.get()).pattern("BGB").pattern("XGX").pattern("BBB").define('G', this.itemTag("forge", "ingots/copper")).define('X', this.itemTag("forge", "plates/silver")).define('B', RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_charger", m_125977_(RegistryManager.COPPER_CHARGER.get())).save(consumer, getResource("ember_siphon"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ITEM_TRANSFER.get(), 3).pattern("PLP").pattern("ILI").pattern("I I").define('P', this.itemTag("forge", "plates/lead")).define('I', this.itemTag("forge", "ingots/lead")).define('L', RegistryManager.ITEM_PIPE.get()).unlockedBy("has_item_pipe", m_125977_(RegistryManager.ITEM_PIPE.get())).save(consumer, getResource("item_transfer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.FLUID_TRANSFER.get(), 3).pattern("PLP").pattern("ILI").pattern("I I").define('P', this.itemTag("forge", "plates/iron")).define('I', this.itemTag("forge", "ingots/iron")).define('L', RegistryManager.FLUID_PIPE.get()).unlockedBy("has_fluid_pipe", m_125977_(RegistryManager.FLUID_PIPE.get())).save(consumer, getResource("fluid_transfer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ALCHEMY_PEDESTAL.get()).pattern("D D").pattern("ICI").pattern("SBS").define('D', this.itemTag("forge", "plates/dawnstone")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('B', this.itemTag("forge", "storage_blocks/copper")).define('C', RegistryManager.EMBER_CRYSTAL.get()).define('S', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("alchemy_pedestal"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ALCHEMY_TABLET.get()).pattern(" D ").pattern("SXS").pattern("SIS").define('D', this.itemTag("forge", "plates/dawnstone")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('X', this.itemTag("forge", "plates/copper")).define('S', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("alchemy_tablet"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.BEAM_CANNON.get()).pattern("PSP").pattern("PSP").pattern("IBI").define('P', this.itemTag("forge", "plates/copper")).define('I', this.itemTag("forge", "ingots/dawnstone")).define('B', RegistryManager.CAMINITE_BRICKS.get()).define('S', RegistryManager.EMBER_CRYSTAL.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("beam_cannon"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MECHANICAL_PUMP.get()).pattern("EPE").pattern("PPP").pattern("BIB").define('E', RegistryManager.FLUID_PIPE.get()).define('I', RegistryManager.FLUID_EXTRACTOR.get()).define('P', this.itemTag("forge", "plates/iron")).define('B', RegistryManager.CAMINITE_BRICK.get()).unlockedBy("has_extractor", m_125977_(RegistryManager.FLUID_EXTRACTOR.get())).save(consumer, getResource("mechanical_pump"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MINI_BOILER.get()).pattern("PPP").pattern("E P").pattern("PPP").define('E', this.itemTag("forge", "ingots/copper")).define('P', this.itemTag("forge", "plates/iron")).unlockedBy("has_iron_plate", m_206406_(this.itemTag("forge", "plates/iron"))).save(consumer, getResource("mini_boiler"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.WILDFIRE_STIRLING.get()).pattern("XGX").pattern("XGX").pattern("BPB").define('G', this.itemTag("forge", "storage_blocks/copper")).define('X', this.itemTag("forge", "plates/dawnstone")).define('P', RegistryManager.WILDFIRE_CORE.get()).define('B', RegistryManager.EMBER_SHARD.get()).unlockedBy("has_wildfire_core", m_125977_(RegistryManager.WILDFIRE_CORE.get())).save(consumer, getResource("wildfire_stirling"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EMBER_INJECTOR.get()).pattern("S S").pattern("DCD").pattern("BPB").define('S', this.itemTag("forge", "ingots/silver")).define('P', this.itemTag("forge", "plates/silver")).define('D', this.itemTag("forge", "plates/dawnstone")).define('C', RegistryManager.WILDFIRE_CORE.get()).define('B', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_wildfire_core", m_125977_(RegistryManager.WILDFIRE_CORE.get())).save(consumer, getResource("ember_injector"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.FIELD_CHART.get()).pattern("BBB").pattern("BCB").pattern("BBB").define('C', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).define('B', RegistryManager.ARCHAIC_BRICK.get()).unlockedBy("has_cluster", m_125977_(RegistryManager.EMBER_CRYSTAL_CLUSTER.get())).save(consumer, getResource("field_chart"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Items.LEAD, 2).pattern("SS ").pattern("SB ").pattern("  S").define('S', Tags.Items.STRING).define('B', Tags.Items.SLIMEBALLS).unlockedBy("has_slime", m_206406_(Tags.Items.SLIMEBALLS)).save(consumer, getResource("lead_adhesive"));
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, Items.STICKY_PISTON).pattern("B").pattern("P").define('P', Items.PISTON).define('B', Tags.Items.SLIMEBALLS).unlockedBy("has_slime", m_206406_(Tags.Items.SLIMEBALLS)).save(consumer, getResource("sticky_piston_adhesive"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.IGNEM_REACTOR.get()).pattern("CCC").pattern("CWC").pattern("SBS").define('C', this.itemTag("forge", "ingots/copper")).define('S', this.itemTag("forge", "plates/silver")).define('W', RegistryManager.WILDFIRE_CORE.get()).define('B', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_wildfire_core", m_125977_(RegistryManager.WILDFIRE_CORE.get())).save(consumer, getResource("ignem_reactor"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CATALYSIS_CHAMBER.get()).pattern(" C ").pattern("PEP").pattern("CMC").define('C', this.itemTag("forge", "ingots/silver")).define('P', this.itemTag("forge", "plates/silver")).define('M', RegistryManager.MECHANICAL_CORE.get()).define('E', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).unlockedBy("has_wildfire_core", m_125977_(RegistryManager.WILDFIRE_CORE.get())).save(consumer, getResource("catalysis_chamber"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.COMBUSTION_CHAMBER.get()).pattern(" C ").pattern("PEP").pattern("CMC").define('C', this.itemTag("forge", "ingots/copper")).define('P', this.itemTag("forge", "plates/copper")).define('M', RegistryManager.MECHANICAL_CORE.get()).define('E', RegistryManager.EMBER_CRYSTAL_CLUSTER.get()).unlockedBy("has_wildfire_core", m_125977_(RegistryManager.WILDFIRE_CORE.get())).save(consumer, getResource("combustion_chamber"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.GLIMMER_LAMP.get()).pattern(" P ").pattern("IGI").pattern(" P ").define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/iron")).define('G', RegistryManager.GLIMMER_CRYSTAL.get()).unlockedBy("has_glimmer_crystal", m_125977_(RegistryManager.GLIMMER_CRYSTAL.get())).save(consumer, getResource("glimmer_lamp"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CINDER_PLINTH.get()).pattern(" P ").pattern("SFS").pattern("PBP").define('S', this.itemTag("forge", "ingots/silver")).define('P', this.itemTag("forge", "plates/lead")).define('B', RegistryManager.CAMINITE_BRICKS.get()).define('F', Blocks.FURNACE).unlockedBy("has_silver", m_206406_(this.itemTag("forge", "ingots/silver"))).save(consumer, getResource("cinder_plinth"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.DAWNSTONE_ANVIL.get()).pattern("BBB").pattern(" I ").pattern("CCC").define('I', this.itemTag("forge", "ingots/dawnstone")).define('B', this.itemTag("forge", "storage_blocks/dawnstone")).define('C', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_dawnstone", m_206406_(this.itemTag("forge", "ingots/dawnstone"))).save(consumer, getResource("dawnstone_anvil"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.AUTOMATIC_HAMMER.get()).pattern("BB ").pattern("CIX").pattern("BB ").define('I', this.itemTag("forge", "ingots/iron")).define('X', this.itemTag("forge", "storage_blocks/iron")).define('C', this.itemTag("forge", "storage_blocks/copper")).define('B', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_anvil", m_125977_(RegistryManager.DAWNSTONE_ANVIL.get())).save(consumer, getResource("automatic_hammer"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.INFERNO_FORGE.get()).pattern("BPB").pattern("DCD").pattern("SWS").define('P', this.itemTag("forge", "plates/iron")).define('C', this.itemTag("forge", "storage_blocks/copper")).define('D', this.itemTag("forge", "ingots/dawnstone")).define('B', this.itemTag("forge", "storage_blocks/dawnstone")).define('W', RegistryManager.WILDFIRE_CORE.get()).define('S', RegistryManager.CAMINITE_BRICKS.get()).unlockedBy("has_wildfire_core", m_125977_(RegistryManager.WILDFIRE_CORE.get())).save(consumer, getResource("inferno_forge"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ARCHAIC_CIRCUIT.get()).pattern(" B ").pattern("BCB").pattern(" B ").define('C', this.itemTag("forge", "ingots/copper")).define('B', RegistryManager.ARCHAIC_BRICK.get()).unlockedBy("has_archaic_brick", m_125977_(RegistryManager.ARCHAIC_BRICK.get())).save(consumer, getResource("archaic_circuit"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.SUPERHEATER.get()).pattern(" ID").pattern("CCI").pattern("CC ").define('I', this.itemTag("forge", "ingots/dawnstone")).define('D', this.itemTag("forge", "plates/dawnstone")).define('C', this.itemTag("forge", "ingots/copper")).unlockedBy("has_inferno_forge", m_125977_(RegistryManager.INFERNO_FORGE.get())).save(consumer, getResource("superheater"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CINDER_JET.get()).pattern("PP ").pattern("ISD").pattern("PP ").define('I', this.itemTag("forge", "ingots/iron")).define('D', this.itemTag("forge", "ingots/dawnstone")).define('P', this.itemTag("forge", "plates/dawnstone")).define('S', RegistryManager.EMBER_SHARD.get()).unlockedBy("has_inferno_forge", m_125977_(RegistryManager.INFERNO_FORGE.get())).save(consumer, getResource("cinder_jet"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CASTER_ORB.get()).pattern("DCD").pattern("D D").pattern(" P ").define('D', this.itemTag("forge", "ingots/dawnstone")).define('P', this.itemTag("forge", "plates/dawnstone")).define('C', RegistryManager.EMBER_CRYSTAL.get()).unlockedBy("has_inferno_forge", m_125977_(RegistryManager.INFERNO_FORGE.get())).save(consumer, getResource("caster_orb"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.RESONATING_BELL.get()).pattern("IIP").pattern(" SI").pattern("V I").define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/iron")).define('S', this.itemTag("forge", "ingots/silver")).define('V', this.itemTag("forge", "plates/silver")).unlockedBy("has_inferno_forge", m_125977_(RegistryManager.INFERNO_FORGE.get())).save(consumer, getResource("resonating_bell"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.DIFFRACTION_BARREL.get()).pattern("IPX").define('I', this.itemTag("forge", "ingots/iron")).define('P', this.itemTag("forge", "plates/iron")).define('X', RegistryManager.SUPERHEATER.get()).unlockedBy("has_superheater", m_125977_(RegistryManager.SUPERHEATER.get())).save(consumer, getResource("diffraction_barrel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.MNEMONIC_INSCRIBER.get()).pattern("C").pattern("A").define('C', RegistryManager.EMBER_CRYSTAL.get()).define('A', RegistryManager.INTELLIGENT_APPARATUS.get()).unlockedBy("has_apparatus", m_125977_(RegistryManager.INTELLIGENT_APPARATUS.get())).save(consumer, getResource("mnemonic_inscriber"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.CHAR_INSTILLER.get()).pattern("PPP").pattern("I I").pattern("PPP").define('I', this.itemTag("forge", "plates/iron")).define('P', RegistryManager.SEALED_PLANKS.get()).unlockedBy("has_hearth_coil", m_125977_(RegistryManager.HEARTH_COIL.get())).save(consumer, getResource("char_instiller"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.ATMOSPHERIC_BELLOWS.get()).pattern("CPC").pattern("LLL").pattern("CPC").define('C', this.itemTag("forge", "plates/copper")).define('L', Tags.Items.LEATHER).define('P', RegistryManager.SEALED_PLANKS.get()).unlockedBy("has_hearth_coil", m_125977_(RegistryManager.HEARTH_COIL.get())).save(consumer, getResource("atmospheric_bellows"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.HEAT_EXCHANGER.get()).pattern("C C").pattern("IPI").pattern("IPI").define('P', this.itemTag("forge", "plates/copper")).define('I', this.itemTag("forge", "ingots/iron")).define('C', this.itemTag("forge", "ingots/copper")).unlockedBy("has_activator", m_125977_(RegistryManager.EMBER_ACTIVATOR.get())).m_176498_(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.HEAT_INSULATION.get()).pattern("P").pattern("C").pattern("A").define('P', this.itemTag("forge", "plates/silver")).define('C', this.itemTag("forge", "storage_blocks/copper")).define('A', EmbersItemTags.ASHEN_STONE).unlockedBy("has_activator", m_125977_(RegistryManager.HEARTH_COIL.get())).m_176498_(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegistryManager.EXCAVATION_BUCKETS.get()).pattern("PPP").pattern("BIP").pattern("PPP").define('P', this.itemTag("forge", "plates/lead")).define('I', this.itemTag("forge", "ingots/iron")).define('B', this.itemTag("forge", "storage_blocks/lead")).unlockedBy("has_bore", m_125977_(RegistryManager.EMBER_BORE.get())).m_176498_(consumer);
    }

    public void fullOreRecipes(String name, ImmutableList<ItemLike> ores, Fluid fluid, Item raw, Item rawBlock, Item block, Item ingot, Item nugget, Item plate, Consumer<FinishedRecipe> consumer, MeltingBonus... bonusses) {
        this.fullMetalRecipes(name, fluid, block, ingot, nugget, plate, consumer);
        this.oreMeltingRecipes(name, fluid, consumer, bonusses);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, rawBlock).pattern("XXX").pattern("XYX").pattern("XXX").define('X', this.itemTag("forge", "raw_materials/" + name)).define('Y', raw).unlockedBy("has_raw", m_125977_(raw)).save(consumer, getResource(name + "_raw_to_raw_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, raw, 9).requires(rawBlock).unlockedBy("has_block", m_125977_(rawBlock)).save(consumer, getResource(name + "_raw_block_to_raw"));
        oreSmelting(consumer, ores, RecipeCategory.MISC, ingot, 0.7F, 200, name + "_ingot");
        oreBlasting(consumer, ores, RecipeCategory.MISC, ingot, 0.7F, 100, name + "_ingot");
    }

    public void fullMetalRecipes(String name, Fluid fluid, Item block, Item ingot, Item nugget, Item plate, Consumer<FinishedRecipe> consumer) {
        this.fullMeltingStampingRecipes(name, fluid, consumer);
        this.blockIngotNuggetCompression(name, block, ingot, nugget, consumer);
        this.plateHammerRecipe(name, plate, consumer);
    }

    public void plateHammerRecipe(String name, Item plate, Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, plate).requires(this.itemTag("forge", "ingots/" + name)).requires(this.itemTag("forge", "ingots/" + name)).requires(EmbersItemTags.TINKER_HAMMER).unlockedBy("has_ingot", m_206406_(this.itemTag("forge", "ingots/" + name))).save(consumer, getResource(name + "_plate_hammering"));
    }

    public void fullOreMeltingStampingRecipes(String name, Fluid fluid, Consumer<FinishedRecipe> consumer, MeltingBonus... bonusses) {
        this.oreMeltingRecipes(name, fluid, consumer, bonusses);
        this.fullMeltingStampingRecipes(name, fluid, consumer);
    }

    public void oreMeltingRecipes(String name, Fluid fluid, Consumer<FinishedRecipe> consumer, MeltingBonus... bonusses) {
        TagKey<Item> raw = this.itemTag("forge", "raw_materials/" + name);
        TagKey<Item> ore = this.itemTag("forge", "ores/" + name);
        TagKey<Item> rawBlock = this.itemTag("forge", "storage_blocks/raw_" + name);
        this.bonusRecipe((condition, bonus) -> MeltingRecipeBuilder.create(raw).domain("embers").folder(meltingFolder).bonusName(bonus.name).output(fluid, 120).bonus(bonus.fluid, bonus.amount).save(ConsumerWrapperBuilder.wrap().addCondition(condition).build(consumer)), this.tagReal(raw), bonusses);
        this.bonusRecipe((condition, bonus) -> MeltingRecipeBuilder.create(ore).domain("embers").folder(meltingFolder).bonusName(bonus.name).output(fluid, 240).bonus(bonus.fluid, bonus.amount * 2).save(ConsumerWrapperBuilder.wrap().addCondition(condition).build(consumer)), this.tagReal(ore), bonusses);
        this.bonusRecipe((condition, bonus) -> MeltingRecipeBuilder.create(rawBlock).domain("embers").folder(meltingFolder).bonusName(bonus.name).output(fluid, 1080).bonus(bonus.fluid, bonus.amount * 9).save(ConsumerWrapperBuilder.wrap().addCondition(condition).build(consumer)), this.tagReal(rawBlock), bonusses);
    }

    public void bonusRecipe(BiConsumer<ICondition, MeltingBonus> recipe, ICondition baseCondition, MeltingBonus... bonusses) {
        ArrayList<ICondition> conditions = new ArrayList();
        for (MeltingBonus bonus : bonusses) {
            ICondition condition = new TagEmptyCondition(this.itemTag("forge", "ingots/" + bonus.name).location());
            ArrayList<ICondition> recipeConditions = new ArrayList();
            recipeConditions.addAll(conditions);
            recipeConditions.add(baseCondition);
            if (bonus.optional) {
                recipeConditions.add(new NotCondition(condition));
            }
            recipe.accept(new AndCondition((ICondition[]) recipeConditions.toArray(new ICondition[recipeConditions.size()])), bonus);
            conditions.add(condition);
        }
    }

    public void fullMeltingStampingRecipes(String name, Fluid fluid, Consumer<FinishedRecipe> consumer) {
        TagKey<Item> ingot = this.itemTag("forge", "ingots/" + name);
        TagKey<Item> nugget = this.itemTag("forge", "nuggets/" + name);
        TagKey<Item> block = this.itemTag("forge", "storage_blocks/" + name);
        TagKey<Item> plate = this.itemTag("forge", "plates/" + name);
        TagKey<Item> gear = this.itemTag("forge", "gears/" + name);
        MeltingRecipeBuilder.create(ingot).domain("embers").folder(meltingFolder).output(fluid, 90).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(ingot)).build(consumer));
        MeltingRecipeBuilder.create(nugget).domain("embers").folder(meltingFolder).output(fluid, 10).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(nugget)).build(consumer));
        MeltingRecipeBuilder.create(block).domain("embers").folder(meltingFolder).output(fluid, 810).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(block)).build(consumer));
        MeltingRecipeBuilder.create(plate).domain("embers").folder(meltingFolder).output(fluid, 90).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(plate)).build(consumer));
        MeltingRecipeBuilder.create(gear).domain("embers").folder(meltingFolder).output(fluid, 180).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(gear)).build(consumer));
        StampingRecipeBuilder.create(ingot).domain("embers").folder(stampingFolder).stamp(RegistryManager.INGOT_STAMP.get()).fluid(this.fluidTag("forge", "molten_" + name), 90).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(ingot)).build(consumer));
        StampingRecipeBuilder.create(nugget).domain("embers").folder(stampingFolder).stamp(RegistryManager.NUGGET_STAMP.get()).fluid(this.fluidTag("forge", "molten_" + name), 10).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(nugget)).build(consumer));
        StampingRecipeBuilder.create(plate).domain("embers").folder(stampingFolder).stamp(RegistryManager.PLATE_STAMP.get()).fluid(this.fluidTag("forge", "molten_" + name), 90).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(plate)).build(consumer));
        StampingRecipeBuilder.create(gear).domain("embers").folder(stampingFolder).stamp(RegistryManager.GEAR_STAMP.get()).fluid(this.fluidTag("forge", "molten_" + name), 180).save(ConsumerWrapperBuilder.wrap().addCondition(this.tagReal(gear)).build(consumer));
    }

    public void blockIngotNuggetCompression(String name, Item block, Item ingot, Item nugget, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block).pattern("XXX").pattern("XYX").pattern("XXX").define('X', this.itemTag("forge", "ingots/" + name)).define('Y', ingot).unlockedBy("has_ingot", m_206406_(this.itemTag("forge", "ingots/" + name))).save(consumer, getResource(name + "_ingot_to_block"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9).requires(block).unlockedBy("has_block", m_125977_(block)).save(consumer, getResource(name + "_block_to_ingot"));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot).pattern("XXX").pattern("XYX").pattern("XXX").define('X', this.itemTag("forge", "nuggets/" + name)).define('Y', nugget).unlockedBy("has_nugget", m_206406_(this.itemTag("forge", "nuggets/" + name))).save(consumer, getResource(name + "_nugget_to_ingot"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9).requires(ingot).unlockedBy("has_ingot", m_125977_(ingot)).save(consumer, getResource(name + "_ingot_to_nugget"));
    }

    public void decoRecipes(RegistryManager.StoneDecoBlocks deco, Consumer<FinishedRecipe> consumer) {
        Item item = deco.block.get().asItem();
        if (deco.stairs != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, deco.stairs.get(), 4).pattern("X  ").pattern("XX ").pattern("XXX").define('X', item).unlockedBy("has_" + deco.name, m_125977_(item)).save(consumer, deco.stairs.getId());
            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, deco.stairs.get(), item);
        }
        if (deco.slab != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, deco.slab.get(), 6).pattern("XXX").define('X', item).unlockedBy("has_" + deco.name, m_125977_(item)).save(consumer, deco.slab.getId());
            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, deco.slab.get(), item, 2);
        }
        if (deco.wall != null) {
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, deco.wall.get(), 6).pattern("XXX").pattern("XXX").define('X', item).unlockedBy("has_" + deco.name, m_125977_(item)).save(consumer, deco.wall.getId());
            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, deco.wall.get(), item);
        }
    }

    protected static void stonecutterResultFromBase(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial) {
        stonecutterResultFromBase(pFinishedRecipeConsumer, pCategory, pResult, pMaterial, 1);
    }

    protected static void stonecutterResultFromBase(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(pMaterial), pCategory, pResult, pResultCount).unlockedBy(m_176602_(pMaterial), m_125977_(pMaterial)).save(pFinishedRecipeConsumer, getResource(m_176517_(pResult, pMaterial) + "_stonecutting"));
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(m_176602_(itemlike), m_125977_(itemlike)).save(pFinishedRecipeConsumer, getResource(m_176632_(pResult) + pRecipeName + "_" + m_176632_(itemlike)));
        }
    }

    public void toolRecipes(RegistryManager.ToolSet set, TagKey<Item> material, Item nugget, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(set.SWORD.get()), RecipeCategory.TOOLS, nugget, 0.1F, 200).unlockedBy("has_" + set.name + "_sword", m_125977_(set.SWORD.get())).save(consumer, getResource(set.name + "_sword_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(set.SWORD.get()), RecipeCategory.TOOLS, nugget, 0.1F, 100).unlockedBy("has_" + set.name + "_sword", m_125977_(set.SWORD.get())).save(consumer, getResource(set.name + "_sword_blasting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(set.SHOVEL.get()), RecipeCategory.TOOLS, nugget, 0.1F, 200).unlockedBy("has_" + set.name + "_shovel", m_125977_(set.SHOVEL.get())).save(consumer, getResource(set.name + "_shovel_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(set.SHOVEL.get()), RecipeCategory.TOOLS, nugget, 0.1F, 100).unlockedBy("has_" + set.name + "_shovel", m_125977_(set.SHOVEL.get())).save(consumer, getResource(set.name + "_shovel_blasting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(set.PICKAXE.get()), RecipeCategory.TOOLS, nugget, 0.1F, 200).unlockedBy("has_" + set.name + "_pickaxe", m_125977_(set.PICKAXE.get())).save(consumer, getResource(set.name + "_pickaxe_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(set.PICKAXE.get()), RecipeCategory.TOOLS, nugget, 0.1F, 100).unlockedBy("has_" + set.name + "_pickaxe", m_125977_(set.PICKAXE.get())).save(consumer, getResource(set.name + "_pickaxe_blasting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(set.AXE.get()), RecipeCategory.TOOLS, nugget, 0.1F, 200).unlockedBy("has_" + set.name + "_axe", m_125977_(set.AXE.get())).save(consumer, getResource(set.name + "_axe_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(set.AXE.get()), RecipeCategory.TOOLS, nugget, 0.1F, 100).unlockedBy("has_" + set.name + "_axe", m_125977_(set.AXE.get())).save(consumer, getResource(set.name + "_axe_blasting"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(set.HOE.get()), RecipeCategory.TOOLS, nugget, 0.1F, 200).unlockedBy("has_" + set.name + "_hoe", m_125977_(set.HOE.get())).save(consumer, getResource(set.name + "_hoe_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(set.HOE.get()), RecipeCategory.TOOLS, nugget, 0.1F, 100).unlockedBy("has_" + set.name + "_hoe", m_125977_(set.HOE.get())).save(consumer, getResource(set.name + "_hoe_blasting"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, set.SWORD.get()).pattern("M").pattern("M").pattern("S").define('S', Tags.Items.RODS_WOODEN).define('M', material).unlockedBy("has_" + set.name, m_206406_(material)).save(consumer, getResource(set.name + "_sword"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, set.SHOVEL.get()).pattern("M").pattern("S").pattern("S").define('S', Tags.Items.RODS_WOODEN).define('M', material).unlockedBy("has_" + set.name, m_206406_(material)).save(consumer, getResource(set.name + "_shovel"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, set.PICKAXE.get()).pattern("MMM").pattern(" S ").pattern(" S ").define('S', Tags.Items.RODS_WOODEN).define('M', material).unlockedBy("has_" + set.name, m_206406_(material)).save(consumer, getResource(set.name + "_pickaxe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, set.AXE.get()).pattern("MM").pattern("MS").pattern(" S").define('S', Tags.Items.RODS_WOODEN).define('M', material).unlockedBy("has_" + set.name, m_206406_(material)).save(consumer, getResource(set.name + "_axe"));
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, set.HOE.get()).pattern("MM").pattern(" S").pattern(" S").define('S', Tags.Items.RODS_WOODEN).define('M', material).unlockedBy("has_" + set.name, m_206406_(material)).save(consumer, getResource(set.name + "_hoe"));
    }

    public TagKey<Item> itemTag(String modId, String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(modId, name));
    }

    public TagKey<Fluid> fluidTag(String modId, String name) {
        return TagKey.create(Registries.FLUID, new ResourceLocation(modId, name));
    }

    public ICondition tagReal(TagKey<?> tag) {
        return new NotCondition(new TagEmptyCondition(tag.location()));
    }

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation("embers", name);
    }
}