package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.TrilocarisEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class RelicheirusMeleeGoal extends Goal {

    private RelicheirusEntity relicheirus;

    public RelicheirusMeleeGoal(RelicheirusEntity relicheirus) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.relicheirus = relicheirus;
    }

    @Override
    public boolean canUse() {
        return this.relicheirus.m_5448_() != null && this.relicheirus.m_5448_().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.relicheirus.m_5448_();
        if (target != null) {
            this.relicheirus.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
            double dist = (double) this.relicheirus.m_20270_(target);
            this.relicheirus.m_21573_().moveTo(target, 1.0);
            if (dist < (double) (this.relicheirus.m_20205_() + target.m_20205_()) + 1.0 && this.relicheirus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                if (target instanceof TrilocarisEntity) {
                    this.relicheirus.setPeckY(target.m_146904_());
                    this.tryAnimation(RelicheirusEntity.ANIMATION_EAT_TRILOCARIS);
                } else {
                    this.tryAnimation(this.relicheirus.m_217043_().nextBoolean() ? RelicheirusEntity.ANIMATION_MELEE_SLASH_1 : RelicheirusEntity.ANIMATION_MELEE_SLASH_2);
                }
            }
            if ((this.relicheirus.getAnimation() == RelicheirusEntity.ANIMATION_MELEE_SLASH_1 || this.relicheirus.getAnimation() == RelicheirusEntity.ANIMATION_MELEE_SLASH_2) && this.relicheirus.getAnimationTick() > 7 && this.relicheirus.getAnimationTick() <= 10) {
                this.checkAndDealDamage(target);
            }
        }
    }

    private void checkAndDealDamage(LivingEntity target) {
        if (this.relicheirus.m_142582_(target) && (double) this.relicheirus.m_20270_(target) < (double) (this.relicheirus.m_20205_() + target.m_20205_()) + 2.0) {
            this.relicheirus.m_216990_(ACSoundRegistry.RELICHEIRUS_SCRATCH.get());
            target.hurt(target.m_269291_().mobAttack(this.relicheirus), (float) this.relicheirus.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
            target.knockback(0.5, this.relicheirus.m_20185_() - target.m_20185_(), this.relicheirus.m_20189_() - target.m_20189_());
        }
    }

    private boolean tryAnimation(Animation animation) {
        if (this.relicheirus.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.relicheirus.setAnimation(animation);
            return true;
        } else {
            return false;
        }
    }
}