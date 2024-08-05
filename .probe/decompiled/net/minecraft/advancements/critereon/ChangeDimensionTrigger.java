package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;

public class ChangeDimensionTrigger extends SimpleCriterionTrigger<ChangeDimensionTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("changed_dimension");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ChangeDimensionTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ResourceKey<Level> $$3 = jsonObject0.has("from") ? ResourceKey.create(Registries.DIMENSION, new ResourceLocation(GsonHelper.getAsString(jsonObject0, "from"))) : null;
        ResourceKey<Level> $$4 = jsonObject0.has("to") ? ResourceKey.create(Registries.DIMENSION, new ResourceLocation(GsonHelper.getAsString(jsonObject0, "to"))) : null;
        return new ChangeDimensionTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4);
    }

    public void trigger(ServerPlayer serverPlayer0, ResourceKey<Level> resourceKeyLevel1, ResourceKey<Level> resourceKeyLevel2) {
        this.m_66234_(serverPlayer0, p_19768_ -> p_19768_.matches(resourceKeyLevel1, resourceKeyLevel2));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        @Nullable
        private final ResourceKey<Level> from;

        @Nullable
        private final ResourceKey<Level> to;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, @Nullable ResourceKey<Level> resourceKeyLevel1, @Nullable ResourceKey<Level> resourceKeyLevel2) {
            super(ChangeDimensionTrigger.ID, contextAwarePredicate0);
            this.from = resourceKeyLevel1;
            this.to = resourceKeyLevel2;
        }

        public static ChangeDimensionTrigger.TriggerInstance changedDimension() {
            return new ChangeDimensionTrigger.TriggerInstance(ContextAwarePredicate.ANY, null, null);
        }

        public static ChangeDimensionTrigger.TriggerInstance changedDimension(ResourceKey<Level> resourceKeyLevel0, ResourceKey<Level> resourceKeyLevel1) {
            return new ChangeDimensionTrigger.TriggerInstance(ContextAwarePredicate.ANY, resourceKeyLevel0, resourceKeyLevel1);
        }

        public static ChangeDimensionTrigger.TriggerInstance changedDimensionTo(ResourceKey<Level> resourceKeyLevel0) {
            return new ChangeDimensionTrigger.TriggerInstance(ContextAwarePredicate.ANY, null, resourceKeyLevel0);
        }

        public static ChangeDimensionTrigger.TriggerInstance changedDimensionFrom(ResourceKey<Level> resourceKeyLevel0) {
            return new ChangeDimensionTrigger.TriggerInstance(ContextAwarePredicate.ANY, resourceKeyLevel0, null);
        }

        public boolean matches(ResourceKey<Level> resourceKeyLevel0, ResourceKey<Level> resourceKeyLevel1) {
            return this.from != null && this.from != resourceKeyLevel0 ? false : this.to == null || this.to == resourceKeyLevel1;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            if (this.from != null) {
                $$1.addProperty("from", this.from.location().toString());
            }
            if (this.to != null) {
                $$1.addProperty("to", this.to.location().toString());
            }
            return $$1;
        }
    }
}