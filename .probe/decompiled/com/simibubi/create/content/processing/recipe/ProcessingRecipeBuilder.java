package com.simibubi.create.content.processing.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.SimpleDatagenIngredient;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Pair;
import com.tterrag.registrate.util.DataIngredient;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.fluids.FluidStack;

public class ProcessingRecipeBuilder<T extends ProcessingRecipe<?>> {

    protected ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory;

    protected ProcessingRecipeBuilder.ProcessingRecipeParams params;

    protected List<ICondition> recipeConditions;

    public ProcessingRecipeBuilder(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory, ResourceLocation recipeId) {
        this.params = new ProcessingRecipeBuilder.ProcessingRecipeParams(recipeId);
        this.recipeConditions = new ArrayList();
        this.factory = factory;
    }

    public ProcessingRecipeBuilder<T> withItemIngredients(Ingredient... ingredients) {
        return this.withItemIngredients(NonNullList.of(Ingredient.EMPTY, ingredients));
    }

    public ProcessingRecipeBuilder<T> withItemIngredients(NonNullList<Ingredient> ingredients) {
        this.params.ingredients = ingredients;
        return this;
    }

    public ProcessingRecipeBuilder<T> withSingleItemOutput(ItemStack output) {
        return this.withItemOutputs(new ProcessingOutput(output, 1.0F));
    }

    public ProcessingRecipeBuilder<T> withItemOutputs(ProcessingOutput... outputs) {
        return this.withItemOutputs(NonNullList.of(ProcessingOutput.EMPTY, outputs));
    }

    public ProcessingRecipeBuilder<T> withItemOutputs(NonNullList<ProcessingOutput> outputs) {
        this.params.results = outputs;
        return this;
    }

    public ProcessingRecipeBuilder<T> withFluidIngredients(FluidIngredient... ingredients) {
        return this.withFluidIngredients(NonNullList.of(FluidIngredient.EMPTY, ingredients));
    }

    public ProcessingRecipeBuilder<T> withFluidIngredients(NonNullList<FluidIngredient> ingredients) {
        this.params.fluidIngredients = ingredients;
        return this;
    }

    public ProcessingRecipeBuilder<T> withFluidOutputs(FluidStack... outputs) {
        return this.withFluidOutputs(NonNullList.of(FluidStack.EMPTY, outputs));
    }

    public ProcessingRecipeBuilder<T> withFluidOutputs(NonNullList<FluidStack> outputs) {
        this.params.fluidResults = outputs;
        return this;
    }

    public ProcessingRecipeBuilder<T> duration(int ticks) {
        this.params.processingDuration = ticks;
        return this;
    }

    public ProcessingRecipeBuilder<T> averageProcessingDuration() {
        return this.duration(100);
    }

    public ProcessingRecipeBuilder<T> requiresHeat(HeatCondition condition) {
        this.params.requiredHeat = condition;
        return this;
    }

    public T build() {
        return this.factory.create(this.params);
    }

