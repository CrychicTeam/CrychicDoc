package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LootItemEntityPropertyCondition implements LootItemCondition {

    final EntityPredicate predicate;

    final LootContext.EntityTarget entityTarget;

    LootItemEntityPropertyCondition(EntityPredicate entityPredicate0, LootContext.EntityTarget lootContextEntityTarget1) {
        this.predicate = entityPredicate0;
        this.entityTarget = lootContextEntityTarget1;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ENTITY_PROPERTIES;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN, this.entityTarget.getParam());
    }

    public boolean test(LootContext lootContext0) {
        Entity $$1 = lootContext0.getParamOrNull(this.entityTarget.getParam());
        Vec3 $$2 = lootContext0.getParamOrNull(LootContextParams.ORIGIN);
        return this.predicate.matches(lootContext0.getLevel(), $$2, $$1);
    }

    public static LootItemCondition.Builder entityPresent(LootContext.EntityTarget lootContextEntityTarget0) {
        return hasProperties(lootContextEntityTarget0, EntityPredicate.Builder.entity());
    }

    public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget lootContextEntityTarget0, EntityPredicate.Builder entityPredicateBuilder1) {
        return () -> new LootItemEntityPropertyCondition(entityPredicateBuilder1.build(), lootContextEntityTarget0);
    }

    public static LootItemCondition.Builder hasProperties(LootContext.EntityTarget lootContextEntityTarget0, EntityPredicate entityPredicate1) {
        return () -> new LootItemEntityPropertyCondition(entityPredicate1, lootContextEntityTarget0);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemEntityPropertyCondition> {

        public void serialize(JsonObject jsonObject0, LootItemEntityPropertyCondition lootItemEntityPropertyCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("predicate", lootItemEntityPropertyCondition1.predicate.serializeToJson());
            jsonObject0.add("entity", jsonSerializationContext2.serialize(lootItemEntityPropertyCondition1.entityTarget));
        }

        public LootItemEntityPropertyCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            EntityPredicate $$2 = EntityPredicate.fromJson(jsonObject0.get("predicate"));
            return new LootItemEntityPropertyCondition($$2, GsonHelper.getAsObject(jsonObject0, "entity", jsonDeserializationContext1, LootContext.EntityTarget.class));
        }
    }
}