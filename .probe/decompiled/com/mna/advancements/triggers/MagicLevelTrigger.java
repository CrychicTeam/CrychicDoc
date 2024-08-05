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

public class MagicLevelTrigger extends SimpleCriterionTrigger<MagicLevelTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("magic_level");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public MagicLevelTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        int minLevel = json.get("minLevel").getAsInt();
        return new MagicLevelTrigger.Instance(entityPredicate, minLevel);
    }

    public void trigger(ServerPlayer player, int magicLevel) {
        this.m_66234_(player, instance -> instance.test(magicLevel));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final int minLevel;

        public Instance(ContextAwarePredicate player, int minLevel) {
            super(MagicLevelTrigger.ID, player);
            this.minLevel = minLevel;
        }

        public boolean test(int level) {
            return level >= this.minLevel;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}