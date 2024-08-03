package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.augment.IAugment;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class AnvilAugmentRecipeBuilder {

    public ResourceLocation id;

    public Ingredient tool;

    public Ingredient input;

    public IAugment augment;

    public static AnvilAugmentRecipeBuilder create(IAugment augment) {
        AnvilAugmentRecipeBuilder builder = new AnvilAugmentRecipeBuilder();
        builder.augment = augment;
        builder.id = augment.getName();
        return builder;
    }

    public AnvilAugmentRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public AnvilAugmentRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public AnvilAugmentRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public AnvilAugmentRecipeBuilder tool(Ingredient tool) {
        this.tool = tool;
        return this;
    }

    public AnvilAugmentRecipeBuilder tool(ItemLike... tool) {
        this.tool(Ingredient.of(tool));
        return this;
    }

    public AnvilAugmentRecipeBuilder tool(TagKey<Item> tag) {
        this.tool(Ingredient.of(tag));
        return this;
    }

    public AnvilAugmentRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public AnvilAugmentRecipeBuilder input(ItemLike... input) {
        this.input(Ingredient.of(input));
        return this;
    }

    public AnvilAugmentRecipeBuilder input(TagKey<Item> tag) {
        this.input(Ingredient.of(tag));
        return this;
    }

    public AnvilAugmentRecipe build() {
        return new AnvilAugmentRecipe(this.id, this.tool, this.input, this.augment);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new AnvilAugmentRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final AnvilAugmentRecipe recipe;

        public Finished(AnvilAugmentRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("tool", this.recipe.tool.toJson());
            json.add("input", this.recipe.input.toJson());
            json.addProperty("augment", this.recipe.augment.getName().toString());
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.TOOL_AUGMENT.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}