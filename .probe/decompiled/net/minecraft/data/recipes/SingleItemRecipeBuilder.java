package net.minecraft.data.recipes;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class SingleItemRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;

    private final Item result;

    private final Ingredient ingredient;

    private final int count;

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    @Nullable
    private String group;

    private final RecipeSerializer<?> type;

    public SingleItemRecipeBuilder(RecipeCategory recipeCategory0, RecipeSerializer<?> recipeSerializer1, Ingredient ingredient2, ItemLike itemLike3, int int4) {
        this.category = recipeCategory0;
        this.type = recipeSerializer1;
        this.result = itemLike3.asItem();
        this.ingredient = ingredient2;
        this.count = int4;
    }

    public static SingleItemRecipeBuilder stonecutting(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2) {
        return new SingleItemRecipeBuilder(recipeCategory1, RecipeSerializer.STONECUTTER, ingredient0, itemLike2, 1);
    }

    public static SingleItemRecipeBuilder stonecutting(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2, int int3) {
        return new SingleItemRecipeBuilder(recipeCategory1, RecipeSerializer.STONECUTTER, ingredient0, itemLike2, int3);
    }

    public SingleItemRecipeBuilder unlockedBy(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public SingleItemRecipeBuilder group(@Nullable String string0) {
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
        consumerFinishedRecipe0.accept(new SingleItemRecipeBuilder.Result(resourceLocation1, this.type, this.group == null ? "" : this.group, this.ingredient, this.result, this.count, this.advancement, resourceLocation1.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
        }
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;

        private final String group;

        private final Ingredient ingredient;

        private final Item result;

        private final int count;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        private final RecipeSerializer<?> type;

        public Result(ResourceLocation resourceLocation0, RecipeSerializer<?> recipeSerializer1, String string2, Ingredient ingredient3, Item item4, int int5, Advancement.Builder advancementBuilder6, ResourceLocation resourceLocation7) {
            this.id = resourceLocation0;
            this.type = recipeSerializer1;
            this.group = string2;
            this.ingredient = ingredient3;
            this.result = item4;
            this.count = int5;
            this.advancement = advancementBuilder6;
            this.advancementId = resourceLocation7;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject0) {
            if (!this.group.isEmpty()) {
                jsonObject0.addProperty("group", this.group);
            }
            jsonObject0.add("ingredient", this.ingredient.toJson());
            jsonObject0.addProperty("result", BuiltInRegistries.ITEM.getKey(this.result).toString());
            jsonObject0.addProperty("count", this.count);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.type;
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