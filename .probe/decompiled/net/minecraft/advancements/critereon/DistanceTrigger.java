package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class DistanceTrigger extends SimpleCriterionTrigger<DistanceTrigger.TriggerInstance> {

    final ResourceLocation id;

    public DistanceTrigger(ResourceLocation resourceLocation0) {
        this.id = resourceLocation0;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public DistanceTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        LocationPredicate $$3 = LocationPredicate.fromJson(jsonObject0.get("start_position"));
        DistancePredicate $$4 = DistancePredicate.fromJson(jsonObject0.get("distance"));
        return new DistanceTrigger.TriggerInstance(this.id, contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, Vec3 vec1) {
        Vec3 $$2 = serverPlayer0.m_20182_();
        this.m_66234_(serverPlayer0, p_284572_ -> p_284572_.matches(serverPlayer0.serverLevel(), vec1, $$2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final LocationPredicate startPosition;

        private final DistancePredicate distance;

        public TriggerInstance(ResourceLocation resourceLocation0, ContextAwarePredicate contextAwarePredicate1, LocationPredicate locationPredicate2, DistancePredicate distancePredicate3) {
            super(resourceLocation0, contextAwarePredicate1);
            this.startPosition = locationPredicate2;
            this.distance = distancePredicate3;
        }

        public static DistanceTrigger.TriggerInstance fallFromHeight(EntityPredicate.Builder entityPredicateBuilder0, DistancePredicate distancePredicate1, LocationPredicate locationPredicate2) {
            return new DistanceTrigger.TriggerInstance(CriteriaTriggers.FALL_FROM_HEIGHT.id, EntityPredicate.wrap(entityPredicateBuilder0.build()), locationPredicate2, distancePredicate1);
        }

        public static DistanceTrigger.TriggerInstance rideEntityInLava(EntityPredicate.Builder entityPredicateBuilder0, DistancePredicate distancePredicate1) {
            return new DistanceTrigger.TriggerInstance(CriteriaTriggers.RIDE_ENTITY_IN_LAVA_TRIGGER.id, EntityPredicate.wrap(entityPredicateBuilder0.build()), LocationPredicate.ANY, distancePredicate1);
        }

        public static DistanceTrigger.TriggerInstance travelledThroughNether(DistancePredicate distancePredicate0) {
            return new DistanceTrigger.TriggerInstance(CriteriaTriggers.NETHER_TRAVEL.id, ContextAwarePredicate.ANY, LocationPredicate.ANY, distancePredicate0);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("start_position", this.startPosition.serializeToJson());
            $$1.add("distance", this.distance.serializeToJson());
            return $$1;
        }

        public boolean matches(ServerLevel serverLevel0, Vec3 vec1, Vec3 vec2) {
            return !this.startPosition.matches(serverLevel0, vec1.x, vec1.y, vec1.z) ? false : this.distance.matches(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
        }
    }
}