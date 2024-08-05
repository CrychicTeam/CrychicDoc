package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;

public class WardenEntitySensor extends NearestLivingEntitySensor<Warden> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
    }

    protected void doTick(ServerLevel serverLevel0, Warden warden1) {
        super.doTick(serverLevel0, warden1);
        getClosest(warden1, p_289409_ -> p_289409_.m_6095_() == EntityType.PLAYER).or(() -> getClosest(warden1, p_289408_ -> p_289408_.m_6095_() != EntityType.PLAYER)).ifPresentOrElse(p_217841_ -> warden1.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, p_217841_), () -> warden1.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE));
    }

    private static Optional<LivingEntity> getClosest(Warden warden0, Predicate<LivingEntity> predicateLivingEntity1) {
        return warden0.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).stream().flatMap(Collection::stream).filter(warden0::m_219385_).filter(predicateLivingEntity1).findFirst();
    }

    @Override
    protected int radiusXZ() {
        return 24;
    }

    @Override
    protected int radiusY() {
        return 24;
    }
}