package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class DamageSourceCondition implements LootItemCondition {

    final DamageSourcePredicate predicate;

    DamageSourceCondition(DamageSourcePredicate damageSourcePredicate0) {
        this.predicate = damageSourcePredicate0;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.DAMAGE_SOURCE_PROPERTIES;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN, LootContextParams.DAMAGE_SOURCE);
    }

    public boolean test(LootContext lootContext0) {
        DamageSource $$1 = lootContext0.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        Vec3 $$2 = lootContext0.getParamOrNull(LootContextParams.ORIGIN);
        return $$2 != null && $$1 != null && this.predicate.matches(lootContext0.getLevel(), $$2, $$1);
    }

    public static LootItemCondition.Builder hasDamageSource(DamageSourcePredicate.Builder damageSourcePredicateBuilder0) {
        return () -> new DamageSourceCondition(damageSourcePredicateBuilder0.build());
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<DamageSourceCondition> {

        public void serialize(JsonObject jsonObject0, DamageSourceCondition damageSourceCondition1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.add("predicate", damageSourceCondition1.predicate.serializeToJson());
        }

        public DamageSourceCondition deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            DamageSourcePredicate $$2 = DamageSourcePredicate.fromJson(jsonObject0.get("predicate"));
            return new DamageSourceCondition($$2);
        }
    }
}