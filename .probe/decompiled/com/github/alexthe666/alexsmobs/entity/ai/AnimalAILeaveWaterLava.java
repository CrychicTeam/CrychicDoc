package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.EnumSet;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class AnimalAILeaveWaterLava extends Goal {

    private final PathfinderMob creature;

    private BlockPos targetPos;

    private final int executionChance = 30;

    public AnimalAILeaveWaterLava(PathfinderMob creature) {
        this.creature = creature;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if ((this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.WATER) || this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.LAVA)) && this.creature instanceof ISemiAquatic && ((ISemiAquatic) this.creature).shouldLeaveWater() && (this.creature.m_5448_() != null || this.creature.m_217043_().nextInt(30) == 0)) {
            this.targetPos = this.generateTarget();
            return this.targetPos != null;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        if (this.targetPos != null) {
            this.creature.m_21573_().moveTo((double) this.targetPos.m_123341_(), (double) this.targetPos.m_123342_(), (double) this.targetPos.m_123343_(), 1.0);
        }
    }

    @Override
    public void tick() {
        if (this.targetPos != null) {
            this.creature.m_21573_().moveTo((double) this.targetPos.m_123341_(), (double) this.targetPos.m_123342_(), (double) this.targetPos.m_123343_(), 1.0);
        }
        if (this.creature.f_19862_ && (this.creature.m_20069_() || this.creature.m_20077_())) {
            float f1 = this.creature.m_146908_() * (float) (Math.PI / 180.0);
            this.creature.m_20256_(this.creature.m_20184_().add((double) (-Mth.sin(f1) * 0.2F), 0.1, (double) (Mth.cos(f1) * 0.2F)));
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.creature instanceof ISemiAquatic && !((ISemiAquatic) this.creature).shouldLeaveWater()) {
            this.creature.m_21573_().stop();
            return false;
        } else {
            return !this.creature.m_21573_().isDone() && this.targetPos != null && !this.creature.m_9236_().getFluidState(this.targetPos).is(FluidTags.WATER) && !this.creature.m_9236_().getFluidState(this.targetPos).is(FluidTags.LAVA);
        }
    }

    public BlockPos generateTarget() {
        Vec3 vector3d = LandRandomPos.getPos(this.creature, 23, 7);
        for (int tries = 0; vector3d != null && tries < 8; tries++) {
            boolean waterDetected = false;
            Iterator var4 = BlockPos.betweenClosed(Mth.floor(vector3d.x - 2.0), Mth.floor(vector3d.y - 1.0), Mth.floor(vector3d.z - 2.0), Mth.floor(vector3d.x + 2.0), Mth.floor(vector3d.y), Mth.floor(vector3d.z + 2.0)).iterator();
            while (true) {
                if (var4.hasNext()) {
                    BlockPos blockpos1 = (BlockPos) var4.next();
                    if (!this.creature.m_9236_().getFluidState(blockpos1).is(FluidTags.WATER) && !this.creature.m_9236_().getFluidState(blockpos1).is(FluidTags.LAVA)) {
                        continue;
                    }
                    waterDetected = true;
                }
                if (!waterDetected) {
                    return AMBlockPos.fromVec3(vector3d);
                }
                vector3d = LandRandomPos.getPos(this.creature, 23, 7);
                break;
            }
        }
        return null;
    }
}