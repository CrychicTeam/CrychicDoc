package com.simibubi.create.foundation.data.recipe;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;

public class StandardRecipeGen extends CreateRecipeProvider {

    private CreateRecipeProvider.Marker MATERIALS = this.enterFolder("materials");

    CreateRecipeProvider.GeneratedRecipe RAW_ZINC = this.create(AllItems.RAW_ZINC).returns(9).unlockedBy(AllBlocks.RAW_ZINC_BLOCK::get).viaShapeless(b -> b.requires((ItemLike) AllBlocks.RAW_ZINC_BLOCK.get()));

    CreateRecipeProvider.GeneratedRecipe RAW_ZINC_BLOCK = this.create(AllBlocks.RAW_ZINC_BLOCK).unlockedBy(AllItems.RAW_ZINC::get).viaShaped(b -> b.define('C', (ItemLike) AllItems.RAW_ZINC.get()).pattern("CCC").pattern("CCC").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe COPPER_NUGGET = this.create(AllItems.COPPER_NUGGET).returns(9).unlockedBy(() -> Items.COPPER_INGOT).viaShapeless(b -> b.requires(CreateRecipeProvider.I.copper()));

    CreateRecipeProvider.GeneratedRecipe COPPER_INGOT = this.create((Supplier<ItemLike>) (() -> Items.COPPER_INGOT)).unlockedBy(AllItems.COPPER_NUGGET::get).viaShaped(b -> b.define('C', CreateRecipeProvider.I.copperNugget()).pattern("CCC").pattern("CCC").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY_FROM_BLOCK = this.create(AllItems.ANDESITE_ALLOY).withSuffix("_from_block").returns(9).unlockedBy(CreateRecipeProvider.I::andesite).viaShapeless(b -> b.requires((ItemLike) AllBlocks.ANDESITE_ALLOY_BLOCK.get()));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY_BLOCK = this.create(AllBlocks.ANDESITE_ALLOY_BLOCK).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesite()).pattern("CCC").pattern("CCC").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe EXPERIENCE_FROM_BLOCK = this.create(AllItems.EXP_NUGGET).withSuffix("_from_block").returns(9).unlockedBy(AllItems.EXP_NUGGET::get).viaShapeless(b -> b.requires((ItemLike) AllBlocks.EXPERIENCE_BLOCK.get()));

    CreateRecipeProvider.GeneratedRecipe EXPERIENCE_BLOCK = this.create(AllBlocks.EXPERIENCE_BLOCK).unlockedBy(AllItems.EXP_NUGGET::get).viaShaped(b -> b.define('C', (ItemLike) AllItems.EXP_NUGGET.get()).pattern("CCC").pattern("CCC").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe BRASS_COMPACTING = this.metalCompacting(ImmutableList.of(AllItems.BRASS_NUGGET, AllItems.BRASS_INGOT, AllBlocks.BRASS_BLOCK), ImmutableList.of(CreateRecipeProvider.I::brassNugget, CreateRecipeProvider.I::brass, CreateRecipeProvider.I::brassBlock));

    CreateRecipeProvider.GeneratedRecipe ZINC_COMPACTING = this.metalCompacting(ImmutableList.of(AllItems.ZINC_NUGGET, AllItems.ZINC_INGOT, AllBlocks.ZINC_BLOCK), ImmutableList.of(CreateRecipeProvider.I::zincNugget, CreateRecipeProvider.I::zinc, CreateRecipeProvider.I::zincBlock));

    CreateRecipeProvider.GeneratedRecipe ROSE_QUARTZ_CYCLE = this.conversionCycle(ImmutableList.of(AllBlocks.ROSE_QUARTZ_TILES, AllBlocks.SMALL_ROSE_QUARTZ_TILES));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY = this.create(AllItems.ANDESITE_ALLOY).unlockedByTag(CreateRecipeProvider.I::iron).viaShaped(b -> b.define('A', Blocks.ANDESITE).define('B', Tags.Items.NUGGETS_IRON).pattern("BA").pattern("AB"));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY_FROM_ZINC = this.create(AllItems.ANDESITE_ALLOY).withSuffix("_from_zinc").unlockedByTag(CreateRecipeProvider.I::zinc).viaShaped(b -> b.define('A', Blocks.ANDESITE).define('B', CreateRecipeProvider.I.zincNugget()).pattern("BA").pattern("AB"));

    CreateRecipeProvider.GeneratedRecipe ELECTRON_TUBE = this.create(AllItems.ELECTRON_TUBE).unlockedBy(AllItems.ROSE_QUARTZ::get).viaShaped(b -> b.define('L', (ItemLike) AllItems.POLISHED_ROSE_QUARTZ.get()).define('N', CreateRecipeProvider.I.ironSheet()).pattern("L").pattern("N"));

    CreateRecipeProvider.GeneratedRecipe ROSE_QUARTZ = this.create(AllItems.ROSE_QUARTZ).unlockedBy(() -> Items.REDSTONE).viaShapeless(b -> b.requires(Tags.Items.GEMS_QUARTZ).requires(Ingredient.of(CreateRecipeProvider.I.redstone()), 8));

    CreateRecipeProvider.GeneratedRecipe SAND_PAPER = this.create(AllItems.SAND_PAPER).unlockedBy(() -> Items.PAPER).viaShapeless(b -> b.requires(Items.PAPER).requires(Tags.Items.SAND_COLORLESS));

    CreateRecipeProvider.GeneratedRecipe RED_SAND_PAPER = this.create(AllItems.RED_SAND_PAPER).unlockedBy(() -> Items.PAPER).viaShapeless(b -> b.requires(Items.PAPER).requires(Tags.Items.SAND_RED));

    private CreateRecipeProvider.Marker CURIOSITIES = this.enterFolder("curiosities");

    CreateRecipeProvider.GeneratedRecipe TOOLBOX = this.create(AllBlocks.TOOLBOXES.get(DyeColor.BROWN)).unlockedByTag(CreateRecipeProvider.I::goldSheet).viaShaped(b -> b.define('S', CreateRecipeProvider.I.goldSheet()).define('C', CreateRecipeProvider.I.cog()).define('W', Tags.Items.CHESTS_WOODEN).define('L', Tags.Items.LEATHER).pattern(" C ").pattern("SWS").pattern(" L "));

    CreateRecipeProvider.GeneratedRecipe TOOLBOX_DYEING = this.createSpecial(AllRecipeTypes.TOOLBOX_DYEING::getSerializer, "crafting", "toolbox_dyeing");

    CreateRecipeProvider.GeneratedRecipe MINECART_COUPLING = this.create(AllItems.MINECART_COUPLING).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('E', CreateRecipeProvider.I.andesite()).define('O', CreateRecipeProvider.I.ironSheet()).pattern("  E").pattern(" O ").pattern("E  "));

    CreateRecipeProvider.GeneratedRecipe PECULIAR_BELL = this.create(AllBlocks.PECULIAR_BELL).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('I', CreateRecipeProvider.I.brassBlock()).define('P', CreateRecipeProvider.I.brassSheet()).pattern("I").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe CAKE = this.create((Supplier<ItemLike>) (() -> Items.CAKE)).unlockedByTag(() -> AllTags.forgeItemTag("dough")).viaShaped(b -> b.define('E', Tags.Items.EGGS).define('S', Items.SUGAR).define('P', AllTags.forgeItemTag("dough")).define('M', () -> Items.MILK_BUCKET).pattern(" M ").pattern("SES").pattern(" P "));

    private CreateRecipeProvider.Marker KINETICS = this.enterFolder("kinetics");

    CreateRecipeProvider.GeneratedRecipe BASIN = this.create(AllBlocks.BASIN).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).pattern("A A").pattern("AAA"));

    CreateRecipeProvider.GeneratedRecipe GOGGLES = this.create(AllItems.GOGGLES).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('G', Tags.Items.GLASS).define('P', CreateRecipeProvider.I.goldSheet()).define('S', Tags.Items.STRING).pattern(" S ").pattern("GPG"));

    CreateRecipeProvider.GeneratedRecipe WRENCH = this.create(AllItems.WRENCH).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('G', CreateRecipeProvider.I.goldSheet()).define('P', CreateRecipeProvider.I.cog()).define('S', Tags.Items.RODS_WOODEN).pattern("GG").pattern("GP").pattern(" S"));

    CreateRecipeProvider.GeneratedRecipe FILTER = this.create(AllItems.FILTER).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', ItemTags.WOOL).define('A', Tags.Items.NUGGETS_IRON).pattern("ASA"));

    CreateRecipeProvider.GeneratedRecipe ATTRIBUTE_FILTER = this.create(AllItems.ATTRIBUTE_FILTER).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('S', ItemTags.WOOL).define('A', CreateRecipeProvider.I.brassNugget()).pattern("ASA"));

    CreateRecipeProvider.GeneratedRecipe BRASS_HAND = this.create(AllItems.BRASS_HAND).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).define('B', CreateRecipeProvider.I.brassSheet()).pattern(" A ").pattern("BBB").pattern(" B "));

    CreateRecipeProvider.GeneratedRecipe SUPER_GLUE = this.create(AllItems.SUPER_GLUE).unlockedByTag(CreateRecipeProvider.I::ironSheet).viaShaped(b -> b.define('A', Tags.Items.SLIMEBALLS).define('S', CreateRecipeProvider.I.ironSheet()).define('N', Tags.Items.NUGGETS_IRON).pattern("AS").pattern("NA"));

    CreateRecipeProvider.GeneratedRecipe CRAFTER_SLOT_COVER = this.create(AllItems.CRAFTER_SLOT_COVER).unlockedBy(AllBlocks.MECHANICAL_CRAFTER::get).viaShaped(b -> b.define('A', CreateRecipeProvider.I.brassNugget()).pattern("AAA"));

    CreateRecipeProvider.GeneratedRecipe COGWHEEL = this.create(AllBlocks.COGWHEEL).unlockedBy(CreateRecipeProvider.I::andesite).viaShapeless(b -> b.requires(CreateRecipeProvider.I.shaft()).requires(CreateRecipeProvider.I.planks()));

    CreateRecipeProvider.GeneratedRecipe LARGE_COGWHEEL = this.create(AllBlocks.LARGE_COGWHEEL).unlockedBy(CreateRecipeProvider.I::andesite).viaShapeless(b -> b.requires(CreateRecipeProvider.I.shaft()).requires(CreateRecipeProvider.I.planks()).requires(CreateRecipeProvider.I.planks()));

    CreateRecipeProvider.GeneratedRecipe LARGE_COGWHEEL_FROM_LITTLE = this.create(AllBlocks.LARGE_COGWHEEL).withSuffix("_from_little").unlockedBy(CreateRecipeProvider.I::andesite).viaShapeless(b -> b.requires(CreateRecipeProvider.I.cog()).requires(CreateRecipeProvider.I.planks()));

    CreateRecipeProvider.GeneratedRecipe WATER_WHEEL = this.create(AllBlocks.WATER_WHEEL).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', CreateRecipeProvider.I.planks()).define('C', CreateRecipeProvider.I.shaft()).pattern("SSS").pattern("SCS").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe LARGE_WATER_WHEEL = this.create(AllBlocks.LARGE_WATER_WHEEL).unlockedBy(AllBlocks.WATER_WHEEL::get).viaShaped(b -> b.define('S', CreateRecipeProvider.I.planks()).define('C', (ItemLike) AllBlocks.WATER_WHEEL.get()).pattern("SSS").pattern("SCS").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe SHAFT = this.create(AllBlocks.SHAFT).returns(8).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).pattern("A").pattern("A"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_PRESS = this.create(AllBlocks.MECHANICAL_PRESS).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('S', CreateRecipeProvider.I.shaft()).define('I', AllTags.forgeItemTag("storage_blocks/iron")).pattern("S").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe MILLSTONE = this.create(AllBlocks.MILLSTONE).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.cog()).define('S', CreateRecipeProvider.I.andesiteCasing()).define('I', CreateRecipeProvider.I.stone()).pattern("C").pattern("S").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_PISTON = this.create(AllBlocks.MECHANICAL_PISTON).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('B', ItemTags.WOODEN_SLABS).define('C', CreateRecipeProvider.I.andesiteCasing()).define('I', (ItemLike) AllBlocks.PISTON_EXTENSION_POLE.get()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe STICKY_MECHANICAL_PISTON = this.create(AllBlocks.STICKY_MECHANICAL_PISTON).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', Tags.Items.SLIMEBALLS).define('P', (ItemLike) AllBlocks.MECHANICAL_PISTON.get()).pattern("S").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe TURNTABLE = this.create(AllBlocks.TURNTABLE).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', CreateRecipeProvider.I.shaft()).define('P', ItemTags.WOODEN_SLABS).pattern("P").pattern("S"));

    CreateRecipeProvider.GeneratedRecipe PISTON_EXTENSION_POLE = this.create(AllBlocks.PISTON_EXTENSION_POLE).returns(8).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).define('P', ItemTags.PLANKS).pattern("P").pattern("A").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe GANTRY_PINION = this.create(AllBlocks.GANTRY_CARRIAGE).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('B', ItemTags.WOODEN_SLABS).define('C', CreateRecipeProvider.I.andesiteCasing()).define('I', CreateRecipeProvider.I.cog()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe GANTRY_SHAFT = this.create(AllBlocks.GANTRY_SHAFT).returns(8).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).define('R', CreateRecipeProvider.I.redstone()).pattern("A").pattern("R").pattern("A"));

    CreateRecipeProvider.GeneratedRecipe PLACARD = this.create(AllBlocks.PLACARD).returns(1).unlockedByTag(() -> CreateRecipeProvider.I.brass()).viaShapeless(b -> b.requires(Items.ITEM_FRAME).requires(CreateRecipeProvider.I.brassSheet()));

    CreateRecipeProvider.GeneratedRecipe TRAIN_DOOR = this.create(AllBlocks.TRAIN_DOOR).returns(1).unlockedBy(() -> CreateRecipeProvider.I.railwayCasing()).viaShapeless(b -> b.requires(ItemTags.WOODEN_DOORS).requires(CreateRecipeProvider.I.railwayCasing()));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_DOOR = this.create(AllBlocks.ANDESITE_DOOR).returns(1).unlockedBy(() -> CreateRecipeProvider.I.andesiteCasing()).viaShapeless(b -> b.requires(ItemTags.WOODEN_DOORS).requires(CreateRecipeProvider.I.andesiteCasing()));

    CreateRecipeProvider.GeneratedRecipe BRASS_DOOR = this.create(AllBlocks.BRASS_DOOR).returns(1).unlockedBy(() -> CreateRecipeProvider.I.brassCasing()).viaShapeless(b -> b.requires(ItemTags.WOODEN_DOORS).requires(CreateRecipeProvider.I.brassCasing()));

    CreateRecipeProvider.GeneratedRecipe COPPER_DOOR = this.create(AllBlocks.COPPER_DOOR).returns(1).unlockedBy(() -> CreateRecipeProvider.I.copperCasing()).viaShapeless(b -> b.requires(ItemTags.WOODEN_DOORS).requires(CreateRecipeProvider.I.copperCasing()));

    CreateRecipeProvider.GeneratedRecipe TRAIN_TRAPDOOR = this.create(AllBlocks.TRAIN_TRAPDOOR).returns(1).unlockedBy(() -> CreateRecipeProvider.I.railwayCasing()).viaShapeless(b -> b.requires(ItemTags.WOODEN_TRAPDOORS).requires(CreateRecipeProvider.I.railwayCasing()));

    CreateRecipeProvider.GeneratedRecipe FRAMED_GLASS_DOOR = this.create(AllBlocks.FRAMED_GLASS_DOOR).returns(1).unlockedBy(AllPaletteBlocks.FRAMED_GLASS::get).viaShapeless(b -> b.requires(ItemTags.WOODEN_DOORS).requires((ItemLike) AllPaletteBlocks.FRAMED_GLASS.get()));

    CreateRecipeProvider.GeneratedRecipe FRAMED_GLASS_TRAPDOOR = this.create(AllBlocks.FRAMED_GLASS_TRAPDOOR).returns(1).unlockedBy(AllPaletteBlocks.FRAMED_GLASS::get).viaShapeless(b -> b.requires(ItemTags.WOODEN_TRAPDOORS).requires((ItemLike) AllPaletteBlocks.FRAMED_GLASS.get()));

    CreateRecipeProvider.GeneratedRecipe ANALOG_LEVER = this.create(AllBlocks.ANALOG_LEVER).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', CreateRecipeProvider.I.andesiteCasing()).define('P', Tags.Items.RODS_WOODEN).pattern("P").pattern("S"));

    CreateRecipeProvider.GeneratedRecipe ROSE_QUARTZ_LAMP = this.create(AllBlocks.ROSE_QUARTZ_LAMP).unlockedByTag(CreateRecipeProvider.I::zinc).viaShapeless(b -> b.requires((ItemLike) AllItems.POLISHED_ROSE_QUARTZ.get()).requires(CreateRecipeProvider.I.redstone()).requires(CreateRecipeProvider.I.zinc()));

    CreateRecipeProvider.GeneratedRecipe BELT_CONNECTOR = this.create(AllItems.BELT_CONNECTOR).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('D', Items.DRIED_KELP).pattern("DDD").pattern("DDD"));

    CreateRecipeProvider.GeneratedRecipe ADJUSTABLE_PULLEY = this.create(AllBlocks.ADJUSTABLE_CHAIN_GEARSHIFT).unlockedBy(CreateRecipeProvider.I::electronTube).viaShapeless(b -> b.requires((ItemLike) AllBlocks.ENCASED_CHAIN_DRIVE.get()).requires(CreateRecipeProvider.I.electronTube()));

    CreateRecipeProvider.GeneratedRecipe CART_ASSEMBLER = this.create(AllBlocks.CART_ASSEMBLER).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('L', ItemTags.LOGS).define('R', CreateRecipeProvider.I.redstone()).define('C', CreateRecipeProvider.I.andesite()).pattern("CRC").pattern("L L"));

    CreateRecipeProvider.GeneratedRecipe CONTROLLER_RAIL = this.create(AllBlocks.CONTROLLER_RAIL).returns(6).unlockedBy(() -> Items.POWERED_RAIL).viaShaped(b -> b.define('A', CreateRecipeProvider.I.gold()).define('E', CreateRecipeProvider.I.electronTube()).define('S', Tags.Items.RODS_WOODEN).pattern("A A").pattern("ASA").pattern("AEA"));

    CreateRecipeProvider.GeneratedRecipe HAND_CRANK = this.create(AllBlocks.HAND_CRANK).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).define('C', ItemTags.PLANKS).pattern("CCC").pattern("  A"));

    CreateRecipeProvider.GeneratedRecipe COPPER_VALVE_HANDLE = this.create(AllBlocks.COPPER_VALVE_HANDLE).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('S', CreateRecipeProvider.I.andesite()).define('C', CreateRecipeProvider.I.copperSheet()).pattern("CCC").pattern(" S "));

    CreateRecipeProvider.GeneratedRecipe COPPER_VALVE_HANDLE_FROM_OTHER_HANDLES = this.create(AllBlocks.COPPER_VALVE_HANDLE).withSuffix("_from_others").unlockedBy(CreateRecipeProvider.I::copper).viaShapeless(b -> b.requires(AllTags.AllItemTags.VALVE_HANDLES.tag));

    CreateRecipeProvider.GeneratedRecipe NOZZLE = this.create(AllBlocks.NOZZLE).unlockedBy(AllBlocks.ENCASED_FAN::get).viaShaped(b -> b.define('S', CreateRecipeProvider.I.andesite()).define('C', ItemTags.WOOL).pattern(" S ").pattern(" C ").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe PROPELLER = this.create(AllItems.PROPELLER).unlockedByTag(CreateRecipeProvider.I::ironSheet).viaShaped(b -> b.define('S', CreateRecipeProvider.I.ironSheet()).define('C', CreateRecipeProvider.I.andesite()).pattern(" S ").pattern("SCS").pattern(" S "));

    CreateRecipeProvider.GeneratedRecipe WHISK = this.create(AllItems.WHISK).unlockedByTag(CreateRecipeProvider.I::ironSheet).viaShaped(b -> b.define('S', CreateRecipeProvider.I.ironSheet()).define('C', CreateRecipeProvider.I.andesite()).pattern(" C ").pattern("SCS").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe ENCASED_FAN = this.create(AllBlocks.ENCASED_FAN).unlockedByTag(CreateRecipeProvider.I::ironSheet).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesiteCasing()).define('S', CreateRecipeProvider.I.shaft()).define('P', (ItemLike) AllItems.PROPELLER.get()).pattern("S").pattern("A").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe CUCKOO_CLOCK = this.create(AllBlocks.CUCKOO_CLOCK).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', ItemTags.PLANKS).define('A', Items.CLOCK).define('C', CreateRecipeProvider.I.andesiteCasing()).pattern("S").pattern("C").pattern("A"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_CRAFTER = this.create(AllBlocks.MECHANICAL_CRAFTER).returns(3).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('B', CreateRecipeProvider.I.electronTube()).define('R', Blocks.CRAFTING_TABLE).define('C', CreateRecipeProvider.I.brassCasing()).pattern("B").pattern("C").pattern("R"));

    CreateRecipeProvider.GeneratedRecipe WINDMILL_BEARING = this.create(AllBlocks.WINDMILL_BEARING).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('B', ItemTags.WOODEN_SLABS).define('C', CreateRecipeProvider.I.stone()).define('I', CreateRecipeProvider.I.shaft()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_BEARING = this.create(AllBlocks.MECHANICAL_BEARING).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('B', ItemTags.WOODEN_SLABS).define('C', CreateRecipeProvider.I.andesiteCasing()).define('I', CreateRecipeProvider.I.shaft()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe CLOCKWORK_BEARING = this.create(AllBlocks.CLOCKWORK_BEARING).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('S', CreateRecipeProvider.I.electronTube()).define('B', CreateRecipeProvider.I.woodSlab()).define('C', CreateRecipeProvider.I.brassCasing()).pattern("B").pattern("C").pattern("S"));

    CreateRecipeProvider.GeneratedRecipe WOODEN_BRACKET = this.create(AllBlocks.WOODEN_BRACKET).returns(4).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', Tags.Items.RODS_WOODEN).define('P', CreateRecipeProvider.I.planks()).define('C', CreateRecipeProvider.I.andesite()).pattern("SSS").pattern("PCP"));

    CreateRecipeProvider.GeneratedRecipe METAL_BRACKET = this.create(AllBlocks.METAL_BRACKET).returns(4).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('S', Tags.Items.NUGGETS_IRON).define('P', CreateRecipeProvider.I.iron()).define('C', CreateRecipeProvider.I.andesite()).pattern("SSS").pattern("PCP"));

    CreateRecipeProvider.GeneratedRecipe METAL_GIRDER = this.create(AllBlocks.METAL_GIRDER).returns(8).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('P', CreateRecipeProvider.I.ironSheet()).define('C', CreateRecipeProvider.I.andesite()).pattern("PPP").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe DISPLAY_BOARD = this.create(AllBlocks.DISPLAY_BOARD).returns(2).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.electronTube()).define('P', CreateRecipeProvider.I.andesite()).pattern("PAP"));

    CreateRecipeProvider.GeneratedRecipe STEAM_WHISTLE = this.create(AllBlocks.STEAM_WHISTLE).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('P', CreateRecipeProvider.I.goldSheet()).define('C', CreateRecipeProvider.I.copper()).pattern("P").pattern("C"));

    CreateRecipeProvider.GeneratedRecipe STEAM_ENGINE = this.create(AllBlocks.STEAM_ENGINE).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('P', CreateRecipeProvider.I.goldSheet()).define('C', CreateRecipeProvider.I.copperBlock()).define('A', CreateRecipeProvider.I.andesite()).pattern("P").pattern("A").pattern("C"));

    CreateRecipeProvider.GeneratedRecipe FLUID_PIPE = this.create(AllBlocks.FLUID_PIPE).returns(4).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('S', CreateRecipeProvider.I.copperSheet()).define('C', CreateRecipeProvider.I.copper()).pattern("SCS"));

    CreateRecipeProvider.GeneratedRecipe FLUID_PIPE_2 = this.create(AllBlocks.FLUID_PIPE).withSuffix("_vertical").returns(4).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('S', CreateRecipeProvider.I.copperSheet()).define('C', CreateRecipeProvider.I.copper()).pattern("S").pattern("C").pattern("S"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_PUMP = this.create(AllBlocks.MECHANICAL_PUMP).unlockedBy(CreateRecipeProvider.I::copper).viaShapeless(b -> b.requires(CreateRecipeProvider.I.cog()).requires((ItemLike) AllBlocks.FLUID_PIPE.get()));

    CreateRecipeProvider.GeneratedRecipe SMART_FLUID_PIPE = this.create(AllBlocks.SMART_FLUID_PIPE).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('P', CreateRecipeProvider.I.electronTube()).define('S', (ItemLike) AllBlocks.FLUID_PIPE.get()).define('I', CreateRecipeProvider.I.brassSheet()).pattern("I").pattern("S").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe FLUID_VALVE = this.create(AllBlocks.FLUID_VALVE).unlockedBy(CreateRecipeProvider.I::copper).viaShapeless(b -> b.requires(CreateRecipeProvider.I.ironSheet()).requires((ItemLike) AllBlocks.FLUID_PIPE.get()));

    CreateRecipeProvider.GeneratedRecipe SPOUT = this.create(AllBlocks.SPOUT).unlockedBy(CreateRecipeProvider.I::copperCasing).viaShaped(b -> b.define('T', CreateRecipeProvider.I.copperCasing()).define('P', Items.DRIED_KELP).pattern("T").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe ITEM_DRAIN = this.create(AllBlocks.ITEM_DRAIN).unlockedBy(CreateRecipeProvider.I::copperCasing).viaShaped(b -> b.define('P', Blocks.IRON_BARS).define('S', CreateRecipeProvider.I.copperCasing()).pattern("P").pattern("S"));

    CreateRecipeProvider.GeneratedRecipe FLUID_TANK = this.create(AllBlocks.FLUID_TANK).unlockedByTag(() -> Tags.Items.BARRELS_WOODEN).viaShaped(b -> b.define('B', CreateRecipeProvider.I.copperSheet()).define('C', Tags.Items.BARRELS_WOODEN).pattern("B").pattern("C").pattern("B"));

    CreateRecipeProvider.GeneratedRecipe ITEM_VAULT = this.create(AllBlocks.ITEM_VAULT).unlockedByTag(() -> Tags.Items.BARRELS_WOODEN).viaShaped(b -> b.define('B', CreateRecipeProvider.I.ironSheet()).define('C', Tags.Items.BARRELS_WOODEN).pattern("B").pattern("C").pattern("B"));

    CreateRecipeProvider.GeneratedRecipe TRAIN_SIGNAL = this.create(AllBlocks.TRACK_SIGNAL).unlockedBy(CreateRecipeProvider.I::railwayCasing).returns(4).viaShapeless(b -> b.requires(CreateRecipeProvider.I.railwayCasing()).requires(CreateRecipeProvider.I.electronTube()));

    CreateRecipeProvider.GeneratedRecipe TRAIN_OBSERVER = this.create(AllBlocks.TRACK_OBSERVER).unlockedBy(CreateRecipeProvider.I::railwayCasing).returns(2).viaShapeless(b -> b.requires(CreateRecipeProvider.I.railwayCasing()).requires(ItemTags.WOODEN_PRESSURE_PLATES));

    CreateRecipeProvider.GeneratedRecipe TRAIN_OBSERVER_2 = this.create(AllBlocks.TRACK_OBSERVER).withSuffix("_from_other_plates").unlockedBy(CreateRecipeProvider.I::railwayCasing).returns(2).viaShapeless(b -> b.requires(CreateRecipeProvider.I.railwayCasing()).requires(Ingredient.of(Items.STONE_PRESSURE_PLATE, Items.POLISHED_BLACKSTONE_PRESSURE_PLATE, Items.HEAVY_WEIGHTED_PRESSURE_PLATE, Items.LIGHT_WEIGHTED_PRESSURE_PLATE)));

    CreateRecipeProvider.GeneratedRecipe TRAIN_SCHEDULE = this.create(AllItems.SCHEDULE).unlockedByTag(CreateRecipeProvider.I::sturdySheet).returns(4).viaShapeless(b -> b.requires(CreateRecipeProvider.I.sturdySheet()).requires(Items.PAPER));

    CreateRecipeProvider.GeneratedRecipe TRAIN_STATION = this.create(AllBlocks.TRACK_STATION).unlockedBy(CreateRecipeProvider.I::railwayCasing).returns(2).viaShapeless(b -> b.requires(CreateRecipeProvider.I.railwayCasing()).requires(Items.COMPASS));

    CreateRecipeProvider.GeneratedRecipe TRAIN_CONTROLS = this.create(AllBlocks.TRAIN_CONTROLS).unlockedBy(CreateRecipeProvider.I::railwayCasing).viaShaped(b -> b.define('I', CreateRecipeProvider.I.precisionMechanism()).define('B', Items.LEVER).define('C', CreateRecipeProvider.I.railwayCasing()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe DEPLOYER = this.create(AllBlocks.DEPLOYER).unlockedBy(CreateRecipeProvider.I::electronTube).viaShaped(b -> b.define('I', (ItemLike) AllItems.BRASS_HAND.get()).define('B', CreateRecipeProvider.I.electronTube()).define('C', CreateRecipeProvider.I.andesiteCasing()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe PORTABLE_STORAGE_INTERFACE = this.create(AllBlocks.PORTABLE_STORAGE_INTERFACE).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.andesiteCasing()).requires((ItemLike) AllBlocks.CHUTE.get()));

    CreateRecipeProvider.GeneratedRecipe PORTABLE_FLUID_INTERFACE = this.create(AllBlocks.PORTABLE_FLUID_INTERFACE).unlockedBy(CreateRecipeProvider.I::copperCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.copperCasing()).requires((ItemLike) AllBlocks.CHUTE.get()));

    CreateRecipeProvider.GeneratedRecipe ROPE_PULLEY = this.create(AllBlocks.ROPE_PULLEY).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('B', CreateRecipeProvider.I.andesiteCasing()).define('C', ItemTags.WOOL).define('I', CreateRecipeProvider.I.ironSheet()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe HOSE_PULLEY = this.create(AllBlocks.HOSE_PULLEY).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('B', CreateRecipeProvider.I.copperCasing()).define('C', Items.DRIED_KELP_BLOCK).define('I', CreateRecipeProvider.I.copperSheet()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe ELEVATOR_PULLEY = this.create(AllBlocks.ELEVATOR_PULLEY).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('B', CreateRecipeProvider.I.brassCasing()).define('C', Items.DRIED_KELP_BLOCK).define('I', CreateRecipeProvider.I.ironSheet()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe CONTRAPTION_CONTROLS = this.create(AllBlocks.CONTRAPTION_CONTROLS).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('B', ItemTags.BUTTONS).define('C', CreateRecipeProvider.I.andesiteCasing()).define('I', CreateRecipeProvider.I.electronTube()).pattern("B").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe EMPTY_BLAZE_BURNER = this.create(AllItems.EMPTY_BLAZE_BURNER).unlockedByTag(CreateRecipeProvider.I::iron).viaShaped(b -> b.define('A', Tags.Items.NETHERRACK).define('I', CreateRecipeProvider.I.ironSheet()).pattern(" I ").pattern("IAI").pattern(" I "));

    CreateRecipeProvider.GeneratedRecipe CHUTE = this.create(AllBlocks.CHUTE).unlockedBy(CreateRecipeProvider.I::andesite).returns(4).viaShaped(b -> b.define('A', CreateRecipeProvider.I.ironSheet()).define('I', CreateRecipeProvider.I.iron()).pattern("A").pattern("I").pattern("A"));

    CreateRecipeProvider.GeneratedRecipe SMART_CHUTE = this.create(AllBlocks.SMART_CHUTE).unlockedBy(AllBlocks.CHUTE::get).viaShaped(b -> b.define('P', CreateRecipeProvider.I.electronTube()).define('S', (ItemLike) AllBlocks.CHUTE.get()).define('I', CreateRecipeProvider.I.brassSheet()).pattern("I").pattern("S").pattern("P"));

    CreateRecipeProvider.GeneratedRecipe DEPOT = this.create(AllBlocks.DEPOT).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.andesite()).requires(CreateRecipeProvider.I.andesiteCasing()));

    CreateRecipeProvider.GeneratedRecipe WEIGHTED_EJECTOR = this.create(AllBlocks.WEIGHTED_EJECTOR).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('A', CreateRecipeProvider.I.goldSheet()).define('D', (ItemLike) AllBlocks.DEPOT.get()).define('I', CreateRecipeProvider.I.cog()).pattern("A").pattern("D").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_ARM = this.create(AllBlocks.MECHANICAL_ARM::get).unlockedBy(CreateRecipeProvider.I::brassCasing).returns(1).viaShaped(b -> b.define('L', CreateRecipeProvider.I.brassSheet()).define('I', CreateRecipeProvider.I.precisionMechanism()).define('A', CreateRecipeProvider.I.andesite()).define('C', CreateRecipeProvider.I.brassCasing()).pattern("LLA").pattern("L  ").pattern("IC "));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_MIXER = this.create(AllBlocks.MECHANICAL_MIXER).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('S', CreateRecipeProvider.I.cog()).define('I', (ItemLike) AllItems.WHISK.get()).pattern("S").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe CLUTCH = this.create(AllBlocks.CLUTCH).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.andesiteCasing()).requires(CreateRecipeProvider.I.shaft()).requires(CreateRecipeProvider.I.redstone()));

    CreateRecipeProvider.GeneratedRecipe GEARSHIFT = this.create(AllBlocks.GEARSHIFT).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.andesiteCasing()).requires(CreateRecipeProvider.I.cog()).requires(CreateRecipeProvider.I.redstone()));

    CreateRecipeProvider.GeneratedRecipe SAIL = this.create(AllBlocks.SAIL).returns(2).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('W', ItemTags.WOOL).define('S', Tags.Items.RODS_WOODEN).define('A', CreateRecipeProvider.I.andesite()).pattern("WS").pattern("SA"));

    CreateRecipeProvider.GeneratedRecipe SAIL_CYCLE = this.conversionCycle(ImmutableList.of(AllBlocks.SAIL_FRAME, AllBlocks.SAIL));

    CreateRecipeProvider.GeneratedRecipe RADIAL_CHASIS = this.create(AllBlocks.RADIAL_CHASSIS).returns(3).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('P', CreateRecipeProvider.I.andesite()).define('L', ItemTags.LOGS).pattern(" L ").pattern("PLP").pattern(" L "));

    CreateRecipeProvider.GeneratedRecipe LINEAR_CHASIS = this.create(AllBlocks.LINEAR_CHASSIS).returns(3).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('P', CreateRecipeProvider.I.andesite()).define('L', ItemTags.LOGS).pattern(" P ").pattern("LLL").pattern(" P "));

    CreateRecipeProvider.GeneratedRecipe LINEAR_CHASSIS_CYCLE = this.conversionCycle(ImmutableList.of(AllBlocks.LINEAR_CHASSIS, AllBlocks.SECONDARY_LINEAR_CHASSIS));

    CreateRecipeProvider.GeneratedRecipe STICKER = this.create(AllBlocks.STICKER).returns(1).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('I', CreateRecipeProvider.I.andesite()).define('C', Tags.Items.COBBLESTONE).define('R', CreateRecipeProvider.I.redstone()).define('S', Tags.Items.SLIMEBALLS).pattern("ISI").pattern("CRC"));

    CreateRecipeProvider.GeneratedRecipe MINECART = this.create((Supplier<ItemLike>) (() -> Items.MINECART)).withSuffix("_from_contraption_cart").unlockedBy(AllBlocks.CART_ASSEMBLER::get).viaShapeless(b -> b.requires((ItemLike) AllItems.MINECART_CONTRAPTION.get()));

    CreateRecipeProvider.GeneratedRecipe FURNACE_MINECART = this.create((Supplier<ItemLike>) (() -> Items.FURNACE_MINECART)).withSuffix("_from_contraption_cart").unlockedBy(AllBlocks.CART_ASSEMBLER::get).viaShapeless(b -> b.requires((ItemLike) AllItems.FURNACE_MINECART_CONTRAPTION.get()));

    CreateRecipeProvider.GeneratedRecipe GEARBOX = this.create(AllBlocks.GEARBOX).unlockedBy(CreateRecipeProvider.I::cog).viaShaped(b -> b.define('C', CreateRecipeProvider.I.cog()).define('B', CreateRecipeProvider.I.andesiteCasing()).pattern(" C ").pattern("CBC").pattern(" C "));

    CreateRecipeProvider.GeneratedRecipe GEARBOX_CYCLE = this.conversionCycle(ImmutableList.of(AllBlocks.GEARBOX, AllItems.VERTICAL_GEARBOX));

    CreateRecipeProvider.GeneratedRecipe MYSTERIOUS_CUCKOO_CLOCK = this.create(AllBlocks.MYSTERIOUS_CUCKOO_CLOCK).unlockedBy(AllBlocks.CUCKOO_CLOCK::get).viaShaped(b -> b.define('C', Tags.Items.GUNPOWDER).define('B', (ItemLike) AllBlocks.CUCKOO_CLOCK.get()).pattern(" C ").pattern("CBC").pattern(" C "));

    CreateRecipeProvider.GeneratedRecipe ENCASED_CHAIN_DRIVE = this.create(AllBlocks.ENCASED_CHAIN_DRIVE).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.andesiteCasing()).requires(CreateRecipeProvider.I.ironNugget()).requires(CreateRecipeProvider.I.ironNugget()).requires(CreateRecipeProvider.I.ironNugget()));

    CreateRecipeProvider.GeneratedRecipe FLYWHEEL = this.create(AllBlocks.FLYWHEEL).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('C', CreateRecipeProvider.I.brass()).define('A', CreateRecipeProvider.I.shaft()).pattern("CCC").pattern("CAC").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe SPEEDOMETER = this.create(AllBlocks.SPEEDOMETER).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('C', Items.COMPASS).define('A', CreateRecipeProvider.I.andesiteCasing()).pattern("C").pattern("A"));

    CreateRecipeProvider.GeneratedRecipe GAUGE_CYCLE = this.conversionCycle(ImmutableList.of(AllBlocks.SPEEDOMETER, AllBlocks.STRESSOMETER));

    CreateRecipeProvider.GeneratedRecipe ROTATION_SPEED_CONTROLLER = this.create(AllBlocks.ROTATION_SPEED_CONTROLLER).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('B', CreateRecipeProvider.I.precisionMechanism()).define('C', CreateRecipeProvider.I.brassCasing()).pattern("B").pattern("C"));

    CreateRecipeProvider.GeneratedRecipe NIXIE_TUBE = this.create(AllBlocks.ORANGE_NIXIE_TUBE).returns(4).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.electronTube()).requires(CreateRecipeProvider.I.electronTube()));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_SAW = this.create(AllBlocks.MECHANICAL_SAW).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('A', CreateRecipeProvider.I.ironSheet()).define('I', CreateRecipeProvider.I.iron()).pattern(" A ").pattern("AIA").pattern(" C "));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_HARVESTER = this.create(AllBlocks.MECHANICAL_HARVESTER).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('A', CreateRecipeProvider.I.andesite()).define('I', CreateRecipeProvider.I.ironSheet()).pattern("AIA").pattern("AIA").pattern(" C "));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_PLOUGH = this.create(AllBlocks.MECHANICAL_PLOUGH).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('A', CreateRecipeProvider.I.andesite()).define('I', CreateRecipeProvider.I.ironSheet()).pattern("III").pattern("AAA").pattern(" C "));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_ROLLER = this.create(AllBlocks.MECHANICAL_ROLLER).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('A', CreateRecipeProvider.I.electronTube()).define('I', (ItemLike) AllBlocks.CRUSHING_WHEEL.get()).pattern("A").pattern("C").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe MECHANICAL_DRILL = this.create(AllBlocks.MECHANICAL_DRILL).unlockedBy(CreateRecipeProvider.I::andesiteCasing).viaShaped(b -> b.define('C', CreateRecipeProvider.I.andesiteCasing()).define('A', CreateRecipeProvider.I.andesite()).define('I', CreateRecipeProvider.I.iron()).pattern(" A ").pattern("AIA").pattern(" C "));

    CreateRecipeProvider.GeneratedRecipe SEQUENCED_GEARSHIFT = this.create(AllBlocks.SEQUENCED_GEARSHIFT).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShapeless(b -> b.requires(CreateRecipeProvider.I.brassCasing()).requires(CreateRecipeProvider.I.cog()).requires(CreateRecipeProvider.I.electronTube()));

    private CreateRecipeProvider.Marker LOGISTICS = this.enterFolder("logistics");

    CreateRecipeProvider.GeneratedRecipe REDSTONE_CONTACT = this.create(AllBlocks.REDSTONE_CONTACT).returns(2).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('W', CreateRecipeProvider.I.redstone()).define('C', Blocks.COBBLESTONE).define('S', CreateRecipeProvider.I.ironSheet()).pattern(" S ").pattern("CWC").pattern("CCC"));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_FUNNEL = this.create(AllBlocks.ANDESITE_FUNNEL).returns(2).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).define('K', Items.DRIED_KELP).pattern("A").pattern("K"));

    CreateRecipeProvider.GeneratedRecipe BRASS_FUNNEL = this.create(AllBlocks.BRASS_FUNNEL).returns(2).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('A', CreateRecipeProvider.I.brass()).define('K', Items.DRIED_KELP).define('E', CreateRecipeProvider.I.electronTube()).pattern("E").pattern("A").pattern("K"));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_TUNNEL = this.create(AllBlocks.ANDESITE_TUNNEL).returns(2).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('A', CreateRecipeProvider.I.andesite()).define('K', Items.DRIED_KELP).pattern("AA").pattern("KK"));

    CreateRecipeProvider.GeneratedRecipe BRASS_TUNNEL = this.create(AllBlocks.BRASS_TUNNEL).returns(2).unlockedByTag(CreateRecipeProvider.I::brass).viaShaped(b -> b.define('A', CreateRecipeProvider.I.brass()).define('K', Items.DRIED_KELP).define('E', CreateRecipeProvider.I.electronTube()).pattern("E ").pattern("AA").pattern("KK"));

    CreateRecipeProvider.GeneratedRecipe SMART_OBSERVER = this.create(AllBlocks.SMART_OBSERVER).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('B', CreateRecipeProvider.I.brassCasing()).define('R', CreateRecipeProvider.I.electronTube()).define('I', Blocks.OBSERVER).pattern("R").pattern("B").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe THRESHOLD_SWITCH = this.create(AllBlocks.THRESHOLD_SWITCH).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('B', CreateRecipeProvider.I.brassCasing()).define('R', CreateRecipeProvider.I.electronTube()).define('I', Blocks.COMPARATOR).pattern("R").pattern("B").pattern("I"));

    CreateRecipeProvider.GeneratedRecipe PULSE_EXTENDER = this.create(AllBlocks.PULSE_EXTENDER).unlockedByTag(CreateRecipeProvider.I::redstone).viaShaped(b -> b.define('T', Blocks.REDSTONE_TORCH).define('C', CreateRecipeProvider.I.brassSheet()).define('R', CreateRecipeProvider.I.redstone()).define('S', CreateRecipeProvider.I.stone()).pattern("  T").pattern("RCT").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe PULSE_REPEATER = this.create(AllBlocks.PULSE_REPEATER).unlockedByTag(CreateRecipeProvider.I::redstone).viaShaped(b -> b.define('T', Blocks.REDSTONE_TORCH).define('C', CreateRecipeProvider.I.brassSheet()).define('R', CreateRecipeProvider.I.redstone()).define('S', CreateRecipeProvider.I.stone()).pattern("RCT").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe POWERED_TOGGLE_LATCH = this.create(AllBlocks.POWERED_TOGGLE_LATCH).unlockedByTag(CreateRecipeProvider.I::redstone).viaShaped(b -> b.define('T', Blocks.REDSTONE_TORCH).define('C', Blocks.LEVER).define('S', CreateRecipeProvider.I.stone()).pattern(" T ").pattern(" C ").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe POWERED_LATCH = this.create(AllBlocks.POWERED_LATCH).unlockedByTag(CreateRecipeProvider.I::redstone).viaShaped(b -> b.define('T', Blocks.REDSTONE_TORCH).define('C', Blocks.LEVER).define('R', CreateRecipeProvider.I.redstone()).define('S', CreateRecipeProvider.I.stone()).pattern(" T ").pattern("RCR").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe REDSTONE_LINK = this.create(AllBlocks.REDSTONE_LINK).returns(2).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('C', Blocks.REDSTONE_TORCH).define('S', CreateRecipeProvider.I.brassCasing()).pattern("C").pattern("S"));

    CreateRecipeProvider.GeneratedRecipe DISPLAY_LINK = this.create(AllBlocks.DISPLAY_LINK).unlockedBy(CreateRecipeProvider.I::brassCasing).viaShaped(b -> b.define('C', Blocks.REDSTONE_TORCH).define('A', CreateRecipeProvider.I.copperSheet()).define('S', CreateRecipeProvider.I.brassCasing()).pattern("C").pattern("S").pattern("A"));

    private CreateRecipeProvider.Marker SCHEMATICS = this.enterFolder("schematics");

    CreateRecipeProvider.GeneratedRecipe SCHEMATIC_TABLE = this.create(AllBlocks.SCHEMATIC_TABLE).unlockedBy(AllItems.EMPTY_SCHEMATIC::get).viaShaped(b -> b.define('W', ItemTags.WOODEN_SLABS).define('S', Blocks.SMOOTH_STONE).pattern("WWW").pattern(" S ").pattern(" S "));

    CreateRecipeProvider.GeneratedRecipe SCHEMATICANNON = this.create(AllBlocks.SCHEMATICANNON).unlockedBy(AllItems.EMPTY_SCHEMATIC::get).viaShaped(b -> b.define('L', ItemTags.LOGS).define('D', Blocks.DISPENSER).define('S', Blocks.SMOOTH_STONE).define('I', Blocks.IRON_BLOCK).pattern(" I ").pattern("LIL").pattern("SDS"));

    CreateRecipeProvider.GeneratedRecipe EMPTY_SCHEMATIC = this.create(AllItems.EMPTY_SCHEMATIC).unlockedBy(() -> Items.PAPER).viaShapeless(b -> b.requires(Items.PAPER).requires(Tags.Items.DYES_LIGHT_BLUE));

    CreateRecipeProvider.GeneratedRecipe SCHEMATIC_AND_QUILL = this.create(AllItems.SCHEMATIC_AND_QUILL).unlockedBy(() -> Items.PAPER).viaShapeless(b -> b.requires((ItemLike) AllItems.EMPTY_SCHEMATIC.get()).requires(Tags.Items.FEATHERS));

    private CreateRecipeProvider.Marker PALETTES = this.enterFolder("palettes");

    CreateRecipeProvider.GeneratedRecipe SCORCHIA = this.create(AllPaletteStoneTypes.SCORCHIA.getBaseBlock()::get).returns(8).unlockedBy(AllPaletteStoneTypes.SCORIA.getBaseBlock()::get).viaShaped(b -> b.define('#', (ItemLike) AllPaletteStoneTypes.SCORIA.getBaseBlock().get()).define('D', Tags.Items.DYES_BLACK).pattern("###").pattern("#D#").pattern("###"));

    private CreateRecipeProvider.Marker APPLIANCES = this.enterFolder("appliances");

    CreateRecipeProvider.GeneratedRecipe DOUGH = this.create(AllItems.DOUGH).unlockedByTag(CreateRecipeProvider.I::wheatFlour).viaShapeless(b -> b.requires(CreateRecipeProvider.I.wheatFlour()).requires(Items.WATER_BUCKET));

    CreateRecipeProvider.GeneratedRecipe CLIPBOARD = this.create(AllBlocks.CLIPBOARD).unlockedBy(CreateRecipeProvider.I::andesite).viaShaped(b -> b.define('G', CreateRecipeProvider.I.planks()).define('P', Items.PAPER).define('A', CreateRecipeProvider.I.andesite()).pattern("A").pattern("P").pattern("G"));

    CreateRecipeProvider.GeneratedRecipe CLIPBOARD_CLEAR = this.clearData(AllBlocks.CLIPBOARD);

    CreateRecipeProvider.GeneratedRecipe SCHEDULE_CLEAR = this.clearData(AllItems.SCHEDULE);

    CreateRecipeProvider.GeneratedRecipe FILTER_CLEAR = this.clearData(AllItems.FILTER);

    CreateRecipeProvider.GeneratedRecipe ATTRIBUTE_FILTER_CLEAR = this.clearData(AllItems.ATTRIBUTE_FILTER);

    CreateRecipeProvider.GeneratedRecipe DIVING_HELMET = this.create(AllItems.COPPER_DIVING_HELMET).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('G', Tags.Items.GLASS).define('P', CreateRecipeProvider.I.copper()).pattern("PPP").pattern("PGP"));

    CreateRecipeProvider.GeneratedRecipe COPPER_BACKTANK = this.create(AllItems.COPPER_BACKTANK).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('G', CreateRecipeProvider.I.shaft()).define('A', CreateRecipeProvider.I.andesite()).define('B', CreateRecipeProvider.I.copperBlock()).define('P', CreateRecipeProvider.I.copper()).pattern("AGA").pattern("PBP").pattern(" P "));

    CreateRecipeProvider.GeneratedRecipe DIVING_BOOTS = this.create(AllItems.COPPER_DIVING_BOOTS).unlockedBy(CreateRecipeProvider.I::copper).viaShaped(b -> b.define('G', CreateRecipeProvider.I.andesite()).define('P', CreateRecipeProvider.I.copper()).pattern("P P").pattern("P P").pattern("G G"));

    CreateRecipeProvider.GeneratedRecipe LINKED_CONTROLLER = this.create(AllItems.LINKED_CONTROLLER).unlockedBy(AllBlocks.REDSTONE_LINK::get).viaShaped(b -> b.define('S', ItemTags.WOODEN_BUTTONS).define('P', (ItemLike) AllBlocks.REDSTONE_LINK.get()).pattern("SSS").pattern(" P ").pattern("SSS"));

    CreateRecipeProvider.GeneratedRecipe CRAFTING_BLUEPRINT = this.create(AllItems.CRAFTING_BLUEPRINT).unlockedBy(() -> Items.CRAFTING_TABLE).viaShapeless(b -> b.requires(Items.PAINTING).requires(Items.CRAFTING_TABLE));

    CreateRecipeProvider.GeneratedRecipe SLIME_BALL = this.create((Supplier<ItemLike>) (() -> Items.SLIME_BALL)).unlockedBy(AllItems.DOUGH::get).viaShapeless(b -> b.requires((ItemLike) AllItems.DOUGH.get()).requires(Tags.Items.DYES_LIME));

    CreateRecipeProvider.GeneratedRecipe TREE_FERTILIZER = this.create(AllItems.TREE_FERTILIZER).returns(2).unlockedBy(() -> Items.BONE_MEAL).viaShapeless(b -> b.requires(Ingredient.of(ItemTags.SMALL_FLOWERS), 2).requires(Ingredient.of(Items.HORN_CORAL, Items.BRAIN_CORAL, Items.TUBE_CORAL, Items.BUBBLE_CORAL, Items.FIRE_CORAL)).requires(Items.BONE_MEAL));

    CreateRecipeProvider.GeneratedRecipe NETHERITE_DIVING_HELMET = this.create(AllItems.NETHERITE_DIVING_HELMET).viaNetheriteSmithing(AllItems.COPPER_DIVING_HELMET::get, CreateRecipeProvider.I::netherite);

    CreateRecipeProvider.GeneratedRecipe NETHERITE_BACKTANK = this.create(AllItems.NETHERITE_BACKTANK).viaNetheriteSmithing(AllItems.COPPER_BACKTANK::get, CreateRecipeProvider.I::netherite);

    CreateRecipeProvider.GeneratedRecipe NETHERITE_DIVING_BOOTS = this.create(AllItems.NETHERITE_DIVING_BOOTS).viaNetheriteSmithing(AllItems.COPPER_DIVING_BOOTS::get, CreateRecipeProvider.I::netherite);

    CreateRecipeProvider.GeneratedRecipe NETHERITE_DIVING_HELMET_2 = this.create(AllItems.NETHERITE_DIVING_HELMET).withSuffix("_from_netherite").viaNetheriteSmithing(() -> Items.NETHERITE_HELMET, () -> Ingredient.of((ItemLike) AllItems.COPPER_DIVING_HELMET.get()));

    CreateRecipeProvider.GeneratedRecipe NETHERITE_BACKTANK_2 = this.create(AllItems.NETHERITE_BACKTANK).withSuffix("_from_netherite").viaNetheriteSmithing(() -> Items.NETHERITE_CHESTPLATE, () -> Ingredient.of((ItemLike) AllItems.COPPER_BACKTANK.get()));

    CreateRecipeProvider.GeneratedRecipe NETHERITE_DIVING_BOOTS_2 = this.create(AllItems.NETHERITE_DIVING_BOOTS).withSuffix("_from_netherite").viaNetheriteSmithing(() -> Items.NETHERITE_BOOTS, () -> Ingredient.of((ItemLike) AllItems.COPPER_DIVING_BOOTS.get()));

    private CreateRecipeProvider.Marker COOKING = this.enterFolder("/");

    CreateRecipeProvider.GeneratedRecipe DOUGH_TO_BREAD = this.create((Supplier<ItemLike>) (() -> Items.BREAD)).viaCooking(AllItems.DOUGH::get).inSmoker();

    CreateRecipeProvider.GeneratedRecipe SOUL_SAND = this.create(AllPaletteStoneTypes.SCORIA.getBaseBlock()::get).viaCooking(() -> Blocks.SOUL_SAND).inFurnace();

    CreateRecipeProvider.GeneratedRecipe FRAMED_GLASS = this.recycleGlass(AllPaletteBlocks.FRAMED_GLASS);

    CreateRecipeProvider.GeneratedRecipe TILED_GLASS = this.recycleGlass(AllPaletteBlocks.TILED_GLASS);

    CreateRecipeProvider.GeneratedRecipe VERTICAL_FRAMED_GLASS = this.recycleGlass(AllPaletteBlocks.VERTICAL_FRAMED_GLASS);

    CreateRecipeProvider.GeneratedRecipe HORIZONTAL_FRAMED_GLASS = this.recycleGlass(AllPaletteBlocks.HORIZONTAL_FRAMED_GLASS);

    CreateRecipeProvider.GeneratedRecipe FRAMED_GLASS_PANE = this.recycleGlassPane(AllPaletteBlocks.FRAMED_GLASS_PANE);

    CreateRecipeProvider.GeneratedRecipe TILED_GLASS_PANE = this.recycleGlassPane(AllPaletteBlocks.TILED_GLASS_PANE);

    CreateRecipeProvider.GeneratedRecipe VERTICAL_FRAMED_GLASS_PANE = this.recycleGlassPane(AllPaletteBlocks.VERTICAL_FRAMED_GLASS_PANE);

    CreateRecipeProvider.GeneratedRecipe HORIZONTAL_FRAMED_GLASS_PANE = this.recycleGlassPane(AllPaletteBlocks.HORIZONTAL_FRAMED_GLASS_PANE);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_IRON = this.blastCrushedMetal(() -> Items.IRON_INGOT, AllItems.CRUSHED_IRON::get);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_GOLD = this.blastCrushedMetal(() -> Items.GOLD_INGOT, AllItems.CRUSHED_GOLD::get);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_COPPER = this.blastCrushedMetal(() -> Items.COPPER_INGOT, AllItems.CRUSHED_COPPER::get);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_ZINC = this.blastCrushedMetal(AllItems.ZINC_INGOT::get, AllItems.CRUSHED_ZINC::get);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_OSMIUM = this.blastModdedCrushedMetal(AllItems.CRUSHED_OSMIUM, CompatMetals.OSMIUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_PLATINUM = this.blastModdedCrushedMetal(AllItems.CRUSHED_PLATINUM, CompatMetals.PLATINUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_SILVER = this.blastModdedCrushedMetal(AllItems.CRUSHED_SILVER, CompatMetals.SILVER);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_TIN = this.blastModdedCrushedMetal(AllItems.CRUSHED_TIN, CompatMetals.TIN);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_LEAD = this.blastModdedCrushedMetal(AllItems.CRUSHED_LEAD, CompatMetals.LEAD);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_QUICKSILVER = this.blastModdedCrushedMetal(AllItems.CRUSHED_QUICKSILVER, CompatMetals.QUICKSILVER);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_BAUXITE = this.blastModdedCrushedMetal(AllItems.CRUSHED_BAUXITE, CompatMetals.ALUMINUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_URANIUM = this.blastModdedCrushedMetal(AllItems.CRUSHED_URANIUM, CompatMetals.URANIUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_NICKEL = this.blastModdedCrushedMetal(AllItems.CRUSHED_NICKEL, CompatMetals.NICKEL);

    CreateRecipeProvider.GeneratedRecipe ZINC_ORE = this.create(AllItems.ZINC_INGOT::get).withSuffix("_from_ore").viaCookingTag(() -> AllTags.forgeItemTag("ores/zinc")).rewardXP(1.0F).inBlastFurnace();

    CreateRecipeProvider.GeneratedRecipe RAW_ZINC_ORE = this.create(AllItems.ZINC_INGOT::get).withSuffix("_from_raw_ore").viaCooking(AllItems.RAW_ZINC::get).rewardXP(0.7F).inBlastFurnace();

    String currentFolder = "";

    CreateRecipeProvider.Marker enterFolder(String folder) {
        this.currentFolder = folder;
        return new CreateRecipeProvider.Marker();
    }

    StandardRecipeGen.GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new StandardRecipeGen.GeneratedRecipeBuilder(this.currentFolder, result);
    }

    StandardRecipeGen.GeneratedRecipeBuilder create(ResourceLocation result) {
        return new StandardRecipeGen.GeneratedRecipeBuilder(this.currentFolder, result);
    }

    StandardRecipeGen.GeneratedRecipeBuilder create(ItemProviderEntry<? extends ItemLike> result) {
        return this.create(result::get);
    }

    CreateRecipeProvider.GeneratedRecipe createSpecial(Supplier<? extends SimpleCraftingRecipeSerializer<?>> serializer, String recipeType, String path) {
        ResourceLocation location = Create.asResource(recipeType + "/" + this.currentFolder + "/" + path);
        return this.register(consumer -> {
            SpecialRecipeBuilder b = SpecialRecipeBuilder.special((RecipeSerializer<? extends CraftingRecipe>) serializer.get());
            b.save(consumer, location.toString());
        });
    }

    CreateRecipeProvider.GeneratedRecipe blastCrushedMetal(Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> ingredient) {
        return this.create(result::get).withSuffix("_from_crushed").viaCooking(ingredient::get).rewardXP(0.1F).inBlastFurnace();
    }

    CreateRecipeProvider.GeneratedRecipe blastModdedCrushedMetal(ItemEntry<? extends Item> ingredient, CompatMetals metal) {
        String metalName = metal.getName();
        for (Mods mod : metal.getMods()) {
            ResourceLocation ingot = mod.ingotOf(metalName);
            String modId = mod.getId();
            this.create(ingot).withSuffix("_compat_" + modId).whenModLoaded(modId).viaCooking(ingredient::get).rewardXP(0.1F).inBlastFurnace();
        }
        return null;
    }

    CreateRecipeProvider.GeneratedRecipe recycleGlass(BlockEntry<? extends Block> ingredient) {
        return this.create((Supplier<ItemLike>) (() -> Blocks.GLASS)).withSuffix("_from_" + ingredient.getId().getPath()).viaCooking(ingredient::get).forDuration(50).inFurnace();
    }

    CreateRecipeProvider.GeneratedRecipe recycleGlassPane(BlockEntry<? extends Block> ingredient) {
        return this.create((Supplier<ItemLike>) (() -> Blocks.GLASS_PANE)).withSuffix("_from_" + ingredient.getId().getPath()).viaCooking(ingredient::get).forDuration(50).inFurnace();
    }

    CreateRecipeProvider.GeneratedRecipe metalCompacting(List<ItemProviderEntry<? extends ItemLike>> variants, List<Supplier<TagKey<Item>>> ingredients) {
        CreateRecipeProvider.GeneratedRecipe result = null;
        for (int i = 0; i + 1 < variants.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = (ItemProviderEntry<? extends ItemLike>) variants.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = (ItemProviderEntry<? extends ItemLike>) variants.get(i + 1);
            Supplier<TagKey<Item>> currentIngredient = (Supplier<TagKey<Item>>) ingredients.get(i);
            Supplier<TagKey<Item>> nextIngredient = (Supplier<TagKey<Item>>) ingredients.get(i + 1);
            result = this.create(nextEntry).withSuffix("_from_compacting").unlockedBy(currentEntry::get).viaShaped(b -> b.pattern("###").pattern("###").pattern("###").define('#', (TagKey<Item>) currentIngredient.get()));
            result = this.create(currentEntry).returns(9).withSuffix("_from_decompacting").unlockedBy(nextEntry::get).viaShapeless(b -> b.requires((TagKey<Item>) nextIngredient.get()));
        }
        return result;
    }

    CreateRecipeProvider.GeneratedRecipe conversionCycle(List<ItemProviderEntry<? extends ItemLike>> cycle) {
        CreateRecipeProvider.GeneratedRecipe result = null;
        for (int i = 0; i < cycle.size(); i++) {
            ItemProviderEntry<? extends ItemLike> currentEntry = (ItemProviderEntry<? extends ItemLike>) cycle.get(i);
            ItemProviderEntry<? extends ItemLike> nextEntry = (ItemProviderEntry<? extends ItemLike>) cycle.get((i + 1) % cycle.size());
            result = this.create(nextEntry).withSuffix("from_conversion").unlockedBy(currentEntry::get).viaShapeless(b -> b.requires((ItemLike) currentEntry.get()));
        }
        return result;
    }

    CreateRecipeProvider.GeneratedRecipe clearData(ItemProviderEntry<? extends ItemLike> item) {
        return this.create(item).withSuffix("_clear").unlockedBy(item::get).viaShapeless(b -> b.requires((ItemLike) item.get()));
    }

    @Override
    public String getName() {
        return "Create's Standard Recipes";
    }

    public StandardRecipeGen(PackOutput p_i48262_1_) {
        super(p_i48262_1_);
    }

    class GeneratedRecipeBuilder {

        private String path;

        private String suffix;

        private Supplier<? extends ItemLike> result;

        private ResourceLocation compatDatagenOutput;

        List<ICondition> recipeConditions;

        private Supplier<ItemPredicate> unlockedBy;

        private int amount;

        private GeneratedRecipeBuilder(String path) {
            this.path = path;
            this.recipeConditions = new ArrayList();
            this.suffix = "";
            this.amount = 1;
        }

        public GeneratedRecipeBuilder(String path, Supplier<? extends ItemLike> result) {
            this(path);
            this.result = result;
        }

        public GeneratedRecipeBuilder(String path, ResourceLocation result) {
            this(path);
            this.compatDatagenOutput = result;
        }

        StandardRecipeGen.GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        StandardRecipeGen.GeneratedRecipeBuilder unlockedBy(Supplier<? extends ItemLike> item) {
            this.unlockedBy = () -> ItemPredicate.Builder.item().of((ItemLike) item.get()).build();
            return this;
        }

        StandardRecipeGen.GeneratedRecipeBuilder unlockedByTag(Supplier<TagKey<Item>> tag) {
            this.unlockedBy = () -> ItemPredicate.Builder.item().of((TagKey<Item>) tag.get()).build();
            return this;
        }

        StandardRecipeGen.GeneratedRecipeBuilder whenModLoaded(String modid) {
            return this.withCondition(new ModLoadedCondition(modid));
        }

        StandardRecipeGen.GeneratedRecipeBuilder whenModMissing(String modid) {
            return this.withCondition(new NotCondition(new ModLoadedCondition(modid)));
        }

        StandardRecipeGen.GeneratedRecipeBuilder withCondition(ICondition condition) {
            this.recipeConditions.add(condition);
            return this;
        }

        StandardRecipeGen.GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        CreateRecipeProvider.GeneratedRecipe viaShaped(UnaryOperator<ShapedRecipeBuilder> builder) {
            return StandardRecipeGen.this.register(consumer -> {
                ShapedRecipeBuilder b = (ShapedRecipeBuilder) builder.apply(ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) this.result.get(), this.amount));
                if (this.unlockedBy != null) {
                    b.unlockedBy("has_item", StandardRecipeGen.m_126011_(new ItemPredicate[] { (ItemPredicate) this.unlockedBy.get() }));
                }
                b.save(consumer, this.createLocation("crafting"));
            });
        }

        CreateRecipeProvider.GeneratedRecipe viaShapeless(UnaryOperator<ShapelessRecipeBuilder> builder) {
            return StandardRecipeGen.this.register(consumer -> {
                ShapelessRecipeBuilder b = (ShapelessRecipeBuilder) builder.apply(ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) this.result.get(), this.amount));
                if (this.unlockedBy != null) {
                    b.unlockedBy("has_item", StandardRecipeGen.m_126011_(new ItemPredicate[] { (ItemPredicate) this.unlockedBy.get() }));
                }
                b.save(consumer, this.createLocation("crafting"));
            });
        }

        CreateRecipeProvider.GeneratedRecipe viaNetheriteSmithing(Supplier<? extends Item> base, Supplier<Ingredient> upgradeMaterial) {
            return StandardRecipeGen.this.register(consumer -> {
                SmithingTransformRecipeBuilder b = SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of((ItemLike) base.get()), (Ingredient) upgradeMaterial.get(), RecipeCategory.COMBAT, ((ItemLike) this.result.get()).asItem());
                b.unlocks("has_item", StandardRecipeGen.m_126011_(new ItemPredicate[] { ItemPredicate.Builder.item().of((ItemLike) base.get()).build() }));
                b.save(consumer, this.createLocation("crafting"));
            });
        }

        private ResourceLocation createSimpleLocation(String recipeType) {
            return Create.asResource(recipeType + "/" + this.getRegistryName().getPath() + this.suffix);
        }

        private ResourceLocation createLocation(String recipeType) {
            return Create.asResource(recipeType + "/" + this.path + "/" + this.getRegistryName().getPath() + this.suffix);
        }

        private ResourceLocation getRegistryName() {
            return this.compatDatagenOutput == null ? RegisteredObjects.getKeyOrThrow(((ItemLike) this.result.get()).asItem()) : this.compatDatagenOutput;
        }

        StandardRecipeGen.GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCooking(Supplier<? extends ItemLike> item) {
            return this.unlockedBy(item).viaCookingIngredient(() -> Ingredient.of((ItemLike) item.get()));
        }

        StandardRecipeGen.GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingTag(Supplier<TagKey<Item>> tag) {
            return this.unlockedByTag(tag).viaCookingIngredient(() -> Ingredient.of((TagKey<Item>) tag.get()));
        }

        StandardRecipeGen.GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder viaCookingIngredient(Supplier<Ingredient> ingredient) {
            return new StandardRecipeGen.GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder(ingredient);
        }

        class GeneratedCookingRecipeBuilder {

            private Supplier<Ingredient> ingredient;

            private float exp;

            private int cookingTime;

            private final RecipeSerializer<? extends AbstractCookingRecipe> FURNACE = RecipeSerializer.SMELTING_RECIPE;

            private final RecipeSerializer<? extends AbstractCookingRecipe> SMOKER = RecipeSerializer.SMOKING_RECIPE;

            private final RecipeSerializer<? extends AbstractCookingRecipe> BLAST = RecipeSerializer.BLASTING_RECIPE;

            private final RecipeSerializer<? extends AbstractCookingRecipe> CAMPFIRE = RecipeSerializer.CAMPFIRE_COOKING_RECIPE;

            GeneratedCookingRecipeBuilder(Supplier<Ingredient> ingredient) {
                this.ingredient = ingredient;
                this.cookingTime = 200;
                this.exp = 0.0F;
            }

            StandardRecipeGen.GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder forDuration(int duration) {
                this.cookingTime = duration;
                return this;
            }

            StandardRecipeGen.GeneratedRecipeBuilder.GeneratedCookingRecipeBuilder rewardXP(float xp) {
                this.exp = xp;
                return this;
            }

            CreateRecipeProvider.GeneratedRecipe inFurnace() {
                return this.inFurnace(b -> b);
            }

            CreateRecipeProvider.GeneratedRecipe inFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                return this.create(this.FURNACE, builder, 1.0F);
            }

            CreateRecipeProvider.GeneratedRecipe inSmoker() {
                return this.inSmoker(b -> b);
            }

            CreateRecipeProvider.GeneratedRecipe inSmoker(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                this.create(this.FURNACE, builder, 1.0F);
                this.create(this.CAMPFIRE, builder, 3.0F);
                return this.create(this.SMOKER, builder, 0.5F);
            }

            CreateRecipeProvider.GeneratedRecipe inBlastFurnace() {
                return this.inBlastFurnace(b -> b);
            }

            CreateRecipeProvider.GeneratedRecipe inBlastFurnace(UnaryOperator<SimpleCookingRecipeBuilder> builder) {
                this.create(this.FURNACE, builder, 1.0F);
                return this.create(this.BLAST, builder, 0.5F);
            }

            private CreateRecipeProvider.GeneratedRecipe create(RecipeSerializer<? extends AbstractCookingRecipe> serializer, UnaryOperator<SimpleCookingRecipeBuilder> builder, float cookingTimeModifier) {
                return StandardRecipeGen.this.register(consumer -> {
                    boolean isOtherMod = GeneratedRecipeBuilder.this.compatDatagenOutput != null;
                    SimpleCookingRecipeBuilder b = (SimpleCookingRecipeBuilder) builder.apply(SimpleCookingRecipeBuilder.generic((Ingredient) this.ingredient.get(), RecipeCategory.MISC, (ItemLike) (isOtherMod ? Items.DIRT : (ItemLike) GeneratedRecipeBuilder.this.result.get()), this.exp, (int) ((float) this.cookingTime * cookingTimeModifier), serializer));
                    if (GeneratedRecipeBuilder.this.unlockedBy != null) {
                        b.unlockedBy("has_item", StandardRecipeGen.m_126011_(new ItemPredicate[] { (ItemPredicate) GeneratedRecipeBuilder.this.unlockedBy.get() }));
                    }
                    b.save(result -> consumer.accept(isOtherMod ? new StandardRecipeGen.ModdedCookingRecipeResult(result, GeneratedRecipeBuilder.this.compatDatagenOutput, GeneratedRecipeBuilder.this.recipeConditions) : result), GeneratedRecipeBuilder.this.createSimpleLocation(RegisteredObjects.getKeyOrThrow(serializer).getPath()));
                });
            }
        }
    }

    private static class ModdedCookingRecipeResult implements FinishedRecipe {

        private FinishedRecipe wrapped;

        private ResourceLocation outputOverride;

        private List<ICondition> conditions;

        public ModdedCookingRecipeResult(FinishedRecipe wrapped, ResourceLocation outputOverride, List<ICondition> conditions) {
            this.wrapped = wrapped;
            this.outputOverride = outputOverride;
            this.conditions = conditions;
        }

        @Override
        public ResourceLocation getId() {
            return this.wrapped.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.wrapped.getType();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.wrapped.serializeAdvancement();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return this.wrapped.getAdvancementId();
        }

        @Override
        public void serializeRecipeData(JsonObject object) {
            this.wrapped.serializeRecipeData(object);
            object.addProperty("result", this.outputOverride.toString());
            JsonArray conds = new JsonArray();
            this.conditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
            object.add("conditions", conds);
        }
    }
}