package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SubterranodonFlightGoal extends Goal {

    private SubterranodonEntity entity;

    private double x;

    private double y;

    private double z;

    private boolean isFlying;

    public SubterranodonFlightGoal(SubterranodonEntity subterranodon) {
        this.entity = subterranodon;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.entity.m_20160_() && (this.entity.m_5448_() == null || !this.entity.m_5448_().isAlive()) && !this.entity.m_20159_() && !this.entity.isDancing() && !this.entity.m_21825_()) {
            boolean flag = false;
            if (this.entity.isPackFollower() && ((SubterranodonEntity) this.entity.getPackLeader()).isFlying()) {
                this.isFlying = true;
                flag = true;
            }
            if (!flag) {
                if (this.entity.m_217043_().nextInt(70) != 0 && !this.entity.isFlying()) {
                    return false;
                }
                if (this.entity.m_20096_()) {
                    this.isFlying = this.entity.m_217043_().nextInt(3) == 0;
                } else {
                    this.isFlying = this.entity.m_217043_().nextInt(8) > 0 && this.entity.timeFlying < 200;
                }
            }
            Vec3 target = this.getPosition();
            if (target == null) {
                return false;
            } else {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.isFlying) {
            if (this.entity.resetFlightAIFlag || this.entity.f_19862_ && this.entity.timeFlying % 10 == 0 || this.entity.m_20275_(this.x, this.y, this.z) < 9.0) {
                Vec3 target = this.getPosition();
                if (target != null) {
                    this.x = target.x;
                    this.y = target.y;
                    this.z = target.z;
                }
                this.entity.resetFlightAIFlag = false;
            }
            this.entity.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
        } else {
            if (this.entity.isFlying() || ((SubterranodonEntity) this.entity.getPackLeader()).landingFlag) {
                this.entity.landingFlag = true;
            }
            this.entity.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
        }
        if (!this.isFlying && this.entity.isFlying() && this.entity.m_20096_()) {
            this.entity.setFlying(false);
        }
        if (this.entity.isFlying() && this.entity.m_20096_() && this.entity.timeFlying > 40) {
            this.entity.setFlying(false);
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        if (this.isOverWaterOrVoid()) {
            this.isFlying = true;
        }
        Vec3 vec3 = this.findOrFollowFlightPos();
        if (!this.isFlying) {
            return this.entity.isFlying() ? this.groundPosition(vec3).add(0.0, -1.0, 0.0) : LandRandomPos.getPos(this.entity, 10, 7);
        } else if ((this.entity.timeFlying < 2000 || this.isLeaderStillGoing() || this.isOverWaterOrVoid()) && !this.entity.m_21827_()) {
            return vec3;
        } else {
            return this.entity.m_21536_() && !this.entity.f_19862_ ? Vec3.atCenterOf(this.entity.m_21534_()) : this.groundPosition(vec3);
        }
    }

    private Vec3 findFlightPos() {
        Vec3 targetVec;
        if (this.entity.m_21536_() && this.entity.m_21534_() != null) {
            float maxRot = 360.0F;
            Vec3 center = Vec3.atCenterOf(this.entity.m_21534_());
            float xRotOffset = (float) Math.toRadians((double) (this.entity.m_217043_().nextFloat() * (maxRot - maxRot / 2.0F) * 0.5F));
            float yRotOffset = (float) Math.toRadians((double) (this.entity.m_217043_().nextFloat() * maxRot - maxRot / 2.0F));
            Vec3 distVec = new Vec3(0.0, 0.0, (double) (15 + this.entity.m_217043_().nextInt(15))).xRot(xRotOffset).yRot(yRotOffset);
            targetVec = center.add(distVec);
        } else {
            float maxRot = this.entity.f_19862_ ? 360.0F : 90.0F;
            float xRotOffset = (float) Math.toRadians((double) (this.entity.m_217043_().nextFloat() * (maxRot - maxRot / 2.0F) * 0.5F));
            float yRotOffset = (float) Math.toRadians((double) (this.entity.m_217043_().nextFloat() * maxRot - maxRot / 2.0F));
            Vec3 lookVec = this.entity.m_20154_().scale((double) (15 + this.entity.m_217043_().nextInt(15))).xRot(xRotOffset).yRot(yRotOffset);
            targetVec = this.entity.m_20182_().add(lookVec);
        }
        if (!this.entity.m_9236_().isLoaded(BlockPos.containing(targetVec))) {
            return this.entity.m_20182_();
        } else {
            Vec3 var8;
            if (this.entity.m_9236_().m_45527_(BlockPos.containing(targetVec))) {
                Vec3 ground = this.groundPosition(targetVec);
                var8 = new Vec3(targetVec.x, ground.y + 5.0 + (double) this.entity.m_217043_().nextInt(10), targetVec.z);
            } else {
                Vec3 ground = this.groundPosition(targetVec);
                BlockPos ceiling = BlockPos.containing(ground).above(2);
                while (ceiling.m_123342_() < this.entity.m_9236_().m_151558_() && !this.entity.m_9236_().getBlockState(ceiling).m_280296_()) {
                    ceiling = ceiling.above();
                }
                float randCeilVal = 0.5F + this.entity.m_217043_().nextFloat() * 0.2F;
                var8 = new Vec3(targetVec.x, ground.y + ((double) ceiling.m_123342_() - ground.y) * (double) randCeilVal, targetVec.z);
            }
            BlockHitResult result = this.entity.m_9236_().m_45547_(new ClipContext(this.entity.m_146892_(), var8, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.entity));
            if (result.getType() == HitResult.Type.MISS) {
                this.entity.lastFlightTargetPos = var8;
                return var8;
            } else {
                return result.m_82450_();
            }
        }
    }

    private Vec3 findOrFollowFlightPos() {
        SubterranodonEntity leader = (SubterranodonEntity) this.entity.getPackLeader();
        if (leader != this.entity && leader.lastFlightTargetPos != null) {
            int index = this.getPackPosition(this.entity, 0);
            int halfIndex = (int) Math.ceil((double) ((float) index / 2.0F));
            float xOffset = 6.0F + this.entity.m_217043_().nextFloat() * 2.0F;
            float zOffset = 4.0F + this.entity.m_217043_().nextFloat() * 3.0F;
            Vec3 offset = new Vec3((double) (((float) (index % 2) - 0.5F) * xOffset * (float) halfIndex), 0.0, (double) ((float) halfIndex * zOffset)).yRot((float) Math.toRadians((double) (180.0F - leader.f_20883_)));
            return leader.lastFlightTargetPos.add(offset);
        } else {
            Vec3 randOffsetMove = new Vec3((double) (this.entity.m_217043_().nextFloat() - 0.5F), (double) (this.entity.m_217043_().nextFloat() - 0.5F), (double) (this.entity.m_217043_().nextFloat() - 0.5F)).scale(2.0);
            return this.findFlightPos().add(randOffsetMove);
        }
    }

    private boolean isLeaderStillGoing() {
        return this.entity.isPackFollower() && ((SubterranodonEntity) this.entity.getPackLeader()).isFlying();
    }

    private int getPackPosition(PackAnimal subterranodon, int index) {
        return index < 16 && subterranodon.getPriorPackMember() != null ? this.getPackPosition(subterranodon.getPriorPackMember(), index + 1) : index;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.entity.m_20183_();
        while (position.m_123342_() > this.entity.m_9236_().m_141937_() && this.entity.m_9236_().m_46859_(position) && this.entity.m_9236_().getFluidState(position).isEmpty()) {
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

    @Override
    public boolean canContinueToUse() {
        return this.isFlying ? this.entity.isFlying() && this.entity.m_20275_(this.x, this.y, this.z) > 5.0 && !this.entity.isDancing() : !this.entity.m_21573_().isDone() && !this.entity.m_20160_() && !this.entity.isDancing();
    }

    @Override
    public void start() {
        if (this.isFlying) {
            this.entity.setFlying(true);
            this.entity.m_21566_().setWantedPosition(this.x, this.y, this.z, this.entity.isPackFollower() ? 2.0 : 1.0);
        } else {
            this.entity.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
        }
    }

    @Override
    public void stop() {
        this.entity.m_21573_().stop();
        this.entity.landingFlag = false;
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        super.stop();
    }
}