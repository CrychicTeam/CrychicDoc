package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;

public class FillingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe HONEY_BOTTLE = this.create("honey_bottle", b -> b.require(AllTags.AllFluidTags.HONEY.tag, 250).require(Items.GLASS_BOTTLE).output(Items.HONEY_BOTTLE));

    CreateRecipeProvider.GeneratedRecipe BUILDERS_TEA = this.create("builders_tea", b -> b.require((Fluid) AllFluids.TEA.get(), 250).require(Items.GLASS_BOTTLE).output((ItemLike) AllItems.BUILDERS_TEA.get()));

    CreateRecipeProvider.GeneratedRecipe FD_MILK = this.create(Mods.FD.recipeId("milk_bottle"), b -> b.require(Tags.Fluids.MILK, 250).require(Items.GLASS_BOTTLE).output(1.0F, Mods.FD, "milk_bottle", 1).whenModLoaded(Mods.FD.getId()));

    CreateRecipeProvider.GeneratedRecipe BLAZE_CAKE = this.create("blaze_cake", b -> b.require(Fluids.LAVA, 250).require((ItemLike) AllItems.BLAZE_CAKE_BASE.get()).output((ItemLike) AllItems.BLAZE_CAKE.get()));

    CreateRecipeProvider.GeneratedRecipe HONEYED_APPLE = this.create("honeyed_apple", b -> b.require(AllTags.AllFluidTags.HONEY.tag, 250).require(Items.APPLE).output((ItemLike) AllItems.HONEYED_APPLE.get()));

    CreateRecipeProvider.GeneratedRecipe SWEET_ROLL = this.create("sweet_roll", b -> b.require(Tags.Fluids.MILK, 250).require(Items.BREAD).output((ItemLike) AllItems.SWEET_ROLL.get()));

    CreateRecipeProvider.GeneratedRecipe CHOCOLATE_BERRIES = this.create("chocolate_glazed_berries", b -> b.require((Fluid) AllFluids.CHOCOLATE.get(), 250).require(Items.SWEET_BERRIES).output((ItemLike) AllItems.CHOCOLATE_BERRIES.get()));

    CreateRecipeProvider.GeneratedRecipe GRASS_BLOCK = this.create("grass_block", b -> b.require(Fluids.WATER, 500).require(Items.DIRT).output(Items.GRASS_BLOCK));

    CreateRecipeProvider.GeneratedRecipe GUNPOWDER = this.create("gunpowder", b -> b.require(PotionFluidHandler.potionIngredient(Potions.HARMING, 25)).require((ItemLike) AllItems.CINDER_FLOUR.get()).output(Items.GUNPOWDER));

    CreateRecipeProvider.GeneratedRecipe REDSTONE = this.create("redstone", b -> b.require(PotionFluidHandler.potionIngredient(Potions.STRENGTH, 25)).require((ItemLike) AllItems.CINDER_FLOUR.get()).output(Items.REDSTONE));

    CreateRecipeProvider.GeneratedRecipe GLOWSTONE = this.create("glowstone", b -> b.require(PotionFluidHandler.potionIngredient(Potions.NIGHT_VISION, 25)).require((ItemLike) AllItems.CINDER_FLOUR.get()).output(Items.GLOWSTONE_DUST));

    public FillingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.FILLING;
    }
}