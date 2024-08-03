package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

public class TameAnimalTrigger extends SimpleCriterionTrigger<TameAnimalTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("tame_animal");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public TameAnimalTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ContextAwarePredicate $$3 = EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2);
        return new TameAnimalTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, Animal animal1) {
        LootContext $$2 = EntityPredicate.createContext(serverPlayer0, animal1);
        this.m_66234_(serverPlayer0, p_68838_ -> p_68838_.matches($$2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ContextAwarePredicate entity;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ContextAwarePredicate contextAwarePredicate1) {
            super(TameAnimalTrigger.ID, contextAwarePredicate0);
            this.entity = contextAwarePredicate1;
        }

        public static TameAnimalTrigger.TriggerInstance tamedAnimal() {
            return new TameAnimalTrigger.TriggerInstance(ContextAwarePredicate.ANY, ContextAwarePredicate.ANY);
        }

        public static TameAnimalTrigger.TriggerInstance tamedAnimal(EntityPredicate entityPredicate0) {
            return new TameAnimalTrigger.TriggerInstance(ContextAwarePredicate.ANY, EntityPredicate.wrap(entityPredicate0));
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