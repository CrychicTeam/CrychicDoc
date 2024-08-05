package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.phys.AABB;

public class NearestLivingEntitySensor<T extends LivingEntity> extends Sensor<T> {

    @Override
    protected void doTick(ServerLevel serverLevel0, T t1) {
        AABB $$2 = t1.m_20191_().inflate((double) this.radiusXZ(), (double) this.radiusY(), (double) this.radiusXZ());
        List<LivingEntity> $$3 = serverLevel0.m_6443_(LivingEntity.class, $$2, p_26717_ -> p_26717_ != t1 && p_26717_.isAlive());
        $$3.sort(Comparator.comparingDouble(t1::m_20280_));
        Brain<?> $$4 = t1.getBrain();
        $$4.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, $$3);
        $$4.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(t1, $$3));
    }

    protected int radiusXZ() {
        return 16;
    }

    protected int radiusY() {
        return 16;
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}