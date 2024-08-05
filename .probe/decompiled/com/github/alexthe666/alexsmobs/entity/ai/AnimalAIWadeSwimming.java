package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.EnumSet;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class AnimalAIWadeSwimming extends Goal {

    private final Mob entity;

    public AnimalAIWadeSwimming(Mob entity) {
        this.entity = entity;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP));
        entity.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        return this.entity.m_20069_() && this.entity.m_204036_(FluidTags.WATER) > 1.0 || this.entity.m_20077_();
    }

    @Override
    public void tick() {
        if (this.entity.m_217043_().nextFloat() < 0.8F) {
            this.entity.getJumpControl().jump();
        }
    }
}