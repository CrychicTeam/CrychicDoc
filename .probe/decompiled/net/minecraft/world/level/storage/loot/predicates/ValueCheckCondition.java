package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class ValueCheckCondition implements LootItemCondition {

    final NumberProvider provider;

    final IntRange range;

    ValueCheckCondition(NumberProvider numberProvider0, IntRange intRange1) {
        this.provider = numberProvider0;
        this.range = intRange1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.VALUE_CHECK;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Sets.union(this.provider.m_6231_(), this.range.getReferencedContextParams());
    }

    public boolean test(LootContext lootContext0) {
        return this.range.test(lootContext0, this.provider.getInt(lootContext0));
    }

    public static LootItemCondition.Builder hasValue(NumberProvider numberProvider0, IntRange intRange1) {
        return () -> new ValueCheckCondition(numberProvider0, intRange1);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ValueCheckCondition> {

        public void serialize(JsonObject jsonObject0, ValueCheckCondition valueCheckCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("value", jsonSerializationContext2.serialize(valueCheckCondition1.provider));
            jsonObject0.add("range", jsonSerializationContext2.serialize(valueCheckCondition1.range));
        }

        public ValueCheckCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            NumberProvider $$2 = GsonHelper.getAsObject(jsonObject0, "value", jsonDeserializationContext1, NumberProvider.class);
            IntRange $$3 = GsonHelper.getAsObject(jsonObject0, "range", jsonDeserializationContext1, IntRange.class);
            return new ValueCheckCondition($$2, $$3);
        }
    }
}