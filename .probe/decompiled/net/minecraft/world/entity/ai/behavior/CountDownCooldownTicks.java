package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class CountDownCooldownTicks extends Behavior<LivingEntity> {

    private final MemoryModuleType<Integer> cooldownTicks;

    public CountDownCooldownTicks(MemoryModuleType<Integer> memoryModuleTypeInteger0) {
        super(ImmutableMap.of(memoryModuleTypeInteger0, MemoryStatus.VALUE_PRESENT));
        this.cooldownTicks = memoryModuleTypeInteger0;
    }

    private Optional<Integer> getCooldownTickMemory(LivingEntity livingEntity0) {
        return livingEntity0.getBrain().getMemory(this.cooldownTicks);
    }

    @Override
    protected boolean timedOut(long long0) {
        return false;
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        Optional<Integer> $$3 = this.getCooldownTickMemory(livingEntity1);
        return $$3.isPresent() && (Integer) $$3.get() > 0;
    }

    @Override
    protected void tick(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        Optional<Integer> $$3 = this.getCooldownTickMemory(livingEntity1);
        livingEntity1.getBrain().setMemory(this.cooldownTicks, (Integer) $$3.get() - 1);
    }

    @Override
    protected void stop(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        livingEntity1.getBrain().eraseMemory(this.cooldownTicks);
    }
}