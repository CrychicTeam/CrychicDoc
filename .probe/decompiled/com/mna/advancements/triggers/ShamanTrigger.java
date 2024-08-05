package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.api.tools.RLoc;
import com.mna.items.ItemInit;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;

public class ShamanTrigger extends SimpleCriterionTrigger<ShamanTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("shaman");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ShamanTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return new ShamanTrigger.Instance(entityPredicate);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, instance -> instance.test(player));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ContextAwarePredicate player) {
            super(ShamanTrigger.ID, player);
        }

        public boolean test(ServerPlayer player) {
            return player.getStats().m_13015_(Stats.ITEM_USED.get(ItemInit.FALL_CHARM.get())) > 0 && player.getStats().m_13015_(Stats.ITEM_USED.get(ItemInit.BED_CHARM.get())) > 0 && player.getStats().m_13015_(Stats.ITEM_USED.get(ItemInit.BURN_CHARM.get())) > 0 && player.getStats().m_13015_(Stats.ITEM_USED.get(ItemInit.DROWN_CHARM.get())) > 0;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}