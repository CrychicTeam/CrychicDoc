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

public class DefeatBossTrigger extends SimpleCriterionTrigger<DefeatBossTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("defeat_boss");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public DefeatBossTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ResourceLocation entityID = new ResourceLocation(json.get("entityType").getAsString());
        int count = json.has("count") ? json.get("count").getAsInt() : 0;
        return new DefeatBossTrigger.Instance(entityPredicate, entityID, count);
    }

    public void trigger(ServerPlayer player, ResourceLocation entityID, int numDefeated) {
        this.m_66234_(player, instance -> instance.test(entityID, numDefeated));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation entityID;

        private final int count;

        public Instance(ContextAwarePredicate player, ResourceLocation entityID, int count) {
            super(DefeatBossTrigger.ID, player);
            this.entityID = entityID;
            this.count = count;
        }

        public boolean test(ResourceLocation entityID, int numDefeated) {
            return entityID.equals(this.entityID) && numDefeated >= this.count;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}