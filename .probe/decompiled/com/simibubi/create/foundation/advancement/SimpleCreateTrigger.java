package com.simibubi.create.foundation.advancement;

import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SimpleCreateTrigger extends CriterionTriggerBase<SimpleCreateTrigger.Instance> {

    public SimpleCreateTrigger(String id) {
        super(id);
    }

    public SimpleCreateTrigger.Instance createInstance(JsonObject json, DeserializationContext context) {
        return new SimpleCreateTrigger.Instance(this.m_7295_());
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    public SimpleCreateTrigger.Instance instance() {
        return new SimpleCreateTrigger.Instance(this.m_7295_());
    }

    public static class Instance extends CriterionTriggerBase.Instance {

        public Instance(ResourceLocation idIn) {
            super(idIn, ContextAwarePredicate.ANY);
        }

        @Override
        protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
            return true;
        }
    }
}