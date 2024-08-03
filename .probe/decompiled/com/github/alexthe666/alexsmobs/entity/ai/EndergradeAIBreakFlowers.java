package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityEndergrade;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class EndergradeAIBreakFlowers extends MoveToBlockGoal {

    private final EntityEndergrade endergrade;

    private int idleAtFlowerTime = 0;

    private boolean isAboveDestinationBear;

    public EndergradeAIBreakFlowers(EntityEndergrade bird) {
        super(bird, 1.0, 32, 8);
        this.endergrade = bird;
    }

    @Override
    public boolean canUse() {
        return !this.endergrade.m_6162_() && !this.endergrade.hasItemTarget && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.endergrade.hasItemTarget && super.canContinueToUse();
    }

    @Override
    public void stop() {
        this.idleAtFlowerTime = 0;
        this.endergrade.stopWandering = false;
    }

    @Override
    public double acceptedDistance() {
        return 2.0;
    }

    @Override
    public void tick() {
        super.tick();
        this.endergrade.stopWandering = true;
        BlockPos blockpos = this.m_6669_();
        if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.acceptedDistance())) {
            this.isAboveDestinationBear = false;
            this.f_25601_++;
            this.f_25598_.m_21566_().setWantedPosition((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_() - 0.5, (double) ((float) blockpos.m_123343_()) + 0.5, 1.0);
        } else {
            this.isAboveDestinationBear = true;
            this.f_25601_--;
        }
        if (this.isReachedTarget() && Math.abs(this.endergrade.m_20186_() - (double) this.f_25602_.m_123342_()) <= 2.0) {
            this.endergrade.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
            if (this.idleAtFlowerTime >= 20) {
                this.endergrade.bite();
                this.pollinate();
                this.stop();
            } else {
                this.idleAtFlowerTime++;
            }
        }
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(new BlockPos((int) positionVec.x(), blockpos.m_123342_(), (int) positionVec.z())) < distance * distance;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.isAboveDestinationBear;
    }

    private void pollinate() {
        this.endergrade.m_9236_().m_46961_(this.f_25602_, true);
        this.stop();
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_60734_() == Blocks.CHORUS_FLOWER;
    }
}