    public void build(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new ProcessingRecipeBuilder.DataGenResult<>(this.build(), this.recipeConditions));
    }

    public ProcessingRecipeBuilder<T> require(TagKey<Item> tag) {
        return this.require(Ingredient.of(tag));
    }

    public ProcessingRecipeBuilder<T> require(ItemLike item) {
        return this.require(Ingredient.of(item));
    }

    public ProcessingRecipeBuilder<T> require(Ingredient ingredient) {
        this.params.ingredients.add(ingredient);
        return this;
    }

    public ProcessingRecipeBuilder<T> require(Mods mod, String id) {
        this.params.ingredients.add(new SimpleDatagenIngredient(mod, id));
        return this;
    }

    public ProcessingRecipeBuilder<T> require(ResourceLocation ingredient) {
        this.params.ingredients.add(DataIngredient.ingredient(null, ingredient, new ItemPredicate[0]));
        return this;
    }

    public ProcessingRecipeBuilder<T> require(Fluid fluid, int amount) {
        return this.require(FluidIngredient.fromFluid(fluid, amount));
    }

    public ProcessingRecipeBuilder<T> require(TagKey<Fluid> fluidTag, int amount) {
        return this.require(FluidIngredient.fromTag(fluidTag, amount));
    }

    public ProcessingRecipeBuilder<T> require(FluidIngredient ingredient) {
        this.params.fluidIngredients.add(ingredient);
        return this;
    }

    public ProcessingRecipeBuilder<T> output(ItemLike item) {
        return this.output(item, 1);
    }

    public ProcessingRecipeBuilder<T> output(float chance, ItemLike item) {
        return this.output(chance, item, 1);
    }

    public ProcessingRecipeBuilder<T> output(ItemLike item, int amount) {
        return this.output(1.0F, item, amount);
    }

    public ProcessingRecipeBuilder<T> output(float chance, ItemLike item, int amount) {
        return this.output(chance, new ItemStack(item, amount));
    }

    public ProcessingRecipeBuilder<T> output(ItemStack output) {
        return this.output(1.0F, output);
    }

    public ProcessingRecipeBuilder<T> output(float chance, ItemStack output) {
        return this.output(new ProcessingOutput(output, chance));
    }

    public ProcessingRecipeBuilder<T> output(float chance, Mods mod, String id, int amount) {
        return this.output(new ProcessingOutput(Pair.of(mod.asResource(id), amount), chance));
    }

    public ProcessingRecipeBuilder<T> output(float chance, ResourceLocation registryName, int amount) {
        return this.output(new ProcessingOutput(Pair.of(registryName, amount), chance));
    }

    public ProcessingRecipeBuilder<T> output(ProcessingOutput output) {
        this.params.results.add(output);
        return this;
    }

    public ProcessingRecipeBuilder<T> output(Fluid fluid, int amount) {
        fluid = FluidHelper.convertToStill(fluid);
        return this.output(new FluidStack(fluid, amount));
    }

    public ProcessingRecipeBuilder<T> output(FluidStack fluidStack) {
        this.params.fluidResults.add(fluidStack);
        return this;
    }

    public ProcessingRecipeBuilder<T> toolNotConsumed() {
        this.params.keepHeldItem = true;
        return this;
    }

    public ProcessingRecipeBuilder<T> whenModLoaded(String modid) {
        return this.withCondition(new ModLoadedCondition(modid));
    }

    public ProcessingRecipeBuilder<T> whenModMissing(String modid) {
        return this.withCondition(new NotCondition(new ModLoadedCondition(modid)));
    }

    public ProcessingRecipeBuilder<T> withCondition(ICondition condition) {
        this.recipeConditions.add(condition);
        return this;
    }

    public static class DataGenResult<S extends ProcessingRecipe<?>> implements FinishedRecipe {

        private List<ICondition> recipeConditions;

        private ProcessingRecipeSerializer<S> serializer;

        private ResourceLocation id;

        private S recipe;

        public DataGenResult(S recipe, List<ICondition> recipeConditions) {
            this.recipe = recipe;
            this.recipeConditions = recipeConditions;
            IRecipeTypeInfo recipeType = this.recipe.getTypeInfo();
            ResourceLocation typeId = recipeType.getId();
            if (!(recipeType.getSerializer() instanceof ProcessingRecipeSerializer)) {
                throw new IllegalStateException("Cannot datagen ProcessingRecipe of type: " + typeId);
            } else {
                this.id = new ResourceLocation(recipe.getId().getNamespace(), typeId.getPath() + "/" + recipe.getId().getPath());
                this.serializer = (ProcessingRecipeSerializer<S>) recipe.getSerializer();
            }
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

    @FunctionalInterface
    public interface ProcessingRecipeFactory<T extends ProcessingRecipe<?>> {

        T create(ProcessingRecipeBuilder.ProcessingRecipeParams var1);
    }

    public static class ProcessingRecipeParams {

        protected ResourceLocation id;

        protected NonNullList<Ingredient> ingredients;

        protected NonNullList<ProcessingOutput> results;

        protected NonNullList<FluidIngredient> fluidIngredients;

        protected NonNullList<FluidStack> fluidResults;

        protected int processingDuration;

        protected HeatCondition requiredHeat;

        public boolean keepHeldItem;

        protected ProcessingRecipeParams(ResourceLocation id) {
            this.id = id;
            this.ingredients = NonNullList.create();
            this.results = NonNullList.create();
            this.fluidIngredients = NonNullList.create();
            this.fluidResults = NonNullList.create();
            this.processingDuration = 0;
            this.requiredHeat = HeatCondition.NONE;
            this.keepHeldItem = false;
        }
    }
}