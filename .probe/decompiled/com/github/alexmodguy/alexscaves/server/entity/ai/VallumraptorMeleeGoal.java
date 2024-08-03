package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class VallumraptorMeleeGoal extends Goal {

    private VallumraptorEntity raptor;

    public VallumraptorMeleeGoal(VallumraptorEntity raptor) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.raptor = raptor;
    }

    @Override
    public boolean canUse() {
        return this.raptor.m_5448_() != null && this.raptor.m_5448_().isAlive() && !this.raptor.isDancing();
    }

    @Override
    public void stop() {
        this.raptor.setRunning(false);
        this.raptor.setLeaping(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.raptor.m_5448_();
        this.raptor.setRunning(true);
        if (target != null) {
            double dist = (double) this.raptor.m_20270_(target);
            if (this.raptor.isLeaping()) {
                this.checkAndDealDamage(target);
                if (this.raptor.m_20096_() || this.raptor.m_20072_()) {
                    this.raptor.setLeaping(false);
                }
            } else if (this.raptor.getAnimation() == VallumraptorEntity.ANIMATION_STARTLEAP) {
                this.raptor.m_21573_().stop();
                this.raptor.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                if (this.raptor.getAnimationTick() > 15 && this.raptor.m_20096_()) {
                    this.raptor.setLeaping(true);
                    this.raptor.m_216990_(ACSoundRegistry.VALLUMRAPTOR_ATTACK.get());
                    Vec3 vector3d = this.raptor.m_20184_();
                    Vec3 vector3d1 = new Vec3(target.m_20185_() - this.raptor.m_20185_(), 0.0, target.m_20189_() - this.raptor.m_20189_());
                    if (vector3d1.lengthSqr() > 1.0E-7) {
                        vector3d1 = vector3d1.normalize().scale(0.9).add(vector3d.scale(0.5));
                    }
                    this.raptor.m_20334_(vector3d1.x, 0.6F, vector3d1.z);
                }
            } else {
                this.raptor.m_21573_().moveTo(target, 1.0);
                if (dist < (double) (this.raptor.m_20205_() + target.m_20205_() + 1.0F)) {
                    this.tryAnimation(this.raptor.m_217043_().nextBoolean() ? VallumraptorEntity.ANIMATION_MELEE_BITE : (this.raptor.m_217043_().nextBoolean() ? VallumraptorEntity.ANIMATION_MELEE_SLASH_2 : VallumraptorEntity.ANIMATION_MELEE_SLASH_1));
                    if (this.raptor.getAnimation() == VallumraptorEntity.ANIMATION_MELEE_BITE && this.raptor.getAnimationTick() > 5 && this.raptor.getAnimationTick() <= 8) {
                        this.checkAndDealDamage(target);
                    }
                    if ((this.raptor.getAnimation() == VallumraptorEntity.ANIMATION_MELEE_SLASH_1 || this.raptor.getAnimation() == VallumraptorEntity.ANIMATION_MELEE_SLASH_2) && this.raptor.getAnimationTick() > 7 && this.raptor.getAnimationTick() <= 10) {
                        this.checkAndDealDamage(target);
                    }
                } else {
                    int jumpChance = this.raptor.m_21824_() ? 5 : 10;
                    if (dist > 3.0 && dist < 7.0 && this.raptor.m_217043_().nextInt(jumpChance) == 0) {
                        this.tryAnimation(VallumraptorEntity.ANIMATION_STARTLEAP);
                    }
                }
            }
        }
    }

    private void checkAndDealDamage(LivingEntity target) {
        if (this.raptor.m_142582_(target) && this.raptor.m_20270_(target) < this.raptor.m_20205_() + target.m_20205_() + 1.0F) {
            this.raptor.m_216990_(ACSoundRegistry.VALLUMRAPTOR_SCRATCH.get());
            target.hurt(target.m_269291_().mobAttack(this.raptor), (float) this.raptor.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
        }
    }

    private boolean tryAnimation(Animation animation) {
        if (this.raptor.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.raptor.setAnimation(animation);
            return true;
        } else {
            return false;
        }
    }
}