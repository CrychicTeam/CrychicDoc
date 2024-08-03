package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.VesperEntity;
import java.util.EnumSet;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class VesperFlyAndHangGoal extends Goal {

    private VesperEntity entity;

    private boolean wantsToHang = false;

    private double x;

    private double y;

    private double z;

    private int hangCheckIn = 0;

    public VesperFlyAndHangGoal(VesperEntity entity) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (this.entity.m_20160_() || this.entity.m_5448_() != null && this.entity.m_5448_().isAlive() || this.entity.m_20159_()) {
            return false;
        } else if (this.entity.isHanging() || this.entity.groundedFor > 0) {
            return false;
        } else if (!this.entity.isFlying() && this.entity.m_217043_().nextInt(70) != 0) {
            return false;
        } else {
            this.wantsToHang = this.entity.timeFlying > 300;
            Vec3 target = this.getPosition();
            if (target == null) {
                return false;
            } else {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
                return true;
            }
        }
    }

    private Vec3 getPosition() {
        if (this.wantsToHang) {
            Vec3 hangPos = this.findHangFromPos();
            if (hangPos != null) {
                return hangPos;
            }
        }
        return this.findFlightPos();
    }

    @Override
    public void start() {
        this.entity.setFlying(true);
        this.entity.setHanging(false);
        this.hangCheckIn = 0;
        this.entity.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
    }

    @Override
    public void tick() {
        if (this.wantsToHang && this.hangCheckIn-- < 0) {
            this.hangCheckIn = 5 + this.entity.m_217043_().nextInt(5);
            if (!this.entity.isHanging() && this.entity.canHangFrom(this.entity.posAbove(), this.entity.m_9236_().getBlockState(this.entity.posAbove()))) {
                this.entity.setHanging(true);
                this.entity.setFlying(false);
            }
        }
        if (this.entity.isFlying() && this.entity.m_20096_() && this.entity.timeFlying > 40) {
            this.entity.setFlying(false);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.wantsToHang ? !this.entity.m_21573_().isDone() && !this.entity.isHanging() && this.entity.groundedFor <= 0 : this.entity.isFlying() && !this.entity.m_21573_().isDone() && this.entity.groundedFor <= 0;
    }

    @Override
    public void stop() {
        if (this.wantsToHang) {
            this.entity.m_21573_().stop();
        }
        this.wantsToHang = false;
    }

    private Vec3 findFlightPos() {
        int range = 13;
        Vec3 heightAdjusted = this.entity.m_20182_().add((double) (this.entity.m_217043_().nextInt(range * 2) - range), 0.0, (double) (this.entity.m_217043_().nextInt(range * 2) - range));
        if (this.entity.m_9236_().m_45527_(BlockPos.containing(heightAdjusted))) {
            Vec3 ground = this.groundPosition(heightAdjusted);
            heightAdjusted = new Vec3(heightAdjusted.x, ground.y + 4.0 + (double) this.entity.m_217043_().nextInt(3), heightAdjusted.z);
        } else {
            Vec3 ground = this.groundPosition(heightAdjusted);
            BlockPos ceiling = BlockPos.containing(ground).above(2);
            while (ceiling.m_123342_() < this.entity.m_9236_().m_151558_() && !this.entity.m_9236_().getBlockState(ceiling).m_280296_()) {
                ceiling = ceiling.above();
            }
            float randCeilVal = 0.3F + this.entity.m_217043_().nextFloat() * 0.5F;
            heightAdjusted = new Vec3(heightAdjusted.x, ground.y + ((double) ceiling.m_123342_() - ground.y) * (double) randCeilVal, heightAdjusted.z);
        }
        BlockHitResult result = this.entity.m_9236_().m_45547_(new ClipContext(this.entity.m_146892_(), heightAdjusted, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.entity));
        return result.getType() == HitResult.Type.MISS ? heightAdjusted : result.m_82450_();
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.entity.m_20183_();
        while (position.m_123342_() > this.entity.m_9236_().m_141937_() && this.entity.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.entity.m_9236_().getFluidState(position).isEmpty() || this.entity.m_9236_().getBlockState(position).m_60713_(Blocks.VINE) || position.m_123342_() <= this.entity.m_9236_().m_141937_();
    }

    public Vec3 groundPosition(Vec3 airPosition) {
        BlockPos.MutableBlockPos ground = new BlockPos.MutableBlockPos();
        ground.set(airPosition.x, airPosition.y, airPosition.z);
        boolean flag;
        for (flag = false; ground.m_123342_() < this.entity.m_9236_().m_151558_() && !this.entity.m_9236_().getBlockState(ground).m_280296_() && this.entity.m_9236_().getFluidState(ground).isEmpty(); flag = true) {
            ground.move(0, 1, 0);
        }
        ground.move(0, -1, 0);
        while (ground.m_123342_() > this.entity.m_9236_().m_141937_() && !this.entity.m_9236_().getBlockState(ground).m_280296_() && this.entity.m_9236_().getFluidState(ground).isEmpty()) {
            ground.move(0, -1, 0);
        }
        return Vec3.atCenterOf(flag ? ground.m_7494_() : ground.m_7495_());
    }

    public Vec3 findHangFromPos() {
        BlockPos blockpos = null;
        Random random = new Random();
        int range = 14;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.entity.m_20183_().offset(random.nextInt(range) - range / 2, 0, random.nextInt(range) - range / 2);
            if (this.entity.m_9236_().m_46859_(blockpos1) && this.entity.m_9236_().isLoaded(blockpos1)) {
                while (this.entity.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() < this.entity.m_9236_().m_151558_()) {
                    blockpos1 = blockpos1.above();
                }
                if ((double) blockpos1.m_123342_() > this.entity.m_20186_() - 1.0 && this.entity.canHangFrom(blockpos1, this.entity.m_9236_().getBlockState(blockpos1)) && this.hasLineOfToPos(blockpos1)) {
                    blockpos = blockpos1;
                }
            }
        }
        return blockpos == null ? null : Vec3.atCenterOf(blockpos);
    }

    public boolean hasLineOfToPos(BlockPos in) {
        HitResult raytraceresult = this.entity.m_9236_().m_45547_(new ClipContext(this.entity.m_20299_(1.0F), new Vec3((double) in.m_123341_() + 0.5, (double) in.m_123342_() + 0.5, (double) in.m_123343_() + 0.5), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.entity));
        if (!(raytraceresult instanceof BlockHitResult blockRayTraceResult)) {
            return true;
        } else {
            BlockPos pos = blockRayTraceResult.getBlockPos();
            return pos.equals(in) || this.entity.m_9236_().m_46859_(pos);
        }
    }
}