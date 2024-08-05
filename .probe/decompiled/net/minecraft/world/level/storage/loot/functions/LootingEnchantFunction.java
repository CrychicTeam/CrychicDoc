package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class LootingEnchantFunction extends LootItemConditionalFunction {

    public static final int NO_LIMIT = 0;

    final NumberProvider value;

    final int limit;

    LootingEnchantFunction(LootItemCondition[] lootItemCondition0, NumberProvider numberProvider1, int int2) {
        super(lootItemCondition0);
        this.value = numberProvider1;
        this.limit = int2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.LOOTING_ENCHANT;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(ImmutableSet.of(LootContextParams.KILLER_ENTITY), this.value.m_6231_());
    }

    boolean hasLimit() {
        return this.limit > 0;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        Entity $$2 = lootContext1.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if ($$2 instanceof LivingEntity) {
            int $$3 = EnchantmentHelper.getMobLooting((LivingEntity) $$2);
            if ($$3 == 0) {
                return itemStack0;
            }
            float $$4 = (float) $$3 * this.value.getFloat(lootContext1);
            itemStack0.grow(Math.round($$4));
            if (this.hasLimit() && itemStack0.getCount() > this.limit) {
                itemStack0.setCount(this.limit);
            }
        }
        return itemStack0;
    }

    public static LootingEnchantFunction.Builder lootingMultiplier(NumberProvider numberProvider0) {
        return new LootingEnchantFunction.Builder(numberProvider0);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<LootingEnchantFunction.Builder> {

        private final NumberProvider count;

        private int limit = 0;

        public Builder(NumberProvider numberProvider0) {
            this.count = numberProvider0;
        }

        protected LootingEnchantFunction.Builder getThis() {
            return this;
        }

        public LootingEnchantFunction.Builder setLimit(int int0) {
            this.limit = int0;
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new LootingEnchantFunction(this.m_80699_(), this.count, this.limit);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<LootingEnchantFunction> {

        public void serialize(JsonObject jsonObject0, LootingEnchantFunction lootingEnchantFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, lootingEnchantFunction1, jsonSerializationContext2);
            jsonObject0.add("count", jsonSerializationContext2.serialize(lootingEnchantFunction1.value));
            if (lootingEnchantFunction1.hasLimit()) {
                jsonObject0.add("limit", jsonSerializationContext2.serialize(lootingEnchantFunction1.limit));
            }
        }

        public LootingEnchantFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            int $$3 = GsonHelper.getAsInt(jsonObject0, "limit", 0);
            return new LootingEnchantFunction(lootItemCondition2, GsonHelper.getAsObject(jsonObject0, "count", jsonDeserializationContext1, NumberProvider.class), $$3);
        }
    }
}