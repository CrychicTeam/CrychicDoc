package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public class EntityHurtPlayerTrigger extends SimpleCriterionTrigger<EntityHurtPlayerTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("entity_hurt_player");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public EntityHurtPlayerTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        DamagePredicate $$3 = DamagePredicate.fromJson(jsonObject0.get("damage"));
        return new EntityHurtPlayerTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, DamageSource damageSource1, float float2, float float3, boolean boolean4) {
        this.m_66234_(serverPlayer0, p_35186_ -> p_35186_.matches(serverPlayer0, damageSource1, float2, float3, boolean4));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final DamagePredicate damage;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, DamagePredicate damagePredicate1) {
            super(EntityHurtPlayerTrigger.ID, contextAwarePredicate0);
            this.damage = damagePredicate1;
        }

        public static EntityHurtPlayerTrigger.TriggerInstance entityHurtPlayer() {
            return new EntityHurtPlayerTrigger.TriggerInstance(ContextAwarePredicate.ANY, DamagePredicate.ANY);
        }

        public static EntityHurtPlayerTrigger.TriggerInstance entityHurtPlayer(DamagePredicate damagePredicate0) {
            return new EntityHurtPlayerTrigger.TriggerInstance(ContextAwarePredicate.ANY, damagePredicate0);
        }

        public static EntityHurtPlayerTrigger.TriggerInstance entityHurtPlayer(DamagePredicate.Builder damagePredicateBuilder0) {
            return new EntityHurtPlayerTrigger.TriggerInstance(ContextAwarePredicate.ANY, damagePredicateBuilder0.build());
        }

        public boolean matches(ServerPlayer serverPlayer0, DamageSource damageSource1, float float2, float float3, boolean boolean4) {
            return this.damage.matches(serverPlayer0, damageSource1, float2, float3, boolean4);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("damage", this.damage.serializeToJson());
            return $$1;
        }
    }
}