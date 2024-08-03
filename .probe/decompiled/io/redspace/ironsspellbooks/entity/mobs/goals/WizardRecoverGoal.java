package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class WizardRecoverGoal extends Goal {

    protected final PathfinderMob mob;

    protected final IMagicEntity spellCastingMob;

    protected final int minDelay;

    protected final int maxDelay;

    protected int delay = 15;

    public WizardRecoverGoal(IMagicEntity mob) {
        this(mob, 50, 120);
    }

    public WizardRecoverGoal(IMagicEntity mob, int minDelay, int maxDelay) {
        this.spellCastingMob = mob;
        if (mob instanceof PathfinderMob m) {
            this.mob = m;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
        } else {
            throw new IllegalStateException("Unable to add " + this.getClass().getSimpleName() + "to entity, must extend PathfinderMob.");
        }
    }

    @Override
    public boolean canUse() {
        return this.mob.m_5448_() == null && this.mob.m_21223_() < this.mob.m_21233_() && !this.spellCastingMob.isDrinkingPotion() && !this.spellCastingMob.isCasting() && --this.delay <= 0;
    }

    @Override
    public void start() {
        this.spellCastingMob.startDrinkingPotion();
        this.delay = this.mob.m_217043_().nextIntBetweenInclusive(this.minDelay, this.maxDelay);
    }
}