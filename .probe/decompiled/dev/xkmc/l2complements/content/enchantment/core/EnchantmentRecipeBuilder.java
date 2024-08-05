package dev.xkmc.l2complements.content.enchantment.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.xkmc.l2complements.compat.ars.ArsRecipeCompat;
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
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentRecipeBuilder implements RecipeBuilder {

    public final Enchantment enchantment;

    public final int level;

    public final List<String> rows = Lists.newArrayList();

    public final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    @Nullable
    private String group;

    public EnchantmentRecipeBuilder(Enchantment enchantment0, int int1) {
        this.enchantment = enchantment0;
        this.level = int1;
    }

    public EnchantmentRecipeBuilder define(Character character0, TagKey<Item> tagKeyItem1) {
        return this.define(character0, Ingredient.of(tagKeyItem1));
    }

    public EnchantmentRecipeBuilder define(Character character0, ItemLike itemLike1) {
        return this.define(character0, Ingredient.of(itemLike1));
    }

    public EnchantmentRecipeBuilder define(Character character0, Ingredient ingredient1) {
        if (this.key.containsKey(character0)) {
            throw new IllegalArgumentException("Symbol '" + character0 + "' is already defined!");
        } else if (character0 == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(character0, ingredient1);
            return this;
        }
    }

    public EnchantmentRecipeBuilder pattern(String string0) {
        if (!this.rows.isEmpty() && string0.length() != ((String) this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(string0);
            return this;
        }
    }

    public EnchantmentRecipeBuilder unlockedBy(String string0, CriterionTriggerInstance criterionTriggerInstance1) {
        this.advancement.addCriterion(string0, criterionTriggerInstance1);
        return this;
    }

    public EnchantmentRecipeBuilder group(@Nullable String string0) {
        this.group = string0;
        return this;
    }

    @Override
    public Item getResult() {
        return Items.ENCHANTED_BOOK;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumerFinishedRecipe0) {
        this.save(consumerFinishedRecipe0, new ResourceLocation("l2complements", ForgeRegistries.ENCHANTMENTS.getKey(this.enchantment).getPath()));
    }

    @Override
    public void save(Consumer<FinishedRecipe> pvd, ResourceLocation loc) {
        pvd = ArsRecipeCompat.saveCompat(this, pvd, loc);
        this.ensureValid(loc);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(loc)).rewards(AdvancementRewards.Builder.recipe(loc)).requirements(RequirementsStrategy.OR);
        pvd.accept(new EnchantmentRecipeBuilder.Result(loc, this.enchantment, this.level, this.group == null ? "" : this.group, this.rows, this.key, this.advancement, new ResourceLocation(loc.getNamespace(), "recipes/enchantments/" + loc.getPath())));
    }

    private void ensureValid(ResourceLocation resourceLocation0) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + resourceLocation0 + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');
            for (String s : this.rows) {
                for (int i = 0; i < s.length(); i++) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + resourceLocation0 + " uses undefined symbol '" + c0 + "'");
                    }
                    set.remove(c0);
                }
            }
            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + resourceLocation0);
            } else if (this.rows.size() == 1 && ((String) this.rows.get(0)).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + resourceLocation0 + " only takes in a single item - should it be a shapeless recipe instead?");
            } else if (this.advancement.getCriteria().isEmpty()) {
                throw new IllegalStateException("No way of obtaining recipe " + resourceLocation0);
            }
        }
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;

        private final Enchantment ench;

        private final int lvl;

        private final String group;

        private final List<String> pattern;

        private final Map<Character, Ingredient> key;

        private final Advancement.Builder advancement;

        private final ResourceLocation advancementId;

        public Result(ResourceLocation resourceLocation0, Enchantment enchantment1, int int2, String string3, List<String> listString4, Map<Character, Ingredient> mapCharacterIngredient5, Advancement.Builder advancementBuilder6, ResourceLocation resourceLocation7) {
            this.id = resourceLocation0;
            this.ench = enchantment1;
            this.lvl = int2;
            this.group = string3;
            this.pattern = listString4;
            this.key = mapCharacterIngredient5;
            this.advancement = advancementBuilder6;
            this.advancementId = resourceLocation7;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject0) {
            if (!this.group.isEmpty()) {
                jsonObject0.addProperty("group", this.group);
            }
            JsonArray jsonarray = new JsonArray();
            for (String s : this.pattern) {
                jsonarray.add(s);
            }
            jsonObject0.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();
            for (Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), ((Ingredient) entry.getValue()).toJson());
            }
            jsonObject0.add("key", jsonobject);
            JsonObject result = new JsonObject();
            result.addProperty("item", ForgeRegistries.ITEMS.getKey(Items.ENCHANTED_BOOK).toString());
            JsonObject tag = new JsonObject();
            JsonArray list = new JsonArray();
            JsonObject entry = new JsonObject();
            entry.addProperty("id", ForgeRegistries.ENCHANTMENTS.getKey(this.ench).toString());
            entry.addProperty("lvl", this.lvl);
            list.add(entry);
            tag.add("StoredEnchantments", list);
            result.add("nbt", tag);
            jsonObject0.add("result", result);
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