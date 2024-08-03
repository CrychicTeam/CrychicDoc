package com.mna.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mna.items.ItemInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomThaumaturgicLink extends LootItemConditionalFunction {

    protected RandomThaumaturgicLink(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        return ItemInit.THAUMATURGIC_LINK.get().getRandomLink(context.getLevel());
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomThaumaturgicLink> {

        public RandomThaumaturgicLink deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new RandomThaumaturgicLink(conditionsIn);
        }
    }
}