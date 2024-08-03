package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.CorrodentEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class CorrodentAttackGoal extends Goal {

    private CorrodentEntity entity;

    private boolean burrowing = false;

    private int burrowCheckTime = 0;

    private int evadeFor = 0;

    public CorrodentAttackGoal(CorrodentEntity entity) {
        this.entity = entity;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.entity.m_5448_() != null && this.entity.m_5448_().isAlive() && this.entity.fleeLightFor <= 0;
    }

    @Override
    public void tick() {
        LivingEntity target = this.entity.m_5448_();
        if (target != null) {
            double dist = (double) this.entity.m_20270_(target);
            float f = this.entity.m_20205_() + target.m_20205_();
            if (this.burrowCheckTime++ > 40 && this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.burrowCheckTime = 0;
                if (!this.burrowing) {
                    if (this.entity.m_20096_() && dist > (double) f && (!this.entity.canReach(target.m_20183_()) || dist > 20.0 || this.entity.m_217043_().nextInt(20) == 0)) {
                        this.burrowing = true;
                        this.evadeFor = 60 + this.entity.m_217043_().nextInt(40);
                    }
                } else if (dist < (double) (f + 1.0F) || this.entity.m_217043_().nextInt(10) == 0) {
                    this.burrowing = false;
                    this.evadeFor = 0;
                }
            }
            if (this.evadeFor > 0) {
                this.evadeFor--;
                this.burrowing = true;
                this.entity.setDigging(true);
                if (this.entity.m_21573_().isDone()) {
                    Vec3 vec3 = this.generateEvadePosition(target.m_20183_());
                    if (vec3 != null) {
                        this.entity.m_21573_().moveTo(vec3.x, vec3.y, vec3.z, 1.0);
                    }
                }
            } else if (this.burrowing) {
                if (this.entity.m_20096_()) {
                    this.entity.setDigging(true);
                }
                this.entity.m_21573_().moveTo(target, 2.0);
                if (!this.entity.m_5830_()) {
                    this.entity.setDigging(false);
                    this.burrowing = false;
                }
            } else {
                if (!this.entity.m_5830_()) {
                    this.entity.setDigging(false);
                } else {
                    this.entity.setDigging(true);
                    this.entity.m_20256_(this.entity.m_20184_().add(0.0, 0.1, 0.0));
                }
                this.entity.m_21573_().moveTo(target, 1.5);
            }
            if (dist < (double) (f + 1.0F)) {
                this.tryAnimation(CorrodentEntity.ANIMATION_BITE);
            }
            if (this.entity.getAnimation() == CorrodentEntity.ANIMATION_BITE) {
                this.entity.setDigging(false);
                if (this.entity.getAnimationTick() == 8) {
                    this.checkAndDealDamage(target, 1.5F);
                    if (this.entity.m_217043_().nextBoolean()) {
                        this.evadeFor = 60 + this.entity.m_217043_().nextInt(40);
                    }
                }
            }
        }
    }

    private Vec3 generateEvadePosition(BlockPos around) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 10; i++) {
            check.move(around);
            check.move(this.entity.m_217043_().nextInt(16) - 8, this.entity.m_217043_().nextInt(16) - 8, this.entity.m_217043_().nextInt(16) - 8);
            if (!this.entity.m_9236_().isLoaded(check) || check.m_123342_() < this.entity.m_9236_().m_141937_()) {
                break;
            }
            while (this.entity.m_9236_().m_46859_(check) && this.entity.m_9236_().isLoaded(check) && check.m_123342_() > this.entity.m_9236_().m_141937_() - 1) {
                check.move(0, -1, 0);
            }
            if (CorrodentEntity.isSafeDig(this.entity.m_9236_(), check.immutable()) && this.entity.canReach(check)) {
                return Vec3.atCenterOf(check.immutable());
            }
        }
        return null;
    }

    private void checkAndDealDamage(LivingEntity target, float multiplier) {
        if (this.entity.m_142582_(target) && (double) this.entity.m_20270_(target) < (double) (this.entity.m_20205_() + target.m_20205_()) + 1.0) {
            float f = (float) this.entity.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * multiplier;
            target.hurt(target.m_269291_().mobAttack(this.entity), f);
            target.knockback(0.2 + 0.3 * (double) multiplier, this.entity.m_20185_() - target.m_20185_(), this.entity.m_20189_() - target.m_20189_());
            Entity entity = target.m_20202_();
            if (entity != null) {
                entity.setDeltaMovement(target.m_20184_());
                entity.hurt(target.m_269291_().mobAttack(this.entity), f * 0.5F);
            }
            this.burrowing = true;
        }
    }

    @Override
    public void stop() {
        this.burrowing = false;
        this.burrowCheckTime = 0;
        this.evadeFor = 0;
    }

    private boolean tryAnimation(Animation animation) {
        if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.entity.setAnimation(animation);
            this.entity.m_216990_(ACSoundRegistry.CORRODENT_ATTACK.get());
            return true;
        } else {
            return false;
        }
    }
}