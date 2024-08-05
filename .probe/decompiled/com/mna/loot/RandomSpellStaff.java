package com.mna.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mna.items.sorcery.ItemStaff;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomSpellStaff extends LootItemConditionalFunction {

    private final float chanceForForceSelf;

    private final float chanceForSkipTags;

    private final boolean allowMessWithMori;

    protected RandomSpellStaff(LootItemCondition[] conditionsIn, float chanceForForceSelf, float chanceForSkipTags, boolean allowMessWithMori) {
        super(conditionsIn);
        this.chanceForForceSelf = chanceForForceSelf;
        this.chanceForSkipTags = chanceForSkipTags;
        this.allowMessWithMori = allowMessWithMori;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        return ItemStaff.buildRandomSpellStaff(this.chanceForForceSelf, this.chanceForSkipTags, this.allowMessWithMori, context.getLevel());
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomSpellStaff> {

        public RandomSpellStaff deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            float chanceForForceSelf = 0.0F;
            float chanceForSkipTags = 0.0F;
            boolean allowMessWithMori = false;
            if (object.has("chanceForForceSelf")) {
                chanceForForceSelf = object.get("chanceForForceSelf").getAsFloat();
            }
            if (object.has("chanceForSkipTags")) {
                chanceForSkipTags = object.get("chanceForSkipTags").getAsFloat();
            }
            if (object.has("allowMessWithMori")) {
                allowMessWithMori = object.get("allowMessWithMori").getAsBoolean();
            }
            return new RandomSpellStaff(conditionsIn, chanceForForceSelf, chanceForSkipTags, allowMessWithMori);
        }
    }
}