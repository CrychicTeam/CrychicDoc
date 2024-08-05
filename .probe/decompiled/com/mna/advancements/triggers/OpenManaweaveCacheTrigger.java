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

public class OpenManaweaveCacheTrigger extends SimpleCriterionTrigger<OpenManaweaveCacheTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("open_manaweave_cache");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public OpenManaweaveCacheTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        int tier = 0;
        if (json.has("tier")) {
            tier = json.get("tier").getAsInt();
        }
        return new OpenManaweaveCacheTrigger.Instance(entityPredicate, tier);
    }

    public void trigger(ServerPlayer player, int cacheTier) {
        this.m_66234_(player, instance -> instance.matches(cacheTier));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final int tier;

        public Instance(ContextAwarePredicate player, int tier) {
            super(OpenManaweaveCacheTrigger.ID, player);
            this.tier = tier;
        }

        public boolean matches(int cacheTier) {
            return cacheTier >= this.tier;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}