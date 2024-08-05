package net.minecraft.world.entity.ai.behavior.warden;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.warden.Warden;

public class Digging<E extends Warden> extends Behavior<E> {

    public Digging(int int0) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), int0);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, E e1, long long2) {
        return e1.m_146911_() == null;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, E e1) {
        return e1.m_20096_() || e1.m_20069_() || e1.m_20077_();
    }

    protected void start(ServerLevel serverLevel0, E e1, long long2) {
        if (e1.m_20096_()) {
            e1.m_20124_(Pose.DIGGING);
            e1.m_5496_(SoundEvents.WARDEN_DIG, 5.0F, 1.0F);
        } else {
            e1.m_5496_(SoundEvents.WARDEN_AGITATED, 5.0F, 1.0F);
            this.stop(serverLevel0, e1, long2);
        }
    }

    protected void stop(ServerLevel serverLevel0, E e1, long long2) {
        if (e1.m_146911_() == null) {
            e1.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }
}