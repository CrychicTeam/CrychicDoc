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

public class NotSoEasyTrigger extends SimpleCriterionTrigger<NotSoEasyTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("not_so_easy");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public NotSoEasyTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new NotSoEasyTrigger.Instance(entityPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, instance -> instance.test());
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ContextAwarePredicate player) {
            super(NotSoEasyTrigger.ID, player);
        }

        public boolean test() {
            return true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}