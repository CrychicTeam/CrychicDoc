package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.util.Misc;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class BoilingRecipeBuilder {

    public ResourceLocation id;

    public FluidIngredient input;

    public FluidStack output;

    public static BoilingRecipeBuilder create(FluidStack fluidStack) {
        BoilingRecipeBuilder builder = new BoilingRecipeBuilder();
        builder.output = fluidStack;
        builder.id = ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid());
        return builder;
    }

    public static BoilingRecipeBuilder create(Fluid fluid, int amount) {
        return create(new FluidStack(fluid, amount));
    }

    public BoilingRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public BoilingRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public BoilingRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public BoilingRecipeBuilder input(FluidIngredient fluid) {
        this.input = fluid;
        return this;
    }

    public BoilingRecipeBuilder input(Fluid fluid, int amount) {
        this.input(FluidIngredient.of(fluid, amount));
        return this;
    }

    public BoilingRecipeBuilder input(FluidStack stack) {
        this.input(FluidIngredient.of(stack));
        return this;
    }

    public BoilingRecipeBuilder input(TagKey<Fluid> fluid, int amount) {
        this.input(FluidIngredient.of(fluid, amount));
        return this;
    }

    public BoilingRecipeBuilder input(FluidIngredient... ingredients) {
        this.input(FluidIngredient.of(ingredients));
        return this;
    }

    public BoilingRecipeBuilder output(FluidStack output) {
        this.output = output;
        return this;
    }

    public BoilingRecipeBuilder output(Fluid fluid, int amount) {
        this.output(new FluidStack(fluid, amount));
        return this;
    }

    public BoilingRecipe build() {
        return new BoilingRecipe(this.id, this.input, this.output);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new BoilingRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final BoilingRecipe recipe;

        public Finished(BoilingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("input", this.recipe.input.serialize());
            json.add("output", Misc.serializeFluidStack(this.recipe.output));
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.BOILING_SERIALIZER.get();
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