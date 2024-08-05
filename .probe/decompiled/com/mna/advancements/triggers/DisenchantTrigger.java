package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.api.tools.RLoc;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class DisenchantTrigger extends SimpleCriterionTrigger<DisenchantTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("disenchant");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public DisenchantTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ItemPredicate itempredicate = ItemPredicate.fromJson(json.get("item"));
        return new DisenchantTrigger.Instance(entityPredicate, itempredicate);
    }

    public void trigger(ServerPlayer player, ItemStack pItem) {
        this.m_66234_(player, instance -> instance.matches(pItem));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        public Instance(ContextAwarePredicate player, ItemPredicate pItem) {
            super(DisenchantTrigger.ID, player);
            this.item = pItem;
        }

        public boolean matches(ItemStack pItem) {
            return this.item.matches(pItem);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}