package net.minecraft.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenAi;

public class Sniffing<E extends Warden> extends Behavior<E> {

    private static final double ANGER_FROM_SNIFFING_MAX_DISTANCE_XZ = 6.0;

    private static final double ANGER_FROM_SNIFFING_MAX_DISTANCE_Y = 20.0;

    public Sniffing(int int0) {
        super(ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.REGISTERED, MemoryModuleType.DISTURBANCE_LOCATION, MemoryStatus.REGISTERED, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.REGISTERED), int0);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, E e1, long long2) {
        return true;
    }

    protected void start(ServerLevel serverLevel0, E e1, long long2) {
        e1.m_5496_(SoundEvents.WARDEN_SNIFF, 5.0F, 1.0F);
    }

    protected void stop(ServerLevel serverLevel0, E e1, long long2) {
        if (e1.m_217003_(Pose.SNIFFING)) {
            e1.m_20124_(Pose.STANDING);
        }
        e1.getBrain().eraseMemory(MemoryModuleType.IS_SNIFFING);
        e1.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).filter(e1::m_219385_).ifPresent(p_289391_ -> {
            if (e1.m_216992_(p_289391_, 6.0, 20.0)) {
                e1.increaseAngerAt(p_289391_);
            }
            if (!e1.getBrain().hasMemoryValue(MemoryModuleType.DISTURBANCE_LOCATION)) {
                WardenAi.setDisturbanceLocation(e1, p_289391_.m_20183_());
            }
        });
    }
}