package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;

public class InvertedLootItemCondition implements LootItemCondition {

    final LootItemCondition term;

    InvertedLootItemCondition(LootItemCondition lootItemCondition0) {
        this.term = lootItemCondition0;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.INVERTED;
    }

    public final boolean test(LootContext lootContext0) {
        return !this.term.test(lootContext0);
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.term.m_6231_();
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        LootItemCondition.super.m_6169_(validationContext0);
        this.term.m_6169_(validationContext0);
    }

    public static LootItemCondition.Builder invert(LootItemCondition.Builder lootItemConditionBuilder0) {
        InvertedLootItemCondition $$1 = new InvertedLootItemCondition(lootItemConditionBuilder0.build());
        return () -> $$1;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<InvertedLootItemCondition> {

        public void serialize(JsonObject jsonObject0, InvertedLootItemCondition invertedLootItemCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("term", jsonSerializationContext2.serialize(invertedLootItemCondition1.term));
        }

        public InvertedLootItemCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            LootItemCondition $$2 = GsonHelper.getAsObject(jsonObject0, "term", jsonDeserializationContext1, LootItemCondition.class);
            return new InvertedLootItemCondition($$2);
        }
    }
}