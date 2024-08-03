package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

public class TargetBlockTrigger extends SimpleCriterionTrigger<TargetBlockTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("target_hit");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public TargetBlockTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        MinMaxBounds.Ints $$3 = MinMaxBounds.Ints.fromJson(jsonObject0.get("signal_strength"));
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "projectile", deserializationContext2);
        return new TargetBlockTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, Entity entity1, Vec3 vec2, int int3) {
        LootContext $$4 = EntityPredicate.createContext(serverPlayer0, entity1);
        this.m_66234_(serverPlayer0, p_70224_ -> p_70224_.matches($$4, vec2, int3));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final MinMaxBounds.Ints signalStrength;

        private final ContextAwarePredicate projectile;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, MinMaxBounds.Ints minMaxBoundsInts1, ContextAwarePredicate contextAwarePredicate2) {
            super(TargetBlockTrigger.ID, contextAwarePredicate0);
            this.signalStrength = minMaxBoundsInts1;
            this.projectile = contextAwarePredicate2;
        }

        public static TargetBlockTrigger.TriggerInstance targetHit(MinMaxBounds.Ints minMaxBoundsInts0, ContextAwarePredicate contextAwarePredicate1) {
            return new TargetBlockTrigger.TriggerInstance(ContextAwarePredicate.ANY, minMaxBoundsInts0, contextAwarePredicate1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("signal_strength", this.signalStrength.m_55328_());
            $$1.add("projectile", this.projectile.toJson(serializationContext0));
            return $$1;
        }

        public boolean matches(LootContext lootContext0, Vec3 vec1, int int2) {
            return !this.signalStrength.matches(int2) ? false : this.projectile.matches(lootContext0);
        }
    }
}