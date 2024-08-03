package com.mna.advancements.triggers;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.advancements.predicates.CraftSpellPredicate;
import com.mna.api.affinity.Affinity;
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

public class AffinityTinkerTrigger extends SimpleCriterionTrigger<AffinityTinkerTrigger.Instance> {

    private static final ResourceLocation ID = RLoc.create("affinity_tinker");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public AffinityTinkerTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate pPredicate, DeserializationContext conditionsParser) {
        Affinity from = Affinity.UNKNOWN;
        Affinity to = Affinity.UNKNOWN;
        CraftSpellPredicate predicate = null;
        if (json.has("spell") && json.get("spell").isJsonObject()) {
            predicate = CraftSpellPredicate.fromJSON(json.get("spell").getAsJsonObject());
        }
        if (json.has("from")) {
            String val = json.get("from").getAsString();
            try {
                from = Affinity.valueOf(val);
            } catch (Throwable var10) {
                ManaAndArtifice.LOGGER.warn("failed to process FROM directive in affinity tinker advancement trigger.  Value: " + val);
            }
        }
        if (json.has("to")) {
            String val = json.get("to").getAsString();
            try {
                to = Affinity.valueOf(val);
            } catch (Throwable var9) {
                ManaAndArtifice.LOGGER.warn("failed to process TO directive in affinity tinker advancement trigger.  Value: " + val);
            }
        }
        return new AffinityTinkerTrigger.Instance(pPredicate, predicate, from, to);
    }

    public void trigger(ServerPlayer player, ISpellDefinition spell, Affinity from, Affinity to) {
        this.m_66234_(player, instance -> instance.matches(player.m_9236_(), spell, from, to));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final CraftSpellPredicate predicate;

        private final Affinity from;

        private final Affinity to;

        public Instance(ContextAwarePredicate player, CraftSpellPredicate predicate, Affinity from, Affinity to) {
            super(AffinityTinkerTrigger.ID, player);
            this.predicate = predicate;
            this.from = from;
            this.to = to;
        }

        public boolean matches(Level world, ISpellDefinition spell, Affinity from, Affinity to) {
            if (this.from != Affinity.UNKNOWN && this.from != from) {
                return false;
            } else if (this.to != Affinity.UNKNOWN && this.to != to) {
                return false;
            } else {
                return this.predicate != null ? this.predicate.test(spell, world) : true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            return super.serializeToJson(conditions);
        }
    }
}