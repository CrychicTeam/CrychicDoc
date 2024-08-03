package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class SummonedEntityTrigger extends SimpleCriterionTrigger<SummonedEntityTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("summoned_entity");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public SummonedEntityTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2);
        return new SummonedEntityTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, Entity entity1) {
        LootContext $$2 = EntityPredicate.createContext(serverPlayer0, entity1);
        this.m_66234_(serverPlayer0, p_68265_ -> p_68265_.matches($$2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate entity;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate contextAwarePredicate1) {
            super(SummonedEntityTrigger.ID, contextAwarePredicate0);
            this.entity = contextAwarePredicate1;
        }

        public static SummonedEntityTrigger.TriggerInstance summonedEntity(EntityPredicate.Builder entityPredicateBuilder0) {
            return new SummonedEntityTrigger.TriggerInstance(ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicateBuilder0.build()));
        }

        public boolean matches(LootContext lootContext0) {
            return this.entity.matches(lootContext0);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("entity", this.entity.toJson(serializationContext0));
            return $$1;
        }
    }
}