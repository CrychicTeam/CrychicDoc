package net.minecraft.world.entity.ai.goal;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class RandomStandGoal extends Goal {

    private final AbstractHorse horse;

    private int nextStand;

    public RandomStandGoal(AbstractHorse abstractHorse0) {
        this.horse = abstractHorse0;
        this.resetStandInterval(abstractHorse0);
    }

    @Override
    public void start() {
        this.horse.standIfPossible();
        this.playStandSound();
    }

    private void playStandSound() {
        SoundEvent $$0 = this.horse.getAmbientStandSound();
        if ($$0 != null) {
            this.horse.m_216990_($$0);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public boolean canUse() {
        this.nextStand++;
        if (this.nextStand > 0 && this.horse.m_217043_().nextInt(1000) < this.nextStand) {
            this.resetStandInterval(this.horse);
            return !this.horse.isImmobile() && this.horse.m_217043_().nextInt(10) == 0;
        } else {
            return false;
        }
    }

    private void resetStandInterval(AbstractHorse abstractHorse0) {
        this.nextStand = -abstractHorse0.getAmbientStandInterval();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}