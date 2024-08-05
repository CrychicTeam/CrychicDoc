package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

public class MillingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe GRANITE = this.create(() -> Blocks.GRANITE, b -> b.duration(200).output(Blocks.RED_SAND));

    CreateRecipeProvider.GeneratedRecipe WOOL = this.create("wool", b -> b.duration(100).require(ItemTags.WOOL).output(Items.STRING));

    CreateRecipeProvider.GeneratedRecipe CLAY = this.create(() -> Blocks.CLAY, b -> b.duration(50).output(Items.CLAY_BALL, 3).output(0.5F, Items.CLAY_BALL));

    CreateRecipeProvider.GeneratedRecipe CALCITE = this.create(() -> Items.CALCITE, b -> b.duration(250).output(0.75F, Items.BONE_MEAL, 1));

    CreateRecipeProvider.GeneratedRecipe DRIPSTONE = this.create(() -> Items.DRIPSTONE_BLOCK, b -> b.duration(250).output(Items.CLAY_BALL, 1));

    CreateRecipeProvider.GeneratedRecipe TERRACOTTA = this.create(() -> Blocks.TERRACOTTA, b -> b.duration(200).output(Blocks.RED_SAND));

    CreateRecipeProvider.GeneratedRecipe ANDESITE = this.create(() -> Blocks.ANDESITE, b -> b.duration(200).output(Blocks.COBBLESTONE));

    CreateRecipeProvider.GeneratedRecipe COBBLESTONE = this.create(() -> Blocks.COBBLESTONE, b -> b.duration(250).output(Blocks.GRAVEL));

    CreateRecipeProvider.GeneratedRecipe GRAVEL = this.create(() -> Blocks.GRAVEL, b -> b.duration(250).output(Items.FLINT));

    CreateRecipeProvider.GeneratedRecipe SANDSTONE = this.create(() -> Blocks.SANDSTONE, b -> b.duration(150).output(Blocks.SAND));

    CreateRecipeProvider.GeneratedRecipe WHEAT = this.create(() -> Items.WHEAT, b -> b.duration(150).output((ItemLike) AllItems.WHEAT_FLOUR.get()).output(0.25F, (ItemLike) AllItems.WHEAT_FLOUR.get(), 2).output(0.25F, Items.WHEAT_SEEDS));

    CreateRecipeProvider.GeneratedRecipe BONE = this.create(() -> Items.BONE, b -> b.duration(100).output(Items.BONE_MEAL, 3).output(0.25F, Items.WHITE_DYE, 1).output(0.25F, Items.BONE_MEAL, 3));

    CreateRecipeProvider.GeneratedRecipe CACTUS = this.create(() -> Blocks.CACTUS, b -> b.duration(50).output(Items.GREEN_DYE, 2).output(0.1F, Items.GREEN_DYE, 1).whenModMissing("quark"));

    CreateRecipeProvider.GeneratedRecipe SEA_PICKLE = this.create(() -> Blocks.SEA_PICKLE, b -> b.duration(50).output(Items.LIME_DYE, 2).output(0.1F, Items.GREEN_DYE));

    CreateRecipeProvider.GeneratedRecipe BONE_MEAL = this.create(() -> Items.BONE_MEAL, b -> b.duration(70).output(Items.WHITE_DYE, 2).output(0.1F, Items.LIGHT_GRAY_DYE, 1));

    CreateRecipeProvider.GeneratedRecipe COCOA_BEANS = this.create(() -> Items.COCOA_BEANS, b -> b.duration(70).output(Items.BROWN_DYE, 2).output(0.1F, Items.BROWN_DYE, 1));

    CreateRecipeProvider.GeneratedRecipe SADDLE = this.create(() -> Items.SADDLE, b -> b.duration(200).output(Items.LEATHER, 2).output(0.5F, Items.LEATHER, 2));

    CreateRecipeProvider.GeneratedRecipe SUGAR_CANE = this.create(() -> Items.SUGAR_CANE, b -> b.duration(50).output(Items.SUGAR, 2).output(0.1F, Items.SUGAR));

    CreateRecipeProvider.GeneratedRecipe BEETROOT = this.create(() -> Items.BEETROOT, b -> b.duration(70).output(Items.RED_DYE, 2).output(0.1F, Items.BEETROOT_SEEDS));

    CreateRecipeProvider.GeneratedRecipe INK_SAC = this.create(() -> Items.INK_SAC, b -> b.duration(100).output(Items.BLACK_DYE, 2).output(0.1F, Items.GRAY_DYE));

    CreateRecipeProvider.GeneratedRecipe CHARCOAL = this.create(() -> Items.CHARCOAL, b -> b.duration(100).output(Items.BLACK_DYE, 1).output(0.1F, Items.GRAY_DYE, 2));

    CreateRecipeProvider.GeneratedRecipe COAL = this.create(() -> Items.COAL, b -> b.duration(100).output(Items.BLACK_DYE, 2).output(0.1F, Items.GRAY_DYE, 1));

    CreateRecipeProvider.GeneratedRecipe LAPIS_LAZULI = this.create(() -> Items.LAPIS_LAZULI, b -> b.duration(100).output(Items.BLUE_DYE, 2).output(0.1F, Items.BLUE_DYE));

    CreateRecipeProvider.GeneratedRecipe AZURE_BLUET = this.create(() -> Blocks.AZURE_BLUET, b -> b.duration(50).output(Items.LIGHT_GRAY_DYE, 2).output(0.1F, Items.WHITE_DYE, 2));

    CreateRecipeProvider.GeneratedRecipe BLUE_ORCHID = this.create(() -> Blocks.BLUE_ORCHID, b -> b.duration(50).output(Items.LIGHT_BLUE_DYE, 2).output(0.05F, Items.LIGHT_GRAY_DYE, 1));

    CreateRecipeProvider.GeneratedRecipe FERN = this.create(() -> Blocks.FERN, b -> b.duration(50).output(Items.GREEN_DYE).output(0.1F, Items.WHEAT_SEEDS));

    CreateRecipeProvider.GeneratedRecipe LARGE_FERN = this.create(() -> Blocks.LARGE_FERN, b -> b.duration(50).output(Items.GREEN_DYE, 2).output(0.5F, Items.GREEN_DYE).output(0.1F, Items.WHEAT_SEEDS));

    CreateRecipeProvider.GeneratedRecipe LILAC = this.create(() -> Blocks.LILAC, b -> b.duration(100).output(Items.MAGENTA_DYE, 3).output(0.25F, Items.MAGENTA_DYE).output(0.25F, Items.PURPLE_DYE));

    CreateRecipeProvider.GeneratedRecipe PEONY = this.create(() -> Blocks.PEONY, b -> b.duration(100).output(Items.PINK_DYE, 3).output(0.25F, Items.MAGENTA_DYE).output(0.25F, Items.PINK_DYE));

    CreateRecipeProvider.GeneratedRecipe ALLIUM = this.create(() -> Blocks.ALLIUM, b -> b.duration(50).output(Items.MAGENTA_DYE, 2).output(0.1F, Items.PURPLE_DYE, 2).output(0.1F, Items.PINK_DYE));

    CreateRecipeProvider.GeneratedRecipe LILY_OF_THE_VALLEY = this.create(() -> Blocks.LILY_OF_THE_VALLEY, b -> b.duration(50).output(Items.WHITE_DYE, 2).output(0.1F, Items.LIME_DYE).output(0.1F, Items.WHITE_DYE));

    CreateRecipeProvider.GeneratedRecipe ROSE_BUSH = this.create(() -> Blocks.ROSE_BUSH, b -> b.duration(50).output(Items.RED_DYE, 3).output(0.05F, Items.GREEN_DYE, 2).output(0.25F, Items.RED_DYE, 2));

    CreateRecipeProvider.GeneratedRecipe SUNFLOWER = this.create(() -> Blocks.SUNFLOWER, b -> b.duration(100).output(Items.YELLOW_DYE, 3).output(0.25F, Items.ORANGE_DYE).output(0.25F, Items.YELLOW_DYE));

    CreateRecipeProvider.GeneratedRecipe OXEYE_DAISY = this.create(() -> Blocks.OXEYE_DAISY, b -> b.duration(50).output(Items.LIGHT_GRAY_DYE, 2).output(0.2F, Items.WHITE_DYE).output(0.05F, Items.YELLOW_DYE));

    CreateRecipeProvider.GeneratedRecipe POPPY = this.create(() -> Blocks.POPPY, b -> b.duration(50).output(Items.RED_DYE, 2).output(0.05F, Items.GREEN_DYE));

    CreateRecipeProvider.GeneratedRecipe DANDELION = this.create(() -> Blocks.DANDELION, b -> b.duration(50).output(Items.YELLOW_DYE, 2).output(0.05F, Items.YELLOW_DYE));

    CreateRecipeProvider.GeneratedRecipe CORNFLOWER = this.create(() -> Blocks.CORNFLOWER, b -> b.duration(50).output(Items.BLUE_DYE, 2));

    CreateRecipeProvider.GeneratedRecipe WITHER_ROSE = this.create(() -> Blocks.WITHER_ROSE, b -> b.duration(50).output(Items.BLACK_DYE, 2).output(0.1F, Items.BLACK_DYE));

    CreateRecipeProvider.GeneratedRecipe ORANGE_TULIP = this.create(() -> Blocks.ORANGE_TULIP, b -> b.duration(50).output(Items.ORANGE_DYE, 2).output(0.1F, Items.LIME_DYE));

    CreateRecipeProvider.GeneratedRecipe RED_TULIP = this.create(() -> Blocks.RED_TULIP, b -> b.duration(50).output(Items.RED_DYE, 2).output(0.1F, Items.LIME_DYE));

    CreateRecipeProvider.GeneratedRecipe WHITE_TULIP = this.create(() -> Blocks.WHITE_TULIP, b -> b.duration(50).output(Items.WHITE_DYE, 2).output(0.1F, Items.LIME_DYE));

    CreateRecipeProvider.GeneratedRecipe PINK_TULIP = this.create(() -> Blocks.PINK_TULIP, b -> b.duration(50).output(Items.PINK_DYE, 2).output(0.1F, Items.LIME_DYE));

    CreateRecipeProvider.GeneratedRecipe TALL_GRASS = this.create(() -> Blocks.TALL_GRASS, b -> b.duration(100).output(0.5F, Items.WHEAT_SEEDS));

    CreateRecipeProvider.GeneratedRecipe GRASS = this.create(() -> Blocks.GRASS, b -> b.duration(50).output(0.25F, Items.WHEAT_SEEDS));

    protected CreateRecipeProvider.GeneratedRecipe metalOre(String name, ItemEntry<? extends Item> crushed, int duration) {
        return this.create(name + "_ore", b -> b.duration(duration).withCondition(new NotCondition(new TagEmptyCondition("forge", "ores/" + name))).require(AllTags.forgeItemTag("ores/" + name)).output((ItemLike) crushed.get()));
    }

    public MillingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MILLING;
    }
}