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

public class StudyDeskRoteTrigger extends SimpleCriterionTrigger<StudyDeskRoteTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("study_desk_rote");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public StudyDeskRoteTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        float percent = 0.0F;
        ArrayList<ResourceLocation> parts = new ArrayList();
        if (json.has("progress")) {
            percent = json.get("progress").getAsFloat();
        }
        if (json.has("parts") && json.get("parts").isJsonArray()) {
            json.get("parts").getAsJsonArray().forEach(e -> parts.add(new ResourceLocation(e.getAsString())));
        }
        return new StudyDeskRoteTrigger.Instance(entityPredicate, parts, percent);
    }

    public void trigger(ServerPlayer player, ResourceLocation part, float progress) {
        this.m_66234_(player, instance -> instance.matches(part, progress));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ArrayList<ResourceLocation> parts;

        private final float percent;

        public Instance(ContextAwarePredicate player, ArrayList<ResourceLocation> parts, float percent) {
            super(StudyDeskRoteTrigger.ID, player);
            this.parts = parts;
            this.percent = percent;
        }

        public boolean matches(ResourceLocation part, float progress) {
            if (progress < this.percent) {
                return false;
            } else {
                return this.parts != null && this.parts.size() > 0 ? this.parts.contains(part) : true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}