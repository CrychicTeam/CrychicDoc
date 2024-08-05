package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class TremorzillaFollowOwnerGoal extends Goal {

    public static final int TELEPORT_WHEN_DISTANCE_IS = 32;

    private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;

    private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;

    private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;

    private final TremorzillaEntity tremorzilla;

    private LivingEntity owner;

    private final LevelReader level;

    private final double speedModifier;

    private int timeToRecalcPath;

    private final float stopDistance;

    private final float startDistance;

    public TremorzillaFollowOwnerGoal(TremorzillaEntity tremorzilla, double speed, float minDist, float maxDist) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.tremorzilla = tremorzilla;
        this.level = tremorzilla.m_9236_();
        this.speedModifier = speed;
        this.startDistance = minDist;
        this.stopDistance = maxDist;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.tremorzilla.m_269323_();
        if (this.tremorzilla.getCommand() != 2) {
            return false;
        } else if (livingentity == null) {
            return false;
        } else if (livingentity.m_5833_()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else if (this.tremorzilla.m_20280_(livingentity) < (double) (this.startDistance * this.startDistance)) {
            return false;
        } else if (this.isInCombat()) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.tremorzilla.getCommand() != 2) {
            return false;
        } else if (this.tremorzilla.m_21573_().isDone()) {
            return false;
        } else if (this.unableToMove()) {
            return false;
        } else {
            return this.isInCombat() ? false : !(this.tremorzilla.m_20280_(this.owner) <= (double) (this.stopDistance * this.stopDistance));
        }
    }

    private boolean unableToMove() {
        return this.tremorzilla.m_21827_() || this.tremorzilla.m_20159_() || this.tremorzilla.m_21523_();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
        this.tremorzilla.m_21573_().stop();
    }

    @Override
    public void tick() {
        this.tremorzilla.m_21563_().setLookAt(this.owner, 10.0F, (float) this.tremorzilla.m_8132_());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.m_183277_(10);
            if (this.tremorzilla.m_20280_(this.owner) >= 1024.0 && AlexsCaves.COMMON_CONFIG.devastatingTremorzillaBeam.get()) {
                this.teleportToOwner();
            } else {
                this.tremorzilla.m_21573_().moveTo(this.owner, this.speedModifier);
            }
        }
    }

    private void teleportToOwner() {
        BlockPos blockpos = this.owner.m_20183_();
        for (int i = 0; i < 10; i++) {
            int j = this.randomIntInclusive(-10, 10);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-10, 10);
            boolean flag = this.maybeTeleportTo(blockpos.m_123341_() + j, blockpos.m_123342_() + k, blockpos.m_123343_() + l);
            if (flag) {
                return;
            }
        }
    }

    private boolean isInCombat() {
        Entity owner = this.tremorzilla.m_269323_();
        return owner == null ? false : this.tremorzilla.m_20270_(owner) < 50.0F && this.tremorzilla.m_5448_() != null && this.tremorzilla.m_5448_().isAlive();
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (Math.abs((double) x - this.owner.m_20185_()) < 6.0 && Math.abs((double) z - this.owner.m_20189_()) < 6.0) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.tremorzilla.m_7678_((double) x + 0.5, (double) y, (double) z + 0.5, this.tremorzilla.m_146908_(), this.tremorzilla.m_146909_());
            this.tremorzilla.m_21573_().stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos blockPos) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, blockPos.mutable());
        if (blockpathtypes != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockPos blockpos = blockPos.subtract(this.tremorzilla.m_20183_());
            return this.level.m_45756_(this.tremorzilla, this.tremorzilla.m_20191_().move(blockpos));
        }
    }

    private int randomIntInclusive(int i, int j) {
        return this.tremorzilla.m_217043_().nextInt(j - i + 1) + i;
    }
}