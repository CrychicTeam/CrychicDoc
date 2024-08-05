package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SauropodBaseEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class AtlatitanMeleeGoal extends Goal {

    private AtlatitanEntity atlatitan;

    public AtlatitanMeleeGoal(AtlatitanEntity atlatitan) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.atlatitan = atlatitan;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.atlatitan.m_5448_();
        return target != null && target.isAlive();
    }

    @Override
    public void stop() {
        this.atlatitan.turningFast = false;
    }

    @Override
    public void tick() {
        LivingEntity target = this.atlatitan.m_5448_();
        if (target != null && target.isAlive()) {
            double distance = (double) this.atlatitan.m_20270_(target);
            double attackDistance = (double) (this.atlatitan.m_20205_() + target.m_20205_());
            if (this.atlatitan.getAnimation() != SauropodBaseEntity.ANIMATION_LEFT_KICK && this.atlatitan.getAnimation() != SauropodBaseEntity.ANIMATION_RIGHT_KICK && this.atlatitan.getAnimation() != SauropodBaseEntity.ANIMATION_LEFT_WHIP && this.atlatitan.getAnimation() != SauropodBaseEntity.ANIMATION_RIGHT_WHIP) {
                this.atlatitan.turningFast = false;
            } else {
                this.atlatitan.turningFast = true;
                Vec3 vec3 = target.m_20182_().subtract(this.atlatitan.m_20182_());
                this.atlatitan.f_20883_ = Mth.approachDegrees(this.atlatitan.f_20883_, -((float) Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float) Math.PI), 15.0F);
                this.atlatitan.f_20884_ = this.atlatitan.f_20883_;
                this.atlatitan.m_21563_().setLookAt(target.m_20185_(), target.m_20188_(), target.m_20189_());
            }
            if (distance > attackDistance) {
                this.atlatitan.m_21573_().moveTo(target, 1.0);
            }
            if (this.atlatitan.getAnimation() == IAnimatedEntity.NO_ANIMATION && distance < attackDistance + 4.0) {
                float random = this.atlatitan.m_217043_().nextFloat();
                if (random < 0.5F && distance < attackDistance + 1.0) {
                    this.atlatitan.m_5496_(ACSoundRegistry.ATLATITAN_KICK.get(), 3.0F, this.atlatitan.m_6100_());
                    this.atlatitan.setAnimation(this.atlatitan.m_217043_().nextBoolean() ? AtlatitanEntity.ANIMATION_LEFT_KICK : AtlatitanEntity.ANIMATION_RIGHT_KICK);
                } else {
                    this.atlatitan.m_5496_(ACSoundRegistry.ATLATITAN_TAIL.get(), 3.0F, this.atlatitan.m_6100_());
                    this.atlatitan.setAnimation(this.atlatitan.m_217043_().nextBoolean() ? AtlatitanEntity.ANIMATION_RIGHT_WHIP : AtlatitanEntity.ANIMATION_LEFT_WHIP);
                }
            }
        }
    }
}