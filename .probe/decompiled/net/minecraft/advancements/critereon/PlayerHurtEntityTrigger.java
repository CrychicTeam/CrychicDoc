package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class PlayerHurtEntityTrigger extends SimpleCriterionTrigger<PlayerHurtEntityTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("player_hurt_entity");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public PlayerHurtEntityTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        DamagePredicate $$3 = DamagePredicate.fromJson(jsonObject0.get("damage"));
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2);
        return new PlayerHurtEntityTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, Entity entity1, DamageSource damageSource2, float float3, float float4, boolean boolean5) {
        LootContext $$6 = EntityPredicate.createContext(serverPlayer0, entity1);
        this.m_66234_(serverPlayer0, p_60126_ -> p_60126_.matches(serverPlayer0, $$6, damageSource2, float3, float4, boolean5));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final DamagePredicate damage;

        private final ContextAwarePredicate entity;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, DamagePredicate damagePredicate1, ContextAwarePredicate contextAwarePredicate2) {
            super(PlayerHurtEntityTrigger.ID, contextAwarePredicate0);
            this.damage = damagePredicate1;
            this.entity = contextAwarePredicate2;
        }

        public static PlayerHurtEntityTrigger.TriggerInstance playerHurtEntity() {
            return new PlayerHurtEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, DamagePredicate.ANY, ContextAwarePredicate.ANY);
        }

        public static PlayerHurtEntityTrigger.TriggerInstance playerHurtEntity(DamagePredicate damagePredicate0) {
            return new PlayerHurtEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, damagePredicate0, ContextAwarePredicate.ANY);
        }

        public static PlayerHurtEntityTrigger.TriggerInstance playerHurtEntity(DamagePredicate.Builder damagePredicateBuilder0) {
            return new PlayerHurtEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, damagePredicateBuilder0.build(), ContextAwarePredicate.ANY);
        }

        public static PlayerHurtEntityTrigger.TriggerInstance playerHurtEntity(EntityPredicate entityPredicate0) {
            return new PlayerHurtEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, DamagePredicate.ANY, EntityPredicate.wrap(entityPredicate0));
        }

        public static PlayerHurtEntityTrigger.TriggerInstance playerHurtEntity(DamagePredicate damagePredicate0, EntityPredicate entityPredicate1) {
            return new PlayerHurtEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, damagePredicate0, EntityPredicate.wrap(entityPredicate1));
        }

        public static PlayerHurtEntityTrigger.TriggerInstance playerHurtEntity(DamagePredicate.Builder damagePredicateBuilder0, EntityPredicate entityPredicate1) {
            return new PlayerHurtEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, damagePredicateBuilder0.build(), EntityPredicate.wrap(entityPredicate1));
        }

        public boolean matches(ServerPlayer serverPlayer0, LootContext lootContext1, DamageSource damageSource2, float float3, float float4, boolean boolean5) {
            return !this.damage.matches(serverPlayer0, damageSource2, float3, float4, boolean5) ? false : this.entity.matches(lootContext1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("damage", this.damage.serializeToJson());
            $$1.add("entity", this.entity.toJson(serializationContext0));
            return $$1;
        }
    }
}