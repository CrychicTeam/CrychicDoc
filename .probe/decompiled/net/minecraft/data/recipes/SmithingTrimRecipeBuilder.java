package net.minecraft.data.recipes;

import com.google.gson.JsonObject;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SmithingTrimRecipeBuilder {

    private final RecipeCategory category;

    private final Ingredient template;

    private final Ingredient base;

    private final Ingredient addition;

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    private final RecipeSerializer<?> type;

    public SmithingTrimRecipeBuilder(RecipeSerializer<?> recipeSerializer0, RecipeCategory recipeCategory1, Ingredient ingredient2, Ingredient ingredient3, Ingredient ingredient4) {
        this.category = recipeCategory1;
        this.type = recipeSerializer0;
        this.template = ingredient2;
        this.base = ingredient3;
        this.addition = ingredient4;
    }

    public static SmithingTrimRecipeBuilder smithingTrim(Ingredient ingredient0, Ingredient ingredient1, Ingredient ingredient2, RecipeCategory recipeCategory3) {
        return new SmithingTrimRecipeBuilder(RecipeSerializer.SMITHING_TRIM, recipeCategory3, ingredient0, ingredient1, ingredient2);
    }

    public SmithingTrimRecipeBuilder unlocks(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumerFinishedRecipe0, ResourceLocation resourceLocation1) {
        this.ensureValid(resourceLocation1);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation1)).rewards(AdvancementRewards.Builder.recipe(resourceLocation1)).requirements(RequirementsStrategy.OR);
        consumerFinishedRecipe0.accept(new SmithingTrimRecipeBuilder.Result(resourceLocation1, this.type, this.template, this.base, this.addition, this.advancement, resourceLocation1.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
        }
    }

    public static record Result(ResourceLocation f_265963_, RecipeSerializer<?> f_266055_, Ingredient f_265865_, Ingredient f_266032_, Ingredient f_266016_, Advancement.Builder f_265961_, ResourceLocation f_265882_) implements FinishedRecipe {

        private final ResourceLocation id;

        private final RecipeSerializer<?> type;

        private final Ingredient template;

        private final Ingredient base;

        private final Ingredient addition;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        public Result(ResourceLocation f_265963_, RecipeSerializer<?> f_266055_, Ingredient f_265865_, Ingredient f_266032_, Ingredient f_266016_, Advancement.Builder f_265961_, ResourceLocation f_265882_) {
            this.id = f_265963_;
            this.type = f_266055_;
            this.template = f_265865_;
            this.base = f_266032_;
            this.addition = f_266016_;
            this.advancement = f_265961_;
            this.advancementId = f_265882_;
        }

        @Override
        public void serializeRecipeData(JsonObject p_267008_) {
            p_267008_.add("template", this.template.toJson());
            p_267008_.add("base", this.base.toJson());
            p_267008_.add("addition", this.addition.toJson());
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