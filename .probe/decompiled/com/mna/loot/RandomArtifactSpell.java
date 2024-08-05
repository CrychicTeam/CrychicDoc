package com.mna.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mna.items.relic.ScrollOfIcarianFlight;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomArtifactSpell extends LootItemConditionalFunction {

    protected RandomArtifactSpell(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        return ScrollOfIcarianFlight.create();
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomArtifactSpell> {

        public RandomArtifactSpell deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new RandomArtifactSpell(conditionsIn);
        }
    }
}