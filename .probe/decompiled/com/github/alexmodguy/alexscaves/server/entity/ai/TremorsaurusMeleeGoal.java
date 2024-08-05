package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorsaurusEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.FlyingAnimal;

public class TremorsaurusMeleeGoal extends Goal {

    private TremorsaurusEntity tremorsaurus;

    public TremorsaurusMeleeGoal(TremorsaurusEntity tremorsaurus) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.tremorsaurus = tremorsaurus;
    }

    @Override
    public boolean canUse() {
        return this.tremorsaurus.m_5448_() != null && this.tremorsaurus.m_5448_().isAlive() && !this.tremorsaurus.isDancing();
    }

    @Override
    public void start() {
        this.tremorsaurus.setRunning(!this.tremorsaurus.m_20160_());
    }

    @Override
    public void stop() {
        this.tremorsaurus.setRunning(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.tremorsaurus.m_5448_();
        if (target != null) {
            boolean grab = this.isFlyingTarget();
            this.tremorsaurus.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
            this.tremorsaurus.tryRoar();
            double dist = (double) this.tremorsaurus.m_20270_(target);
            this.tremorsaurus.m_21573_().moveTo(target, 1.0);
            if (dist < (double) (this.tremorsaurus.m_20205_() + target.m_20205_()) + 1.0 && this.tremorsaurus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                if ((!this.tremorsaurus.m_217043_().nextBoolean() && !(Math.max(target.m_20206_(), target.m_20205_()) >= 2.0F) || grab) && !this.tremorsaurus.m_6162_()) {
                    this.tryAnimation(TremorsaurusEntity.ANIMATION_SHAKE_PREY);
                } else {
                    this.tryAnimation(TremorsaurusEntity.ANIMATION_BITE);
                }
            }
            if (this.tremorsaurus.getAnimation() == TremorsaurusEntity.ANIMATION_BITE && this.tremorsaurus.getAnimationTick() > 10 && this.tremorsaurus.getAnimationTick() <= 12) {
                this.checkAndDealDamage(target);
            }
        }
    }

    private void checkAndDealDamage(LivingEntity target) {
        if (this.tremorsaurus.m_142582_(target) && (double) this.tremorsaurus.m_20270_(target) < (double) (this.tremorsaurus.m_20205_() + target.m_20205_()) + 2.0) {
            this.tremorsaurus.m_216990_(ACSoundRegistry.TREMORSAURUS_BITE.get());
            target.hurt(target.m_269291_().mobAttack(this.tremorsaurus), (float) this.tremorsaurus.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
            target.knockback(0.5, this.tremorsaurus.m_20185_() - target.m_20185_(), this.tremorsaurus.m_20189_() - target.m_20189_());
        }
    }

    private boolean isFlyingTarget() {
        return this.tremorsaurus.m_5448_() instanceof FlyingAnimal;
    }

    private boolean tryAnimation(Animation animation) {
        if (this.tremorsaurus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.tremorsaurus.setAnimation(animation);
            return true;
        } else {
            return false;
        }
    }
}