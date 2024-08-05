package net.minecraft.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.warden.Warden;

public class Emerging<E extends Warden> extends Behavior<E> {

    public Emerging(int int0) {
        super(ImmutableMap.of(MemoryModuleType.IS_EMERGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), int0);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, E e1, long long2) {
        return true;
    }

    protected void start(ServerLevel serverLevel0, E e1, long long2) {
        e1.m_20124_(Pose.EMERGING);
        e1.m_5496_(SoundEvents.WARDEN_EMERGE, 5.0F, 1.0F);
    }

    protected void stop(ServerLevel serverLevel0, E e1, long long2) {
        if (e1.m_217003_(Pose.EMERGING)) {
            e1.m_20124_(Pose.STANDING);
        }
    }
}