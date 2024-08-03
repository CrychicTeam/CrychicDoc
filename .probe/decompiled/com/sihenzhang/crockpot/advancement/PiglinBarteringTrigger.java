package com.sihenzhang.crockpot.advancement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PiglinBarteringTrigger extends SimpleCriterionTrigger<PiglinBarteringTrigger.Instance> {

    private static final ResourceLocation ID = RLUtils.createRL("piglin_bartering");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    protected PiglinBarteringTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new PiglinBarteringTrigger.Instance(entityPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.m_66234_(player, testTrigger -> testTrigger.matches(player, stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public Instance(ContextAwarePredicate player, ItemPredicate item) {
            super(PiglinBarteringTrigger.ID, player);
            this.item = item;
        }

        public boolean matches(ServerPlayer player, ItemStack stack) {
            return this.item.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject conditionsJson = super.serializeToJson(conditions);
            conditionsJson.add("item", this.item.serializeToJson());
            return conditionsJson;
        }
    }
}