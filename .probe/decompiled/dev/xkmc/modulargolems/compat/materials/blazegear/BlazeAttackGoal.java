package dev.xkmc.modulargolems.compat.materials.blazegear;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.SmallFireball;

public class BlazeAttackGoal extends Goal {

    private final AbstractGolemEntity<?, ?> blaze;

    private final int lv;

    private int attackStep;

    private int attackTime;

    private boolean charged;

    public BlazeAttackGoal(AbstractGolemEntity<?, ?> pBlaze, int lv) {
        this.blaze = pBlaze;
        this.lv = lv;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.blaze.m_5448_();
        return livingentity != null && livingentity.isAlive() && this.blaze.canAttack(livingentity);
    }

    @Override
    public void start() {
        this.attackStep = 0;
    }

    @Override
    public void stop() {
        this.charged = false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.attackTime--;
        LivingEntity livingentity = this.blaze.m_5448_();
        if (livingentity != null) {
            boolean flag = this.blaze.m_21574_().hasLineOfSight(livingentity);
            double distSqr = this.blaze.m_20280_(livingentity);
            if (distSqr < 0.0) {
                return;
            }
            if (flag) {
                double d1 = livingentity.m_20185_() - this.blaze.m_20185_();
                double d2 = livingentity.m_20227_(0.5) - this.blaze.m_20227_(0.5);
                double d3 = livingentity.m_20189_() - this.blaze.m_20189_();
                if (this.attackTime <= 0) {
                    this.attackStep++;
                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                        this.charged = true;
                    } else if (this.attackStep < this.lv + 2) {
                        this.attackTime = 6;
                    } else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                        this.charged = false;
                    }
                    if (this.attackStep > 1) {
                        double d4 = Math.sqrt(Math.sqrt(distSqr)) * 0.5;
                        if (!this.blaze.m_20067_()) {
                            this.blaze.m_9236_().m_5898_(null, 1018, this.blaze.m_20183_(), 0);
                        }
                        for (int i = 0; i < 1; i++) {
                            SmallFireball smallfireball = new SmallFireball(this.blaze.m_9236_(), this.blaze, this.blaze.m_217043_().triangle(d1, 2.297 * d4), d2, this.blaze.m_217043_().triangle(d3, 2.297 * d4));
                            smallfireball.m_6034_(smallfireball.m_20185_(), this.blaze.m_20227_(0.5) + 0.5, smallfireball.m_20189_());
                            this.blaze.m_9236_().m_7967_(smallfireball);
                        }
                    }
                }
            }
            super.tick();
        }
    }
}