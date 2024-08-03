package com.sihenzhang.crockpot.advancement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class EatFoodTrigger extends SimpleCriterionTrigger<EatFoodTrigger.Instance> {

    private static final ResourceLocation ID = RLUtils.createRL("eat_food");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    protected EatFoodTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        MinMaxBounds.Ints count = MinMaxBounds.Ints.fromJson(json.get("count"));
        return new EatFoodTrigger.Instance(entityPredicate, itemPredicate, count);
    }

    public void trigger(ServerPlayer player, ItemStack stack, int count) {
        this.m_66234_(player, testTrigger -> testTrigger.matches(player, stack, count));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final MinMaxBounds.Ints count;

        public Instance(ContextAwarePredicate player, ItemPredicate item, MinMaxBounds.Ints count) {
            super(EatFoodTrigger.ID, player);
            this.item = item;
            this.count = count;
        }

        public boolean matches(ServerPlayer player, ItemStack stack, int count) {
            return this.item.matches(stack) && this.count.matches(count);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject conditionsJson = super.serializeToJson(conditions);
            conditionsJson.add("item", this.item.serializeToJson());
            conditionsJson.add("count", this.count.m_55328_());
            return conditionsJson;
        }
    }
}