package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

public class CompactingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe GRANITE = this.create("granite_from_flint", b -> b.require(Items.FLINT).require(Items.FLINT).require(Fluids.LAVA, 100).require(Items.RED_SAND).output(Blocks.GRANITE, 1));

    CreateRecipeProvider.GeneratedRecipe DIORITE = this.create("diorite_from_flint", b -> b.require(Items.FLINT).require(Items.FLINT).require(Fluids.LAVA, 100).require(Items.CALCITE).output(Blocks.DIORITE, 1));

    CreateRecipeProvider.GeneratedRecipe ANDESITE = this.create("andesite_from_flint", b -> b.require(Items.FLINT).require(Items.FLINT).require(Fluids.LAVA, 100).require(Items.GRAVEL).output(Blocks.ANDESITE, 1));

    CreateRecipeProvider.GeneratedRecipe CHOCOLATE = this.create("chocolate", b -> b.require((Fluid) AllFluids.CHOCOLATE.get(), 250).output((ItemLike) AllItems.BAR_OF_CHOCOLATE.get(), 1));

    CreateRecipeProvider.GeneratedRecipe BLAZE_CAKE = this.create("blaze_cake", b -> b.require(Tags.Items.EGGS).require(Items.SUGAR).require((ItemLike) AllItems.CINDER_FLOUR.get()).output((ItemLike) AllItems.BLAZE_CAKE_BASE.get(), 1));

    CreateRecipeProvider.GeneratedRecipe HONEY = this.create("honey", b -> b.require(AllTags.AllFluidTags.HONEY.tag, 1000).output(Items.HONEY_BLOCK, 1));

    public CompactingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.COMPACTING;
    }
}