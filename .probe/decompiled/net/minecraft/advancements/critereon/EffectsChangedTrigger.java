package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;

public class EffectsChangedTrigger extends SimpleCriterionTrigger<EffectsChangedTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("effects_changed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public EffectsChangedTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        MobEffectsPredicate $$3 = MobEffectsPredicate.fromJson(jsonObject0.get("effects"));
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "source", deserializationContext2);
        return new EffectsChangedTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, @Nullable Entity entity1) {
        LootContext $$2 = entity1 != null ? EntityPredicate.createContext(serverPlayer0, entity1) : null;
        this.m_66234_(serverPlayer0, p_149268_ -> p_149268_.matches(serverPlayer0, $$2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final MobEffectsPredicate effects;

        private final ContextAwarePredicate source;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, MobEffectsPredicate mobEffectsPredicate1, ContextAwarePredicate contextAwarePredicate2) {
            super(EffectsChangedTrigger.ID, contextAwarePredicate0);
            this.effects = mobEffectsPredicate1;
            this.source = contextAwarePredicate2;
        }

        public static EffectsChangedTrigger.TriggerInstance hasEffects(MobEffectsPredicate mobEffectsPredicate0) {
            return new EffectsChangedTrigger.TriggerInstance(ContextAwarePredicate.ANY, mobEffectsPredicate0, ContextAwarePredicate.ANY);
        }

        public static EffectsChangedTrigger.TriggerInstance gotEffectsFrom(EntityPredicate entityPredicate0) {
            return new EffectsChangedTrigger.TriggerInstance(ContextAwarePredicate.ANY, MobEffectsPredicate.ANY, EntityPredicate.wrap(entityPredicate0));
        }

        public boolean matches(ServerPlayer serverPlayer0, @Nullable LootContext lootContext1) {
            return !this.effects.matches((LivingEntity) serverPlayer0) ? false : this.source == ContextAwarePredicate.ANY || lootContext1 != null && this.source.matches(lootContext1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("effects", this.effects.serializeToJson());
            $$1.add("source", this.source.toJson(serializationContext0));
            return $$1;
        }
    }
}