package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class FollowOwnerGoal extends Goal {

    public static final int TELEPORT_WHEN_DISTANCE_IS = 12;

    private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;

    private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;

    private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;

    private final TamableAnimal tamable;

    private LivingEntity owner;

    private final LevelReader level;

    private final double speedModifier;

    private final PathNavigation navigation;

    private int timeToRecalcPath;

    private final float stopDistance;

    private final float startDistance;

    private float oldWaterCost;

    private final boolean canFly;

    public FollowOwnerGoal(TamableAnimal tamableAnimal0, double double1, float float2, float float3, boolean boolean4) {
        this.tamable = tamableAnimal0;
        this.level = tamableAnimal0.m_9236_();
        this.speedModifier = double1;
        this.navigation = tamableAnimal0.m_21573_();
        this.startDistance = float2;
        this.stopDistance = float3;
        this.canFly = boolean4;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(tamableAnimal0.m_21573_() instanceof GroundPathNavigation) && !(tamableAnimal0.m_21573_() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = this.tamable.m_269323_();
        if ($$0 == null) {
            return false;
        } else if ($$0.m_5833_()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else if (this.tamable.m_20280_($$0) < (double) (this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = $$0;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()) {
            return false;
        } else {
            return this.unableToMove() ? false : !(this.tamable.m_20280_(this.owner) <= (double) (this.stopDistance * this.stopDistance));
        }
    }

    private boolean unableToMove() {
        return this.tamable.isOrderedToSit() || this.tamable.m_20159_() || this.tamable.m_21523_();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tamable.m_21439_(BlockPathTypes.WATER);
        this.tamable.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.tamable.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.tamable.m_21563_().setLookAt(this.owner, 10.0F, (float) this.tamable.m_8132_());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.m_183277_(10);
            if (this.tamable.m_20280_(this.owner) >= 144.0) {
                this.teleportToOwner();
            } else {
                this.navigation.moveTo(this.owner, this.speedModifier);
            }
        }
    }

    private void teleportToOwner() {
        BlockPos $$0 = this.owner.m_20183_();
        for (int $$1 = 0; $$1 < 10; $$1++) {
            int $$2 = this.randomIntInclusive(-3, 3);
            int $$3 = this.randomIntInclusive(-1, 1);
            int $$4 = this.randomIntInclusive(-3, 3);
            boolean $$5 = this.maybeTeleportTo($$0.m_123341_() + $$2, $$0.m_123342_() + $$3, $$0.m_123343_() + $$4);
            if ($$5) {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(int int0, int int1, int int2) {
        if (Math.abs((double) int0 - this.owner.m_20185_()) < 2.0 && Math.abs((double) int2 - this.owner.m_20189_()) < 2.0) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(int0, int1, int2))) {
            return false;
        } else {
            this.tamable.m_7678_((double) int0 + 0.5, (double) int1, (double) int2 + 0.5, this.tamable.m_146908_(), this.tamable.m_146909_());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos blockPos0) {
        BlockPathTypes $$1 = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, blockPos0.mutable());
        if ($$1 != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState $$2 = this.level.m_8055_(blockPos0.below());
            if (!this.canFly && $$2.m_60734_() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos $$3 = blockPos0.subtract(this.tamable.m_20183_());
                return this.level.m_45756_(this.tamable, this.tamable.m_20191_().move($$3));
            }
        }
    }

    private int randomIntInclusive(int int0, int int1) {
        return this.tamable.m_217043_().nextInt(int1 - int0 + 1) + int0;
    }
}