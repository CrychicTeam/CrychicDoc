package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class UsedEnderEyeTrigger extends SimpleCriterionTrigger<UsedEnderEyeTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("used_ender_eye");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public UsedEnderEyeTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        MinMaxBounds.Doubles $$3 = MinMaxBounds.Doubles.fromJson(jsonObject0.get("distance"));
        return new UsedEnderEyeTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, BlockPos blockPos1) {
        double $$2 = serverPlayer0.m_20185_() - (double) blockPos1.m_123341_();
        double $$3 = serverPlayer0.m_20189_() - (double) blockPos1.m_123343_();
        double $$4 = $$2 * $$2 + $$3 * $$3;
        this.m_66234_(serverPlayer0, p_73934_ -> p_73934_.matches($$4));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final MinMaxBounds.Doubles level;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, MinMaxBounds.Doubles minMaxBoundsDoubles1) {
            super(UsedEnderEyeTrigger.ID, contextAwarePredicate0);
            this.level = minMaxBoundsDoubles1;
        }

        public boolean matches(double double0) {
            return this.level.matchesSqr(double0);
        }
    }
}