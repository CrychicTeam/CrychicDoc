package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
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

public class CastCantripTrigger extends SimpleCriterionTrigger<CastCantripTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("cast_cantrip");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CastCantripTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ArrayList<ResourceLocation> cantripIDs = new ArrayList();
        int tier = 0;
        if (json.has("cantripID")) {
            cantripIDs.add(new ResourceLocation(json.get("cantripID").getAsString()));
        }
        if (json.has("cantripIDs") && json.get("cantripIDs").isJsonArray()) {
            json.get("cantripIDs").getAsJsonArray().forEach(e -> cantripIDs.add(new ResourceLocation(e.getAsString())));
        }
        if (json.has("tier")) {
            tier = json.get("tier").getAsInt();
        }
        return new CastCantripTrigger.Instance(entityPredicate, cantripIDs, tier);
    }

    public void trigger(ServerPlayer player, ResourceLocation cantripId, int cantripTier) {
        this.m_66234_(player, instance -> instance.matches(cantripId, cantripTier));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final List<ResourceLocation> cantripIDs;

        private final int tier;

        public Instance(ContextAwarePredicate player, List<ResourceLocation> cantripIDs, int tier) {
            super(CastCantripTrigger.ID, player);
            this.cantripIDs = cantripIDs;
            this.tier = tier;
        }

        public boolean matches(ResourceLocation cantripId, int cantripTier) {
            if (cantripTier < this.tier) {
                return false;
            } else {
                return this.cantripIDs != null && this.cantripIDs.size() > 0 ? this.cantripIDs.contains(cantripId) : true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}