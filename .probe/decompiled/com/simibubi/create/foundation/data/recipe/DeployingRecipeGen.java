package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.block.CopperBlockSet;
import java.util.function.Supplier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;

public class DeployingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe COPPER_TILES = this.copperChain(AllBlocks.COPPER_TILES);

    CreateRecipeProvider.GeneratedRecipe COPPER_SHINGLES = this.copperChain(AllBlocks.COPPER_SHINGLES);

    CreateRecipeProvider.GeneratedRecipe COGWHEEL = this.create("cogwheel", b -> b.require(CreateRecipeProvider.I.shaft()).require(CreateRecipeProvider.I.planks()).output(CreateRecipeProvider.I.cog()));

    CreateRecipeProvider.GeneratedRecipe LARGE_COGWHEEL = this.create("large_cogwheel", b -> b.require(CreateRecipeProvider.I.cog()).require(CreateRecipeProvider.I.planks()).output(CreateRecipeProvider.I.largeCog()));

    CreateRecipeProvider.GeneratedRecipe CB1 = this.addWax(() -> Blocks.WAXED_COPPER_BLOCK, () -> Blocks.COPPER_BLOCK);

    CreateRecipeProvider.GeneratedRecipe CB2 = this.addWax(() -> Blocks.WAXED_EXPOSED_COPPER, () -> Blocks.EXPOSED_COPPER);

    CreateRecipeProvider.GeneratedRecipe CB3 = this.addWax(() -> Blocks.WAXED_WEATHERED_COPPER, () -> Blocks.WEATHERED_COPPER);

    CreateRecipeProvider.GeneratedRecipe CB4 = this.addWax(() -> Blocks.WAXED_OXIDIZED_COPPER, () -> Blocks.OXIDIZED_COPPER);

    CreateRecipeProvider.GeneratedRecipe CCB1 = this.addWax(() -> Blocks.WAXED_CUT_COPPER, () -> Blocks.CUT_COPPER);

    CreateRecipeProvider.GeneratedRecipe CCB2 = this.addWax(() -> Blocks.WAXED_EXPOSED_CUT_COPPER, () -> Blocks.EXPOSED_CUT_COPPER);

    CreateRecipeProvider.GeneratedRecipe CCB3 = this.addWax(() -> Blocks.WAXED_WEATHERED_CUT_COPPER, () -> Blocks.WEATHERED_CUT_COPPER);

    CreateRecipeProvider.GeneratedRecipe CCB4 = this.addWax(() -> Blocks.WAXED_OXIDIZED_CUT_COPPER, () -> Blocks.OXIDIZED_CUT_COPPER);

    CreateRecipeProvider.GeneratedRecipe CCST1 = this.addWax(() -> Blocks.WAXED_CUT_COPPER_STAIRS, () -> Blocks.CUT_COPPER_STAIRS);

    CreateRecipeProvider.GeneratedRecipe CCST2 = this.addWax(() -> Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, () -> Blocks.EXPOSED_CUT_COPPER_STAIRS);

    CreateRecipeProvider.GeneratedRecipe CCST3 = this.addWax(() -> Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, () -> Blocks.WEATHERED_CUT_COPPER_STAIRS);

    CreateRecipeProvider.GeneratedRecipe CCST4 = this.addWax(() -> Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS, () -> Blocks.OXIDIZED_CUT_COPPER_STAIRS);

    CreateRecipeProvider.GeneratedRecipe CCS1 = this.addWax(() -> Blocks.WAXED_CUT_COPPER_SLAB, () -> Blocks.CUT_COPPER_SLAB);

    CreateRecipeProvider.GeneratedRecipe CCS2 = this.addWax(() -> Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, () -> Blocks.EXPOSED_CUT_COPPER_SLAB);

    CreateRecipeProvider.GeneratedRecipe CCS3 = this.addWax(() -> Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, () -> Blocks.WEATHERED_CUT_COPPER_SLAB);

    CreateRecipeProvider.GeneratedRecipe CCS4 = this.addWax(() -> Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, () -> Blocks.OXIDIZED_CUT_COPPER_SLAB);

    public CreateRecipeProvider.GeneratedRecipe copperChain(CopperBlockSet set) {
        for (CopperBlockSet.Variant<?> variant : set.getVariants()) {
            for (WeatheringCopper.WeatherState state : WeatheringCopper.WeatherState.values()) {
                this.addWax(set.get(variant, state, true)::get, set.get(variant, state, false)::get);
            }
        }
        return null;
    }

    public CreateRecipeProvider.GeneratedRecipe addWax(Supplier<ItemLike> waxed, Supplier<ItemLike> nonWaxed) {
        return this.createWithDeferredId(this.idWithSuffix(waxed, "_from_adding_wax"), b -> b.require((ItemLike) nonWaxed.get()).require(Items.HONEYCOMB_BLOCK).toolNotConsumed().output((ItemLike) waxed.get()));
    }

    public DeployingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }
}