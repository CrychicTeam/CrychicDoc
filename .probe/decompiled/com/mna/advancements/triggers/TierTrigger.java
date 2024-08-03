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

public class TierTrigger extends SimpleCriterionTrigger<TierTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("tier");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public TierTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        int minLevel = json.get("tier").getAsInt();
        return new TierTrigger.Instance(entityPredicate, minLevel);
    }

    public void trigger(ServerPlayer player, int tier) {
        this.m_66234_(player, instance -> instance.test(tier));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final int tier;

        public Instance(ContextAwarePredicate player, int tier) {
            super(TierTrigger.ID, player);
            this.tier = tier;
        }

        public boolean test(int tier) {
            return tier >= this.tier;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}