package com.mna.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweavingPattern;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomManaweaveRecipe extends LootItemConditionalFunction {

    protected RandomManaweaveRecipe(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        ItemStack mwRecipe = new ItemStack(ItemInit.RECIPE_SCRAP_MANAWEAVING_PATTERN.get());
        List<ManaweavingPattern> patterns = context.getLevel().getRecipeManager().getAllRecipesFor(RecipeInit.MANAWEAVING_PATTERN_TYPE.get());
        if (patterns.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemManaweavingPattern.setRecipe(mwRecipe, (ManaweavingPattern) patterns.get((int) (Math.random() * (double) patterns.size())));
            return mwRecipe;
        }
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomManaweaveRecipe> {

        public RandomManaweaveRecipe deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new RandomManaweaveRecipe(conditionsIn);
        }
    }
}