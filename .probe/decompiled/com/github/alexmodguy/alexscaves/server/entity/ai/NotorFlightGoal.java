package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.NotorEntity;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NotorFlightGoal extends Goal {

    private NotorEntity entity;

    private double x;

    private double y;

    private double z;

    public NotorFlightGoal(NotorEntity notor) {
        this.entity = notor;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.entity.m_20160_() && (this.entity.m_5448_() == null || !this.entity.m_5448_().isAlive()) && !this.entity.m_20159_()) {
            boolean flag = false;
            if (this.entity.m_20096_() && this.entity.m_217043_().nextInt(45) != 0) {
                return false;
            } else {
                Vec3 target = this.generatePosition();
                if (target == null) {
                    return false;
                } else {
                    this.x = target.x;
                    this.y = target.y;
                    this.z = target.z;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (this.entity.f_19862_ || this.entity.f_19863_ && !this.entity.m_20096_() || this.entity.m_20275_(this.x, this.y, this.z) < 3.0) {
            Vec3 target = this.generatePosition();
            if (target != null) {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
            }
        }
        this.entity.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
    }

    @Nullable
    protected Vec3 generatePosition() {
        Vec3 vec3 = this.findFlightPos();
        return this.entity.m_217043_().nextInt(20) == 0 && !this.isOverWaterOrVoid() ? this.groundPosition(vec3) : vec3;
    }

    private Vec3 findFlightPos() {
        float maxRot = !this.entity.f_19862_ && !this.entity.f_19863_ ? 40.0F : 360.0F;
        float xRotOffset = (float) Math.toRadians((double) (this.entity.m_217043_().nextFloat() * (maxRot - maxRot / 2.0F) * 0.5F));
        float yRotOffset = (float) Math.toRadians((double) (this.entity.m_217043_().nextFloat() * maxRot - maxRot / 2.0F));
        Vec3 lookVec = this.entity.m_20154_().scale((double) (6 + this.entity.m_217043_().nextInt(6))).xRot(xRotOffset).yRot(yRotOffset);
        Vec3 targetVec = this.entity.m_20182_().add(lookVec);
        Vec3 heightAdjusted;
        if (this.entity.m_9236_().m_45527_(BlockPos.containing(targetVec))) {
            Vec3 ground = this.groundPosition(targetVec);
            heightAdjusted = new Vec3(targetVec.x, ground.y + 4.0 + (double) this.entity.m_217043_().nextInt(3), targetVec.z);
        } else {
            Vec3 ground = this.groundPosition(targetVec);
            BlockPos ceiling = BlockPos.containing(ground).above(2);
            while (ceiling.m_123342_() < this.entity.m_9236_().m_151558_() && !this.entity.m_9236_().getBlockState(ceiling).m_280296_()) {
                ceiling = ceiling.above();
            }
            float randCeilVal = 0.3F + this.entity.m_217043_().nextFloat() * 0.5F;
            heightAdjusted = new Vec3(targetVec.x, ground.y + ((double) ceiling.m_123342_() - ground.y) * (double) randCeilVal, targetVec.z);
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
        BlockPos ground = BlockPos.containing(airPosition);
        while (ground.m_123342_() > this.entity.m_9236_().m_141937_() && !this.entity.m_9236_().getBlockState(ground).m_280296_()) {
            ground = ground.below();
        }
        return Vec3.atCenterOf(ground.below());
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.m_20096_() && this.entity.m_20275_(this.x, this.y, this.z) > 5.0;
    }

    @Override
    public void start() {
        this.entity.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
    }

    @Override
    public void stop() {
        this.entity.m_21573_().stop();
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        super.stop();
    }
}