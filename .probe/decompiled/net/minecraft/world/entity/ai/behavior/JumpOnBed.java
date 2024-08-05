package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class JumpOnBed extends Behavior<Mob> {

    private static final int MAX_TIME_TO_REACH_BED = 100;

    private static final int MIN_JUMPS = 3;

    private static final int MAX_JUMPS = 6;

    private static final int COOLDOWN_BETWEEN_JUMPS = 5;

    private final float speedModifier;

    @Nullable
    private BlockPos targetBed;

    private int remainingTimeToReachBed;

    private int remainingJumps;

    private int remainingCooldownUntilNextJump;

    public JumpOnBed(float float0) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float0;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Mob mob1) {
        return mob1.m_6162_() && this.nearBed(serverLevel0, mob1);
    }

    protected void start(ServerLevel serverLevel0, Mob mob1, long long2) {
        super.start(serverLevel0, mob1, long2);
        this.getNearestBed(mob1).ifPresent(p_264901_ -> {
            this.targetBed = p_264901_;
            this.remainingTimeToReachBed = 100;
            this.remainingJumps = 3 + serverLevel0.f_46441_.nextInt(4);
            this.remainingCooldownUntilNextJump = 0;
            this.startWalkingTowardsBed(mob1, p_264901_);
        });
    }

    protected void stop(ServerLevel serverLevel0, Mob mob1, long long2) {
        super.stop(serverLevel0, mob1, long2);
        this.targetBed = null;
        this.remainingTimeToReachBed = 0;
        this.remainingJumps = 0;
        this.remainingCooldownUntilNextJump = 0;
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Mob mob1, long long2) {
        return mob1.m_6162_() && this.targetBed != null && this.isBed(serverLevel0, this.targetBed) && !this.tiredOfWalking(serverLevel0, mob1) && !this.tiredOfJumping(serverLevel0, mob1);
    }

    @Override
    protected boolean timedOut(long long0) {
        return false;
    }

    protected void tick(ServerLevel serverLevel0, Mob mob1, long long2) {
        if (!this.onOrOverBed(serverLevel0, mob1)) {
            this.remainingTimeToReachBed--;
        } else if (this.remainingCooldownUntilNextJump > 0) {
            this.remainingCooldownUntilNextJump--;
        } else {
            if (this.onBedSurface(serverLevel0, mob1)) {
                mob1.getJumpControl().jump();
                this.remainingJumps--;
                this.remainingCooldownUntilNextJump = 5;
            }
        }
    }

    private void startWalkingTowardsBed(Mob mob0, BlockPos blockPos1) {
        mob0.m_6274_().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(blockPos1, this.speedModifier, 0));
    }

    private boolean nearBed(ServerLevel serverLevel0, Mob mob1) {
        return this.onOrOverBed(serverLevel0, mob1) || this.getNearestBed(mob1).isPresent();
    }

    private boolean onOrOverBed(ServerLevel serverLevel0, Mob mob1) {
        BlockPos $$2 = mob1.m_20183_();
        BlockPos $$3 = $$2.below();
        return this.isBed(serverLevel0, $$2) || this.isBed(serverLevel0, $$3);
    }

    private boolean onBedSurface(ServerLevel serverLevel0, Mob mob1) {
        return this.isBed(serverLevel0, mob1.m_20183_());
    }

    private boolean isBed(ServerLevel serverLevel0, BlockPos blockPos1) {
        return serverLevel0.m_8055_(blockPos1).m_204336_(BlockTags.BEDS);
    }

    private Optional<BlockPos> getNearestBed(Mob mob0) {
        return mob0.m_6274_().getMemory(MemoryModuleType.NEAREST_BED);
    }

    private boolean tiredOfWalking(ServerLevel serverLevel0, Mob mob1) {
        return !this.onOrOverBed(serverLevel0, mob1) && this.remainingTimeToReachBed <= 0;
    }

    private boolean tiredOfJumping(ServerLevel serverLevel0, Mob mob1) {
        return this.onOrOverBed(serverLevel0, mob1) && this.remainingJumps <= 0;
    }
}