package net.minecraft.data.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class ShapelessRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;

    private final Item result;

    private final int count;

    private final List<Ingredient> ingredients = Lists.newArrayList();

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    @Nullable
    private String group;

    public ShapelessRecipeBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, int int2) {
        this.category = recipeCategory0;
        this.result = itemLike1.asItem();
        this.count = int2;
    }

    public static ShapelessRecipeBuilder shapeless(RecipeCategory recipeCategory0, ItemLike itemLike1) {
        return new ShapelessRecipeBuilder(recipeCategory0, itemLike1, 1);
    }

    public static ShapelessRecipeBuilder shapeless(RecipeCategory recipeCategory0, ItemLike itemLike1, int int2) {
        return new ShapelessRecipeBuilder(recipeCategory0, itemLike1, int2);
    }

    public ShapelessRecipeBuilder requires(TagKey<Item> tagKeyItem0) {
        return this.requires(Ingredient.of(tagKeyItem0));
    }

    public ShapelessRecipeBuilder requires(ItemLike itemLike0) {
        return this.requires(itemLike0, 1);
    }

    public ShapelessRecipeBuilder requires(ItemLike itemLike0, int int1) {
        for (int $$2 = 0; $$2 < int1; $$2++) {
            this.requires(Ingredient.of(itemLike0));
        }
        return this;
    }

    public ShapelessRecipeBuilder requires(Ingredient ingredient0) {
        return this.requires(ingredient0, 1);
    }

    public ShapelessRecipeBuilder requires(Ingredient ingredient0, int int1) {
        for (int $$2 = 0; $$2 < int1; $$2++) {
            this.ingredients.add(ingredient0);
        }
        return this;
    }

    public ShapelessRecipeBuilder unlockedBy(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public ShapelessRecipeBuilder group(@Nullable String string0) {
        this.group = string0;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumerFinishedRecipe0, ResourceLocation resourceLocation1) {
        this.ensureValid(resourceLocation1);
        this.advancement.parent(f_236353_).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation1)).rewards(AdvancementRewards.Builder.recipe(resourceLocation1)).requirements(RequirementsStrategy.OR);
        consumerFinishedRecipe0.accept(new ShapelessRecipeBuilder.Result(resourceLocation1, this.result, this.count, this.group == null ? "" : this.group, m_245179_(this.category), this.ingredients, this.advancement, resourceLocation1.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
        }
    }

    public static class Result extends CraftingRecipeBuilder.CraftingResult {

        private final ResourceLocation id;

        private final Item result;

        private final int count;

        private final String group;

        private final List<Ingredient> ingredients;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        public Result(ResourceLocation resourceLocation0, Item item1, int int2, String string3, CraftingBookCategory craftingBookCategory4, List<Ingredient> listIngredient5, Advancement.Builder advancementBuilder6, ResourceLocation resourceLocation7) {
            super(craftingBookCategory4);
            this.id = resourceLocation0;
            this.result = item1;
            this.count = int2;
            this.group = string3;
            this.ingredients = listIngredient5;
            this.advancement = advancementBuilder6;
            this.advancementId = resourceLocation7;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject0) {
            super.serializeRecipeData(jsonObject0);
            if (!this.group.isEmpty()) {
                jsonObject0.addProperty("group", this.group);
            }
            JsonArray $$1 = new JsonArray();
            for (Ingredient $$2 : this.ingredients) {
                $$1.add($$2.toJson());
            }
            jsonObject0.add("ingredients", $$1);
            JsonObject $$3 = new JsonObject();
            $$3.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                $$3.addProperty("count", this.count);
            }
            jsonObject0.add("result", $$3);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPELESS_RECIPE;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}