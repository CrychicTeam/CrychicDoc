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

public class PerformRitualTrigger extends SimpleCriterionTrigger<PerformRitualTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("perform_ritual");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public PerformRitualTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ArrayList<ResourceLocation> ritualIDs = new ArrayList();
        int tier = 0;
        if (json.has("ritualID")) {
            ritualIDs.add(new ResourceLocation(json.get("ritualID").getAsString()));
        }
        if (json.has("ritualIDs") && json.get("ritualIDs").isJsonArray()) {
            json.get("ritualIDs").getAsJsonArray().forEach(e -> ritualIDs.add(new ResourceLocation(e.getAsString())));
        }
        if (json.has("tier")) {
            tier = json.get("tier").getAsInt();
        }
        return new PerformRitualTrigger.Instance(entityPredicate, ritualIDs, tier);
    }

    public void trigger(ServerPlayer player, ResourceLocation ritualId, int tier) {
        this.m_66234_(player, instance -> instance.matches(ritualId, tier));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final List<ResourceLocation> ritualIds;

        private final int tier;

        public Instance(ContextAwarePredicate player, List<ResourceLocation> ritualIds, int tier) {
            super(PerformRitualTrigger.ID, player);
            this.ritualIds = ritualIds;
            this.tier = tier;
        }

        public boolean matches(ResourceLocation patternId, int tier) {
            if (tier < this.tier) {
                return false;
            } else {
                return this.ritualIds != null && this.ritualIds.size() > 0 ? this.ritualIds.contains(patternId) : true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}