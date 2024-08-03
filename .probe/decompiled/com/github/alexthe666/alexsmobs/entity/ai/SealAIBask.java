package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

public class SealAIBask extends Goal {

    private final EntitySeal seal;

    public SealAIBask(EntitySeal seal) {
        this.seal = seal;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return this.seal.isBasking() && !this.seal.m_20072_();
    }

    @Override
    public boolean canUse() {
        return this.seal.m_20072_() ? false : this.seal.m_21188_() == null && this.seal.m_5448_() == null && this.seal.isBasking();
    }

    @Override
    public void tick() {
        this.seal.m_21573_().stop();
    }

    @Override
    public void stop() {
        this.seal.setBasking(false);
    }
}