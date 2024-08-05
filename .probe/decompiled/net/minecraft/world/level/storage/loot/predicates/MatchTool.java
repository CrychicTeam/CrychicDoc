package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class MatchTool implements LootItemCondition {

    final ItemPredicate predicate;

    public MatchTool(ItemPredicate itemPredicate0) {
        this.predicate = itemPredicate0;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.MATCH_TOOL;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    public boolean test(LootContext lootContext0) {
        ItemStack $$1 = lootContext0.getParamOrNull(LootContextParams.TOOL);
        return $$1 != null && this.predicate.matches($$1);
    }

    public static LootItemCondition.Builder toolMatches(ItemPredicate.Builder itemPredicateBuilder0) {
        return () -> new MatchTool(itemPredicateBuilder0.build());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<MatchTool> {

        public void serialize(JsonObject jsonObject0, MatchTool matchTool1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("predicate", matchTool1.predicate.serializeToJson());
        }

        public MatchTool deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            ItemPredicate $$2 = ItemPredicate.fromJson(jsonObject0.get("predicate"));
            return new MatchTool($$2);
        }
    }
}