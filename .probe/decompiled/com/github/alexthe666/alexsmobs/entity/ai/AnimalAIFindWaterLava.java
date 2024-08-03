package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.ISemiAquatic;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class AnimalAIFindWaterLava extends Goal {

    private final PathfinderMob creature;

    private BlockPos targetPos;

    private final int executionChance = 30;

    private final double speed;

    public AnimalAIFindWaterLava(PathfinderMob creature) {
        this(creature, 1.0);
    }

    public AnimalAIFindWaterLava(PathfinderMob creature, double speed) {
        this.creature = creature;
        this.speed = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.creature.m_20096_() && !this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.WATER) && !this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.LAVA) && this.creature instanceof ISemiAquatic && ((ISemiAquatic) this.creature).shouldEnterWater() && (this.creature.m_5448_() != null || this.creature.m_217043_().nextInt(30) == 0)) {
            this.targetPos = this.generateTarget();
            return this.targetPos != null;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        if (this.targetPos != null) {
            this.creature.m_21573_().moveTo((double) this.targetPos.m_123341_(), (double) this.targetPos.m_123342_(), (double) this.targetPos.m_123343_(), this.speed);
        }
    }

    @Override
    public void tick() {
        if (this.targetPos != null) {
            this.creature.m_21573_().moveTo((double) this.targetPos.m_123341_(), (double) this.targetPos.m_123342_(), (double) this.targetPos.m_123343_(), this.speed);
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.creature instanceof ISemiAquatic && !((ISemiAquatic) this.creature).shouldEnterWater()) {
            this.creature.m_21573_().stop();
            return false;
        } else {
            return !this.creature.m_21573_().isDone() && this.targetPos != null && !this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.LAVA) && !this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.WATER);
        }
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        RandomSource random = this.creature.m_217043_();
        int range = this.creature instanceof ISemiAquatic ? ((ISemiAquatic) this.creature).getWaterSearchRange() : 14;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.creature.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
            while (this.creature.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.below();
            }
            if (this.creature.m_9236_().getFluidState(blockpos1).is(FluidTags.WATER) || this.creature.m_9236_().getFluidState(blockpos1).is(FluidTags.LAVA)) {
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }
}