package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class GolemSensor extends Sensor<LivingEntity> {

    private static final int GOLEM_SCAN_RATE = 200;

    private static final int MEMORY_TIME_TO_LIVE = 600;

    public GolemSensor() {
        this(200);
    }

    public GolemSensor(int int0) {
        super(int0);
    }

    @Override
    protected void doTick(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        checkForNearbyGolem(livingEntity1);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
    }

    public static void checkForNearbyGolem(LivingEntity livingEntity0) {
        Optional<List<LivingEntity>> $$1 = livingEntity0.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
        if ($$1.isPresent()) {
            boolean $$2 = ((List) $$1.get()).stream().anyMatch(p_289404_ -> p_289404_.m_6095_().equals(EntityType.IRON_GOLEM));
            if ($$2) {
                golemDetected(livingEntity0);
            }
        }
    }

    public static void golemDetected(LivingEntity livingEntity0) {
        livingEntity0.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 600L);
    }
}