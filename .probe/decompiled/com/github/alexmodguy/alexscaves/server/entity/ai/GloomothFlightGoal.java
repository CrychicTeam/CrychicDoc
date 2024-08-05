package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GloomothFlightGoal extends Goal {

    private GloomothEntity entity;

    private double x;

    private double y;

    private double z;

    private int orbitAngleOffset = 0;

    private int orbitDistance = 1;

    public GloomothFlightGoal(GloomothEntity notor) {
        this.entity = notor;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.entity.m_20160_() || this.entity.m_5448_() != null && this.entity.m_5448_().isAlive() || this.entity.m_20159_()) {
            return false;
        } else if (this.entity.m_20096_() && !this.entity.isFlying() && this.entity.m_217043_().nextInt(4) != 0) {
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
    }

    @Override
    public void tick() {
        if (this.entity.f_19862_ || this.entity.f_19863_ && !this.entity.m_20096_() || this.entity.m_20275_(this.x, this.y, this.z) > 100.0) {
            Vec3 target = this.generatePosition();
            if (target != null) {
                this.x = target.x;
                this.y = target.y;
                this.z = target.z;
            }
        }
        float speed = 1.0F;
        if (this.entity.lightPos != null) {
            this.entity.setFlying(true);
            speed = 1.1F;
        } else if (this.entity.m_20096_()) {
            this.entity.setFlying(false);
            speed = 1.0F;
        }
        this.entity.m_21573_().moveTo(this.x, this.y, this.z, (double) speed);
    }

    @Nullable
    protected Vec3 generatePosition() {
        if (this.entity.lightPos != null) {
            return this.findLightCirclePos(this.entity.lightPos);
        } else {
            Vec3 vec3 = this.findFlightPos();
            if (this.isOverWaterOrVoid()) {
                return vec3.add(0.0, (double) (this.entity.m_217043_().nextFloat() * 8.0F), 0.0);
            } else {
                return this.entity.m_217043_().nextInt(20) != 0 ? vec3 : this.groundPosition(vec3);
            }
        }
    }

    private Vec3 findLightCirclePos(BlockPos lightPos) {
        Vec3 center = lightPos.getCenter();
        Vec3 offset = new Vec3((double) (this.entity.m_217043_().nextFloat() * 4.0F + 1.0F), (double) (this.entity.m_217043_().nextFloat() * 2.0F), 0.0).yRot((float) ((Math.PI * 2) * (double) this.entity.m_217043_().nextFloat()));
        return center.add(offset);
    }

    private Vec3 findFlightPos() {
        Vec3 heightAdjusted = this.entity.m_20182_().add((double) (this.entity.m_217043_().nextInt(10) - 5), 0.0, (double) (this.entity.m_217043_().nextInt(10) - 5));
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
        BlockPos ground = BlockPos.containing(airPosition);
        while (ground.m_123342_() > this.entity.m_9236_().m_141937_() && !this.entity.m_9236_().getBlockState(ground).m_280296_()) {
            ground = ground.below();
        }
        return Vec3.atCenterOf(ground.below());
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.m_20275_(this.x, this.y, this.z) > 3.0 && !this.entity.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.entity.setFlying(true);
        this.entity.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
    }

    @Override
    public void stop() {
        this.entity.m_21573_().stop();
        super.stop();
    }
}