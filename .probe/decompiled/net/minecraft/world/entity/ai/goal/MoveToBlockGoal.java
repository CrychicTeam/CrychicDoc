package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.LevelReader;

public abstract class MoveToBlockGoal extends Goal {

    private static final int GIVE_UP_TICKS = 1200;

    private static final int STAY_TICKS = 1200;

    private static final int INTERVAL_TICKS = 200;

    protected final PathfinderMob mob;

    public final double speedModifier;

    protected int nextStartTick;

    protected int tryTicks;

    private int maxStayTicks;

    protected BlockPos blockPos = BlockPos.ZERO;

    private boolean reachedTarget;

    private final int searchRange;

    private final int verticalSearchRange;

    protected int verticalSearchStart;

    public MoveToBlockGoal(PathfinderMob pathfinderMob0, double double1, int int2) {
        this(pathfinderMob0, double1, int2, 1);
    }

    public MoveToBlockGoal(PathfinderMob pathfinderMob0, double double1, int int2, int int3) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.searchRange = int2;
        this.verticalSearchStart = 0;
        this.verticalSearchRange = int3;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.nextStartTick > 0) {
            this.nextStartTick--;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            return this.findNearestBlock();
        }
    }

    protected int nextStartTick(PathfinderMob pathfinderMob0) {
        return m_186073_(200 + pathfinderMob0.m_217043_().nextInt(200));
    }

    @Override
    public boolean canContinueToUse() {
        return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && this.isValidTarget(this.mob.m_9236_(), this.blockPos);
    }

    @Override
    public void start() {
        this.moveMobToBlock();
        this.tryTicks = 0;
        this.maxStayTicks = this.mob.m_217043_().nextInt(this.mob.m_217043_().nextInt(1200) + 1200) + 1200;
    }

    protected void moveMobToBlock() {
        this.mob.m_21573_().moveTo((double) ((float) this.blockPos.m_123341_()) + 0.5, (double) (this.blockPos.m_123342_() + 1), (double) ((float) this.blockPos.m_123343_()) + 0.5, this.speedModifier);
    }

    public double acceptedDistance() {
        return 1.0;
    }

    protected BlockPos getMoveToTarget() {
        return this.blockPos.above();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        BlockPos $$0 = this.getMoveToTarget();
        if (!$$0.m_203195_(this.mob.m_20182_(), this.acceptedDistance())) {
            this.reachedTarget = false;
            this.tryTicks++;
            if (this.shouldRecalculatePath()) {
                this.mob.m_21573_().moveTo((double) ((float) $$0.m_123341_()) + 0.5, (double) $$0.m_123342_(), (double) ((float) $$0.m_123343_()) + 0.5, this.speedModifier);
            }
        } else {
            this.reachedTarget = true;
            this.tryTicks--;
        }
    }

    public boolean shouldRecalculatePath() {
        return this.tryTicks % 40 == 0;
    }

    protected boolean isReachedTarget() {
        return this.reachedTarget;
    }

    protected boolean findNearestBlock() {
        int $$0 = this.searchRange;
        int $$1 = this.verticalSearchRange;
        BlockPos $$2 = this.mob.m_20183_();
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos();
        for (int $$4 = this.verticalSearchStart; $$4 <= $$1; $$4 = $$4 > 0 ? -$$4 : 1 - $$4) {
            for (int $$5 = 0; $$5 < $$0; $$5++) {
                for (int $$6 = 0; $$6 <= $$5; $$6 = $$6 > 0 ? -$$6 : 1 - $$6) {
                    for (int $$7 = $$6 < $$5 && $$6 > -$$5 ? $$5 : 0; $$7 <= $$5; $$7 = $$7 > 0 ? -$$7 : 1 - $$7) {
                        $$3.setWithOffset($$2, $$6, $$4 - 1, $$7);
                        if (this.mob.m_21444_($$3) && this.isValidTarget(this.mob.m_9236_(), $$3)) {
                            this.blockPos = $$3;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected abstract boolean isValidTarget(LevelReader var1, BlockPos var2);
}