package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import java.util.function.Supplier;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ItemApplicationRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe ANDESITE = this.woodCasing("andesite", CreateRecipeProvider.I::andesite, CreateRecipeProvider.I::andesiteCasing);

    CreateRecipeProvider.GeneratedRecipe COPPER = this.woodCasing("copper", CreateRecipeProvider.I::copper, CreateRecipeProvider.I::copperCasing);

    CreateRecipeProvider.GeneratedRecipe BRASS = this.woodCasingTag("brass", CreateRecipeProvider.I::brass, CreateRecipeProvider.I::brassCasing);

    CreateRecipeProvider.GeneratedRecipe RAILWAY = this.create("railway_casing", b -> b.require(CreateRecipeProvider.I.brassCasing()).require(CreateRecipeProvider.I.sturdySheet()).output(CreateRecipeProvider.I.railwayCasing()));

    protected CreateRecipeProvider.GeneratedRecipe woodCasing(String type, Supplier<ItemLike> ingredient, Supplier<ItemLike> output) {
        return this.woodCasingIngredient(type, () -> Ingredient.of((ItemLike) ingredient.get()), output);
    }

    protected CreateRecipeProvider.GeneratedRecipe woodCasingTag(String type, Supplier<TagKey<Item>> ingredient, Supplier<ItemLike> output) {
        return this.woodCasingIngredient(type, () -> Ingredient.of((TagKey<Item>) ingredient.get()), output);
    }

    protected CreateRecipeProvider.GeneratedRecipe woodCasingIngredient(String type, Supplier<Ingredient> ingredient, Supplier<ItemLike> output) {
        this.create(type + "_casing_from_log", b -> b.require(AllTags.AllItemTags.STRIPPED_LOGS.tag).require((Ingredient) ingredient.get()).output((ItemLike) output.get()));
        return this.create(type + "_casing_from_wood", b -> b.require(AllTags.AllItemTags.STRIPPED_WOOD.tag).require((Ingredient) ingredient.get()).output((ItemLike) output.get()));
    }

    public ItemApplicationRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.ITEM_APPLICATION;
    }
}