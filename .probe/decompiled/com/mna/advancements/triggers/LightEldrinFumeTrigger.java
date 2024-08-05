package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.tools.RLoc;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class LightEldrinFumeTrigger extends SimpleCriterionTrigger<LightEldrinFumeTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("light_fume");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public LightEldrinFumeTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ArrayList<Affinity> affinities = new ArrayList();
        if (json.has("affinities") && json.get("affinities").isJsonArray()) {
            json.get("affinities").getAsJsonArray().forEach(e -> {
                String val = e.getAsString();
                try {
                    affinities.add(Affinity.valueOf(val));
                } catch (Throwable var4x) {
                    ManaAndArtifice.LOGGER.warn("failed to parse affinity enum value in light fume advancement trigger.  Value:" + val);
                }
            });
        }
        return new LightEldrinFumeTrigger.Instance(entityPredicate, affinities);
    }

    public void trigger(ServerPlayer player, Affinity type) {
        this.m_66234_(player, instance -> instance.matches(type));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final List<Affinity> affinities;

        public Instance(ContextAwarePredicate player, List<Affinity> affinities) {
            super(LightEldrinFumeTrigger.ID, player);
            this.affinities = affinities;
        }

        public boolean matches(Affinity type) {
            return this.affinities != null && this.affinities.size() > 0 ? this.affinities.contains(type) : true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}