package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.api.tools.RLoc;
import java.util.ArrayList;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CompleteMultiblockTrigger extends SimpleCriterionTrigger<CompleteMultiblockTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("multiblock_complete");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CompleteMultiblockTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ArrayList<ResourceLocation> structureIDs = new ArrayList();
        if (json.has("multiblockIDs") && json.get("multiblockIDs").isJsonArray()) {
            json.get("multiblockIDs").getAsJsonArray().forEach(e -> structureIDs.add(new ResourceLocation(e.getAsString())));
        }
        return new CompleteMultiblockTrigger.Instance(entityPredicate, structureIDs);
    }

    public void trigger(ServerPlayer player, ResourceLocation structureID) {
        this.m_66234_(player, instance -> instance.matches(structureID));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ArrayList<ResourceLocation> structureIDs;

        public Instance(ContextAwarePredicate player, ArrayList<ResourceLocation> structureIDs) {
            super(CompleteMultiblockTrigger.ID, player);
            this.structureIDs = structureIDs;
        }

        public boolean matches(ResourceLocation structureID) {
            return this.structureIDs != null && this.structureIDs.size() > 0 ? this.structureIDs.contains(structureID) : true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}