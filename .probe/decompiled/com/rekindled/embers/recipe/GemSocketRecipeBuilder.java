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

public class GemSocketRecipeBuilder {

    public ResourceLocation id;

    public Ingredient ingredient;

    public static GemSocketRecipeBuilder create(Ingredient ingredient) {
        GemSocketRecipeBuilder builder = new GemSocketRecipeBuilder();
        builder.ingredient = ingredient;
        return builder;
    }

    public static GemSocketRecipeBuilder create(TagKey<Item> tag) {
        GemSocketRecipeBuilder builder = create(Ingredient.of(tag));
        builder.id = tag.location();
        return builder;
    }

    public static GemSocketRecipeBuilder create(ItemStack itemStack) {
        GemSocketRecipeBuilder builder = create(Ingredient.of(itemStack));
        builder.id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        return builder;
    }

    public static GemSocketRecipeBuilder create(Item item) {
        return create(new ItemStack(item));
    }

    public GemSocketRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public GemSocketRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public GemSocketRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public GemSocketRecipe build() {
        return new GemSocketRecipe(this.id, this.ingredient);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new GemSocketRecipeBuilder.Finished(this.build()));
    }

    public static class Finished implements FinishedRecipe {

        public final GemSocketRecipe recipe;

        public Finished(GemSocketRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("ingredient", this.recipe.ingredient.toJson());
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegistryManager.GEM_SOCKET_SERIALIZER.get();
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