package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

public class AdultSensor extends Sensor<AgeableMob> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    protected void doTick(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        ageableMob1.m_6274_().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(p_186145_ -> this.setNearestVisibleAdult(ageableMob1, p_186145_));
    }

    private void setNearestVisibleAdult(AgeableMob ageableMob0, NearestVisibleLivingEntities nearestVisibleLivingEntities1) {
        Optional<AgeableMob> $$2 = nearestVisibleLivingEntities1.findClosest(p_289403_ -> p_289403_.m_6095_() == ageableMob0.m_6095_() && !p_289403_.isBaby()).map(AgeableMob.class::cast);
        ageableMob0.m_6274_().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, $$2);
    }
}