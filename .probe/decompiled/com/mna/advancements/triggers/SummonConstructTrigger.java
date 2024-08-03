package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.advancements.predicates.SummonConstructPredicate;
import com.mna.api.tools.RLoc;
import com.mna.entities.constructs.animated.Construct;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SummonConstructTrigger extends SimpleCriterionTrigger<SummonConstructTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("summon_construct");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public SummonConstructTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        SummonConstructPredicate predicate = null;
        if (json.has("construction")) {
            predicate = SummonConstructPredicate.fromJSON(json.get("construction").getAsJsonObject());
        }
        return new SummonConstructTrigger.Instance(entityPredicate, predicate);
    }

    public void trigger(ServerPlayer player, Construct construct) {
        this.m_66234_(player, instance -> instance.test(construct));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final SummonConstructPredicate predicate;

        public Instance(ContextAwarePredicate player, SummonConstructPredicate predicate) {
            super(SummonConstructTrigger.ID, player);
            this.predicate = predicate;
        }

        public boolean test(Construct construct) {
            return this.predicate != null ? this.predicate.test(construct) : true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}