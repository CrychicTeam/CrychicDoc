package com.mna.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.items.ItemInit;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomConstructPart extends LootItemConditionalFunction {

    protected RandomConstructPart(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        List<ItemConstructPart> parts = (List<ItemConstructPart>) ItemInit.ITEMS.getEntries().stream().filter(i -> i.isPresent() && i.get() instanceof ItemConstructPart && ((ItemConstructPart) i.get()).canBeInLoot(context)).map(i -> (ItemConstructPart) i.get()).collect(Collectors.toList());
        if (parts.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemConstructPart selected = (ItemConstructPart) parts.get((int) (Math.random() * (double) parts.size()));
            return new ItemStack(selected);
        }
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomConstructPart> {

        public RandomConstructPart deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
            return new RandomConstructPart(conditionsIn);
        }
    }
}