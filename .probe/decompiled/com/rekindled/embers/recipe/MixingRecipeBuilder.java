package com.rekindled.embers.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.util.Misc;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class MixingRecipeBuilder {

    public ResourceLocation id;

    public ArrayList<FluidIngredient> inputs = new ArrayList();

    public FluidStack output;

    public static MixingRecipeBuilder create(FluidStack fluidStack) {
        MixingRecipeBuilder builder = new MixingRecipeBuilder();
        builder.output = fluidStack;
        builder.id = ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid());
        return builder;
    }

    public static MixingRecipeBuilder create(Fluid fluid, int amount) {
        return create(new FluidStack(fluid, amount));
    }

    public MixingRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public MixingRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public MixingRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public MixingRecipeBuilder input(FluidIngredient fluid) {
        this.inputs.add(fluid);
        return this;
    }

    public MixingRecipeBuilder input(Fluid fluid, int amount) {
        this.input(FluidIngredient.of(fluid, amount));
        return this;
    }

    public MixingRecipeBuilder input(FluidStack stack) {
        this.input(FluidIngredient.of(stack));
        return this;
    }

    public MixingRecipeBuilder input(TagKey<Fluid> fluid, int amount) {
        this.input(FluidIngredient.of(fluid, amount));
        return this;
    }

    public MixingRecipeBuilder input(FluidIngredient... ingredients) {
        this.input(FluidIngredient.of(ingredients));
        return this;
    }

    public MixingRecipeBuilder output(FluidStack output) {
        this.output = output;
        return this;
    }

    public MixingRecipeBuilder output(Fluid fluid, int amount) {
        this.output(new FluidStack(fluid, amount));
        return this;
    }

    public MixingRecipe build() {
        return new MixingRecipe(this.id, this.inputs, this.output);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new MixingRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final MixingRecipe recipe;

        public Finished(MixingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray inputJson = new JsonArray();
            for (FluidIngredient input : this.recipe.inputs) {
                inputJson.add(input.serialize());
            }
            json.add("inputs", inputJson);
            json.add("output", Misc.serializeFluidStack(this.recipe.output));
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.MIXING_SERIALIZER.get();
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