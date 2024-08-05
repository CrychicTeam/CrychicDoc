package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.tools.RLoc;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CaptureWellspringTrigger extends SimpleCriterionTrigger<CaptureWellspringTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("capture_wellspring");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CaptureWellspringTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        Affinity type = null;
        if (json.has("type")) {
            String val = json.get("type").getAsString();
            try {
                type = Affinity.valueOf(val);
            } catch (Throwable var7) {
                ManaAndArtifice.LOGGER.warn("failed to process FROM directive in wellspring capture advancement trigger.  Value: " + val);
            }
        }
        return new CaptureWellspringTrigger.Instance(entityPredicate, type);
    }

    public void trigger(ServerPlayer player, Affinity type) {
        this.m_66234_(player, instance -> instance.matches(type));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final Affinity type;

        public Instance(ContextAwarePredicate player, Affinity type) {
            super(CaptureWellspringTrigger.ID, player);
            this.type = type;
        }

        public boolean matches(Affinity type) {
            return this.type != null ? type == this.type : true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}