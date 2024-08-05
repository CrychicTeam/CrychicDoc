package net.minecraft.world.entity.ai.sensing;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class AxolotlAttackablesSensor extends NearestVisibleLivingEntitySensor {

    public static final float TARGET_DETECTION_DISTANCE = 8.0F;

    @Override
    protected boolean isMatchingEntity(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return this.isClose(livingEntity0, livingEntity1) && livingEntity1.m_20072_() && (this.isHostileTarget(livingEntity1) || this.isHuntTarget(livingEntity0, livingEntity1)) && Sensor.isEntityAttackable(livingEntity0, livingEntity1);
    }

    private boolean isHuntTarget(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return !livingEntity0.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && livingEntity1.m_6095_().is(EntityTypeTags.AXOLOTL_HUNT_TARGETS);
    }

    private boolean isHostileTarget(LivingEntity livingEntity0) {
        return livingEntity0.m_6095_().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
    }

    private boolean isClose(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return livingEntity1.m_20280_(livingEntity0) <= 64.0;
    }

    @Override
    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}