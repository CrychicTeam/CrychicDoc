package com.rekindled.embers.util;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.rekindled.embers.compat.curios.CuriosCompat;
import java.util.Set;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.fml.ModList;

public class MatchCurioLootCondition implements LootItemCondition {

    public static final LootItemConditionType LOOT_CONDITION_TYPE = new LootItemConditionType(new MatchCurioLootCondition.Serializer());

    final ItemPredicate predicate;

    public MatchCurioLootCondition(ItemPredicate pToolPredicate) {
        this.predicate = pToolPredicate;
    }

    @Override
    public LootItemConditionType getType() {
        return LOOT_CONDITION_TYPE;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.KILLER_ENTITY, LootContextParams.THIS_ENTITY);
    }

    public boolean test(LootContext context) {
        if (!ModList.get().isLoaded("curios")) {
            return false;
        } else {
            Entity user = null;
            if (context.hasParam(LootContextParams.KILLER_ENTITY)) {
                user = context.getParam(LootContextParams.KILLER_ENTITY);
            } else if (context.hasParam(LootContextParams.THIS_ENTITY)) {
                user = context.getParam(LootContextParams.THIS_ENTITY);
            }
            return user instanceof LivingEntity ? CuriosCompat.checkForCurios((LivingEntity) user, stack -> this.predicate.matches(stack)) : false;
        }
    }

    public static LootItemCondition.Builder curioMatches(ItemPredicate.Builder pToolPredicateBuilder) {
        return () -> new MatchCurioLootCondition(pToolPredicateBuilder.build());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<MatchCurioLootCondition> {

        public void serialize(JsonObject jsonObject0, MatchCurioLootCondition matchCurioLootCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("predicate", matchCurioLootCondition1.predicate.serializeToJson());
        }

        public MatchCurioLootCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            ItemPredicate itempredicate = ItemPredicate.fromJson(jsonObject0.get("predicate"));
            return new MatchCurioLootCondition(itempredicate);
        }
    }
}