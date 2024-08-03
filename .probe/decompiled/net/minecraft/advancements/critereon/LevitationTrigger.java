package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class LevitationTrigger extends SimpleCriterionTrigger<LevitationTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("levitation");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public LevitationTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        DistancePredicate $$3 = DistancePredicate.fromJson(jsonObject0.get("distance"));
        MinMaxBounds.Ints $$4 = MinMaxBounds.Ints.fromJson(jsonObject0.get("duration"));
        return new LevitationTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, Vec3 vec1, int int2) {
        this.m_66234_(serverPlayer0, p_49124_ -> p_49124_.matches(serverPlayer0, vec1, int2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final DistancePredicate distance;

        private final MinMaxBounds.Ints duration;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, DistancePredicate distancePredicate1, MinMaxBounds.Ints minMaxBoundsInts2) {
            super(LevitationTrigger.ID, contextAwarePredicate0);
            this.distance = distancePredicate1;
            this.duration = minMaxBoundsInts2;
        }

        public static LevitationTrigger.TriggerInstance levitated(DistancePredicate distancePredicate0) {
            return new LevitationTrigger.TriggerInstance(ContextAwarePredicate.ANY, distancePredicate0, MinMaxBounds.Ints.ANY);
        }

        public boolean matches(ServerPlayer serverPlayer0, Vec3 vec1, int int2) {
            return !this.distance.matches(vec1.x, vec1.y, vec1.z, serverPlayer0.m_20185_(), serverPlayer0.m_20186_(), serverPlayer0.m_20189_()) ? false : this.duration.matches(int2);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("distance", this.distance.serializeToJson());
            $$1.add("duration", this.duration.m_55328_());
            return $$1;
        }
    }
}