package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;

public class SeaSerpentAIGetInWater extends Goal {

    private final EntitySeaSerpent creature;

    private BlockPos targetPos;

    public SeaSerpentAIGetInWater(EntitySeaSerpent creature) {
        this.creature = creature;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if ((this.creature.jumpCooldown == 0 || this.creature.m_20096_()) && !this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.WATER)) {
            this.targetPos = this.generateTarget();
            return this.targetPos != null;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        if (this.targetPos != null) {
            this.creature.m_21573_().moveTo((double) this.targetPos.m_123341_(), (double) this.targetPos.m_123342_(), (double) this.targetPos.m_123343_(), 1.5);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.creature.m_21573_().isDone() && this.targetPos != null && !this.creature.m_9236_().getFluidState(this.creature.m_20183_()).is(FluidTags.WATER);
    }

    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        int range = 16;
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = this.creature.m_20183_().offset(ThreadLocalRandom.current().nextInt(16) - 8, 3, ThreadLocalRandom.current().nextInt(16) - 8);
            while (this.creature.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > 1) {
                blockpos1 = blockpos1.below();
            }
            if (this.creature.m_9236_().getFluidState(blockpos1).is(FluidTags.WATER)) {
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }
}