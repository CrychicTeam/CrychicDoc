package com.simibubi.create.content.processing.sequenced;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;

public class SequencedAssemblyRecipeBuilder {

    private SequencedAssemblyRecipe recipe;

    protected List<ICondition> recipeConditions = new ArrayList();

    public SequencedAssemblyRecipeBuilder(ResourceLocation id) {
        this.recipe = new SequencedAssemblyRecipe(id, AllRecipeTypes.SEQUENCED_ASSEMBLY.getSerializer());
    }

    public <T extends ProcessingRecipe<?>> SequencedAssemblyRecipeBuilder addStep(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, UnaryOperator<ProcessingRecipeBuilder<T>> builder) {
        ProcessingRecipeBuilder<T> recipeBuilder = new ProcessingRecipeBuilder<>(factory, new ResourceLocation("dummy"));
        Item placeHolder = this.recipe.getTransitionalItem().getItem();
        this.recipe.getSequence().add(new SequencedRecipe((T) ((ProcessingRecipeBuilder) builder.apply(recipeBuilder.require(placeHolder).output(placeHolder))).build()));
        return this;
    }

    public SequencedAssemblyRecipeBuilder require(ItemLike ingredient) {
        return this.require(Ingredient.of(ingredient));
    }

    public SequencedAssemblyRecipeBuilder require(TagKey<Item> tag) {
        return this.require(Ingredient.of(tag));
    }

    public SequencedAssemblyRecipeBuilder require(Ingredient ingredient) {
        this.recipe.ingredient = ingredient;
        return this;
    }

    public SequencedAssemblyRecipeBuilder transitionTo(ItemLike item) {
        this.recipe.transitionalItem = new ProcessingOutput(new ItemStack(item), 1.0F);
        return this;
    }

    public SequencedAssemblyRecipeBuilder loops(int loops) {
        this.recipe.loops = loops;
        return this;
    }

    public SequencedAssemblyRecipeBuilder addOutput(ItemLike item, float weight) {
        return this.addOutput(new ItemStack(item), weight);
    }

    public SequencedAssemblyRecipeBuilder addOutput(ItemStack item, float weight) {
        this.recipe.resultPool.add(new ProcessingOutput(item, weight));
        return this;
    }

    public SequencedAssemblyRecipe build() {
        return this.recipe;
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new SequencedAssemblyRecipeBuilder.DataGenResult(this.build(), this.recipeConditions));
    }

    public static class DataGenResult implements FinishedRecipe {

        private SequencedAssemblyRecipe recipe;

        private List<ICondition> recipeConditions;

        private ResourceLocation id;

        private SequencedAssemblyRecipeSerializer serializer;

        public DataGenResult(SequencedAssemblyRecipe recipe, List<ICondition> recipeConditions) {
            this.recipeConditions = recipeConditions;
            this.recipe = recipe;
            this.id = new ResourceLocation(recipe.getId().getNamespace(), AllRecipeTypes.SEQUENCED_ASSEMBLY.getId().getPath() + "/" + recipe.getId().getPath());
            this.serializer = (SequencedAssemblyRecipeSerializer) recipe.getSerializer();
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            this.serializer.write(json, this.recipe);
            if (!this.recipeConditions.isEmpty()) {
                JsonArray conds = new JsonArray();
                this.recipeConditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
                json.add("conditions", conds);
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
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