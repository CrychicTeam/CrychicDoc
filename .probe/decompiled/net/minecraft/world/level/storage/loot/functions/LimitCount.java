package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LimitCount extends LootItemConditionalFunction {

    final IntRange limiter;

    LimitCount(LootItemCondition[] lootItemCondition0, IntRange intRange1) {
        super(lootItemCondition0);
        this.limiter = intRange1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.LIMIT_COUNT;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.limiter.getReferencedContextParams();
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        int $$2 = this.limiter.clamp(lootContext1, itemStack0.getCount());
        itemStack0.setCount($$2);
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> limitCount(IntRange intRange0) {
        return m_80683_(p_165219_ -> new LimitCount(p_165219_, intRange0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<LimitCount> {

        public void serialize(JsonObject jsonObject0, LimitCount limitCount1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, limitCount1, jsonSerializationContext2);
            jsonObject0.add("limit", jsonSerializationContext2.serialize(limitCount1.limiter));
        }

        public LimitCount deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            IntRange $$3 = GsonHelper.getAsObject(jsonObject0, "limit", jsonDeserializationContext1, IntRange.class);
            return new LimitCount(lootItemCondition2, $$3);
        }
    }
}