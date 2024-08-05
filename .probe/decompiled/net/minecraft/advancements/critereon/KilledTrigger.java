package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class KilledTrigger extends SimpleCriterionTrigger<KilledTrigger.TriggerInstance> {

    final ResourceLocation id;

    public KilledTrigger(ResourceLocation resourceLocation0) {
        this.id = resourceLocation0;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public KilledTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        return new KilledTrigger.TriggerInstance(this.id, contextAwarePredicate1, EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2), DamageSourcePredicate.fromJson(jsonObject0.get("killing_blow")));
    }

    public void trigger(ServerPlayer serverPlayer0, Entity entity1, DamageSource damageSource2) {
        LootContext $$3 = EntityPredicate.createContext(serverPlayer0, entity1);
        this.m_66234_(serverPlayer0, p_48112_ -> p_48112_.matches(serverPlayer0, $$3, damageSource2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate entityPredicate;

        private final DamageSourcePredicate killingBlow;

        public TriggerInstance(ResourceLocation resourceLocation0, ContextAwarePredicate contextAwarePredicate1, ContextAwarePredicate contextAwarePredicate2, DamageSourcePredicate damageSourcePredicate3) {
            super(resourceLocation0, contextAwarePredicate1);
            this.entityPredicate = contextAwarePredicate2;
            this.killingBlow = damageSourcePredicate3;
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity(EntityPredicate entityPredicate0) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity(EntityPredicate.Builder entityPredicateBuilder0) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()), DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity() {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity(EntityPredicate entityPredicate0, DamageSourcePredicate damageSourcePredicate1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), damageSourcePredicate1);
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity(EntityPredicate.Builder entityPredicateBuilder0, DamageSourcePredicate damageSourcePredicate1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()), damageSourcePredicate1);
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity(EntityPredicate entityPredicate0, DamageSourcePredicate.Builder damageSourcePredicateBuilder1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), damageSourcePredicateBuilder1.build());
        }

        public static KilledTrigger.TriggerInstance playerKilledEntity(EntityPredicate.Builder entityPredicateBuilder0, DamageSourcePredicate.Builder damageSourcePredicateBuilder1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()), damageSourcePredicateBuilder1.build());
        }

        public static KilledTrigger.TriggerInstance playerKilledEntityNearSculkCatalyst() {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.id, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer(EntityPredicate entityPredicate0) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer(EntityPredicate.Builder entityPredicateBuilder0) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()), DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer() {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, ContextAwarePredicate.ANY, DamageSourcePredicate.ANY);
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer(EntityPredicate entityPredicate0, DamageSourcePredicate damageSourcePredicate1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), damageSourcePredicate1);
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer(EntityPredicate.Builder entityPredicateBuilder0, DamageSourcePredicate damageSourcePredicate1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()), damageSourcePredicate1);
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer(EntityPredicate entityPredicate0, DamageSourcePredicate.Builder damageSourcePredicateBuilder1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0), damageSourcePredicateBuilder1.build());
        }

        public static KilledTrigger.TriggerInstance entityKilledPlayer(EntityPredicate.Builder entityPredicateBuilder0, DamageSourcePredicate.Builder damageSourcePredicateBuilder1) {
            return new KilledTrigger.TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()), damageSourcePredicateBuilder1.build());
        }

        public boolean matches(ServerPlayer serverPlayer0, LootContext lootContext1, DamageSource damageSource2) {
            return !this.killingBlow.matches(serverPlayer0, damageSource2) ? false : this.entityPredicate.matches(lootContext1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("entity", this.entityPredicate.toJson(serializationContext0));
            $$1.add("killing_blow", this.killingBlow.serializeToJson());
            return $$1;
        }
    }
}