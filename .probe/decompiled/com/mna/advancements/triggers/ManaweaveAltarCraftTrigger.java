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

public class ManaweaveAltarCraftTrigger extends SimpleCriterionTrigger<ManaweaveAltarCraftTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("manaweave_altar_craft");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public ManaweaveAltarCraftTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ItemPredicate itempredicate = ItemPredicate.fromJson(json.get("item"));
        boolean matchFaction = json.has("mustMatchFaction") ? json.get("mustMatchFaction").getAsBoolean() : false;
        return new ManaweaveAltarCraftTrigger.Instance(entityPredicate, itempredicate, matchFaction);
    }

    public void trigger(ServerPlayer player, ItemStack pItem, boolean factionMatch) {
        this.m_66234_(player, instance -> instance.matches(pItem, factionMatch));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final boolean matchFaction;

        public Instance(ContextAwarePredicate player, ItemPredicate pItem, boolean matchFaction) {
            super(ManaweaveAltarCraftTrigger.ID, player);
            this.item = pItem;
            this.matchFaction = matchFaction;
        }

        public boolean matches(ItemStack pItem, boolean factionMatch) {
            return this.item.matches(pItem) && (!this.matchFaction || factionMatch);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}