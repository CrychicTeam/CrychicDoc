package com.rekindled.embers.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class AlchemyRecipeBuilder {

    public ResourceLocation id;

    public ItemStack output;

    public ItemStack failure = ItemStack.EMPTY;

    public Ingredient tablet;

    public ArrayList<Ingredient> aspects = new ArrayList();

    public ArrayList<Ingredient> inputs = new ArrayList();

    public static AlchemyRecipeBuilder create(ItemStack itemStack) {
        AlchemyRecipeBuilder builder = new AlchemyRecipeBuilder();
        builder.output = itemStack;
        builder.id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        return builder;
    }

    public static AlchemyRecipeBuilder create(ItemLike item) {
        return create(new ItemStack(item));
    }

    public AlchemyRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public AlchemyRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public AlchemyRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public AlchemyRecipeBuilder tablet(Ingredient tablet) {
        this.tablet = tablet;
        return this;
    }

    public AlchemyRecipeBuilder tablet(ItemLike... tablet) {
        this.tablet(Ingredient.of(tablet));
        return this;
    }

    public AlchemyRecipeBuilder tablet(TagKey<Item> tag) {
        this.tablet(Ingredient.of(tag));
        return this;
    }

    public AlchemyRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public AlchemyRecipeBuilder output(Item item) {
        this.output(new ItemStack(item));
        return this;
    }

    public AlchemyRecipeBuilder failure(ItemStack failure) {
        this.failure = failure;
        return this;
    }

    public AlchemyRecipeBuilder failure(Item item) {
        this.failure(new ItemStack(item));
        return this;
    }

    public AlchemyRecipeBuilder aspects(ArrayList<Ingredient> aspects) {
        this.aspects = aspects;
        return this;
    }

    public AlchemyRecipeBuilder inputs(ArrayList<Ingredient> inputs) {
        this.inputs = inputs;
        return this;
    }

    public AlchemyRecipeBuilder aspects(Ingredient... aspects) {
        for (Ingredient aspect : aspects) {
            this.aspects.add(aspect);
        }
        return this;
    }

    public AlchemyRecipeBuilder inputs(Ingredient... inputs) {
        for (Ingredient input : inputs) {
            this.inputs.add(input);
        }
        return this;
    }

    public AlchemyRecipeBuilder aspects(ItemLike... aspects) {
        for (ItemLike aspect : aspects) {
            this.aspects.add(Ingredient.of(aspect));
        }
        return this;
    }

    public AlchemyRecipeBuilder inputs(ItemLike... inputs) {
        for (ItemLike input : inputs) {
            this.inputs.add(Ingredient.of(input));
        }
        return this;
    }

    @SafeVarargs
    public final AlchemyRecipeBuilder aspects(TagKey<Item>... aspects) {
        for (TagKey<Item> aspect : aspects) {
            this.aspects.add(Ingredient.of(aspect));
        }
        return this;
    }

    @SafeVarargs
    public final AlchemyRecipeBuilder inputs(TagKey<Item>... inputs) {
        for (TagKey<Item> input : inputs) {
            this.inputs.add(Ingredient.of(input));
        }
        return this;
    }

    public AlchemyRecipe build() {
        return new AlchemyRecipe(this.id, this.tablet, this.aspects, this.inputs, this.output, this.failure);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new AlchemyRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final AlchemyRecipe recipe;

        public Finished(AlchemyRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.recipe.output.getItem()).toString());
            int count = this.recipe.output.getCount();
            if (count > 1) {
                outputJson.addProperty("count", count);
            }
            json.add("output", outputJson);
            if (!this.recipe.failure.isEmpty()) {
                JsonObject failureJson = new JsonObject();
                failureJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.recipe.failure.getItem()).toString());
                int failureCount = this.recipe.failure.getCount();
                if (failureCount > 1) {
                    failureJson.addProperty("count", failureCount);
                }
                json.add("failure", failureJson);
            }
            json.add("tablet", this.recipe.tablet.toJson());
            JsonArray aspectJson = new JsonArray();
            for (Ingredient aspect : this.recipe.aspects) {
                aspectJson.add(aspect.toJson());
            }
            json.add("aspects", aspectJson);
            JsonArray inputJson = new JsonArray();
            for (Ingredient input : this.recipe.inputs) {
                inputJson.add(input.toJson());
            }
            json.add("inputs", inputJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.ALCHEMY_SERIALIZER.get();
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