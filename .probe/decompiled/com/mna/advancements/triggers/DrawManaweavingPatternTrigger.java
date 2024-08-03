package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.api.tools.RLoc;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class DrawManaweavingPatternTrigger extends SimpleCriterionTrigger<DrawManaweavingPatternTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("draw_manaweave_pattern");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public DrawManaweavingPatternTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ResourceLocation patternId = null;
        if (json.has("patternId")) {
            patternId = new ResourceLocation(json.get("patternId").getAsString());
        }
        return new DrawManaweavingPatternTrigger.Instance(entityPredicate, patternId);
    }

    public void trigger(ServerPlayer player, ResourceLocation patternId) {
        this.m_66234_(player, instance -> instance.matches(patternId));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation patternId;

        public Instance(ContextAwarePredicate player, ResourceLocation patternId) {
            super(DrawManaweavingPatternTrigger.ID, player);
            this.patternId = patternId;
        }

        public boolean matches(ResourceLocation patternId) {
            return this.patternId != null ? this.patternId.equals(patternId) : true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}