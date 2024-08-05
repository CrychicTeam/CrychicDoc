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

public class SetBonusTrigger extends SimpleCriterionTrigger<SetBonusTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("set_bonus");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public SetBonusTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ResourceLocation setID = null;
        if (json.has("set")) {
            setID = new ResourceLocation(json.get("set").getAsString());
        }
        return new SetBonusTrigger.Instance(entityPredicate, setID);
    }

    public void trigger(ServerPlayer player, ResourceLocation setID) {
        this.m_66234_(player, instance -> instance.test(setID));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ResourceLocation setID;

        public Instance(ContextAwarePredicate player, ResourceLocation setID) {
            super(SetBonusTrigger.ID, player);
            this.setID = setID;
        }

        public boolean test(ResourceLocation setID) {
            if (setID == null) {
                return false;
            } else {
                return this.setID != null ? this.setID.equals(setID) : true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}