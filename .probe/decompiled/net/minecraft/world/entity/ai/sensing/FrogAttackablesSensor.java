package net.minecraft.world.entity.ai.sensing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.frog.Frog;

public class FrogAttackablesSensor extends NearestVisibleLivingEntitySensor {

    public static final float TARGET_DETECTION_DISTANCE = 10.0F;

    @Override
    protected boolean isMatchingEntity(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        return !livingEntity0.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && Sensor.isEntityAttackable(livingEntity0, livingEntity1) && Frog.canEat(livingEntity1) && !this.isUnreachableAttackTarget(livingEntity0, livingEntity1) ? livingEntity1.m_19950_(livingEntity0, 10.0) : false;
    }

    private boolean isUnreachableAttackTarget(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        List<UUID> $$2 = (List<UUID>) livingEntity0.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS).orElseGet(ArrayList::new);
        return $$2.contains(livingEntity1.m_20148_());
    }

    @Override
    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}