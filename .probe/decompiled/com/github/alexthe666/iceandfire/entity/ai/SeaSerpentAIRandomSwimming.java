package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

public class SeaSerpentAIRandomSwimming extends RandomStrollGoal {

    public SeaSerpentAIRandomSwimming(PathfinderMob creature, double speed, int chance) {
        super(creature, speed, chance, false);
    }

    @Override
    public boolean canUse() {
        if (!this.f_25725_.m_20160_() && this.f_25725_.m_5448_() == null) {
            if (!this.f_25731_ && this.f_25725_.m_217043_().nextInt(this.f_25730_) != 0) {
                return false;
            } else {
                Vec3 vector3d = this.getPosition();
                if (vector3d == null) {
                    return false;
                } else {
                    this.f_25726_ = vector3d.x;
                    this.f_25727_ = vector3d.y;
                    this.f_25728_ = vector3d.z;
                    this.f_25731_ = false;
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (((EntitySeaSerpent) this.f_25725_).jumpCooldown <= 0) {
            Vec3 vector3d = this.findSurfaceTarget(this.f_25725_, 32, 16);
            return vector3d != null ? vector3d.add(0.0, 1.0, 0.0) : null;
        } else {
            BlockPos blockpos = null;
            Random random = ThreadLocalRandom.current();
            int range = 16;
            for (int i = 0; i < 15; i++) {
                BlockPos blockpos1 = this.f_25725_.m_20183_().offset(random.nextInt(16) - 8, random.nextInt(16) - 8, random.nextInt(16) - 8);
                while (this.f_25725_.m_9236_().m_46859_(blockpos1) && this.f_25725_.m_9236_().getFluidState(blockpos1).isEmpty() && blockpos1.m_123342_() > 1) {
                    blockpos1 = blockpos1.below();
                }
                if (this.f_25725_.m_9236_().getFluidState(blockpos1).is(FluidTags.WATER)) {
                    blockpos = blockpos1;
                }
            }
            return blockpos == null ? null : new Vec3((double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_() + 0.5, (double) blockpos.m_123343_() + 0.5);
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
        return this.f_25725_.m_9236_().getFluidState(blockpos).is(FluidTags.WATER) && !this.f_25725_.m_9236_().getBlockState(blockpos).m_280555_();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.f_25725_.m_9236_().getBlockState(pos.offset(dx * scale, 1, dz * scale)).m_60795_() && this.f_25725_.m_9236_().getBlockState(pos.offset(dx * scale, 2, dz * scale)).m_60795_();
    }

    private Vec3 findSurfaceTarget(PathfinderMob creature, int i, int i1) {
        BlockPos upPos = creature.m_20183_();
        while (creature.m_9236_().getFluidState(upPos).is(FluidTags.WATER)) {
            upPos = upPos.above();
        }
        return this.isAirAbove(upPos.below(), 0, 0, 0) && this.canJumpTo(upPos.below(), 0, 0, 0) ? new Vec3((double) ((float) upPos.m_123341_() + 0.5F), (double) ((float) upPos.m_123342_() + 3.5F), (double) ((float) upPos.m_123343_() + 0.5F)) : null;
    }
}