package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class LongJumpMidJump extends Behavior<Mob> {

    public static final int TIME_OUT_DURATION = 100;

    private final UniformInt timeBetweenLongJumps;

    private final SoundEvent landingSound;

    public LongJumpMidJump(UniformInt uniformInt0, SoundEvent soundEvent1) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_PRESENT), 100);
        this.timeBetweenLongJumps = uniformInt0;
        this.landingSound = soundEvent1;
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Mob mob1, long long2) {
        return !mob1.m_20096_();
    }

    protected void start(ServerLevel serverLevel0, Mob mob1, long long2) {
        mob1.m_147244_(true);
        mob1.m_20124_(Pose.LONG_JUMPING);
    }

    protected void stop(ServerLevel serverLevel0, Mob mob1, long long2) {
        if (mob1.m_20096_()) {
            mob1.m_20256_(mob1.m_20184_().multiply(0.1F, 1.0, 0.1F));
            serverLevel0.m_6269_(null, mob1, this.landingSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
        }
        mob1.m_147244_(false);
        mob1.m_20124_(Pose.STANDING);
        mob1.m_6274_().eraseMemory(MemoryModuleType.LONG_JUMP_MID_JUMP);
        mob1.m_6274_().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, this.timeBetweenLongJumps.sample(serverLevel0.f_46441_));
    }
}