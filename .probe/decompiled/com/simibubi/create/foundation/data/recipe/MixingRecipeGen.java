package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.recipe.BlockTagIngredient;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

public class MixingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe TEMP_LAVA = this.create("lava_from_cobble", b -> b.require(Tags.Items.COBBLESTONE).output(Fluids.LAVA, 50).requiresHeat(HeatCondition.SUPERHEATED));

    CreateRecipeProvider.GeneratedRecipe TEA = this.create("tea", b -> b.require(Fluids.WATER, 250).require(Tags.Fluids.MILK, 250).require(ItemTags.LEAVES).output((Fluid) AllFluids.TEA.get(), 500).requiresHeat(HeatCondition.HEATED));

    CreateRecipeProvider.GeneratedRecipe CHOCOLATE = this.create("chocolate", b -> b.require(Tags.Fluids.MILK, 250).require(Items.SUGAR).require(Items.COCOA_BEANS).output((Fluid) AllFluids.CHOCOLATE.get(), 250).requiresHeat(HeatCondition.HEATED));

    CreateRecipeProvider.GeneratedRecipe CHOCOLATE_MELTING = this.create("chocolate_melting", b -> b.require((ItemLike) AllItems.BAR_OF_CHOCOLATE.get()).output((Fluid) AllFluids.CHOCOLATE.get(), 250).requiresHeat(HeatCondition.HEATED));

    CreateRecipeProvider.GeneratedRecipe HONEY = this.create("honey", b -> b.require(Items.HONEY_BLOCK).output((Fluid) AllFluids.HONEY.get(), 1000).requiresHeat(HeatCondition.HEATED));

    CreateRecipeProvider.GeneratedRecipe DOUGH = this.create("dough_by_mixing", b -> b.require(CreateRecipeProvider.I.wheatFlour()).require(Fluids.WATER, 1000).output((ItemLike) AllItems.DOUGH.get(), 1));

    CreateRecipeProvider.GeneratedRecipe BRASS_INGOT = this.create("brass_ingot", b -> b.require(CreateRecipeProvider.I.copper()).require(CreateRecipeProvider.I.zinc()).output((ItemLike) AllItems.BRASS_INGOT.get(), 2).requiresHeat(HeatCondition.HEATED));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY = this.create("andesite_alloy", b -> b.require(Blocks.ANDESITE).require(CreateRecipeProvider.I.ironNugget()).output(CreateRecipeProvider.I.andesite(), 1));

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY_FROM_ZINC = this.create("andesite_alloy_from_zinc", b -> b.require(Blocks.ANDESITE).require(CreateRecipeProvider.I.zincNugget()).output(CreateRecipeProvider.I.andesite(), 1));

    CreateRecipeProvider.GeneratedRecipe MUD = this.create("mud_by_mixing", b -> b.require(BlockTagIngredient.create(BlockTags.CONVERTABLE_TO_MUD)).require(Fluids.WATER, 250).output(Blocks.MUD, 1));

    public MixingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.MIXING;
    }
}