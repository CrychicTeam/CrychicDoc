package net.minecraft.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class ForceUnmount extends Behavior<LivingEntity> {

    public ForceUnmount() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        return livingEntity1.m_20159_();
    }

    @Override
    protected void start(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        livingEntity1.m_19877_();
    }
}