package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class HippocampusAIWander extends RandomStrollGoal {

    public HippocampusAIWander(PathfinderMob creatureIn, double speedIn) {
        super(creatureIn, speedIn);
    }

    @Override
    public boolean canUse() {
        return (!(this.f_25725_ instanceof TamableAnimal) || !((TamableAnimal) this.f_25725_).isOrderedToSit()) && !this.f_25725_.m_20069_() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return (!(this.f_25725_ instanceof TamableAnimal) || !((TamableAnimal) this.f_25725_).isOrderedToSit()) && !this.f_25725_.m_20069_() && super.canContinueToUse();
    }
}