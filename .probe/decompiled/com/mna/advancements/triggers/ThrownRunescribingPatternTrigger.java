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

public class ThrownRunescribingPatternTrigger extends SimpleCriterionTrigger<ThrownRunescribingPatternTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("thrown_runescribe_pattern");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ThrownRunescribingPatternTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new ThrownRunescribingPatternTrigger.Instance(entityPredicate);
    }

    public void trigger(ServerPlayer shooter) {
        this.m_66234_(shooter, instance -> instance.test());
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ContextAwarePredicate player) {
            super(ThrownRunescribingPatternTrigger.ID, player);
        }

        public boolean test() {
            return true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}