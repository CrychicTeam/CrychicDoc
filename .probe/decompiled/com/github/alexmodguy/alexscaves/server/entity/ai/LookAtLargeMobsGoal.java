package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class LookAtLargeMobsGoal extends LookAtPlayerGoal {

    private final float minHeight;

    public LookAtLargeMobsGoal(Mob looker, float minHeight, float f) {
        super(looker, LivingEntity.class, f);
        this.minHeight = minHeight;
    }

    @Override
    public boolean canUse() {
        if (this.f_25512_.m_217043_().nextFloat() >= this.f_25515_) {
            return false;
        } else {
            if (this.f_25512_.getTarget() != null) {
                this.f_25513_ = this.f_25512_.getTarget();
            }
            this.f_25513_ = this.f_25512_.m_9236_().m_45982_(this.f_25512_.m_9236_().m_6443_(this.f_25516_, this.f_25512_.m_20191_().inflate((double) this.f_25514_, 3.0, (double) this.f_25514_), entity -> entity.m_20206_() > this.minHeight), this.f_25517_, this.f_25512_, this.f_25512_.m_20185_(), this.f_25512_.m_20188_(), this.f_25512_.m_20189_());
            return this.f_25513_ != null;
        }
    }
}