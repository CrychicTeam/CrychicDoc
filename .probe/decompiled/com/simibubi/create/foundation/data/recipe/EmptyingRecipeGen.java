package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;

public class EmptyingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe HONEY_BOTTLE = this.create("honey_bottle", b -> b.require(Items.HONEY_BOTTLE).output((Fluid) AllFluids.HONEY.get(), 250).output(Items.GLASS_BOTTLE));

    CreateRecipeProvider.GeneratedRecipe BUILDERS_TEA = this.create("builders_tea", b -> b.require((ItemLike) AllItems.BUILDERS_TEA.get()).output((Fluid) AllFluids.TEA.get(), 250).output(Items.GLASS_BOTTLE));

    CreateRecipeProvider.GeneratedRecipe FD_MILK = this.create(Mods.FD.recipeId("milk_bottle"), b -> b.require(Mods.FD, "milk_bottle").output(ForgeMod.MILK.get(), 250).output(Items.GLASS_BOTTLE).whenModLoaded(Mods.FD.getId()));

    public EmptyingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.EMPTYING;
    }
}