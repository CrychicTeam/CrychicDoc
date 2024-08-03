package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.advancements.predicates.CraftSpellPredicate;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.tools.RLoc;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CraftSpellTrigger extends SimpleCriterionTrigger<CraftSpellTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("craft_spell");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public CraftSpellTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        CraftSpellPredicate predicate = null;
        if (json.has("spell") && json.get("spell").isJsonObject()) {
            predicate = CraftSpellPredicate.fromJSON(json.get("spell").getAsJsonObject());
        }
        return new CraftSpellTrigger.Instance(entityPredicate, predicate);
    }

    public void trigger(ServerPlayer player, ISpellDefinition spell) {
        this.m_66234_(player, instance -> instance.test(spell, player.m_9236_()));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final CraftSpellPredicate predicate;

        public Instance(ContextAwarePredicate player, CraftSpellPredicate predicate) {
            super(CraftSpellTrigger.ID, player);
            this.predicate = predicate;
        }

        public boolean test(ISpellDefinition spell, Level world) {
            return this.predicate != null ? this.predicate.test(spell, world) : true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}