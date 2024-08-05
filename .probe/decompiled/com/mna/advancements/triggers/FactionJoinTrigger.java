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

public class FactionJoinTrigger extends SimpleCriterionTrigger<FactionJoinTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("faction_join");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public FactionJoinTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ResourceLocation factionID = null;
        if (json.has("faction")) {
            factionID = new ResourceLocation(json.get("faction").getAsString());
        }
        return new FactionJoinTrigger.Instance(entityPredicate, factionID);
    }

    public void trigger(ServerPlayer player, ResourceLocation faction) {
        this.m_66234_(player, instance -> instance.test(faction));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation faction;

        public Instance(ContextAwarePredicate player, ResourceLocation faction) {
            super(FactionJoinTrigger.ID, player);
            this.faction = faction;
        }

        public boolean test(ResourceLocation factionID) {
            if (factionID == null) {
                return false;
            } else {
                return this.faction != null ? this.faction.equals(factionID) : true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}