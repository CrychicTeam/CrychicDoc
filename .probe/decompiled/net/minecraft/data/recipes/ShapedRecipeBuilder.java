package net.minecraft.data.recipes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
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

public class ShapedRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;

    private final Item result;

    private final int count;

    private final List<String> rows = Lists.newArrayList();

    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    @Nullable
    private String group;

    private boolean showNotification = true;

    public ShapedRecipeBuilder(RecipeCategory recipeCategory0, ItemLike itemLike1, int int2) {
        this.category = recipeCategory0;
        this.result = itemLike1.asItem();
        this.count = int2;
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory recipeCategory0, ItemLike itemLike1) {
        return shaped(recipeCategory0, itemLike1, 1);
    }

    public static ShapedRecipeBuilder shaped(RecipeCategory recipeCategory0, ItemLike itemLike1, int int2) {
        return new ShapedRecipeBuilder(recipeCategory0, itemLike1, int2);
    }

    public ShapedRecipeBuilder define(Character character0, TagKey<Item> tagKeyItem1) {
        return this.define(character0, Ingredient.of(tagKeyItem1));
    }

    public ShapedRecipeBuilder define(Character character0, ItemLike itemLike1) {
        return this.define(character0, Ingredient.of(itemLike1));
    }

    public ShapedRecipeBuilder define(Character character0, Ingredient ingredient1) {
        if (this.key.containsKey(character0)) {
            throw new IllegalArgumentException("Symbol '" + character0 + "' is already defined!");
        } else if (character0 == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(character0, ingredient1);
            return this;
        }
    }

    public ShapedRecipeBuilder pattern(String string0) {
        if (!this.rows.isEmpty() && string0.length() != ((String) this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(string0);
            return this;
        }
    }

    public ShapedRecipeBuilder unlockedBy(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public ShapedRecipeBuilder group(@Nullable String string0) {
        this.group = string0;
        return this;
    }

    public ShapedRecipeBuilder showNotification(boolean boolean0) {
        this.showNotification = boolean0;
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
        consumerFinishedRecipe0.accept(new ShapedRecipeBuilder.Result(resourceLocation1, this.result, this.count, this.group == null ? "" : this.group, m_245179_(this.category), this.rows, this.key, this.advancement, resourceLocation1.withPrefix("recipes/" + this.category.getFolderName() + "/"), this.showNotification));
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + resourceLocation0 + "!");
        } else {
            Set<Character> $$1 = Sets.newHashSet(this.key.keySet());
            $$1.remove(' ');
            for (String $$2 : this.rows) {
                for (int $$3 = 0; $$3 < $$2.length(); $$3++) {
                    char $$4 = $$2.charAt($$3);
                    if (!this.key.containsKey($$4) && $$4 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + resourceLocation0 + " uses undefined symbol '" + $$4 + "'");
                    }
                    $$1.remove($$4);
                }
            }
            if (!$$1.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + resourceLocation0);
            } else if (this.rows.size() == 1 && ((String) this.rows.get(0)).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + resourceLocation0 + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
            }
        }
    }

    static class Result extends CraftingRecipeBuilder.CraftingResult {

        private final ResourceLocation id;

        private final Item result;

        private final int count;

        private final String group;

        private final List<String> pattern;

        private final Map<Character, Ingredient> key;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        private final boolean showNotification;

        public Result(ResourceLocation resourceLocation0, Item item1, int int2, String string3, CraftingBookCategory craftingBookCategory4, List<String> listString5, Map<Character, Ingredient> mapCharacterIngredient6, Advancement.Builder advancementBuilder7, ResourceLocation resourceLocation8, boolean boolean9) {
            super(craftingBookCategory4);
            this.id = resourceLocation0;
            this.result = item1;
            this.count = int2;
            this.group = string3;
            this.pattern = listString5;
            this.key = mapCharacterIngredient6;
            this.advancement = advancementBuilder7;
            this.advancementId = resourceLocation8;
            this.showNotification = boolean9;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject0) {
            super.serializeRecipeData(jsonObject0);
            if (!this.group.isEmpty()) {
                jsonObject0.addProperty("group", this.group);
            }
            JsonArray $$1 = new JsonArray();
            for (String $$2 : this.pattern) {
                $$1.add($$2);
            }
            jsonObject0.add("pattern", $$1);
            JsonObject $$3 = new JsonObject();
            for (Entry<Character, Ingredient> $$4 : this.key.entrySet()) {
                $$3.add(String.valueOf($$4.getKey()), ((Ingredient) $$4.getValue()).toJson());
            }
            jsonObject0.add("key", $$3);
            JsonObject $$5 = new JsonObject();
            $$5.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                $$5.addProperty("count", this.count);
            }
            jsonObject0.add("result", $$5);
            jsonObject0.addProperty("show_notification", this.showNotification);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPED_RECIPE;
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