package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class CrowAICircleCrops extends MoveToBlockGoal {

    private final EntityCrow crow;

    private int idleAtFlowerTime = 0;

    private boolean isAboveDestinationBear;

    float circlingTime = 0.0F;

    float circleDistance = 2.0F;

    float maxCirclingTime = 80.0F;

    float yLevel = 2.0F;

    boolean clockwise = false;

    boolean circlePhase = false;

    public CrowAICircleCrops(EntityCrow bird) {
        super(bird, 1.0, 32, 8);
        this.crow = bird;
    }

    @Override
    public void start() {
        super.start();
        this.circlePhase = true;
        this.clockwise = this.crow.m_217043_().nextBoolean();
        this.yLevel = (float) (1 + this.crow.m_217043_().nextInt(3));
        this.circleDistance = (float) (1 + this.crow.m_217043_().nextInt(3));
    }

    @Override
    public boolean canUse() {
        return !this.crow.m_6162_() && AMConfig.crowsStealCrops && (this.crow.m_5448_() == null || !this.crow.m_5448_().isAlive()) && !this.crow.m_21824_() && this.crow.fleePumpkinFlag == 0 && !this.crow.aiItemFlag && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.f_25602_ != null && AMConfig.crowsStealCrops && (this.crow.m_5448_() == null || !this.crow.m_5448_().isAlive()) && !this.crow.m_21824_() && !this.crow.aiItemFlag && this.crow.fleePumpkinFlag == 0 && super.canContinueToUse();
    }

    @Override
    public void stop() {
        this.idleAtFlowerTime = 0;
        this.circlingTime = 0.0F;
        this.f_25601_ = 0;
        this.f_25602_ = BlockPos.ZERO;
    }

    @Override
    public double acceptedDistance() {
        return 1.0;
    }

    @Override
    public void tick() {
        if (this.f_25602_ != null) {
            BlockPos blockpos = this.m_6669_();
            if (this.circlePhase) {
                this.f_25601_ = 0;
                BlockPos circlePos = this.getVultureCirclePos(blockpos);
                if (circlePos != null) {
                    this.crow.setFlying(true);
                    this.crow.m_21566_().setWantedPosition((double) circlePos.m_123341_() + 0.5, (double) circlePos.m_123342_() + 0.5, (double) circlePos.m_123343_() + 0.5, 0.7F);
                }
                this.circlingTime++;
                if (this.circlingTime > 200.0F) {
                    this.circlingTime = 0.0F;
                    this.circlePhase = false;
                }
            } else {
                super.tick();
                if (this.crow.m_20096_()) {
                    this.crow.setFlying(false);
                }
                if (!this.isWithinXZDist(blockpos, this.f_25598_.m_20182_(), this.acceptedDistance())) {
                    this.isAboveDestinationBear = false;
                    this.f_25601_++;
                    this.f_25598_.m_21573_().moveTo((double) ((float) blockpos.m_123341_()) + 0.5, (double) blockpos.m_123342_() - 0.5, (double) ((float) blockpos.m_123343_()) + 0.5, 1.0);
                } else {
                    this.isAboveDestinationBear = true;
                    this.f_25601_--;
                }
                if (this.isReachedTarget()) {
                    this.crow.m_7618_(EntityAnchorArgument.Anchor.EYES, new Vec3((double) this.f_25602_.m_123341_() + 0.5, (double) this.f_25602_.m_123342_(), (double) this.f_25602_.m_123343_() + 0.5));
                    if (this.idleAtFlowerTime >= 5) {
                        this.destroyCrop();
                        this.stop();
                    } else {
                        this.crow.peck();
                        this.idleAtFlowerTime++;
                    }
                }
            }
        }
    }

    public BlockPos getVultureCirclePos(BlockPos target) {
        float angle = 0.13962634F * (this.clockwise ? -this.circlingTime : this.circlingTime);
        double extraX = (double) (this.circleDistance * Mth.sin(angle));
        double extraZ = (double) (this.circleDistance * Mth.cos(angle));
        BlockPos pos = AMBlockPos.fromCoords((double) ((float) target.m_123341_() + 0.5F) + extraX, (double) ((float) (target.m_123342_() + 1) + this.yLevel), (double) ((float) target.m_123343_() + 0.5F) + extraZ);
        return this.crow.m_9236_().m_46859_(pos) ? pos : null;
    }

    private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
        return blockpos.m_123331_(AMBlockPos.fromCoords(positionVec.x(), (double) blockpos.m_123342_(), positionVec.z())) < distance * distance;
    }

    @Override
    protected boolean isReachedTarget() {
        return this.isAboveDestinationBear;
    }

    private void destroyCrop() {
        if (!this.canSeeBlock(this.f_25602_)) {
            this.stop();
            this.f_25601_ = 1200;
        } else {
            if (this.crow.m_9236_().getBlockState(this.f_25602_).m_60734_() instanceof CropBlock) {
                if (this.crow.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    CropBlock block = (CropBlock) this.crow.m_9236_().getBlockState(this.f_25602_).m_60734_();
                    int cropAge = block.getAge(this.crow.m_9236_().getBlockState(this.f_25602_));
                    if (cropAge > 0) {
                        this.crow.m_9236_().setBlockAndUpdate(this.f_25602_, block.getStateForAge(Math.max(0, cropAge - 1)));
                    } else {
                        this.crow.m_9236_().m_46961_(this.f_25602_, true);
                    }
                }
            } else if (this.crow.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                this.crow.m_9236_().m_46961_(this.f_25602_, true);
            }
            this.stop();
            this.f_25601_ = 1200;
        }
    }

    private boolean canSeeBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.crow.m_20185_(), this.crow.m_20188_(), this.crow.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.crow.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.crow));
        return result.getBlockPos().equals(destinationBlock);
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_204336_(AMTagRegistry.CROW_FOODBLOCKS);
    }
}