package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class DeathWormAIWander extends WaterAvoidingRandomStrollGoal {

    private final EntityDeathWorm worm;

    public DeathWormAIWander(EntityDeathWorm creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.worm = creatureIn;
    }

    @Override
    public boolean canUse() {
        return !this.worm.isInSand() && !this.worm.m_20160_() && super.m_8036_();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.worm.isInSand() && !this.worm.m_20160_() && super.m_8045_();
    }
}