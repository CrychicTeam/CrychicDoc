package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class EmberActivationRecipeBuilder {

    public ResourceLocation id;

    public Ingredient ingredient;

    public int ember;

    public static EmberActivationRecipeBuilder create(Ingredient ingredient) {
        EmberActivationRecipeBuilder builder = new EmberActivationRecipeBuilder();
        builder.ingredient = ingredient;
        return builder;
    }

    public static EmberActivationRecipeBuilder create(TagKey<Item> tag) {
        EmberActivationRecipeBuilder builder = create(Ingredient.of(tag));
        builder.id = tag.location();
        return builder;
    }

    public static EmberActivationRecipeBuilder create(ItemStack itemStack) {
        EmberActivationRecipeBuilder builder = create(Ingredient.of(itemStack));
        builder.id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        return builder;
    }

    public static EmberActivationRecipeBuilder create(Item item) {
        return create(new ItemStack(item));
    }

    public EmberActivationRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public EmberActivationRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public EmberActivationRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public EmberActivationRecipeBuilder ember(int ember) {
        this.ember = ember;
        return this;
    }

    public EmberActivationRecipe build() {
        return new EmberActivationRecipe(this.id, this.ingredient, this.ember);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new EmberActivationRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final EmberActivationRecipe recipe;

        public Finished(EmberActivationRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("input", this.recipe.ingredient.toJson());
            json.addProperty("ember", this.recipe.ember);
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.EMBER_ACTIVATION_SERIALIZER.get();
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