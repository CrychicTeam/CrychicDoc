package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class PressingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe SUGAR_CANE = this.create(() -> Items.SUGAR_CANE, b -> b.output(Items.PAPER));

    CreateRecipeProvider.GeneratedRecipe PATH = this.create("path", b -> b.require(Ingredient.of(Items.GRASS_BLOCK, Items.DIRT)).output(Items.DIRT_PATH));

    CreateRecipeProvider.GeneratedRecipe IRON = this.create("iron_ingot", b -> b.require(CreateRecipeProvider.I.iron()).output((ItemLike) AllItems.IRON_SHEET.get()));

    CreateRecipeProvider.GeneratedRecipe GOLD = this.create("gold_ingot", b -> b.require(CreateRecipeProvider.I.gold()).output((ItemLike) AllItems.GOLDEN_SHEET.get()));

    CreateRecipeProvider.GeneratedRecipe COPPER = this.create("copper_ingot", b -> b.require(CreateRecipeProvider.I.copper()).output((ItemLike) AllItems.COPPER_SHEET.get()));

    CreateRecipeProvider.GeneratedRecipe BRASS = this.create("brass_ingot", b -> b.require(CreateRecipeProvider.I.brass()).output((ItemLike) AllItems.BRASS_SHEET.get()));

    public PressingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.PRESSING;
    }
}