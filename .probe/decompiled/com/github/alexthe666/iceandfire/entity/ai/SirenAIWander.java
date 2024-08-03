package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class SirenAIWander extends RandomStrollGoal {

    private final EntitySiren siren;

    public SirenAIWander(EntitySiren creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.siren = creatureIn;
    }

    @Override
    public boolean canUse() {
        return !this.siren.m_20069_() && !this.siren.isSinging() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.siren.m_20069_() && !this.siren.isSinging() && super.canContinueToUse();
    }
}