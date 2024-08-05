package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class RaccoonAIWash extends Goal {

    private final EntityRaccoon raccoon;

    private BlockPos waterPos;

    private BlockPos targetPos;

    private int washTime = 0;

    private int executionChance = 30;

    private Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };

    public RaccoonAIWash(EntityRaccoon creature) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.raccoon = creature;
    }

    @Override
    public boolean canUse() {
        if (this.raccoon.m_21205_().isEmpty()) {
            return false;
        } else {
            if (this.raccoon.lookForWaterBeforeEatingTimer > 0) {
                this.waterPos = this.generateTarget();
                if (this.waterPos != null) {
                    this.targetPos = this.getLandPos(this.waterPos);
                    return this.targetPos != null;
                }
            }
            return false;
        }
    }

    @Override
    public void start() {
        this.raccoon.lookForWaterBeforeEatingTimer = 1800;
    }

    @Override
    public void stop() {
        this.targetPos = null;
        this.waterPos = null;
        this.washTime = 0;
        this.raccoon.setWashPos(null);
        this.raccoon.setWashing(false);
        this.raccoon.lookForWaterBeforeEatingTimer = 100;
        this.raccoon.m_21573_().stop();
    }

    @Override
    public void tick() {
        if (this.targetPos != null && this.waterPos != null) {
            double dist = this.raccoon.m_20238_(Vec3.atCenterOf(this.waterPos));
            if (dist > 2.0 && this.raccoon.isWashing()) {
                this.raccoon.setWashing(false);
            }
            if (dist <= 1.0) {
                double d0 = (double) this.waterPos.m_123341_() + 0.5 - this.raccoon.m_20185_();
                double d2 = (double) this.waterPos.m_123343_() + 0.5 - this.raccoon.m_20189_();
                float yaw = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                this.raccoon.m_146922_(yaw);
                this.raccoon.f_20885_ = yaw;
                this.raccoon.f_20883_ = yaw;
                this.raccoon.m_21573_().stop();
                this.raccoon.setWashing(true);
                this.raccoon.setWashPos(this.waterPos);
                this.raccoon.lookForWaterBeforeEatingTimer = 0;
                if (this.washTime % 10 == 0) {
                    this.raccoon.m_146850_(GameEvent.BLOCK_ACTIVATE);
                    this.raccoon.m_5496_(SoundEvents.GENERIC_SWIM, 0.7F, 0.5F + this.raccoon.m_217043_().nextFloat());
                }
                this.washTime++;
                if (this.washTime > 100 || this.raccoon.isHoldingSugar() && this.washTime > 20) {
                    this.stop();
                    if (!this.raccoon.isHoldingSugar()) {
                        this.raccoon.onEatItem();
                    }
                    this.raccoon.postWashItem(this.raccoon.m_21205_());
                    if (this.raccoon.m_21205_().hasCraftingRemainingItem()) {
                        this.raccoon.m_19983_(this.raccoon.m_21205_().getCraftingRemainingItem());
                    }
                    this.raccoon.m_21205_().shrink(1);
                }
            } else {
                this.raccoon.m_21573_().moveTo((double) this.waterPos.m_123341_(), (double) this.waterPos.m_123342_(), (double) this.waterPos.m_123343_(), 1.2);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.raccoon.m_21205_().isEmpty() ? false : this.targetPos != null && !this.raccoon.m_20069_() && EntityRaccoon.isRaccoonFood(this.raccoon.m_21205_());
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        RandomSource random = this.raccoon.m_217043_();
        int range = 32;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.raccoon.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
            while (this.raccoon.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.below();
            }
            if (this.isConnectedToLand(blockpos1)) {
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }

    public boolean isConnectedToLand(BlockPos pos) {
        if (this.raccoon.m_9236_().getFluidState(pos).is(FluidTags.WATER)) {
            for (Direction dir : this.HORIZONTALS) {
                BlockPos offsetPos = pos.relative(dir);
                if (this.raccoon.m_9236_().getFluidState(offsetPos).isEmpty() && this.raccoon.m_9236_().getFluidState(offsetPos.above()).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public BlockPos getLandPos(BlockPos pos) {
        if (this.raccoon.m_9236_().getFluidState(pos).is(FluidTags.WATER)) {
            for (Direction dir : this.HORIZONTALS) {
                BlockPos offsetPos = pos.relative(dir);
                if (this.raccoon.m_9236_().getFluidState(offsetPos).isEmpty() && this.raccoon.m_9236_().getFluidState(offsetPos.above()).isEmpty()) {
                    return offsetPos;
                }
            }
        }
        return null;
    }
}