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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class SimpleCookingRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;

    private final CookingBookCategory bookCategory;

    private final Item result;

    private final Ingredient ingredient;

    private final float experience;

    private final int cookingTime;

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    @Nullable
    private String group;

    private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;

    private SimpleCookingRecipeBuilder(RecipeCategory recipeCategory0, CookingBookCategory cookingBookCategory1, ItemLike itemLike2, Ingredient ingredient3, float float4, int int5, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe6) {
        this.category = recipeCategory0;
        this.bookCategory = cookingBookCategory1;
        this.result = itemLike2.asItem();
        this.ingredient = ingredient3;
        this.experience = float4;
        this.cookingTime = int5;
        this.serializer = recipeSerializerExtendsAbstractCookingRecipe6;
    }

    public static SimpleCookingRecipeBuilder generic(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2, float float3, int int4, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe5) {
        return new SimpleCookingRecipeBuilder(recipeCategory1, determineRecipeCategory(recipeSerializerExtendsAbstractCookingRecipe5, itemLike2), itemLike2, ingredient0, float3, int4, recipeSerializerExtendsAbstractCookingRecipe5);
    }

    public static SimpleCookingRecipeBuilder campfireCooking(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2, float float3, int int4) {
        return new SimpleCookingRecipeBuilder(recipeCategory1, CookingBookCategory.FOOD, itemLike2, ingredient0, float3, int4, RecipeSerializer.CAMPFIRE_COOKING_RECIPE);
    }

    public static SimpleCookingRecipeBuilder blasting(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2, float float3, int int4) {
        return new SimpleCookingRecipeBuilder(recipeCategory1, determineBlastingRecipeCategory(itemLike2), itemLike2, ingredient0, float3, int4, RecipeSerializer.BLASTING_RECIPE);
    }

    public static SimpleCookingRecipeBuilder smelting(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2, float float3, int int4) {
        return new SimpleCookingRecipeBuilder(recipeCategory1, determineSmeltingRecipeCategory(itemLike2), itemLike2, ingredient0, float3, int4, RecipeSerializer.SMELTING_RECIPE);
    }

    public static SimpleCookingRecipeBuilder smoking(Ingredient ingredient0, RecipeCategory recipeCategory1, ItemLike itemLike2, float float3, int int4) {
        return new SimpleCookingRecipeBuilder(recipeCategory1, CookingBookCategory.FOOD, itemLike2, ingredient0, float3, int4, RecipeSerializer.SMOKING_RECIPE);
    }

    public SimpleCookingRecipeBuilder unlockedBy(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public SimpleCookingRecipeBuilder group(@Nullable String string0) {
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
        consumerFinishedRecipe0.accept(new SimpleCookingRecipeBuilder.Result(resourceLocation1, this.group == null ? "" : this.group, this.bookCategory, this.ingredient, this.result, this.experience, this.cookingTime, this.advancement, resourceLocation1.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.serializer));
    }

    private static CookingBookCategory determineSmeltingRecipeCategory(ItemLike itemLike0) {
        if (itemLike0.asItem().isEdible()) {
            return CookingBookCategory.FOOD;
        } else {
            return itemLike0.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
        }
    }

    private static CookingBookCategory determineBlastingRecipeCategory(ItemLike itemLike0) {
        return itemLike0.asItem() instanceof BlockItem ? CookingBookCategory.BLOCKS : CookingBookCategory.MISC;
    }

    private static CookingBookCategory determineRecipeCategory(RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe0, ItemLike itemLike1) {
        if (recipeSerializerExtendsAbstractCookingRecipe0 == RecipeSerializer.SMELTING_RECIPE) {
            return determineSmeltingRecipeCategory(itemLike1);
        } else if (recipeSerializerExtendsAbstractCookingRecipe0 == RecipeSerializer.BLASTING_RECIPE) {
            return determineBlastingRecipeCategory(itemLike1);
        } else if (recipeSerializerExtendsAbstractCookingRecipe0 != RecipeSerializer.SMOKING_RECIPE && recipeSerializerExtendsAbstractCookingRecipe0 != RecipeSerializer.CAMPFIRE_COOKING_RECIPE) {
            throw new IllegalStateException("Unknown cooking recipe type");
        } else {
            return CookingBookCategory.FOOD;
        }
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
        }
    }

    static class Result implements FinishedRecipe {

        private final ResourceLocation id;

        private final String group;

        private final CookingBookCategory category;

        private final Ingredient ingredient;

        private final Item result;

        private final float experience;

        private final int cookingTime;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result(ResourceLocation resourceLocation0, String string1, CookingBookCategory cookingBookCategory2, Ingredient ingredient3, Item item4, float float5, int int6, Advancement.Builder advancementBuilder7, ResourceLocation resourceLocation8, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializerExtendsAbstractCookingRecipe9) {
            this.id = resourceLocation0;
            this.group = string1;
            this.category = cookingBookCategory2;
            this.ingredient = ingredient3;
            this.result = item4;
            this.experience = float5;
            this.cookingTime = int6;
            this.advancement = advancementBuilder7;
            this.advancementId = resourceLocation8;
            this.serializer = recipeSerializerExtendsAbstractCookingRecipe9;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject0) {
            if (!this.group.isEmpty()) {
                jsonObject0.addProperty("group", this.group);
            }
            jsonObject0.addProperty("category", this.category.getSerializedName());
            jsonObject0.add("ingredient", this.ingredient.toJson());
            jsonObject0.addProperty("result", BuiltInRegistries.ITEM.getKey(this.result).toString());
            jsonObject0.addProperty("experience", this.experience);
            jsonObject0.addProperty("cookingtime", this.cookingTime);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
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