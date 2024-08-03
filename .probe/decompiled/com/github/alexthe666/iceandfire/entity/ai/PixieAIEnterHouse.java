package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;

public class PixieAIEnterHouse extends Goal {

    EntityPixie pixie;

    RandomSource random;

    public PixieAIEnterHouse(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.m_217043_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.pixie.isOwnerClose() && !this.pixie.m_21566_().hasWanted() && !this.pixie.isPixieSitting() && this.random.nextInt(20) == 0 && this.pixie.ticksUntilHouseAI == 0) {
            BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.m_9236_());
            return !blockpos1.toString().equals(this.pixie.m_20183_().toString());
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {
        for (int i = 0; i < 3; i++) {
            BlockPos blockpos1 = EntityPixie.findAHouse(this.pixie, this.pixie.m_9236_());
            this.pixie.m_21566_().setWantedPosition((double) blockpos1.m_123341_() + 0.5, (double) blockpos1.m_123342_() + 0.5, (double) blockpos1.m_123343_() + 0.5, 0.25);
            this.pixie.setHousePosition(blockpos1);
            if (this.pixie.m_5448_() == null) {
                this.pixie.m_21563_().setLookAt((double) blockpos1.m_123341_() + 0.5, (double) blockpos1.m_123342_() + 0.5, (double) blockpos1.m_123343_() + 0.5, 180.0F, 20.0F);
            }
        }
    }
}