package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public abstract class NearestVisibleLivingEntitySensor extends Sensor<LivingEntity> {

    protected abstract boolean isMatchingEntity(LivingEntity var1, LivingEntity var2);

    protected abstract MemoryModuleType<LivingEntity> getMemory();

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(this.getMemory());
    }

    @Override
    protected void doTick(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        livingEntity1.getBrain().setMemory(this.getMemory(), this.getNearestEntity(livingEntity1));
    }

    private Optional<LivingEntity> getNearestEntity(LivingEntity livingEntity0) {
        return this.getVisibleEntities(livingEntity0).flatMap(p_186153_ -> p_186153_.findClosest(p_148301_ -> this.isMatchingEntity(livingEntity0, p_148301_)));
    }

    protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity livingEntity0) {
        return livingEntity0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}