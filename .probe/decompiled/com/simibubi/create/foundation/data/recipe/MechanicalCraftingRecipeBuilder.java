package com.simibubi.create.foundation.data.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;

public class MechanicalCraftingRecipeBuilder {

    private final Item result;

    private final int count;

    private final List<String> pattern = Lists.newArrayList();

    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();

    private boolean acceptMirrored;

    private List<ICondition> recipeConditions;

    public MechanicalCraftingRecipeBuilder(ItemLike p_i48261_1_, int p_i48261_2_) {
        this.result = p_i48261_1_.asItem();
        this.count = p_i48261_2_;
        this.acceptMirrored = true;
        this.recipeConditions = new ArrayList();
    }

    public static MechanicalCraftingRecipeBuilder shapedRecipe(ItemLike p_200470_0_) {
        return shapedRecipe(p_200470_0_, 1);
    }

    public static MechanicalCraftingRecipeBuilder shapedRecipe(ItemLike p_200468_0_, int p_200468_1_) {
        return new MechanicalCraftingRecipeBuilder(p_200468_0_, p_200468_1_);
    }

    public MechanicalCraftingRecipeBuilder key(Character p_200469_1_, TagKey<Item> p_200469_2_) {
        return this.key(p_200469_1_, Ingredient.of(p_200469_2_));
    }

    public MechanicalCraftingRecipeBuilder key(Character p_200462_1_, ItemLike p_200462_2_) {
        return this.key(p_200462_1_, Ingredient.of(p_200462_2_));
    }

    public MechanicalCraftingRecipeBuilder key(Character p_200471_1_, Ingredient p_200471_2_) {
        if (this.key.containsKey(p_200471_1_)) {
            throw new IllegalArgumentException("Symbol '" + p_200471_1_ + "' is already defined!");
        } else if (p_200471_1_ == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(p_200471_1_, p_200471_2_);
            return this;
        }
    }

    public MechanicalCraftingRecipeBuilder patternLine(String p_200472_1_) {
        if (!this.pattern.isEmpty() && p_200472_1_.length() != ((String) this.pattern.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.pattern.add(p_200472_1_);
            return this;
        }
    }

    public MechanicalCraftingRecipeBuilder disallowMirrored() {
        this.acceptMirrored = false;
        return this;
    }

    public void build(Consumer<FinishedRecipe> p_200464_1_) {
        this.build(p_200464_1_, RegisteredObjects.getKeyOrThrow(this.result));
    }

    public void build(Consumer<FinishedRecipe> p_200466_1_, String p_200466_2_) {
        ResourceLocation resourcelocation = RegisteredObjects.getKeyOrThrow(this.result);
        if (new ResourceLocation(p_200466_2_).equals(resourcelocation)) {
            throw new IllegalStateException("Shaped Recipe " + p_200466_2_ + " should remove its 'save' argument");
        } else {
            this.build(p_200466_1_, new ResourceLocation(p_200466_2_));
        }
    }

    public void build(Consumer<FinishedRecipe> p_200467_1_, ResourceLocation p_200467_2_) {
        this.validate(p_200467_2_);
        p_200467_1_.accept(new MechanicalCraftingRecipeBuilder.Result(p_200467_2_, this.result, this.count, this.pattern, this.key, this.acceptMirrored, this.recipeConditions));
    }

    private void validate(ResourceLocation p_200463_1_) {
        if (this.pattern.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + p_200463_1_ + "!");
        } else {
            Set<Character> set = Sets.newHashSet(this.key.keySet());
            set.remove(' ');
            for (String s : this.pattern) {
                for (int i = 0; i < s.length(); i++) {
                    char c0 = s.charAt(i);
                    if (!this.key.containsKey(c0) && c0 != ' ') {
                        throw new IllegalStateException("Pattern in recipe " + p_200463_1_ + " uses undefined symbol '" + c0 + "'");
                    }
                    set.remove(c0);
                }
            }
            if (!set.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + p_200463_1_);
            }
        }
    }

    public MechanicalCraftingRecipeBuilder whenModLoaded(String modid) {
        return this.withCondition(new ModLoadedCondition(modid));
    }

    public MechanicalCraftingRecipeBuilder whenModMissing(String modid) {
        return this.withCondition(new NotCondition(new ModLoadedCondition(modid)));
    }

    public MechanicalCraftingRecipeBuilder withCondition(ICondition condition) {
        this.recipeConditions.add(condition);
        return this;
    }

    public class Result implements FinishedRecipe {

        private final ResourceLocation id;

        private final Item result;

        private final int count;

        private final List<String> pattern;

        private final Map<Character, Ingredient> key;

        private final boolean acceptMirrored;

        private List<ICondition> recipeConditions;

        public Result(ResourceLocation p_i48271_2_, Item p_i48271_3_, int p_i48271_4_, List<String> p_i48271_6_, Map<Character, Ingredient> p_i48271_7_, boolean asymmetrical, List<ICondition> recipeConditions) {
            this.id = p_i48271_2_;
            this.result = p_i48271_3_;
            this.count = p_i48271_4_;
            this.pattern = p_i48271_6_;
            this.key = p_i48271_7_;
            this.acceptMirrored = asymmetrical;
            this.recipeConditions = recipeConditions;
        }

        @Override
        public void serializeRecipeData(JsonObject p_218610_1_) {
            JsonArray jsonarray = new JsonArray();
            for (String s : this.pattern) {
                jsonarray.add(s);
            }
            p_218610_1_.add("pattern", jsonarray);
            JsonObject jsonobject = new JsonObject();
            for (Entry<Character, Ingredient> entry : this.key.entrySet()) {
                jsonobject.add(String.valueOf(entry.getKey()), ((Ingredient) entry.getValue()).toJson());
            }
            p_218610_1_.add("key", jsonobject);
            JsonObject jsonobject1 = new JsonObject();
            jsonobject1.addProperty("item", RegisteredObjects.getKeyOrThrow(this.result).toString());
            if (this.count > 1) {
                jsonobject1.addProperty("count", this.count);
            }
            p_218610_1_.add("result", jsonobject1);
            p_218610_1_.addProperty("acceptMirrored", this.acceptMirrored);
            if (!this.recipeConditions.isEmpty()) {
                JsonArray conds = new JsonArray();
                this.recipeConditions.forEach(c -> conds.add(CraftingHelper.serialize(c)));
                p_218610_1_.add("conditions", conds);
            }
        }

        @Override
        public RecipeSerializer<?> getType() {
            return AllRecipeTypes.MECHANICAL_CRAFTING.getSerializer();
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}