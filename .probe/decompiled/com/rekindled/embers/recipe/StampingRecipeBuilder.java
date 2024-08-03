package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.rekindled.embers.RegistryManager;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class StampingRecipeBuilder {

    public ResourceLocation id;

    public Ingredient stamp;

    public Ingredient input = Ingredient.EMPTY;

    public FluidIngredient fluid = FluidIngredient.EMPTY;

    public Either<ItemStack, StampingRecipe.TagAmount> output;

    public static StampingRecipeBuilder create(ItemStack itemStack) {
        StampingRecipeBuilder builder = new StampingRecipeBuilder();
        builder.output = Either.left(itemStack);
        builder.id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        return builder;
    }

    public static StampingRecipeBuilder create(Item item) {
        return create(new ItemStack(item));
    }

    public static StampingRecipeBuilder create(TagKey<Item> tag, int amount) {
        StampingRecipeBuilder builder = new StampingRecipeBuilder();
        builder.output = Either.right(new StampingRecipe.TagAmount(tag, amount));
        builder.id = tag.location();
        return builder;
    }

    public static StampingRecipeBuilder create(TagKey<Item> tag) {
        return create(tag, 1);
    }

    public StampingRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public StampingRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public StampingRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public StampingRecipeBuilder stamp(Ingredient stamp) {
        this.stamp = stamp;
        return this;
    }

    public StampingRecipeBuilder stamp(ItemLike... stamp) {
        this.stamp(Ingredient.of(stamp));
        return this;
    }

    public StampingRecipeBuilder stamp(TagKey<Item> tag) {
        this.stamp(Ingredient.of(tag));
        return this;
    }

    public StampingRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public StampingRecipeBuilder input(ItemLike... input) {
        this.input(Ingredient.of(input));
        return this;
    }

    public StampingRecipeBuilder input(TagKey<Item> tag) {
        this.input(Ingredient.of(tag));
        return this;
    }

    public StampingRecipeBuilder fluid(FluidIngredient fluid) {
        this.fluid = fluid;
        return this;
    }

    public StampingRecipeBuilder fluid(Fluid fluid, int amount) {
        this.fluid(FluidIngredient.of(fluid, amount));
        return this;
    }

    public StampingRecipeBuilder fluid(FluidStack stack) {
        this.fluid(FluidIngredient.of(stack));
        return this;
    }

    public StampingRecipeBuilder fluid(TagKey<Fluid> fluid, int amount) {
        this.fluid(FluidIngredient.of(fluid, amount));
        return this;
    }

    public StampingRecipeBuilder fluid(FluidIngredient... ingredients) {
        this.fluid(FluidIngredient.of(ingredients));
        return this;
    }

    public StampingRecipe build() {
        return new StampingRecipe(this.id, this.stamp, this.input, this.fluid, this.output);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new StampingRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final StampingRecipe recipe;

        public Finished(StampingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject outputJson = new JsonObject();
            if (this.recipe.output.right().isPresent()) {
                outputJson.addProperty("tag", ((StampingRecipe.TagAmount) this.recipe.output.right().get()).tag.location().toString());
                int count = ((StampingRecipe.TagAmount) this.recipe.output.right().get()).amount;
                if (count > 1) {
                    outputJson.addProperty("count", count);
                }
            } else {
                outputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(((ItemStack) this.recipe.output.left().get()).getItem()).toString());
                int count = ((ItemStack) this.recipe.output.left().get()).getCount();
                if (count > 1) {
                    outputJson.addProperty("count", count);
                }
            }
            json.add("output", outputJson);
            json.add("stamp", this.recipe.stamp.toJson());
            if (!this.recipe.input.isEmpty()) {
                json.add("input", this.recipe.input.toJson());
            }
            if (this.recipe.fluid != FluidIngredient.EMPTY) {
                json.add("fluid", this.recipe.fluid.serialize());
            }
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.STAMPING_SERIALIZER.get();
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