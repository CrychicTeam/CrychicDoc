package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConstructBeaconTrigger extends SimpleCriterionTrigger<ConstructBeaconTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("construct_beacon");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ConstructBeaconTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        MinMaxBounds.Ints $$3 = MinMaxBounds.Ints.fromJson(jsonObject0.get("level"));
        return new ConstructBeaconTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, int int1) {
        this.m_66234_(serverPlayer0, p_148028_ -> p_148028_.matches(int1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final MinMaxBounds.Ints level;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, MinMaxBounds.Ints minMaxBoundsInts1) {
            super(ConstructBeaconTrigger.ID, contextAwarePredicate0);
            this.level = minMaxBoundsInts1;
        }

        public static ConstructBeaconTrigger.TriggerInstance constructedBeacon() {
            return new ConstructBeaconTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY);
        }

        public static ConstructBeaconTrigger.TriggerInstance constructedBeacon(MinMaxBounds.Ints minMaxBoundsInts0) {
            return new ConstructBeaconTrigger.TriggerInstance(ContextAwarePredicate.ANY, minMaxBoundsInts0);
        }

        public boolean matches(int int0) {
            return this.level.matches(int0);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("level", this.level.m_55328_());
            return $$1;
        }
    }
}