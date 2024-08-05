package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class StartRidingTrigger extends SimpleCriterionTrigger<StartRidingTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("started_riding");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public StartRidingTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        return new StartRidingTrigger.TriggerInstance(contextAwarePredicate1);
    }

    public void trigger(ServerPlayer serverPlayer0) {
        this.m_66234_(serverPlayer0, p_160394_ -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0) {
            super(StartRidingTrigger.ID, contextAwarePredicate0);
        }

        public static StartRidingTrigger.TriggerInstance playerStartsRiding(EntityPredicate.Builder entityPredicateBuilder0) {
            return new StartRidingTrigger.TriggerInstance(EntityPredicate.wrap(entityPredicateBuilder0.build()));
        }
    }
}