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

public class BeOurGuestTrigger extends SimpleCriterionTrigger<BeOurGuestTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("animus_mana_tea");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public BeOurGuestTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new BeOurGuestTrigger.Instance(entityPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, instance -> instance.test());
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ContextAwarePredicate player) {
            super(BeOurGuestTrigger.ID, player);
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