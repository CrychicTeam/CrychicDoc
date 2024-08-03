package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.frog.Frog;

public class Croak extends Behavior<Frog> {

    private static final int CROAK_TICKS = 60;

    private static final int TIME_OUT_DURATION = 100;

    private int croakCounter;

    public Croak() {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 100);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Frog frog1) {
        return frog1.m_20089_() == Pose.STANDING;
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Frog frog1, long long2) {
        return this.croakCounter < 60;
    }

    protected void start(ServerLevel serverLevel0, Frog frog1, long long2) {
        if (!frog1.m_20072_() && !frog1.m_20077_()) {
            frog1.m_20124_(Pose.CROAKING);
            this.croakCounter = 0;
        }
    }

    protected void stop(ServerLevel serverLevel0, Frog frog1, long long2) {
        frog1.m_20124_(Pose.STANDING);
    }

    protected void tick(ServerLevel serverLevel0, Frog frog1, long long2) {
        this.croakCounter++;
    }
}