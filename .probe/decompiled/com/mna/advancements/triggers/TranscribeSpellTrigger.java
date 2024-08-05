package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.advancements.predicates.CraftSpellPredicate;
import com.mna.api.spells.base.ISpellDefinition;
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
import net.minecraft.world.level.Level;

public class TranscribeSpellTrigger extends SimpleCriterionTrigger<TranscribeSpellTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("transcribe_spell");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public TranscribeSpellTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        CraftSpellPredicate spellPredicate = null;
        ItemPredicate itemPredicate = null;
        if (json.has("spell") && json.get("spell").isJsonObject()) {
            spellPredicate = CraftSpellPredicate.fromJSON(json.get("spell").getAsJsonObject());
        }
        if (json.has("item") && json.get("item").isJsonObject()) {
            itemPredicate = ItemPredicate.fromJson(json.get("item").getAsJsonObject());
        }
        return new TranscribeSpellTrigger.Instance(entityPredicate, spellPredicate, itemPredicate);
    }

    public void trigger(ServerPlayer player, ISpellDefinition spell, ItemStack destination) {
        this.m_66234_(player, instance -> instance.test(spell, player.m_9236_(), destination));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final CraftSpellPredicate spellPredicate;

        private final ItemPredicate itemPredicate;

        public Instance(ContextAwarePredicate player, CraftSpellPredicate spellPredicate, ItemPredicate itemPredicate) {
            super(TranscribeSpellTrigger.ID, player);
            this.spellPredicate = spellPredicate;
            this.itemPredicate = itemPredicate;
        }

        public boolean test(ISpellDefinition spell, Level world, ItemStack destinationStack) {
            boolean success = true;
            if (this.spellPredicate != null) {
                success &= this.spellPredicate.test(spell, world);
            }
            if (this.itemPredicate != null) {
                success &= this.itemPredicate.matches(destinationStack);
            }
            return success;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}