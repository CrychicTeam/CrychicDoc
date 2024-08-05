package org.violetmoon.zeta.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ManualTrigger extends SimpleCriterionTrigger<ManualTrigger.Instance> {

    final ResourceLocation id;

    public ManualTrigger(ResourceLocation id) {
        this.id = id;
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, instance -> true);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @NotNull
    protected ManualTrigger.Instance createInstance(@NotNull JsonObject jsonObject, @NotNull ContextAwarePredicate contextAwarePredicate, @NotNull DeserializationContext deserializationContext) {
        return new ManualTrigger.Instance(this.id, contextAwarePredicate);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        public Instance(ResourceLocation id, ContextAwarePredicate contextAwarePredicate) {
            super(id, contextAwarePredicate);
        }
    }
}