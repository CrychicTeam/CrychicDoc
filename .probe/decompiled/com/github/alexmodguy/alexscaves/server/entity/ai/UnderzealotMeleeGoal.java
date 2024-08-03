package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.UnderzealotEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class UnderzealotMeleeGoal extends Goal {

    private UnderzealotEntity entity;

    private boolean shouldBurrow = false;

    public UnderzealotMeleeGoal(UnderzealotEntity entity) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return this.entity.m_5448_() != null && this.entity.m_5448_().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.m_5448_();
        if (target != null) {
            double dist = (double) this.entity.m_20270_(target);
            float f = this.entity.m_20205_() + target.m_20205_();
            if (this.shouldBurrow && this.entity.m_20096_()) {
                this.shouldBurrow = false;
                this.entity.setBuried(true);
                this.entity.reemergeAt(this.entity.findReemergePos(target.m_20183_(), 15), 20 + this.entity.m_217043_().nextInt(60));
            } else if (!this.entity.isBuried()) {
                if (this.entity.isDiggingInProgress()) {
                    this.entity.m_21573_().stop();
                } else {
                    this.entity.m_21573_().moveTo(target, 1.3);
                    if (dist < (double) (f + 1.0F)) {
                        this.tryAnimation(this.entity.m_217043_().nextBoolean() ? UnderzealotEntity.ANIMATION_ATTACK_0 : UnderzealotEntity.ANIMATION_ATTACK_1);
                    }
                    if ((this.entity.getAnimation() == UnderzealotEntity.ANIMATION_ATTACK_0 || this.entity.getAnimation() == UnderzealotEntity.ANIMATION_ATTACK_1) && this.entity.getAnimationTick() == 8) {
                        this.checkAndDealDamage(target, 1.0F);
                    }
                }
            }
        }
    }

    private void checkAndDealDamage(LivingEntity target, float multiplier) {
        if (this.entity.m_142582_(target) && (double) this.entity.m_20270_(target) < (double) (this.entity.m_20205_() + target.m_20205_()) + 1.0) {
            float f = (float) this.entity.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * multiplier;
            target.hurt(target.m_269291_().mobAttack(this.entity), f);
            target.knockback(0.2 + 0.3 * (double) multiplier, this.entity.m_20185_() - target.m_20185_(), this.entity.m_20189_() - target.m_20189_());
            this.shouldBurrow = this.entity.m_9236_().random.nextBoolean();
            Entity vehicle = target.m_20202_();
            if (vehicle != null) {
                vehicle.setDeltaMovement(target.m_20184_());
                vehicle.hurt(target.m_269291_().mobAttack(this.entity), f * 0.5F);
            }
        }
    }

    @Override
    public void stop() {
        this.shouldBurrow = false;
    }

    private boolean tryAnimation(Animation animation) {
        if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.entity.setAnimation(animation);
            this.entity.m_216990_(ACSoundRegistry.UNDERZEALOT_ATTACK.get());
            return true;
        } else {
            return false;
        }
    }
}