package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class HullbreakerMeleeGoal extends Goal {

    private HullbreakerEntity hullbreaker;

    public HullbreakerMeleeGoal(HullbreakerEntity hullbreaker) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.hullbreaker = hullbreaker;
    }

    @Override
    public boolean canUse() {
        return this.hullbreaker.m_5448_() != null && this.hullbreaker.m_5448_().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.hullbreaker.m_5448_();
        if (target != null) {
            double dist = (double) this.hullbreaker.m_20270_(target);
            float f = this.hullbreaker.m_20205_() + target.m_20205_();
            if (dist < (double) f + 7.0 && this.hullbreaker.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.tryAnimation(this.hullbreaker.m_217043_().nextBoolean() && this.hullbreaker.m_142582_(target) ? HullbreakerEntity.ANIMATION_BITE : HullbreakerEntity.ANIMATION_BASH);
            }
            if (dist > (double) (f + 2.0F)) {
                this.hullbreaker.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                this.hullbreaker.m_21573_().moveTo(target, 1.6);
            }
            if (this.hullbreaker.getAnimation() == HullbreakerEntity.ANIMATION_BITE && this.hullbreaker.getAnimationTick() > 10 && this.hullbreaker.getAnimationTick() <= 14) {
                this.checkAndDealDamage(target, 1.0F);
            }
            if (this.hullbreaker.getAnimation() == HullbreakerEntity.ANIMATION_BASH && this.hullbreaker.getAnimationTick() > 10 && this.hullbreaker.getAnimationTick() <= 12) {
                this.checkAndDealDamage(target, 1.5F);
            }
            SubmarineEntity.alertSubmarineMountOf(target);
        }
    }

    @Override
    public void start() {
        this.hullbreaker.setInterestLevel(6);
    }

    @Override
    public void stop() {
        this.hullbreaker.setInterestLevel(0);
    }

    private void checkAndDealDamage(LivingEntity target, float multiplier) {
        if (this.hullbreaker.m_142582_(target) && (double) this.hullbreaker.m_20270_(target) < (double) (this.hullbreaker.m_20205_() + target.m_20205_()) + 5.0) {
            float f = (float) this.hullbreaker.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * multiplier;
            target.hurt(target.m_269291_().mobAttack(this.hullbreaker), f);
            target.knockback(0.8 + 0.5 * (double) multiplier, this.hullbreaker.m_20185_() - target.m_20185_(), this.hullbreaker.m_20189_() - target.m_20189_());
            Entity entity = target.m_20202_();
            if (entity != null) {
                entity.setDeltaMovement(target.m_20184_());
                entity.hurt(target.m_269291_().mobAttack(this.hullbreaker), f * 0.5F);
            }
        }
    }

    private boolean tryAnimation(Animation animation) {
        if (this.hullbreaker.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.hullbreaker.setAnimation(animation);
            if (this.hullbreaker.m_20072_()) {
                this.hullbreaker.m_216990_(ACSoundRegistry.HULLBREAKER_ATTACK.get());
            }
            return true;
        } else {
            return false;
        }
    }
}