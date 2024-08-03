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

public class SmithingTransformRecipeBuilder {

    private final Ingredient template;

    private final Ingredient base;

    private final Ingredient addition;

    private final RecipeCategory category;

    private final Item result;

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    private final RecipeSerializer<?> type;

    public SmithingTransformRecipeBuilder(RecipeSerializer<?> recipeSerializer0, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, RecipeCategory recipeCategory4, Item item5) {
        this.category = recipeCategory4;
        this.type = recipeSerializer0;
        this.template = ingredient1;
        this.base = ingredient2;
        this.addition = ingredient3;
        this.result = item5;
    }

    public static SmithingTransformRecipeBuilder smithing(Ingredient ingredient0, Ingredient ingredient1, Ingredient ingredient2, RecipeCategory recipeCategory3, Item item4) {
        return new SmithingTransformRecipeBuilder(RecipeSerializer.SMITHING_TRANSFORM, ingredient0, ingredient1, ingredient2, recipeCategory3, item4);
    }

    public SmithingTransformRecipeBuilder unlocks(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumerFinishedRecipe0, String string1) {
        this.save(consumerFinishedRecipe0, new ResourceLocation(string1));
    }

    public void save(Consumer<FinishedRecipe> consumerFinishedRecipe0, ResourceLocation resourceLocation1) {
        this.ensureValid(resourceLocation1);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation1)).rewards(AdvancementRewards.Builder.recipe(resourceLocation1)).requirements(RequirementsStrategy.OR);
        consumerFinishedRecipe0.accept(new SmithingTransformRecipeBuilder.Result(resourceLocation1, this.type, this.template, this.base, this.addition, this.result, this.advancement, resourceLocation1.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
        }
    }

    public static record Result(ResourceLocation f_266011_, RecipeSerializer<?> f_265962_, Ingredient f_266002_, Ingredient f_266112_, Ingredient f_265903_, Item f_265972_, Advancement.Builder f_265855_, ResourceLocation f_266094_) implements FinishedRecipe {

        private final ResourceLocation id;

        private final RecipeSerializer<?> type;

        private final Ingredient template;

        private final Ingredient base;

        private final Ingredient addition;

        private final Item result;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        public Result(ResourceLocation f_266011_, RecipeSerializer<?> f_265962_, Ingredient f_266002_, Ingredient f_266112_, Ingredient f_265903_, Item f_265972_, Advancement.Builder f_265855_, ResourceLocation f_266094_) {
            this.id = f_266011_;
            this.type = f_265962_;
            this.template = f_266002_;
            this.base = f_266112_;
            this.addition = f_265903_;
            this.result = f_265972_;
            this.advancement = f_265855_;
            this.advancementId = f_266094_;
        }

        @Override
        public void serializeRecipeData(JsonObject p_266713_) {
            p_266713_.add("template", this.template.toJson());
            p_266713_.add("base", this.base.toJson());
            p_266713_.add("addition", this.addition.toJson());
            JsonObject $$1 = new JsonObject();
            $$1.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            p_266713_.add("result", $$1);
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